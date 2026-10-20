"""Release gates must fail closed for missing, stale, or tampered evidence."""
import contextlib
import io
import json
import tempfile
import unittest
from pathlib import Path
from evidence import digest, manifest, validate


class EvidenceTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name) / 'candidate'
        self.root.mkdir()
        self.version = '2.3.17'
        self.sha = 'a' * 40
        (self.root / 'sample.apk').write_bytes(b'tested sample')
        manifest(self.root, 'android', self.sha, '123', self.version)
        dotnet = Path(self.temp.name) / 'dotnet'
        dotnet.mkdir()
        (dotnet / 'sdk.nupkg').write_bytes(b'package')
        manifest(dotnet, 'dotnet', self.sha, '123', self.version)
        for path in dotnet.iterdir():
            (self.root / path.name).write_bytes(path.read_bytes())
        self.record = dict(status='passed', by='Reviewer', reference='test-log:42')
        self.data = dict(source_sha=self.sha, version=self.version, candidate_run_id='123',
                         candidate_manifests={p.name: digest(p) for p in self.root.glob('candidate-*.json')},
                         code_review=self.record.copy(), app_verification=self.record.copy(),
                         impact_review=dict(**self.record, regression_models=['SM25']),
                         manuals={k: self.record.copy() for k in ('app', 'kotlin_ko', 'kotlin_en', 'csharp_ko', 'csharp_en', 'pdf_visual_review')},
                         release_notes=dict(draft=self.record.copy(), excel=dict(status='pending')),
                         pre_release={k: self.record.copy() for k in (
                             'ui', 'broadcast', 'persistence', 'restart_reboot', 'kotlin_package', 'csharp_package')},
                         devices=[dict(model='SM24', os='1.2.2', app_version='1.4.3'),
                                  dict(model='SM25', os='test-os', app_version='1.4.3')],
                         regression=[dict(model='SM25', status='passed', by='Reviewer', reference='test-log:60')])

    def check(self, stage='pre'):
        evidence = self.root.parent / 'evidence.json'
        evidence.write_text(json.dumps(self.data), encoding='utf-8')
        with contextlib.redirect_stdout(io.StringIO()):
            return validate(self.root, evidence, 'm3sdk', stage, self.version, '123')

    def test_complete_pre_release(self):
        self.assertEqual(self.sha, self.check())

    def test_missing_reviews_and_manual_tests_are_blocked(self):
        for field in ('code_review', 'app_verification', 'impact_review', 'devices', 'regression'):
            with self.subTest(field=field):
                saved = self.data.pop(field)
                with self.assertRaises(ValueError):
                    self.check()
                self.data[field] = saved

    def test_every_pre_release_route_is_required(self):
        for key, record in self.data['pre_release'].items():
            with self.subTest(key=key):
                record['status'] = 'pending'
                with self.assertRaises(ValueError):
                    self.check()
                record['status'] = 'passed'

    def test_stale_version_source_run_or_manifest_is_blocked(self):
        for key, value in dict(version='2.3.16', source_sha='b' * 40,
                               candidate_run_id='122', candidate_manifests={}).items():
            with self.subTest(key=key):
                saved = self.data[key]
                self.data[key] = value
                with self.assertRaises(ValueError):
                    self.check()
                self.data[key] = saved

    def test_changed_package_is_blocked(self):
        (self.root / 'sample.apk').write_bytes(b'changed after test')
        with self.assertRaisesRegex(ValueError, 'changed/missing'):
            self.check()

    def test_unlisted_file_is_blocked(self):
        (self.root / 'extra.nupkg').write_bytes(b'unreviewed')
        with self.assertRaisesRegex(ValueError, 'Unexpected files'):
            self.check()

    def test_mixed_source_commits_are_blocked(self):
        path = self.root / 'candidate-dotnet.json'
        data = json.loads(path.read_text())
        data['source_sha'] = 'b' * 40
        path.write_text(json.dumps(data))
        with self.assertRaisesRegex(ValueError, 'Mixed source'):
            self.check()

    def test_regression_skip_needs_explicit_proof(self):
        item = self.data['regression'][0]
        item['status'] = 'skipped'
        with self.assertRaisesRegex(ValueError, 'skip needs'):
            self.check()
        item.update(reason='Unreachable model path', affected_paths=['DeviceBuild.kt'],
                    test_coverage=['ModelSupportTest; CI run 123'])
        self.check()

    def test_omitting_a_reviewed_regression_model_is_blocked(self):
        self.data['impact_review']['regression_models'].append('SL20K')
        with self.assertRaisesRegex(ValueError, 'cover the reviewed'):
            self.check()

    def test_passed_regression_needs_device_identity(self):
        self.data['devices'] = self.data['devices'][:1]
        with self.assertRaisesRegex(ValueError, 'device regression needs'):
            self.check()

    def test_pre_release_pass_does_not_mean_complete(self):
        self.check()
        with self.assertRaises(ValueError):
            self.check('complete')

    def test_completion_needs_public_package_and_device_restore(self):
        self.data.update(published_packages=self.record.copy(),
                         post_release={k: self.record.copy() for k in ('kotlin_device', 'csharp_device')},
                         device_restored=self.record.copy())
        self.data['release_notes']['excel'] = dict(**self.record, workbook='example.xlsx', sheet='Example', range='A7:G8', verification='owner-confirmed:test')
        self.check('complete')
        self.data['device_restored']['status'] = 'pending'
        with self.assertRaises(ValueError):
            self.check('complete')

    def test_manual_review_and_note_draft_are_required_before_release(self):
        for key in ('app', 'kotlin_ko', 'kotlin_en', 'csharp_ko', 'csharp_en', 'pdf_visual_review'):
            with self.subTest(key=key):
                self.data['manuals'][key]['status'] = 'pending'
                with self.assertRaises(ValueError):
                    self.check()
                self.data['manuals'][key]['status'] = 'passed'
        self.data['release_notes']['draft']['status'] = 'pending'
        with self.assertRaises(ValueError):
            self.check()

    def test_no_applicable_route_requires_reviewed_scope_and_reason(self):
        item = dict(status='not_applicable', by='Reviewer', reference='review:42', reason='No UI in this API')
        self.data['pre_release']['ui'] = item
        with self.assertRaises(ValueError):
            self.check()
        self.data['impact_review']['not_applicable_checks'] = ['pre_release.ui']
        self.check()
        del item['reason']
        with self.assertRaises(ValueError):
            self.check()

    def test_excel_pending_is_not_completion(self):
        self.data.update(published_packages=self.record.copy(), device_restored=self.record.copy(),
                         post_release={k: self.record.copy() for k in ('kotlin_device', 'csharp_device')})
        self.check()
        with self.assertRaises(ValueError):
            self.check('complete')
        self.data['release_notes']['excel'] = self.record.copy()
        with self.assertRaisesRegex(ValueError, 'Excel completion needs'):
            self.check('complete')

    def test_generic_app_manifest_is_supported(self):
        manifest(self.root, 'app', self.sha, '123', self.version)
        for name in ('android', 'dotnet'):
            (self.root / f'candidate-{name}.json').unlink()
        self.data['candidate_manifests'] = {'candidate-app.json': digest(self.root / 'candidate-app.json')}
        evidence = self.root.parent / 'evidence.json'
        evidence.write_text(json.dumps(self.data))
        with contextlib.redirect_stdout(io.StringIO()):
            validate(self.root, evidence, 'app', 'pre', self.version, '123')

    def test_manifest_path_traversal_is_blocked(self):
        path = self.root / 'candidate-android.json'
        item = json.loads(path.read_text())
        item['files'] = {'../outside.apk': '0' * 64}
        path.write_text(json.dumps(item))
        self.data['candidate_manifests'][path.name] = digest(path)
        with self.assertRaisesRegex(ValueError, 'escapes root'):
            self.check()

    def test_keytool_requires_only_keytool_manifest(self):
        manifest(self.root, 'keytool', self.sha, '123', self.version)
        (self.root / 'candidate-android.json').unlink()
        (self.root / 'candidate-dotnet.json').unlink()
        self.data['candidate_manifests'] = {'candidate-keytool.json': digest(self.root / 'candidate-keytool.json')}
        evidence = self.root.parent / 'evidence.json'
        evidence.write_text(json.dumps(self.data))
        with contextlib.redirect_stdout(io.StringIO()):
            self.assertEqual(self.sha, validate(self.root, evidence, 'keytool', 'pre', self.version, '123'))


if __name__ == '__main__':
    unittest.main()
