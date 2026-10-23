import importlib.util
import shutil
import subprocess
import sys
import tempfile
import unittest
from pathlib import Path
import xml.etree.ElementTree as ET


WORKFLOW = Path(__file__).resolve().parent
spec = importlib.util.spec_from_file_location('xamarin_sources', WORKFLOW / 'check-xamarin-sources.py')
sources = importlib.util.module_from_spec(spec)
spec.loader.exec_module(sources)


class XamarinSourcesTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.project = self.root / 'Sdk.csproj'

    def write_project(self, *includes):
        project = ET.Element('Project', xmlns=sources.NAMESPACE['m'])
        group = ET.SubElement(project, 'ItemGroup')
        for include in includes:
            ET.SubElement(group, 'Compile', Include=include)
        ET.ElementTree(project).write(self.project, encoding='utf-8')

    def write_source(self, relative):
        path = self.root / relative
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text('// fixture\n', encoding='utf-8')

    def test_current_sdk_has_no_omitted_sources(self):
        sources.verify(sources.DEFAULT_PROJECT)

    def test_compile_paths_accept_both_separators_and_windows_case(self):
        self.write_source('Startup/StartUpApi.cs')
        self.write_source('Shared/TimeApi.cs')
        self.write_source('sdk.CS')
        self.write_project('Startup\\StartUpApi.cs', 'Shared/TimeApi.cs', 'Sdk.cs')
        sources.verify(self.project)

    def test_new_top_level_and_nested_sources_must_be_included(self):
        self.write_project('Existing.cs')
        self.write_source('Existing.cs')
        for relative in ('Added.cs', 'KeyTool/Added.cs'):
            with self.subTest(relative=relative):
                self.write_source(relative)
                with self.assertRaisesRegex(ValueError, relative):
                    sources.verify(self.project)
                (self.root / relative).unlink()

    def test_build_outputs_and_optional_generated_version_are_ignored(self):
        self.write_project('Existing.cs')
        for relative in ('Existing.cs', 'bin/Release/Generated.cs', 'obj/Debug/Generated.cs',
                         'Internal/obj/Generated.cs', 'Properties/GeneratedVersionInfo.cs'):
            self.write_source(relative)
        sources.verify(self.project)

    def test_other_generated_sources_are_not_exempt(self):
        self.write_project()
        self.write_source('Properties/GeneratedOther.cs')
        with self.assertRaisesRegex(ValueError, 'Properties/GeneratedOther.cs'):
            sources.verify(self.project)

    def test_cli_reports_missing_source_and_fails(self):
        self.write_project()
        self.write_source('Omitted.cs')
        result = subprocess.run([sys.executable, str(WORKFLOW / 'check-xamarin-sources.py'),
                                 '--project', str(self.project)], capture_output=True, text=True)
        self.assertNotEqual(result.returncode, 0)
        self.assertIn('Legacy Xamarin project omits C# sources: Omitted.cs', result.stderr)


@unittest.skipUnless(shutil.which('pwsh'), 'PowerShell 7 required for candidate gate integration')
class CandidateGateTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        workflow = self.root / 'tools/workflow'
        workflow.mkdir(parents=True)
        for name in ('build-nuget-candidate.ps1', 'check-xamarin-sources.py'):
            shutil.copyfile(WORKFLOW / name, workflow / name)
        self.sdk = self.root / 'dotnet/M3Mobile.M3Sdk.Xamarin'
        (self.sdk / 'Properties').mkdir(parents=True)
        (self.sdk / 'M3Mobile.M3Sdk.Xamarin.csproj').write_text(
            '<Project xmlns="http://schemas.microsoft.com/developer/msbuild/2003">'
            '<ItemGroup><Compile Include="Sdk.cs" /></ItemGroup></Project>', encoding='utf-8')
        (self.sdk / 'Sdk.cs').write_text('// fixture\n', encoding='utf-8')
        self.original_version = b'// original version\r\n'
        self.generated = self.sdk / 'Properties/GeneratedVersionInfo.cs'
        self.generated.write_bytes(self.original_version)
        (self.root / 'build').mkdir()
        self.commands = self.root / 'build/commands.txt'

    def candidate(self):
        # External build/publish tools are replaced with record-only failures.
        # The real Python guard and PowerShell candidate entry point still execute.
        script = '''
$ErrorActionPreference = 'Stop'
function dotnet {
    Add-Content -LiteralPath build/commands.txt -Value "dotnet $args"
    $global:LASTEXITCODE = 37
}
function node {
    Add-Content -LiteralPath build/commands.txt -Value "node $args"
    throw 'Version generation must not start after a failed gate'
}
& ./tools/workflow/build-nuget-candidate.ps1 -Version 0.0.0-ci.test
'''
        return subprocess.run(['pwsh', '-NoLogo', '-NoProfile', '-NonInteractive',
                               '-Command', script], cwd=self.root, capture_output=True,
                              text=True, encoding='utf-8')

    def test_omitted_source_stops_before_contract_test_and_package_build(self):
        (self.sdk / 'Omitted.cs').write_text('// fixture\n', encoding='utf-8')
        result = self.candidate()
        self.assertNotEqual(result.returncode, 0)
        self.assertIn('Legacy Xamarin project omits C# sources: Omitted.cs', result.stderr)
        self.assertFalse(self.commands.exists())
        self.assertFalse((self.root / 'build/candidate').exists())
        self.assertEqual(self.generated.read_bytes(), self.original_version)

    def test_contract_failure_stops_before_version_generation_and_package_build(self):
        result = self.candidate()
        self.assertNotEqual(result.returncode, 0)
        self.assertIn('dotnet failed: 37', result.stderr)
        commands = self.commands.read_text(encoding='utf-8').splitlines()
        self.assertEqual(len(commands), 1)
        self.assertIn('dotnet test dotnet/M3Mobile.M3Sdk.Xamarin.Tests/', commands[0])
        self.assertFalse((self.root / 'build/candidate').exists())
        self.assertEqual(self.generated.read_bytes(), self.original_version)


if __name__ == '__main__':
    unittest.main()
