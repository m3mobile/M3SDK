"""Audit GitHub onboarding without reading secret values or changing branch policies."""
import argparse
import json
import re
import subprocess


def read(path):
    result = subprocess.run(['gh', 'api', path], capture_output=True, text=True)
    if result.returncode:
        try:
            error = json.loads(result.stdout)
            return dict(available=False, reason=error.get('message', 'API access failed'))
        except ValueError:
            return dict(available=False, reason='API access failed')
    return dict(available=True, data=json.loads(result.stdout))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--repo', required=True)
    args = parser.parse_args()
    if not re.fullmatch(r'[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+', args.repo):
        raise ValueError('GitHub owner/repo required')
    info = read('repos/' + args.repo)
    if not info['available']:
        raise ValueError('Repository unavailable; authenticate with your own account')
    branch = info['data']['default_branch']
    prefix = 'repos/' + args.repo
    permissions = read(prefix + '/actions/permissions')
    protection = read(prefix + '/branches/' + branch + '/protection')
    secrets = read(prefix + '/actions/secrets')
    definition = read(prefix + '/contents/.github/workflows/verify-candidate.yml?ref=' + branch)
    report = dict(repo=args.repo, default_branch=branch, actions=permissions,
                  branch_protection_available=protection['available'],
                  branch_protection_reason=protection.get('reason'),
                  candidate_registered_on_default=definition['available'],
                  secret_names=[item['name'] for item in secrets.get('data', {}).get('secrets', [])],
                  secret_values_read=False,
                  next_step='Review/merge the workflow PR before registering manual release runs and enforcing newly named CI checks.')
    print(json.dumps(report, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    main()
