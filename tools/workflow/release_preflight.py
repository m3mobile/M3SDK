"""Check production environment access. No tag, package, or Release writes."""
import argparse
import os
from pathlib import Path
from artifacts import metadata, numeric, require, write_json


def availability(environment):
    require(environment.get('GITHUB_EVENT_NAME') == 'workflow_dispatch', 'Manual preflight required')
    require(environment.get('GITHUB_REF') == 'refs/heads/develop', 'Preflight requires develop')
    require(environment.get('GITHUB_REPOSITORY') == 'm3mobile/M3SDK', 'Wrong preflight repository')
    metadata(environment.get('GITHUB_SHA'), environment.get('GITHUB_RUN_ID'), '0.0.0')
    numeric(environment.get('GITHUB_RUN_ATTEMPT'), 'preflight attempt')
    require(bool(environment.get('NUGET_API_KEY', '').strip()), 'NUGET_API_KEY is unavailable')
    return dict(kind='sdk-release-preflight', repository=environment['GITHUB_REPOSITORY'],
                run_id=environment['GITHUB_RUN_ID'], run_attempt=environment['GITHUB_RUN_ATTEMPT'],
                source_sha=environment['GITHUB_SHA'], ref=environment['GITHUB_REF'],
                environment='sdk-release', environment_access='available', nuget_key_available=True,
                nuget_upload_authorization='not_verified', github_release_write='not_verified',
                sdk_publication_writes=0, device_verification='pending')


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    write_json(args.output, availability(os.environ))
    print('Environment access verified. Actual publication authorization remains unverified.')


if __name__ == '__main__':
    main()
