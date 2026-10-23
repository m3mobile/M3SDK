"""SDK promotion binds trusted run provenance to machine-recorded file hashes."""
import contextlib
import importlib.util
import io
import json
import os
import sys
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch
import artifacts
from artifacts import delivered_identity, delivery_record, digest, manifest, read_json, write_json

spec = importlib.util.spec_from_file_location('provenance', Path(__file__).with_name('run-provenance.py'))
provenance = importlib.util.module_from_spec(spec)
spec.loader.exec_module(provenance)


class ProvenanceTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.base = Path(self.temp.name)
        self.root = self.base / 'candidate'
        self.root.mkdir()
        self.version, self.sha, self.repo = '2.4.1', 'a' * 40, 'm3mobile/M3SDK'
        self.run = dict(id=123, run_attempt=1, path='.github/workflows/verify-candidate.yml',
                        event='workflow_dispatch', status='completed', conclusion='success',
                        head_sha=self.sha, head_branch='develop', repository=dict(full_name=self.repo),
                        head_repository=dict(full_name=self.repo))
        (self.root / 'sample.apk').write_bytes(b'android sample')
        manifest(self.root, 'android', self.sha, '123', self.version)
        dotnet = self.base / 'dotnet'
        dotnet.mkdir()
        (dotnet / 'sdk.nupkg').write_bytes(b'nuget package')
        manifest(dotnet, 'dotnet', self.sha, '123', self.version)
        for path in dotnet.iterdir():
            (self.root / path.name).write_bytes(path.read_bytes())

    def check(self):
        return provenance.verify(self.run, 'verify-candidate.yml', '123', self.root, self.version, self.repo)

    def mutate_manifest(self, **changes):
        path = self.root / 'candidate-dotnet.json'
        write_json(path, dict(read_json(path), **changes))

    def cli(self, *extra):
        args = ['run-provenance.py', '--workflow', 'verify-candidate.yml', '--run-id', '123',
                '--root', str(self.root), '--version', self.version,
                '--output', str(self.base / 'verification.json'), *map(str, extra)]
        with patch.object(sys, 'argv', args), patch.dict(os.environ, GH_REPO=self.repo), \
                patch.object(provenance.subprocess, 'check_output', return_value=json.dumps(self.run)) as api, \
                contextlib.redirect_stdout(io.StringIO()):
            provenance.main()
            api.assert_called_once_with(['gh', 'api', 'repos/m3mobile/M3SDK/actions/runs/123'], text=True)

    def test_matching_dispatch_records_integrity_without_human_claims(self):
        record = self.check()
        self.assertEqual((record['source_sha'], record['candidate_run_id'], record['version']),
                         (self.sha, '123', self.version))
        self.assertEqual(record['candidate_manifests'], {
            name: digest(self.root / name) for name in ('candidate-android.json', 'candidate-dotnet.json')})
        self.assertEqual(set(record['files']), {'sample.apk', 'sdk.nupkg'})
        self.assertEqual(record['provenance']['head_branch'], 'develop')
        for human in ('devices', 'pre_release', 'post_release', 'code_review', 'app_verification'):
            self.assertNotIn(human, record)

    def test_wrong_workflow_event_status_branch_sha_id_attempt_or_repository_is_rejected(self):
        changes = dict(id=122, path='.github/workflows/other.yml', event='pull_request',
                       status='in_progress', conclusion='failure', head_branch='feature/test',
                       head_sha='not-a-sha', run_attempt=0, repository=dict(full_name='fork/M3SDK'),
                       head_repository=dict(full_name='fork/M3SDK'))
        for key, value in changes.items():
            with self.subTest(key=key):
                saved = self.run[key]
                self.run[key] = value
                with self.assertRaises(ValueError):
                    self.check()
                self.run[key] = saved

    def test_manifest_commit_version_and_run_are_bound_to_dispatch(self):
        original = (self.root / 'candidate-dotnet.json').read_bytes()
        for key, value in dict(source_sha='b' * 40, candidate_run_id='122', version='2.4.0').items():
            with self.subTest(key=key):
                self.mutate_manifest(**{key: value})
                with self.assertRaises(ValueError):
                    self.check()
                (self.root / 'candidate-dotnet.json').write_bytes(original)

    def test_both_platform_manifests_are_required(self):
        for name in ('android', 'dotnet'):
            path = self.root / f'candidate-{name}.json'
            original = path.read_bytes()
            path.unlink()
            with self.subTest(name=name), self.assertRaises(FileNotFoundError):
                self.check()
            path.write_bytes(original)

    def test_modified_missing_or_extra_payload_is_rejected(self):
        package = self.root / 'sdk.nupkg'
        original = package.read_bytes()
        package.write_bytes(b'changed')
        with self.assertRaisesRegex(ValueError, 'changed/missing'):
            self.check()
        package.unlink()
        with self.assertRaisesRegex(ValueError, 'changed/missing'):
            self.check()
        package.write_bytes(original)
        (self.root / 'extra.apk').write_bytes(b'extra')
        with self.assertRaisesRegex(ValueError, 'Unexpected files'):
            self.check()

    def test_overlapping_platform_payloads_are_rejected(self):
        self.mutate_manifest(files={'sample.apk': digest(self.root / 'sample.apk')})
        with self.assertRaisesRegex(ValueError, 'Overlapping'):
            self.check()

    def test_escaping_noncanonical_and_reserved_paths_are_rejected(self):
        original = (self.root / 'candidate-dotnet.json').read_bytes()
        for relative in ('../outside', '/outside', 'C:/outside', '..\\outside',
                         'sub/../sdk.nupkg', './sdk.nupkg', 'sub//sdk.nupkg', 'candidate-android.json'):
            with self.subTest(relative=relative):
                self.mutate_manifest(files={relative: '0' * 64})
                with self.assertRaises(ValueError):
                    self.check()
                (self.root / 'candidate-dotnet.json').write_bytes(original)

    def test_invalid_or_empty_manifest_hashes_are_rejected(self):
        original = (self.root / 'candidate-dotnet.json').read_bytes()
        for files in ({}, [], {'sdk.nupkg': 'not-a-hash'}, {'sdk.nupkg': None}):
            with self.subTest(files=files):
                self.mutate_manifest(files=files)
                with self.assertRaises(ValueError):
                    self.check()
                (self.root / 'candidate-dotnet.json').write_bytes(original)

    def test_duplicate_json_keys_are_rejected(self):
        (self.root / 'candidate-dotnet.json').write_text('{"source_sha":"a", "source_sha":"b"}', encoding='utf-8')
        with self.assertRaisesRegex(ValueError, 'Duplicate JSON key'):
            self.check()

    def test_manifest_generation_hashes_candidate_prefixed_payloads_too(self):
        root = self.base / 'fresh'
        root.mkdir()
        (root / 'candidate-sample.apk').write_bytes(b'payload')
        manifest(root, 'android', self.sha, '123', self.version)
        self.assertIn('candidate-sample.apk', read_json(root / 'candidate-android.json')['files'])

    def test_cli_writes_automatic_record_and_validated_github_output(self):
        output = self.base / 'github-output'
        self.cli('--github-output', output)
        self.assertEqual(read_json(self.base / 'verification.json'), self.check())
        self.assertEqual(output.read_text(encoding='utf-8'), 'source_sha=' + self.sha + '\n')

    def test_changed_package_and_rewritten_manifest_cannot_pass_saved_gate_record(self):
        saved = self.base / 'gate.json'
        write_json(saved, self.check())
        (self.root / 'sdk.nupkg').write_bytes(b'replaced with updated manifest')
        self.mutate_manifest(files={'sdk.nupkg': digest(self.root / 'sdk.nupkg')})
        self.check()  # Internal consistency alone is insufficient after the gate.
        with self.assertRaisesRegex(ValueError, 'changed since gate/deployment'):
            self.cli('--expected-record', saved)
        self.assertFalse((self.base / 'verification.json').exists())

    def test_manifest_bytes_are_pinned_even_when_payload_is_unchanged(self):
        saved = self.base / 'gate.json'
        write_json(saved, self.check())
        path = self.root / 'candidate-dotnet.json'
        path.write_text(json.dumps(read_json(path), separators=(',', ':')), encoding='utf-8')
        with self.assertRaisesRegex(ValueError, 'changed since gate/deployment'):
            self.cli('--expected-record', saved)

    def test_saved_record_can_be_rechecked_but_a_new_run_attempt_cannot(self):
        saved = self.base / 'gate.json'
        write_json(saved, self.check())
        self.cli('--expected-record', saved)
        self.run['run_attempt'] = 2
        with self.assertRaisesRegex(ValueError, 'changed since gate/deployment'):
            self.cli('--expected-record', saved)

    def test_output_cannot_be_written_into_verified_payload(self):
        with self.assertRaisesRegex(ValueError, 'outside candidate root'):
            self.cli('--output', self.root / 'unexpected.json')
        self.assertFalse((self.root / 'unexpected.json').exists())

    def test_invalid_run_id_does_not_call_github(self):
        with patch.object(sys, 'argv', ['run-provenance.py', '--workflow', 'deployment.yml',
                                      '--run-id', '../wrong', '--output', str(self.base / 'audit.json')]), \
                patch.object(provenance.subprocess, 'check_output') as api:
            with self.assertRaises(ValueError):
                provenance.main()
            api.assert_not_called()

    def deployment(self):
        run = dict(self.run, id=456, path='.github/workflows/deployment.yml', head_sha='c' * 40)
        return provenance.verify(run, 'deployment.yml', '456', repo=self.repo)

    def test_delivery_binds_candidate_and_deployment_without_human_json(self):
        verified = self.check()
        receipt = delivery_record(verified, '456', 'c' * 40, 'd' * 40)
        self.assertEqual(delivered_identity(receipt, self.deployment()), dict(
            version=self.version, candidate_run_id='123', source_sha=self.sha, release_sha='d' * 40))
        self.assertEqual(receipt['candidate_verification'], verified)

    def test_old_dry_run_or_mismatched_delivery_record_is_rejected(self):
        original = delivery_record(self.check(), '456', 'c' * 40, 'd' * 40)
        for key, value in (('schema_version', None), ('publication_state', 'dry_run'),
                           ('deployment_run_id', '455'), ('workflow_sha', 'f' * 40),
                           ('source_sha', 'b' * 40), ('candidate_run_id', '122'),
                           ('version', '2.4.0'), ('release_sha', 'bad\noutput=injected')):
            with self.subTest(key=key), self.assertRaises(ValueError):
                delivered_identity(dict(original, **{key: value}), self.deployment())
        with self.assertRaises(ValueError):
            delivered_identity({'VERSION': self.version, 'CANDIDATE_RUN': '123'}, self.deployment())

    def test_delivery_readback_supplies_exact_record_for_candidate_recheck(self):
        verified = self.check()
        receipt, audit = self.base / 'delivery.json', self.base / 'deployment.json'
        output, expected = self.base / 'github-output', self.base / 'expected.json'
        published = self.base / 'publish-verification.json'
        write_json(published, verified)
        with patch.object(sys, 'argv', ['artifacts.py', 'record-delivery', '--verification', str(published),
                                      '--deployment-run-id', '456', '--workflow-sha', 'c' * 40,
                                      '--release-sha', 'd' * 40, '--output', str(receipt)]):
            artifacts.main()
        write_json(audit, self.deployment())
        with patch.object(sys, 'argv', ['artifacts.py', 'read-delivery', '--record', str(receipt),
                                      '--provenance', str(audit), '--verification-output', str(expected),
                                      '--github-output', str(output)]):
            artifacts.main()
        self.assertEqual(read_json(expected), verified)
        self.assertIn('candidate_run_id=123\n', output.read_text(encoding='utf-8'))
        self.cli('--expected-record', expected)

    def test_candidate_record_cannot_claim_a_different_source_or_failed_run(self):
        for key, value in (('source_sha', 'b' * 40), ('conclusion', 'failure'), ('head_branch', 'feature/test')):
            record = self.check()
            record['provenance'][key] = value
            with self.subTest(key=key), self.assertRaises(ValueError):
                delivery_record(record, '456', 'c' * 40, 'd' * 40)

    def test_no_delivery_for_failed_or_different_repository_deployment(self):
        receipt = delivery_record(self.check(), '456', 'c' * 40, 'd' * 40)
        for key, value in (('conclusion', 'failure'), ('repository', 'fork/M3SDK')):
            with self.subTest(key=key), self.assertRaises(ValueError):
                delivered_identity(receipt, dict(self.deployment(), **{key: value}))


if __name__ == '__main__':
    unittest.main()
