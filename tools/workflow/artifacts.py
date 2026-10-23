"""Candidate file integrity and automatic delivery records. No human test claims."""
import argparse
import hashlib
import json
import re
from pathlib import Path, PurePosixPath


MANIFEST_NAMES = {f'candidate-{name}.json' for name in ('android', 'dotnet', 'keytool', 'app')}


def require(condition, message):
    if not condition:
        raise ValueError(message)


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def numeric(value, label):
    require(re.fullmatch(r'[1-9][0-9]*', str(value)) is not None, f'Invalid {label}')


def metadata(sha, run, version):
    require(isinstance(sha, str) and re.fullmatch(r'[0-9a-f]{40}', sha), 'Full source SHA required')
    numeric(run, 'candidate run ID')
    require(isinstance(version, str) and
            re.fullmatch(r'[0-9]+\.[0-9]+\.[0-9]+(?:-[0-9A-Za-z.-]+)?', version),
            'Invalid candidate version')


def read_json(path):
    def unique(pairs):
        data = {}
        for key, value in pairs:
            require(key not in data, f'Duplicate JSON key: {key}')
            data[key] = value
        return data
    return json.loads(path.read_text(encoding='utf-8-sig'), object_pairs_hook=unique)


def write_json(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + '\n', encoding='utf-8')


def entries(root):
    require(root.is_dir() and not root.is_symlink(), 'Candidate root must be a directory, not a link')
    paths = sorted(root.rglob('*'))
    for path in paths:
        require(not path.is_symlink() and path.resolve().is_relative_to(root.resolve()),
                f'Artifact link/path forbidden: {path}')
    return paths


def manifest(root, name, sha, run, version):
    require(name in ('android', 'dotnet', 'keytool', 'app'), 'Unknown manifest kind')
    metadata(sha, run, version)
    files = {p.relative_to(root).as_posix(): digest(p) for p in entries(root)
             if p.is_file() and not (p.parent == root and p.name in MANIFEST_NAMES)}
    require(files, 'Empty candidate')
    write_json(root / f'candidate-{name}.json',
               dict(source_sha=sha, candidate_run_id=str(run), version=version, files=files))


def validate_files(root, kind, version, run, source_sha=None):
    require(kind in ('m3sdk', 'app', 'keytool'), 'Unknown candidate kind')
    paths = entries(root)
    names = ('android', 'dotnet') if kind == 'm3sdk' else (kind,)
    manifests, files = {}, {}
    for name in names:
        path = root / f'candidate-{name}.json'
        item = read_json(path)
        require(isinstance(item, dict), 'Manifest must be an object')
        metadata(item.get('source_sha'), item.get('candidate_run_id'), item.get('version'))
        require(item['version'] == version, 'Candidate version mismatch')
        require(str(item['candidate_run_id']) == str(run), 'Candidate run mismatch')
        source_sha = source_sha or item['source_sha']
        require(item['source_sha'] == source_sha, 'Mixed/unexpected source commits')
        require(isinstance(item.get('files'), dict) and item['files'], 'Empty/invalid manifest files')
        for relative, expected in item['files'].items():
            logical = PurePosixPath(relative)
            require(relative and logical.as_posix() == relative and not logical.is_absolute() and
                    '..' not in logical.parts and '\\' not in relative and ':' not in relative and
                    '\x00' not in relative, 'Artifact path escapes root or is not canonical')
            require(relative not in MANIFEST_NAMES, 'Manifest cannot declare a manifest as payload')
            require(relative not in files, f'Overlapping candidate artifacts: {relative}')
            require(isinstance(expected, str) and re.fullmatch(r'[0-9a-f]{64}', expected),
                    f'Invalid file hash: {relative}')
            target = root / relative
            require(target.resolve().is_relative_to(root.resolve()), 'Artifact path escapes root')
            require(target.is_file() and digest(target) == expected, f'Artifact changed/missing: {relative}')
            files[relative] = expected
        manifests[path.name] = digest(path)
    actual = {p.relative_to(root).as_posix() for p in paths if p.is_file()}
    require(actual == set(files) | set(manifests), 'Unexpected files in candidate artifacts')
    return dict(source_sha=source_sha, version=version, candidate_run_id=str(run),
                candidate_manifests=manifests, files=files)


def output_path(path, root):
    require(not path.resolve().is_relative_to(root.resolve()), 'Record output must be outside candidate root')


