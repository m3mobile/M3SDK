"""Read-only onboarding checks. Never modifies user settings, workbooks, or devices."""
import argparse
import json
import shutil
import subprocess
from pathlib import Path


def inspect_root(root):
    if root is None:
        return None
    root = root.resolve()
    if not (root/'gradlew').exists():
        raise ValueError(f'Not an Android Gradle root: {root}')
    version = subprocess.check_output(['git', '-C', str(root), 'rev-parse', 'HEAD'], text=True).strip()
    return dict(path=str(root), source_sha=version)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--app-root', type=Path)
    parser.add_argument('--sdk-root', type=Path)
    parser.add_argument('--history-dir', type=Path)
    args = parser.parse_args()
    tools = {tool: bool(shutil.which(tool)) for tool in ('git', 'java', 'python', 'gh', 'dotnet', 'msbuild', 'nuget')}
    excel = 'manual_unconfigured'
    if args.history_dir:
        excel = 'manual_file_found_sync_unverified' if (args.history_dir/'M3_APP_RELEASE_NOTE.xlsx').is_file() else 'manual_file_missing'
    print(json.dumps(dict(app=inspect_root(args.app_root), sdk=inspect_root(args.sdk_root), tools=tools,
                          excel=excel, device_access='not_performed',
                          notes='Missing Windows Xamarin/.NET tools may be handled by the package CI; no local C# pass is implied.'), ensure_ascii=False, indent=2))
    if not all(tools[k] for k in ('git', 'java', 'python')):
        raise SystemExit(1)


if __name__ == '__main__':
    main()
