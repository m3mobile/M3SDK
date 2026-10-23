const { test } = require("node:test");
const assert = require("node:assert/strict");
const fs = require("node:fs");
const os = require("node:os");
const path = require("node:path");
const { validateManuals } = require("./validate-manuals.js");

const REPO_ROOT = path.resolve(__dirname, "../..");
const MANUALS = [
  "docs/M3SDK_Manual_kr.md", "docs/M3SDK_Manual_en.md",
  "docs/M3SDK_Xamarin_Manual_kr.md", "docs/M3SDK_Xamarin_Manual_en.md",
];
const SOURCES = [
  "feature/startup/src/main/java/net/m3mobile/feature/startup/constants/RequestAction.kt",
  "feature/startup/src/main/java/net/m3mobile/feature/startup/constants/ResponseAction.kt",
  "feature/scanemul/src/main/java/net/m3mobile/feature/scanemul/constants/RequestAction.kt",
  "feature/scanemul/src/main/java/net/m3mobile/feature/scanemul/constants/ResponseAction.kt",
  "feature/keytool/src/main/java/net/m3mobile/feature/keytool/constants/RequestAction.kt",
  "feature/appcenter/src/main/java/net/m3mobile/feature/appcenter/internal/AppCenterContract.kt",
  "dotnet/M3Mobile.M3Sdk.Xamarin/Internal/Constants.cs",
];

function fixture(t) {
  const temp = path.resolve(os.tmpdir());
  const root = fs.mkdtempSync(path.join(temp, "m3sdk-manual-contracts-"));
  t.after(() => {
    assert.equal(path.dirname(path.resolve(root)), temp);
    assert.ok(path.basename(root).startsWith("m3sdk-manual-contracts-"));
    fs.rmSync(root, { recursive: true, force: true });
  });
  for (const file of [...MANUALS, ...SOURCES]) {
    fs.mkdirSync(path.dirname(path.join(root, file)), { recursive: true });
    fs.copyFileSync(path.join(REPO_ROOT, file), path.join(root, file));
  }
  return root;
}

function edit(root, file, before, after) {
  const target = path.join(root, file);
  const original = fs.readFileSync(target, "utf8");
  const changed = original.replace(before, after);
  assert.notEqual(changed, original, "fixture mutation must match existing content");
  fs.writeFileSync(target, changed);
}

test("current four manuals match the Kotlin/C# action contract", () => {
  assert.match(validateManuals(REPO_ROOT), /Validated 4 manuals/);
});

test("a new API leaf can be documented in all manuals without changing a count constant", (t) => {
  const root = fixture(t);
  const before = validateManuals(root);
  const extra = "\n#### Fixture API\n\n**Direct Broadcast**\n\n" +
    "No direct command broadcast is available. Fixture-only explanation.\n\n";
  for (const file of MANUALS) edit(root, file, "## API\n", "## API\n" + extra);
  const counts = (report) => [...report.matchAll(/\.md=(\d+)/g)].map((m) => Number(m[1]));
  assert.deepEqual(counts(validateManuals(root)), counts(before).map((count) => count + 1));
});

test("new inline code and version references in explanatory prose do not change the contract", (t) => {
  const root = fixture(t);
  edit(root, MANUALS[1], "**Direct Broadcast**", "**Direct Broadcast**\n\n" +
    "Editorial note: check `MDM` and version `99.1.0` before using `exampleTerm`.\n");
  assert.doesNotThrow(() => validateManuals(root));
});

test("translated table descriptions can change without changing wire literals", (t) => {
  const root = fixture(t);
  edit(root, MANUALS[1], "Absolute APK path", "A local path selected by the administrator");
  assert.doesNotThrow(() => validateManuals(root));
});

test("equivalent required flags and conditional descriptions can be reworded", (t) => {
  const root = fixture(t);
  edit(root, MANUALS[1], "| `airplane` | `Boolean` | O | `true` |",
    "| `airplane` | `Boolean` | Yes | `true` |");
  edit(root, MANUALS[1], "When changing mounting settings", "Only while updating mounting policy");
  assert.doesNotThrow(() => validateManuals(root));
});

