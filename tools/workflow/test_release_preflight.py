"""Preflight cannot claim publication or expose the environment credential."""
import contextlib
import io
import json
import os
import sys
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch
from release_preflight import availability, main


class PreflightTests(unittest.TestCase):
    def setUp(self):
        self.environment = dict(GITHUB_EVENT_NAME='workflow_dispatch', GITHUB_REF='refs/heads/develop',
                                GITHUB_REPOSITORY='m3mobile/M3SDK', GITHUB_SHA='a' * 40,
                                GITHUB_RUN_ID='123', GITHUB_RUN_ATTEMPT='1',
                                NUGET_API_KEY='test-secret-never-record-this')

    def test_record_only_proves_environment_availability(self):
        record = availability(self.environment)
        self.assertTrue(record['nuget_key_available'])
        self.assertEqual(record['nuget_upload_authorization'], 'not_verified')
        self.assertEqual(record['github_release_write'], 'not_verified')
        self.assertEqual(record['sdk_publication_writes'], 0)
        self.assertEqual(record['device_verification'], 'pending')
        self.assertNotIn(self.environment['NUGET_API_KEY'], json.dumps(record))

    def test_non_manual_wrong_ref_and_fork_are_rejected(self):
        for changes in (dict(GITHUB_EVENT_NAME='push'), dict(GITHUB_EVENT_NAME='pull_request'),
                        dict(GITHUB_REF='refs/heads/feature/test'), dict(GITHUB_REF='refs/tags/2.4.1'),
                        dict(GITHUB_REPOSITORY='fork/M3SDK')):
            with self.subTest(changes=changes), self.assertRaises(ValueError):
                availability(dict(self.environment, **changes))

    def test_missing_and_blank_secret_are_rejected_without_disclosing_value(self):
        for value in ('', '  '):
            with self.subTest(value=value), self.assertRaisesRegex(ValueError, 'is unavailable'):
                availability(dict(self.environment, NUGET_API_KEY=value))
        self.environment.pop('NUGET_API_KEY')
        with self.assertRaisesRegex(ValueError, 'is unavailable'):
            availability(self.environment)

    def test_invalid_run_sha_and_attempt_are_rejected(self):
        for changes in (dict(GITHUB_SHA='short'), dict(GITHUB_RUN_ID='0'), dict(GITHUB_RUN_ATTEMPT='0')):
            with self.subTest(changes=changes), self.assertRaises(ValueError):
                availability(dict(self.environment, **changes))

    def test_cli_writes_no_record_on_failure_and_no_secret_on_success(self):
        with tempfile.TemporaryDirectory() as directory:
            target = Path(directory) / 'preflight.json'
            output = io.StringIO()
            with patch.object(sys, 'argv', ['release_preflight.py', '--output', str(target)]), \
                    patch.dict(os.environ, self.environment, clear=True), contextlib.redirect_stdout(output):
                with patch.dict(os.environ, NUGET_API_KEY=''):
                    with self.assertRaises(ValueError):
                        main()
                self.assertFalse(target.exists())
                main()
            self.assertEqual(json.loads(target.read_text())['source_sha'], self.environment['GITHUB_SHA'])
            self.assertNotIn(self.environment['NUGET_API_KEY'], target.read_text() + output.getvalue())


if __name__ == '__main__':
    unittest.main()
