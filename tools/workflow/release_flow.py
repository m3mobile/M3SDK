"""Shared release preparation and same-run completion. Rehearsal never writes to GitHub."""
import argparse
import importlib.util
import json
import os
import subprocess
import urllib.error
import urllib.request
import xml.etree.ElementTree as ET
import zipfile
from pathlib import Path
from artifacts import (candidate_record, digest, metadata, numeric, read_json, require,
                       validate_files, write_json)

spec = importlib.util.spec_from_file_location('provenance', Path(__file__).with_name('run-provenance.py'))
provenance = importlib.util.module_from_spec(spec)
spec.loader.exec_module(provenance)


def command(*args, cwd=None):
    return subprocess.check_output(list(args), cwd=cwd, text=True).strip()


def api(path, *args):
    return json.loads(command('gh', 'api', f'repos/{os.environ["GH_REPO"]}/{path}', *args))


def context():
    return dict(run_id=os.environ['GITHUB_RUN_ID'], workflow_sha=os.environ['GITHUB_SHA'],
                ref=os.environ['GITHUB_REF'], event=os.environ['GITHUB_EVENT_NAME'],
                repository=os.environ['GH_REPO'], mode=os.environ['RELEASE_MODE'])


def verified_candidate(run, root, version, ctx, rehearsal=False):
    if not rehearsal:
        return provenance.verify(run, 'verify-candidate.yml', str(run['id']), root, version,
                                 ctx['repository'])
    # A separate policy/record, never accepted by the production candidate validator.
    require(ctx['event'] == 'workflow_dispatch' and ctx['ref'].startswith('refs/heads/'),
            'Rehearsal requires a branch dispatch')
    require(run.get('path') == '.github/workflows/verify-candidate.yml' and
            run.get('event') == 'workflow_dispatch' and run.get('status') == 'completed' and
            run.get('conclusion') == 'success', 'Successful manual candidate required')
    require(run.get('head_sha') == ctx['workflow_sha'] and
            'refs/heads/' + run.get('head_branch', '') == ctx['ref'],
            'Rehearsal candidate must match this exact workflow branch and SHA')
    require(all(run.get(key, {}).get('full_name') == ctx['repository']
                for key in ('repository', 'head_repository')), 'Wrong repository or fork')
    numeric(run.get('run_attempt'), 'candidate attempt')
    return dict(schema_version=1, verification_scope='rehearsal_only',
                provenance=dict(repository=ctx['repository'], run_id=str(run['id']),
                                run_attempt=run['run_attempt'], source_sha=run['head_sha']),
                **validate_files(root, 'm3sdk', version, str(run['id']), run['head_sha']))


def validate_payload(root, verification):
    version = verification['version']
    identity = validate_files(root, 'm3sdk', version, verification['candidate_run_id'],
                              verification['source_sha'])
    require(all(identity[key] == verification[key] for key in identity), 'Candidate identity changed')
    package = f'nuget/M3Mobile.M3Sdk.Xamarin.{version}.nupkg'
    manuals = [f'manuals/{name}_v{version}.pdf' for name in
               ('M3SDK_Manual_en', 'M3SDK_Manual_kr', 'M3SDK_Xamarin_Manual_en', 'M3SDK_Xamarin_Manual_kr')]
    for relative in [package, *manuals]:
        require(relative in identity['files'], f'Missing release asset: {relative}')
    for folder in ('samples/', 'dotnet-samples/', 'maven/'):
        require(any(name.startswith(folder) for name in identity['files']), f'Missing {folder}')
    with zipfile.ZipFile(root / package) as archive:
        nuspecs = [name for name in archive.namelist() if name.endswith('.nuspec')]
        require(len(nuspecs) == 1, 'One package nuspec required')
        document = ET.fromstring(archive.read(nuspecs[0]))
        values = {element.tag.split('}')[-1]: element.text for element in document.iter()
                  if element.tag.split('}')[-1] in ('id', 'version')}
        require(values == dict(id='M3Mobile.M3Sdk.Xamarin', version=version), 'NuGet identity mismatch')
    return {relative: identity['files'][relative] for relative in [package, *manuals]}


def documentation_only(source, base, head):
    changed = command('git', 'diff', '--name-only', base, head, cwd=source).splitlines()
    require(all(name == 'README.md' or name == 'dotnet/M3Mobile.M3Sdk.Xamarin/README.md' or
                (name.startswith('docs/') and name.endswith('.md')) for name in changed),
            'Prepared commit contains changes outside release Markdown')
    require(command('git', 'merge-base', '--is-ancestor', base, head, cwd=source) == '',
            'Candidate must be an ancestor of prepared release')


