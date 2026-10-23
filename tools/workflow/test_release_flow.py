"""Release transport, production/rehearsal separation, and same-run failure gates."""
import copy
import os
import shutil
import subprocess
import tempfile
import unittest
import zipfile
from pathlib import Path
from artifacts import candidate_record, manifest, read_json
from release_flow import (check_plan, command, completion_allowed, digest_record,
                          finish, marker, prepare, verified_candidate)


class ReleaseFlowTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.base = Path(self.temp.name)
        self.source = self.base / 'source'
        self.source.mkdir()
        self.git('init')
        self.git('config', 'user.name', 'Test')
        self.git('config', 'user.email', 'test@example.invalid')
        self.git('config', 'core.autocrlf', 'false')
        script = self.source / 'tools/release/prepare-release.js'
        script.parent.mkdir(parents=True)
        shutil.copyfile(Path(__file__).parents[1] / 'release/prepare-release.js', script)
        (self.source / 'README.md').write_text('com.github.m3mobile:M3SDK:2.4.0\n')
        (self.source / 'docs').mkdir()
        (self.source / 'docs/manual.md').write_text('Manual\n')
        readme = self.source / 'dotnet/M3Mobile.M3Sdk.Xamarin/README.md'
        readme.parent.mkdir(parents=True)
        readme.write_text('Package\n')
        self.git('add', '.')
        self.git('commit', '-m', 'Candidate')
        self.sha = self.git('rev-parse', 'HEAD')
        self.ctx = dict(run_id='456', workflow_sha=self.sha, ref='refs/heads/develop',
                        event='workflow_dispatch', repository='m3mobile/M3SDK', mode='publish')
        self.run = dict(id=123, run_attempt=1, head_sha=self.sha, head_branch='develop',
                        path='.github/workflows/verify-candidate.yml', event='workflow_dispatch',
                        status='completed', conclusion='success',
                        repository=dict(full_name=self.ctx['repository']),
                        head_repository=dict(full_name=self.ctx['repository']))
        self.root = self.base / 'candidate'
        self.root.mkdir()
        self.version = '2.4.1'
        for name in ('M3SDK_Manual_en', 'M3SDK_Manual_kr', 'M3SDK_Xamarin_Manual_en', 'M3SDK_Xamarin_Manual_kr'):
            self.file(f'manuals/{name}_v{self.version}.pdf', b'%PDF test')
        self.file('samples/sample.apk', b'android')
        self.file('maven/net/m3/sdk.aar', b'aar')
        manifest(self.root, 'android', self.sha, '123', self.version)
        dotnet = self.base / 'dotnet'
        (dotnet / 'nuget').mkdir(parents=True)
        with zipfile.ZipFile(dotnet / f'nuget/M3Mobile.M3Sdk.Xamarin.{self.version}.nupkg', 'w') as archive:
            archive.writestr('M3Mobile.M3Sdk.Xamarin.nuspec',
                            f'<package><metadata><id>M3Mobile.M3Sdk.Xamarin</id><version>{self.version}</version></metadata></package>')
        (dotnet / 'dotnet-samples').mkdir()
        (dotnet / 'dotnet-samples/sample.apk').write_bytes(b'dotnet')
        manifest(dotnet, 'dotnet', self.sha, '123', self.version)
        shutil.copytree(dotnet, self.root, dirs_exist_ok=True)
        self.verification = verified_candidate(self.run, self.root, self.version, self.ctx)
        self.destination = self.base / 'plan'
        self.needs = {job: dict(result='success') for job in ('gate', 'prepare', 'publish', 'verify-public')}

    def git(self, *args):
        return command('git', *args, cwd=self.source)

    def file(self, name, content):
        path = self.root / name
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(content)

    def plan(self, rehearsal=False):
        record = self.verification if not rehearsal else verified_candidate(
            self.run, self.root, self.version, self.ctx, rehearsal=True)
        return prepare(self.source, self.root, record, self.destination, self.ctx, rehearsal)

    def test_real_prepare_bundle_roundtrip_preserves_commit_and_exact_assets(self):
        plan = self.plan()
        restored = self.base / 'restored'
        command('git', 'clone', str(self.destination / 'release.bundle'), str(restored))
        check_plan(plan, restored, self.root, self.destination / 'release.bundle', self.ctx)
        self.assertNotEqual(plan['release_sha'], self.sha)
        self.assertIn('2.4.1', (restored / 'README.md').read_text())
        self.assertEqual(set(plan['assets']), {name for name in self.verification['files']
                                             if name.startswith(('manuals/', 'nuget/'))})

    def test_modified_payload_bundle_or_context_cannot_pass_check(self):
        plan = self.plan()
        package = self.root / f'nuget/M3Mobile.M3Sdk.Xamarin.{self.version}.nupkg'
        before = package.read_bytes()
        package.write_bytes(b'changed')
        with self.assertRaisesRegex(ValueError, 'changed/missing'):
            check_plan(plan, self.source, self.root, self.destination / 'release.bundle', self.ctx)
        package.write_bytes(before)
        for ctx in (dict(self.ctx, run_id='457'), dict(self.ctx, workflow_sha='b' * 40)):
            with self.assertRaises(ValueError):
                check_plan(plan, self.source, self.root, self.destination / 'release.bundle', ctx)
        with (self.destination / 'release.bundle').open('ab') as output:
            output.write(b'tampered')
        with self.assertRaisesRegex(ValueError, 'bundle changed'):
            check_plan(plan, self.source, self.root, self.destination / 'release.bundle', self.ctx)

    def test_dirty_source_and_non_documentation_commits_are_rejected(self):
        self.file('extra', b'test')  # Extra artifacts are rejected before any publication.
        with self.assertRaises(ValueError):
            self.plan()
        (self.root / 'extra').unlink()
        (self.source / 'product.cs').write_text('modified')
        with self.assertRaisesRegex(ValueError, 'Clean source'):
            self.plan()
        self.git('add', '.')
        self.git('commit', '-m', 'Unexpected product change')
        self.verification['source_sha'] = self.git('rev-parse', 'HEAD')
        with self.assertRaises(ValueError):
            self.plan()

    def test_missing_manual_and_wrong_nuspec_are_rejected_even_with_rewritten_manifest(self):
        dotnet = self.base / 'dotnet'
        with zipfile.ZipFile(dotnet / f'nuget/M3Mobile.M3Sdk.Xamarin.{self.version}.nupkg', 'w') as archive:
            archive.writestr('sdk.nuspec', '<package><metadata><id>Wrong</id><version>2.4.1</version></metadata></package>')
        manifest(dotnet, 'dotnet', self.sha, '123', self.version)
        shutil.copytree(dotnet, self.root, dirs_exist_ok=True)
        self.verification = verified_candidate(self.run, self.root, self.version, self.ctx)
        with self.assertRaisesRegex(ValueError, 'NuGet identity'):
            self.plan()

    def test_every_actual_failure_cancel_skip_missing_blocks_both_completion_modes(self):
        for rehearsal in (False, True):
            jobs = ('gate', 'prepare', 'sink', 'android', 'dotnet') if rehearsal else tuple(self.needs)
            for job in jobs:
                for result in ('failure', 'cancelled', 'skipped', None):
                    needs = {name: dict(result='success') for name in jobs}
                    needs[job]['result'] = result
                    with self.subTest(rehearsal=rehearsal, job=job, result=result), self.assertRaises(ValueError):
                        completion_allowed(dict(self.ctx, mode='rehearsal' if rehearsal else 'publish'), needs, rehearsal)

    def test_dry_run_and_non_manual_or_non_develop_cannot_publish(self):
        for changes in (dict(mode='dry-run'), dict(mode='rehearsal'), dict(event='push'),
                        dict(ref='refs/heads/ci/test'), dict(ref='refs/tags/2.4.1')):
            with self.subTest(changes=changes), self.assertRaises(ValueError):
                completion_allowed(dict(self.ctx, **changes), self.needs)

    def test_rehearsal_branch_policy_does_not_relax_production(self):
        self.ctx['ref'] = 'refs/heads/ci/test'
        self.run['head_branch'] = 'ci/test'
        record = verified_candidate(self.run, self.root, self.version, self.ctx, True)
        with self.assertRaises(ValueError):
            candidate_record(record)
        with self.assertRaises(ValueError):
            verified_candidate(self.run, self.root, self.version, self.ctx)
        for key, value in dict(head_sha='b' * 40, head_branch='ci/other',
                               head_repository=dict(full_name='fork/M3SDK')).items():
            run = dict(self.run, **{key: value})
            with self.subTest(key=key), self.assertRaises(ValueError):
                verified_candidate(run, self.root, self.version, self.ctx, True)

    def test_rehearsal_plan_is_never_accepted_as_production(self):
        plan = self.plan(True)
        with self.assertRaisesRegex(ValueError, 'plan kind'):
            check_plan(plan, self.source, self.root, self.destination / 'release.bundle', self.ctx)

    def finish_data(self):
        plan = self.plan()
        record = dict(kind='draft-created', context=self.ctx, release_id='789', plan_sha256=digest_record(plan))
        run = dict(self.run, id=456, path='.github/workflows/deployment.yml', status='in_progress', conclusion=None)
        release = dict(id=789, tag_name=self.version, body=marker(plan), draft=True)
        return plan, record, run, release

    def test_same_run_in_progress_can_finish_and_repeat_without_republishing(self):
        plan, record, run, release = self.finish_data()
        for draft in (True, False):
            release['draft'] = draft
            self.assertEqual(finish(plan, record, run, release, self.ctx, self.needs, plan['release_sha']), '789')
        self.assertEqual(run['status'], 'in_progress')

    def test_existing_release_wrong_id_tag_sha_run_and_marker_are_rejected(self):
        plan, record, run, release = self.finish_data()
        for changes in (dict(id=419108633), dict(tag_name='2.4.0'), dict(body='Existing release')):
            with self.subTest(changes=changes), self.assertRaises(ValueError):
                finish(plan, record, run, dict(release, **changes), self.ctx, self.needs, plan['release_sha'])
        for invalid in (dict(run, id=455), dict(run, head_branch='ci/test'), dict(run, event='push')):
            with self.assertRaises(ValueError):
                finish(plan, record, invalid, release, self.ctx, self.needs, plan['release_sha'])
        with self.assertRaises(ValueError):
            finish(plan, record, run, release, self.ctx, self.needs, 'b' * 40)


if __name__ == '__main__':
    unittest.main()
