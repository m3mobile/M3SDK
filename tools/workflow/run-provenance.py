"""Verify completed dispatch provenance before consuming release artifacts."""
import argparse
import json
import os
import subprocess
from pathlib import Path
from artifacts import (candidate_record, metadata, numeric, output_path, read_json,
                       require, validate_files, write_json)


def verify(run, workflow, run_id, root=None, version=None, repo=None):
    numeric(run_id, 'run ID')
    require(workflow in ('verify-candidate.yml', 'deployment.yml'), 'Unsupported release workflow')
    require(str(run.get('id')) == str(run_id), 'Run ID mismatch')
    require(run['path'] == '.github/workflows/' + workflow, 'Wrong workflow')
    require(run['event'] == 'workflow_dispatch', 'Explicit dispatch required')
    require(run['status'] == 'completed' and run['conclusion'] == 'success', 'Workflow did not pass')
    require(run.get('head_branch') == 'develop', 'Release workflow must run on develop')
    require(repo and run.get('repository', {}).get('full_name') == repo and
            run.get('head_repository', {}).get('full_name') == repo, 'Wrong repository or fork')
    numeric(run.get('run_attempt'), 'run attempt')
    metadata(run.get('head_sha'), run_id, '0.0.0')
    record = dict(schema_version=1, repository=repo, run_id=str(run_id),
                  run_attempt=run['run_attempt'], workflow=run['path'], event=run['event'],
                  head_branch=run['head_branch'], source_sha=run['head_sha'],
                  status=run['status'], conclusion=run['conclusion'])
    if workflow == 'verify-candidate.yml':
        require(root is not None, 'Candidate artifact root required')
        identity = validate_files(root, 'm3sdk', version, run_id, run['head_sha'])
        return dict(schema_version=1, verification_scope='workflow_provenance_and_artifact_integrity',
                    provenance=record, **identity)
    return record


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--run-id', required=True)
    parser.add_argument('--workflow', choices=['verify-candidate.yml', 'deployment.yml'], required=True)
    parser.add_argument('--root', type=Path)
    parser.add_argument('--version')
    parser.add_argument('--output', type=Path, required=True)
    parser.add_argument('--expected-record', type=Path)
    parser.add_argument('--github-output', type=Path)
    args = parser.parse_args()
    numeric(args.run_id, 'run ID')
    run = json.loads(subprocess.check_output([
        'gh', 'api', f'repos/{os.environ["GH_REPO"]}/actions/runs/{args.run_id}'
    ], text=True))
    record = verify(run, args.workflow, args.run_id, args.root, args.version, os.environ['GH_REPO'])
    if args.root is not None:
        output_path(args.output, args.root)
    if args.expected_record:
        expected = candidate_record(read_json(args.expected_record))
        require(record == expected, 'Candidate verification record changed since gate/deployment')
    write_json(args.output, record)
    if args.github_output:
        with args.github_output.open('a', encoding='utf-8') as output:
            print('source_sha=' + record['source_sha'], file=output)
    print('Verified workflow run', args.run_id, run['head_sha'])


if __name__ == '__main__':
    main()
