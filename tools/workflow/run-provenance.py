"""Verify completed dispatch provenance before consuming release artifacts."""
import argparse
import json
import os
import subprocess
from pathlib import Path


def require(condition, message):
    if not condition:
        raise ValueError(message)


def verify(run, workflow, run_id, root=None):
    require(run['path'] == '.github/workflows/' + workflow, 'Wrong workflow')
    require(run['event'] == 'workflow_dispatch', 'Explicit dispatch required')
    require(run['status'] == 'completed' and run['conclusion'] == 'success', 'Workflow did not pass')
    if workflow == 'verify-candidate.yml':
        require(root is not None, 'Candidate artifact root required')
        manifests = list(root.glob('candidate-*.json'))
        require(manifests, 'Candidate manifest missing')
        for path in manifests:
            data = json.loads(path.read_text(encoding='utf-8'))
            require(data['source_sha'] == run['head_sha'], 'Manifest source is not the tested dispatch commit')
            require(str(data['candidate_run_id']) == run_id, 'Manifest belongs to another run')


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--run-id', required=True)
    parser.add_argument('--workflow', required=True)
    parser.add_argument('--root', type=Path)
    args = parser.parse_args()
    require(args.run_id.isdigit(), 'Numeric run ID required')
    run = json.loads(subprocess.check_output([
        'gh', 'api', f'repos/{os.environ["GH_REPO"]}/actions/runs/{args.run_id}'
    ], text=True))
    verify(run, args.workflow, args.run_id, args.root)
    print('Verified workflow run', args.run_id, run['head_sha'])


if __name__ == '__main__':
    main()