def candidate_record(data):
    require(isinstance(data, dict) and type(data.get('schema_version')) is int and
            data['schema_version'] == 1, 'Candidate record schema required')
    metadata(data.get('source_sha'), data.get('candidate_run_id'), data.get('version'))
    require(data.get('verification_scope') == 'workflow_provenance_and_artifact_integrity',
            'Candidate machine verification required')
    require(isinstance(data.get('candidate_manifests'), dict) and
            set(data['candidate_manifests']) == {'candidate-android.json', 'candidate-dotnet.json'},
            'Both candidate manifests required in record')
    require(isinstance(data.get('files'), dict) and data['files'], 'Candidate file hashes required')
    require(all(isinstance(value, str) and re.fullmatch(r'[0-9a-f]{64}', value)
                for group in ('candidate_manifests', 'files') for value in data[group].values()),
            'Invalid candidate record hash')
    provenance = data.get('provenance', {})
    require(provenance.get('workflow') == '.github/workflows/verify-candidate.yml' and
            provenance.get('head_branch') == 'develop' and provenance.get('event') == 'workflow_dispatch' and
            provenance.get('status') == 'completed' and provenance.get('conclusion') == 'success',
            'Successful develop candidate provenance required')
    require(provenance.get('run_id') == data['candidate_run_id'] and
            provenance.get('source_sha') == data['source_sha'], 'Candidate record provenance mismatch')
    return data


def delivery_record(verification, deployment_run, workflow_sha, release_sha):
    candidate_record(verification)
    numeric(deployment_run, 'deployment run ID')
    metadata(workflow_sha, deployment_run, verification['version'])
    metadata(release_sha, deployment_run, verification['version'])
    return dict(schema_version=1, deployment_run_id=str(deployment_run), workflow_sha=workflow_sha,
                publication_state='published', version=verification['version'],
                candidate_run_id=verification['candidate_run_id'], source_sha=verification['source_sha'],
                release_sha=release_sha, candidate_verification=verification)


def delivered_identity(record, deployment):
    require(isinstance(record, dict) and type(record.get('schema_version')) is int and
            record['schema_version'] == 1, 'Delivery record schema required')
    require(record.get('publication_state') == 'published', 'Published delivery record required')
    require(record.get('deployment_run_id') == deployment['run_id'], 'Delivery run mismatch')
    require(record.get('workflow_sha') == deployment['source_sha'], 'Delivery workflow SHA mismatch')
    require(deployment.get('workflow') == '.github/workflows/deployment.yml', 'Deployment provenance required')
    verified = candidate_record(record.get('candidate_verification'))
    require(deployment.get('status') == 'completed' and deployment.get('conclusion') == 'success' and
            deployment.get('repository') == verified['provenance'].get('repository'),
            'Successful deployment in the candidate repository required')
    for key in ('source_sha', 'version', 'candidate_run_id'):
        require(record.get(key) == verified[key], f'Delivered {key} mismatch')
    metadata(record.get('release_sha'), record['deployment_run_id'], record['version'])
    return {key: record[key] for key in ('version', 'candidate_run_id', 'source_sha', 'release_sha')}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    commands = parser.add_subparsers(dest='command', required=True)
    create = commands.add_parser('manifest')
    create.add_argument('--root', type=Path, required=True)
    create.add_argument('--name', choices=['android', 'dotnet', 'app', 'keytool'], required=True)
    create.add_argument('--source-sha', required=True)
    create.add_argument('--run-id', required=True)
    create.add_argument('--version', required=True)
    delivery = commands.add_parser('record-delivery')
    delivery.add_argument('--verification', type=Path, required=True)
    delivery.add_argument('--deployment-run-id', required=True)
    delivery.add_argument('--workflow-sha', required=True)
    delivery.add_argument('--release-sha', required=True)
    delivery.add_argument('--output', type=Path, required=True)
    read = commands.add_parser('read-delivery')
    read.add_argument('--record', type=Path, required=True)
    read.add_argument('--provenance', type=Path, required=True)
    read.add_argument('--verification-output', type=Path, required=True)
    read.add_argument('--github-output', type=Path, required=True)
    args = parser.parse_args()
    if args.command == 'manifest':
        manifest(args.root, args.name, args.source_sha, args.run_id, args.version)
    elif args.command == 'record-delivery':
        write_json(args.output, delivery_record(read_json(args.verification), args.deployment_run_id,
                                               args.workflow_sha, args.release_sha))
    else:
        record = read_json(args.record)
        identity = delivered_identity(record, read_json(args.provenance))
        write_json(args.verification_output, record['candidate_verification'])
        with args.github_output.open('a', encoding='utf-8') as output:
            for key, value in identity.items():
                print(f'{key}={value}', file=output)


if __name__ == '__main__':
    main()