test("contract table row order does not change the contract", (t) => {
  const root = fixture(t);
  edit(root, MANUALS[1],
    "| `setting` | `String` | O | `airplane` |\n| `airplane` | `Boolean` | O | `true` |",
    "| `airplane` | `Boolean` | O | `true` |\n| `setting` | `String` | O | `airplane` |");
  assert.doesNotThrow(() => validateManuals(root));
});

const mutations = [
  ["missing leaf Broadcast status", "**Direct Broadcast**", "", /leaf API section has no direct Broadcast status/],
  ["a code example cannot replace real Broadcast status", "**Direct Broadcast**",
    "```text\n**Direct Broadcast**\n```", /leaf API section has no direct Broadcast status/],
  ["unknown request action", "**Action**: `com.android.server.startupservice.system`",
    "**Action**: `invalid.request.action`", /documented request actions differ/],
  ["wrong response action mapping", "**Response action**: `com.android.server.startupservice.system.response`",
    "**Response action**: `scanemul.action.status`", /response action does not match/],
  ["missing required scanner response", "**Response action**: `scanemul.action.status`",
    "", /required response action is missing/],
  ["changed target package", "**Target package**: Not set (implicit broadcast)",
    "**Target package**: `incorrect.package`", /contracts differ/],
  ["changed Extra key", "| `airplane` | `Boolean` | O | `true` |",
    "| `wrong_key` | `Boolean` | O | `true` |", /contracts differ/],
  ["changed Extra type", "| `airplane` | `Boolean` | O | `true` |",
    "| `airplane` | `String` | O | `true` |", /contracts differ/],
  ["changed required flag", "| `airplane` | `Boolean` | O | `true` |",
    "| `airplane` | `Boolean` | X | `true` |", /contracts differ/],
  ["missing conditional status", "| When changing mounting settings |", "| |",
    /no required\/optional status/],
  ["changed wire literal", "| `airplane` | `Boolean` | O | `true` |",
    "| `airplane` | `Boolean` | O | `false` |", /contracts differ/],
  ["swapped Extra key and value with the same token set", "| `setting` | `String` | O | `airplane` |",
    "| `airplane` | `String` | O | `setting` |", /contracts differ/],
  ["missing contract row", "| `airplane` | `Boolean` | O | `true` |",
    "", /contracts differ/],
  ["changed follow-up action described in prose", "`com.android.server.startupservice.config.fin`",
    "`com.android.server.startupservice.config.wrong`", /contracts differ/],
  ["changed ordered delivery", "**Delivery**: Ordered broadcast",
    "**Delivery**: Unordered broadcast", /contracts differ/],
  ["missing unsupported reason", "No direct command broadcast is available.",
    "An explanation without a supported/unsupported status.", /no action or unsupported reason/],
  ["invalid TOC target", /\]\(#[^)]+\)/,
    "](#fixture-nonexistent-heading)", /TOC target does not match/],
  ["missing common broadcast example", /Implicit broadcast/g,
    "Example removed", /common Broadcast example is missing/],
  ["API with no leaf sections", /^#{4,5} /gm,
    "###### ", /API has no leaf sections/],
];

for (const [name, before, after, error] of mutations) {
  test(name + " is rejected", (t) => {
    const root = fixture(t);
    edit(root, MANUALS[1], before, after);
    assert.throws(() => validateManuals(root), error);
  });
}

test("an extra Broadcast block in only one manual is rejected", (t) => {
  const root = fixture(t);
  edit(root, MANUALS[1], "**Direct Broadcast**", "**Direct Broadcast**\n\n" +
    "No direct command broadcast is available.\n\n**Direct Broadcast**");
  assert.throws(() => validateManuals(root), /contracts differ/);
});

test("Kotlin and C# source action drift is rejected", (t) => {
  const root = fixture(t);
  edit(root, SOURCES.at(-1), "com.android.server.startupservice.system", "incorrect.source.action");
  assert.throws(() => validateManuals(root), /Kotlin and C# SDK request actions differ/);
});

test("stale Broadcast sections are still rejected", (t) => {
  const root = fixture(t);
  edit(root, MANUALS[1], /$/, "\n## Fixture appendix\n\n#### Direct Broadcast Usage\n");
  assert.throws(() => validateManuals(root), /stale manual content remains/);
});
