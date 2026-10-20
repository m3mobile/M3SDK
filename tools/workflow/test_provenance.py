import importlib.util
import json
import tempfile
import unittest
from pathlib import Path

spec = importlib.util.spec_from_file_location('provenance', Path(__file__).with_name('run-provenance.py'))
provenance = importlib.util.module_from_spec(spec)
spec.loader.exec_module(provenance)


class ProvenanceTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.run = dict(path='.github/workflows/verify-candidate.yml', event='workflow_dispatch',
                        status='completed', conclusion='success', head_sha='a' * 40)
        self.manifest = dict(source_sha='a' * 40, candidate_run_id='123')

    def check(self):
        (self.root / 'candidate-android.json').write_text(json.dumps(self.manifest))
        provenance.verify(self.run, 'verify-candidate.yml', '123', self.root)

    def test_matching_dispatch(self):
        self.check()

    def test_other_workflow_pr_failed_or_incomplete_run_is_rejected(self):
        for key, value in dict(path='.github/workflows/other.yml', event='pull_request',
                               status='in_progress', conclusion='failure').items():
            with self.subTest(key=key):
                saved = self.run[key]
                self.run[key] = value
                with self.assertRaises(ValueError):
                    self.check()
                self.run[key] = saved

    def test_manifest_commit_and_run_are_bound_to_github_run(self):
        for key, value in dict(source_sha='b' * 40, candidate_run_id='122').items():
            with self.subTest(key=key):
                saved = self.manifest[key]
                self.manifest[key] = value
                with self.assertRaises(ValueError):
                    self.check()
                self.manifest[key] = saved

    def test_missing_candidate_manifest_is_rejected(self):
        with self.assertRaises(ValueError):
            provenance.verify(self.run, 'verify-candidate.yml', '123', self.root)


if __name__ == '__main__':
    unittest.main()
