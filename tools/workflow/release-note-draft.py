"""Create offline paste/import files. This tool never opens or writes an Excel workbook."""
import argparse
import csv
import hashlib
import json
import re
from datetime import date
from pathlib import Path

HEADERS = ['Date', 'Branch', 'Version', '작성자', '분류', 'Note', 'Release Note']
FIELDS = ['date', 'branch', 'version', 'author', 'category', 'note', 'release_note']


def text_cell(value):
    # Excel paste/import must treat commit-derived text as text, not as formulas.
    return "'" + value if value.lstrip().startswith(('=', '+', '-', '@')) else value


def draft(source, destination):
    data = json.loads(source.read_text(encoding='utf-8-sig'))
    if not data.get('app') or not data.get('sheet') or not data.get('records'):
        raise ValueError('App, target sheet and commit records required')
    seen = set()
    for item in data['records']:
        if not all(isinstance(item.get(k), str) and item[k].strip() for k in FIELDS):
            raise ValueError('Every Excel field must be explicitly supplied; no author/Jira/version guessing')
        if not re.fullmatch(r'[0-9]{4}-[0-9]{2}-[0-9]{2}', item['date']):
            raise ValueError('Commit date must use YYYY-MM-DD')
        date.fromisoformat(item['date'])
        if item['category'] not in ('F', 'B', 'R', 'F, B'):
            raise ValueError('Use the existing F/B/R classification')
        if not re.fullmatch(r'[0-9a-f]{40}', item.get('source_sha', '')):
            raise ValueError('Every record needs the actual full commit SHA')
        if item['source_sha'] in seen:
            raise ValueError('Duplicate commit record; review before generating a draft')
        seen.add(item['source_sha'])
    destination.mkdir(parents=True, exist_ok=True)
    output = destination / 'release-note.tsv'
    receipt_path = destination / 'release-note-receipt.json'
    if output.exists() or receipt_path.exists():
        raise FileExistsError('Use a new draft directory; reviewed files are never overwritten')
    # Exclusive creation avoids overwriting a draft that another person already reviewed.
    with output.open('x', encoding='utf-8-sig', newline='') as stream:
        writer = csv.writer(stream, delimiter='\t', lineterminator='\n')
        writer.writerow(HEADERS)
        writer.writerows([text_cell(item[k]) for k in FIELDS] for item in data['records'])
    receipt = dict(app=data['app'], workbook='M3_APP_RELEASE_NOTE.xlsx', sheet=data['sheet'],
                   status='draft_ready_excel_pending',
                   tsv_sha256=hashlib.sha256(output.read_bytes()).hexdigest(),
                   commits=[item['source_sha'] for item in data['records']],
                   instructions='Review exact header, branch, version and existing records; insert new record blocks without overwriting merged rows. Confirm synchronization after manual entry.')
    with receipt_path.open('x', encoding='utf-8') as stream:
        stream.write(json.dumps(receipt, ensure_ascii=False, indent=2) + '\n')
    return receipt


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--input', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    print(json.dumps(draft(args.input, args.output), ensure_ascii=False))


if __name__ == '__main__':
    main()
