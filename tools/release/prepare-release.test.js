const { test } = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const { spawnSync } = require('node:child_process');

test('release metadata updates docs-only tag links and both version headers', () => {
  const root = fs.mkdtempSync(path.join(os.tmpdir(), 'm3sdk-metadata-'));
  try {
    fs.mkdirSync(path.join(root, 'docs'));
    const file = path.join(root, 'docs', 'M3SDK_Manual_kr.md');
    fs.writeFileSync(file, '**매뉴얼 버전: 2.3.16 · 대상 SDK 버전: 2.3.15**\n' +
      'https://github.com/m3mobile/M3SDK/releases/download/docs-2.3.16/M3SDK_Manual_kr_v2.3.16.pdf\n');
    const args = [path.join(__dirname, 'prepare-release.js'), '--version', '2.3.17', '--repo-root', root];
    const result = spawnSync(process.execPath, args, { encoding: 'utf8' });
    assert.equal(result.status, 0, result.stderr);
    const text = fs.readFileSync(file, 'utf8');
    assert.match(text, /매뉴얼 버전: 2\.3\.17 · 대상 SDK 버전: 2\.3\.17/);
    assert.match(text, /download\/2\.3\.17\/M3SDK_Manual_kr_v2\.3\.17\.pdf/);
    assert.equal(spawnSync(process.execPath, args).status, 0);
    assert.equal(fs.readFileSync(file, 'utf8'), text, 'metadata preparation must be idempotent');
  } finally {
    assert.equal(path.dirname(path.resolve(root)), path.resolve(os.tmpdir()));
    assert.ok(path.basename(root).startsWith('m3sdk-metadata-'));
    fs.rmSync(root, { recursive: true, force: true });
  }
});

test('invalid release versions fail before changing any metadata', () => {
  const result = spawnSync(process.execPath, [path.join(__dirname, 'prepare-release.js'), '--version', 'invalid']);
  assert.notEqual(result.status, 0);
});
