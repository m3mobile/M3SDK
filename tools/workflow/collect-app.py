"""Collect configured signed Android variants from output metadata, with no device access."""
import argparse
import json
import os
import shutil
from pathlib import Path
from evidence import manifest


def collect(config, root):
    version = None
    names = set()
    for variant in config['variants']:
        name = variant['name']
        if not name.replace('-', '').replace('_', '').isalnum() or name in names:
            raise ValueError('Unique simple variant names required')
        names.add(name)
        metadata_path = Path(variant['metadata'])
        if not metadata_path.resolve().is_relative_to(Path.cwd().resolve()):
            raise ValueError('Metadata must stay within the app repository')
        data = json.loads(metadata_path.read_text(encoding='utf-8-sig'))
        if data['applicationId'] != config['package_name'] or len(data['elements']) != 1:
            raise ValueError('Expected one APK of the configured application')
        element = data['elements'][0]
        version = version or element['versionName']
        if element['versionName'] != version:
            raise ValueError('Variants have different versions')
        apk = metadata_path.parent/element['outputFile']
        if not apk.resolve().is_relative_to(metadata_path.parent.resolve()) or not apk.is_file() or 'unsigned' in apk.name:
            raise ValueError('Expected signed APK inside the variant directory')
        dest = root/'app'/name
        dest.mkdir(parents=True, exist_ok=True)
        shutil.copy2(apk, dest/apk.name)
    if not names:
        raise ValueError('At least one variant required')
    manifest(root, 'app', os.environ['GITHUB_SHA'], os.environ['GITHUB_RUN_ID'], version)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--config', type=Path, default=Path('workflow/app.json'))
    parser.add_argument('--output', type=Path, default=Path('build/candidate'))
    args = parser.parse_args()
    collect(json.loads(args.config.read_text(encoding='utf-8-sig')), args.output)


if __name__ == '__main__':
    main()
