import csv
import importlib.util
import json
import tempfile
import unittest
from pathlib import Path

spec = importlib.util.spec_from_file_location('draft', Path(__file__).with_name('release-note-draft.py'))
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class DraftTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.data = dict(app='Example', sheet='Example', records=[dict(date='2026-10-20', branch='Standard',
                         version='0.0.0-example', author='Example author', category='R',
                         note='Example internal maintenance', release_note='First line\nSecond line\ttext', source_sha='a'*40)])

    def generate(self):
        source = self.root/'input.json'
        source.write_text(json.dumps(self.data), encoding='utf-8')
        return module.draft(source, self.root/'output')

    def test_draft_has_seven_columns_and_remains_pending(self):
        receipt = self.generate()
        with (self.root/'output/release-note.tsv').open(encoding='utf-8-sig', newline='') as stream:
            rows = list(csv.reader(stream, delimiter='\t'))
        self.assertEqual(rows[0], module.HEADERS)
        self.assertEqual(len(rows[1]), 7)
        self.assertEqual(rows[1][6], self.data['records'][0]['release_note'])
        self.assertEqual(receipt['status'], 'draft_ready_excel_pending')
        self.assertFalse(list(self.root.rglob('*.xlsx')))

    def test_missing_author_or_commit_is_not_guessed(self):
        for field in ('author', 'source_sha'):
            item = self.data['records'][0]
            saved = item.pop(field)
            with self.assertRaises(ValueError):
                self.generate()
            item[field] = saved

    def test_duplicate_commits_are_rejected(self):
        self.data['records'].append(self.data['records'][0].copy())
        with self.assertRaises(ValueError):
            self.generate()

    def test_formula_text_is_escaped(self):
        self.data['records'][0]['note'] = '=1+2'
        self.generate()
        with (self.root/'output/release-note.tsv').open(encoding='utf-8-sig', newline='') as stream:
            self.assertEqual(list(csv.reader(stream, delimiter='\t'))[1][5], "'=1+2")

    def test_existing_reviewed_draft_is_not_overwritten(self):
        self.generate()
        with self.assertRaises(FileExistsError):
            self.generate()


if __name__ == '__main__':
    unittest.main()
