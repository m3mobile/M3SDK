"""Execute only configured JVM-test and assemble tasks; never install or control a device."""
import argparse
import json
import os
import re
import subprocess
from pathlib import Path


def tasks(config):
    values = config['build_tasks']
    if not isinstance(values, list) or not values:
        raise ValueError('Explicit build tasks required')
    for task in values:
        if not isinstance(task, str) or not re.fullmatch(r':[A-Za-z0-9_:-]+', task):
            raise ValueError('Invalid Gradle task')
        if not re.fullmatch(r'(test[A-Za-z0-9_]*UnitTest|assemble[A-Za-z0-9_]*)', task.rsplit(':',1)[-1]):
            raise ValueError('Only JVM-test and assemble tasks are allowed in this workflow')
    return values


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--config', type=Path, default=Path('workflow/app.json'))
    args = parser.parse_args()
    config = json.loads(args.config.read_text(encoding='utf-8-sig'))
    wrapper = 'gradlew.bat' if os.name == 'nt' else './gradlew'
    subprocess.run([wrapper, '--no-daemon', *tasks(config), '--console=plain'], check=True)


if __name__ == '__main__':
    main()
