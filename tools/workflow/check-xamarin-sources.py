"""Reject C# source files omitted from the classic Xamarin project's Compile list."""
import argparse
from pathlib import Path
import xml.etree.ElementTree as ET


DEFAULT_PROJECT = (Path(__file__).resolve().parents[2] /
                   'dotnet/M3Mobile.M3Sdk.Xamarin/M3Mobile.M3Sdk.Xamarin.csproj')
NAMESPACE = {'m': 'http://schemas.microsoft.com/developer/msbuild/2003'}


def verify(project):
    project = Path(project)
    included = {item.attrib['Include'].replace('\\', '/').casefold()
                for item in ET.parse(project).findall('.//m:Compile', NAMESPACE)}
    missing = []
    for path in project.parent.rglob('*'):
        if not path.is_file() or path.suffix.casefold() != '.cs':
            continue
        relative = path.relative_to(project.parent)
        if any(part.casefold() in {'bin', 'obj'} for part in relative.parts[:-1]):
            continue
        name = relative.as_posix()
        # The version writer generates this optional source during candidate builds.
        if name.casefold() not in included and name.casefold() != 'properties/generatedversioninfo.cs':
            missing.append(name)
    if missing:
        raise ValueError('Legacy Xamarin project omits C# sources: ' + ', '.join(sorted(missing)))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--project', type=Path, default=DEFAULT_PROJECT)
    args = parser.parse_args()
    verify(args.project)
    print('Verified legacy Xamarin source inclusion')


if __name__ == '__main__':
    main()