def prepare(source, root, verification, destination, ctx, rehearsal=False):
    if rehearsal:
        require(verification.get('verification_scope') == 'rehearsal_only', 'Rehearsal record required')
    else:
        candidate_record(verification)
    metadata(ctx['workflow_sha'], ctx['run_id'], verification['version'])
    require(command('git', 'rev-parse', 'HEAD', cwd=source) == verification['source_sha'],
            'Source checkout mismatch')
    require(not command('git', 'status', '--porcelain', cwd=source), 'Clean source checkout required')
    assets = validate_payload(root, verification)
    command('node', 'tools/release/prepare-release.js', '--version', verification['version'],
            '--repo-root', '.', cwd=source)
    command('git', 'add', 'README.md', 'docs', 'dotnet/M3Mobile.M3Sdk.Xamarin/README.md', cwd=source)
    if command('git', 'diff', '--cached', '--name-only', cwd=source):
        command('git', '-c', 'user.name=github-actions[bot]',
                '-c', 'user.email=github-actions[bot]@users.noreply.github.com',
                'commit', '-m', f'release: {verification["version"]} documentation', cwd=source)
    release_sha = command('git', 'rev-parse', 'HEAD', cwd=source)
    documentation_only(source, verification['source_sha'], release_sha)
    destination.mkdir(parents=True, exist_ok=True)
    # Transport the exact commit, rather than recreate a timestamp-dependent commit in publish.
    command('git', 'bundle', 'create', str((destination / 'release.bundle').resolve()), 'HEAD', cwd=source)
    plan = dict(schema_version=1, kind='rehearsal' if rehearsal else 'production',
                context=ctx, candidate=verification, release_sha=release_sha, assets=assets,
                bundle_sha256=digest(destination / 'release.bundle'))
    write_json(destination / 'release-plan.json', plan)
    return plan


def check_plan(plan, source, root, bundle, ctx, rehearsal=False):
    require(plan.get('schema_version') == 1 and plan.get('kind') ==
            ('rehearsal' if rehearsal else 'production'), 'Wrong release plan kind/schema')
    require(plan['context'] == ctx, 'Plan belongs to another run/ref/SHA')
    require(digest(bundle) == plan['bundle_sha256'], 'Prepared bundle changed')
    require(command('git', 'rev-parse', 'HEAD', cwd=source) == plan['release_sha'],
            'Prepared release checkout mismatch')
    documentation_only(source, plan['candidate']['source_sha'], plan['release_sha'])
    require(validate_payload(root, plan['candidate']) == plan['assets'], 'Release assets changed')
    if not rehearsal:
        candidate_record(plan['candidate'])
    return plan


def collision_check(version):
    # Distinguish an absent tag from auth/network failures; no writes are performed.
    require(not command('git', 'ls-remote', '--tags', 'origin', f'refs/tags/{version}'),
            'Tag already exists; inspect partial release instead of republishing')
    url = 'https://api.nuget.org/v3-flatcontainer/m3mobile.m3sdk.xamarin/' + version.lower() + \
          '/m3mobile.m3sdk.xamarin.' + version.lower() + '.nupkg'
    try:
        with urllib.request.urlopen(urllib.request.Request(url, method='HEAD'), timeout=30):
            raise ValueError('NuGet version already exists')
    except urllib.error.HTTPError as error:
        require(error.code == 404, f'NuGet collision check failed: {error.code}')


def completion_allowed(ctx, needs, rehearsal=False):
    required = ('gate', 'prepare', 'publish', 'verify-public') if not rehearsal else \
               ('gate', 'prepare', 'sink', 'android', 'dotnet')
    require(ctx['event'] == 'workflow_dispatch', 'Manual start required')
    require(ctx.get('mode') == ('rehearsal' if rehearsal else 'publish'), 'Wrong completion mode')
    require(ctx['ref'].startswith('refs/heads/') if rehearsal else ctx['ref'] == 'refs/heads/develop',
            'Wrong completion ref')
    require(all(needs.get(job, {}).get('result') == 'success' for job in required),
            'Every prerequisite must succeed; failure/cancel/skip cannot complete')


def marker(plan):
    return f'M3SDK deployment run {plan["context"]["run_id"]}; candidate {plan["candidate"]["candidate_run_id"]}; source {plan["release_sha"]}'


def finish(plan, record, current_run, release, ctx, needs, tagged_sha):
    completion_allowed(ctx, needs)
    candidate_record(plan['candidate'])
    require(plan['kind'] == 'production' and plan['context'] == ctx, 'Production plan mismatch')
    require(record.get('kind') == 'draft-created' and record.get('context') == ctx and
            record.get('plan_sha256') == digest_record(plan), 'Delivery record mismatch')
    numeric(record.get('release_id'), 'Release ID')
    require(str(current_run.get('id')) == ctx['run_id'] and
            current_run.get('head_sha') == ctx['workflow_sha'] and
            current_run.get('head_branch') == 'develop' and
            current_run.get('event') == 'workflow_dispatch' and
            current_run.get('path') == '.github/workflows/deployment.yml' and
            current_run.get('repository', {}).get('full_name') == ctx['repository'] and
            current_run.get('head_repository', {}).get('full_name') == ctx['repository'],
            'Current deployment provenance mismatch')
    # The parent run can still be in_progress. Do not fabricate a completed run.
    require(str(release.get('id')) == record['release_id'] and
            release.get('tag_name') == plan['candidate']['version'] and
            release.get('body') == marker(plan) and tagged_sha == plan['release_sha'],
            'Release ID/tag/source/owner-run mismatch')
    return record['release_id']


