import importlib.util
import unittest
from pathlib import Path

spec = importlib.util.spec_from_file_location('checks', Path(__file__).with_name('run-app-checks.py'))
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class AppChecksTests(unittest.TestCase):
    def test_flavored_jvm_and_release_tasks_are_allowed(self):
        values = [':app:testMasterDebugUnitTest', ':app:testNougatDebugUnitTest', ':app:assembleMasterRelease', ':app:assembleNougatRelease']
        self.assertEqual(values, module.tasks(dict(build_tasks=values)))

    def test_install_device_publish_and_shell_tasks_are_rejected(self):
        for value in (':app:installDebug', ':app:connectedAndroidTest', ':app:publish', ':app:assembleRelease;adb', '--init-script=unknown', ':app:uninstallAll'):
            with self.subTest(value=value):
                with self.assertRaises(ValueError):
                    module.tasks(dict(build_tasks=[value]))

    def test_empty_task_list_is_rejected(self):
        with self.assertRaises(ValueError):
            module.tasks(dict(build_tasks=[]))


if __name__ == '__main__':
    unittest.main()