def digest_record(value):
    import hashlib
    return hashlib.sha256(json.dumps(value, sort_keys=True).encode()).hexdigest()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('command', choices=['gate', 'prepare', 'check', 'record', 'finalize', 'rehearsal-finalize'])
    parser.add_argument('--rehearsal', action='store_true')
    parser.add_argument('--root', type=Path, default=Path('build/candidate'))
    parser.add_argument('--source', type=Path, default=Path('release-source'))
    parser.add_argument('--plan-dir', type=Path, default=Path('build/plan'))
    parser.add_argument('--verification', type=Path, default=Path('build/candidate-verification.json'))
    parser.add_argument('--record', type=Path, default=Path('build/release-record.json'))
    args = parser.parse_args()
    ctx = context()
    if args.command == 'gate':
        run_id = os.environ['CANDIDATE_RUN']
        numeric(run_id, 'candidate run ID')
        run = api(f'actions/runs/{run_id}')
        require(str(run.get('id')) == run_id, 'Candidate API ID mismatch')
        record = verified_candidate(run, args.root, os.environ['VERSION'], ctx, args.rehearsal)
        write_json(args.verification, record)
        if os.environ.get('GITHUB_OUTPUT'):
            with open(os.environ['GITHUB_OUTPUT'], 'a', encoding='utf-8') as output:
                print('source_sha=' + record['source_sha'], file=output)
        return
    if args.command == 'prepare':
        record = read_json(args.verification)
        if not args.rehearsal:
            collision_check(record['version'])
        prepare(args.source, args.root, record, args.plan_dir, ctx, args.rehearsal)
        return
    plan = read_json(args.plan_dir / 'release-plan.json')
    if args.command == 'check':
        check_plan(plan, args.source, args.root, args.plan_dir / 'release.bundle', ctx, args.rehearsal)
        # Also pin actual candidate attempt and provenance, not only local hashes.
        actual = verified_candidate(api(f'actions/runs/{plan["candidate"]["candidate_run_id"]}'),
                                    args.root, plan['candidate']['version'], ctx, args.rehearsal)
        require(actual == plan['candidate'], 'Candidate attempt/provenance changed after gate')
        if not args.rehearsal:
            collision_check(plan['candidate']['version'])
    elif args.command == 'record':
        require(plan['kind'] == 'production' and plan['context'] == ctx, 'Production plan required')
        release_id = os.environ['RELEASE_ID']
        numeric(release_id, 'Release ID')
        write_json(args.record, dict(kind='draft-created', context=ctx, release_id=release_id,
                                    plan_sha256=digest_record(plan)))
    elif args.command == 'rehearsal-finalize':
        require(plan['kind'] == 'rehearsal' and plan['context'] == ctx, 'Rehearsal plan required')
        allowed = True
        try:
            completion_allowed(ctx, json.loads(os.environ['NEEDS']), rehearsal=True)
        except ValueError:
            allowed = False
        write_json(Path('build/rehearsal-result.json'), dict(kind='rehearsal',
                   decision='would_publish' if allowed else 'blocked', context=ctx,
                   prerequisites=json.loads(os.environ['NEEDS']), sdk_publication_writes=0))
        expected = os.environ.get('EXPECTED_DECISION', 'would_publish')
        require(('would_publish' if allowed else 'blocked') == expected, 'Unexpected rehearsal decision')
    else:
        record = read_json(args.record)
        release = api(f'releases/{record["release_id"]}')
        version = plan['candidate']['version']
        command('git', 'fetch', 'origin', f'refs/tags/{version}:refs/tags/{version}', '--depth=1')
        tagged_sha = command('git', 'rev-parse', f'{version}^{{commit}}')
        release_id = finish(plan, record, api(f'actions/runs/{ctx["run_id"]}'), release,
                            ctx, json.loads(os.environ['NEEDS']), tagged_sha)
        actual = verified_candidate(api(f'actions/runs/{plan["candidate"]["candidate_run_id"]}'),
                                    args.root, version, ctx)
        require(actual == plan['candidate'], 'Candidate changed before completion')
        if release['draft']:
            api(f'releases/{release_id}', '--method', 'PATCH', '-F', 'draft=false', '-f', 'make_latest=true')
        write_json(Path('build/completion.json'), dict(kind='release-public', context=ctx,
                   release_id=release_id, device_verification='pending'))


if __name__ == '__main__':
    main()
