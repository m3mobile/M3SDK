#!/usr/bin/env node

const fs = require("fs");
const path = require("path");

const REPO_ROOT = path.resolve(__dirname, "..", "..");
const MANUALS = [
  "docs/M3SDK_Manual_kr.md",
  "docs/M3SDK_Manual_en.md",
  "docs/M3SDK_Xamarin_Manual_kr.md",
  "docs/M3SDK_Xamarin_Manual_en.md",
];
// Existing M3OTA app broadcast, intentionally outside the SDK action contract.
const M3OTA_OTA_REQUEST = "com.m3.intent.action.UPDATE_PACKAGES";
const KOTLIN_ACTION_SOURCES = {
  startUpRequest:
    "feature/startup/src/main/java/net/m3mobile/feature/startup/constants/RequestAction.kt",
  startUpResponse:
    "feature/startup/src/main/java/net/m3mobile/feature/startup/constants/ResponseAction.kt",
  scanEmulRequest:
    "feature/scanemul/src/main/java/net/m3mobile/feature/scanemul/constants/RequestAction.kt",
  scanEmulResponse:
    "feature/scanemul/src/main/java/net/m3mobile/feature/scanemul/constants/ResponseAction.kt",
  keyToolRequest:
    "feature/keytool/src/main/java/net/m3mobile/feature/keytool/constants/RequestAction.kt",
  appCenter:
    "feature/appcenter/src/main/java/net/m3mobile/feature/appcenter/internal/AppCenterContract.kt",
};
const CSHARP_ACTION_SOURCE =
  "dotnet/M3Mobile.M3Sdk.Xamarin/Internal/Constants.cs";

function fail(message) {
  throw new Error(message);
}

function source(relativePath, repoRoot = REPO_ROOT) {
  const absolutePath = path.join(repoRoot, relativePath);
  return {
    relativePath,
    content: fs.readFileSync(absolutePath, "utf8").replace(/\r\n/g, "\n"),
  };
}

function markdownWithoutFencedCode(content) {
  const lines = content.split("\n");
  let inFence = false;

  return lines
    .map((line) => {
      if (/^\s*```/.test(line)) {
        inFence = !inFence;
        return " ".repeat(line.length);
      }
      return inFence ? " ".repeat(line.length) : line;
    })
    .join("\n");
}

function slug(text) {
  return text
    .replace(/<[^>]+>/g, "")
    .replace(/[`*_~]/g, "")
    .toLowerCase()
    .trim()
    .replace(/[^\p{L}\p{M}\p{N}\s_-]/gu, "")
    .replace(/\s+/g, "-")
    .replace(/-+/g, "-");
}

function headings(content) {
  const plain = markdownWithoutFencedCode(content);
  return [...plain.matchAll(/^(#{1,6})\s+(.+?)\s*$/gm)].map((match) => ({
    level: match[1].length,
    text: match[2],
    anchor: slug(match[2]),
    index: match.index,
  }));
}

function validateToc(manual) {
  const allHeadings = headings(manual.content);
  const anchorSet = new Set(allHeadings.map((heading) => heading.anchor));
  // Explicit anchors keep TOC links stable across GitHub and PDF heading sluggers.
  const plain = markdownWithoutFencedCode(manual.content);
  for (const match of plain.matchAll(/<a\s+id=["']([^"']+)["'][^>]*>/gi)) {
    if (anchorSet.has(match[1])) {
      fail(`${manual.relativePath}: duplicate explicit anchor: #${match[1]}`);
    }
    anchorSet.add(match[1]);
  }
  const tocHeading = allHeadings.find(
    (heading) =>
      heading.level === 2 &&
      (heading.text === "목차" || heading.text === "Table of Contents"),
  );

  if (!tocHeading) {
    fail(`${manual.relativePath}: table of contents heading is missing`);
  }

  const nextH2 = allHeadings.find(
    (heading) => heading.level === 2 && heading.index > tocHeading.index,
  );
  const toc = manual.content.slice(
    tocHeading.index,
    nextH2 ? nextH2.index : manual.content.length,
  );
  const targets = [...toc.matchAll(/\]\(#([^)]+)\)/g)].map((match) =>
    decodeURIComponent(match[1]),
  );

  if (targets.length === 0) {
    fail(`${manual.relativePath}: table of contents has no local links`);
  }

  for (const target of targets) {
    if (!anchorSet.has(target)) {
      fail(`${manual.relativePath}: TOC target does not match a heading: #${target}`);
    }
  }
}

function sectionEnd(allHeadings, heading, contentLength) {
  const next = allHeadings.find(
    (candidate) =>
      candidate.index > heading.index && candidate.level <= heading.level,
  );
  return next ? next.index : contentLength;
}

function validateLeafApiSections(manual) {
  const allHeadings = headings(manual.content);
  const apiHeading = allHeadings.find(
    (heading) => heading.level === 2 && heading.text === "API",
  );

  if (!apiHeading) {
    fail(`${manual.relativePath}: API heading is missing`);
  }

  const apiEnd = sectionEnd(allHeadings, apiHeading, manual.content.length);
  const apiHeadings = allHeadings.filter(
    (heading) =>
      heading.index > apiHeading.index &&
      heading.index < apiEnd &&
      (heading.level === 4 || heading.level === 5),
  );

  if (apiHeadings.length === 0) {
    fail(`${manual.relativePath}: API has no leaf sections`);
  }
  const plain = markdownWithoutFencedCode(manual.content);

  for (const heading of apiHeadings) {
    const end = sectionEnd(apiHeadings, heading, apiEnd);
    const hasChild = apiHeadings.some(
      (candidate) =>
        candidate.index > heading.index &&
        candidate.index < end &&
        candidate.level > heading.level,
    );
    if (hasChild) continue;

    const section = plain.slice(heading.index, end);
    if (!/\*\*(직접 Broadcast|Direct Broadcast)\*\*/.test(section)) {
      fail(
        `${manual.relativePath}: leaf API section has no direct Broadcast status: ${heading.text}`,
      );
    }
  }
}

function directBlocks(content) {
  const plain = markdownWithoutFencedCode(content);
  const marker = /\*\*(직접 Broadcast|Direct Broadcast)\*\*/g;
  const matches = [...plain.matchAll(marker)];

  return matches.map((match, index) => {
    const afterMarker = match.index + match[0].length;
    const nextMarker = matches[index + 1]?.index ?? plain.length;
    const nextHeadingMatch = /^#{1,6}\s+/gm;
    nextHeadingMatch.lastIndex = afterMarker;
    const nextHeading = nextHeadingMatch.exec(plain);
    const end = Math.min(nextMarker, nextHeading?.index ?? plain.length);
    return plain.slice(afterMarker, end);
  });
}

function inlineCodeTokens(block) {
  const tokens = [];
  for (const line of block.split("\n")) {
    for (const match of line.matchAll(/(?<!`)`([^`\n]+)`(?!`)/g)) {
      tokens.push(match[1]);
    }
  }
  return tokens.sort();
}

function contractSignatures(manual) {
  return directBlocks(manual.content)
    .map((block) => {
      const hasAction = /\*\*Action\*\*:\s*`[^`]+`/.test(block);
      const unsupported =
        /직접 명령 Broadcast를 지원하지 않습니다|No direct command broadcast is available/.test(
          block,
        );

      if (!hasAction && !unsupported) {
        fail(`${manual.relativePath}: direct Broadcast block has no action or unsupported reason`);
      }

      return JSON.stringify({
        unsupported,
        fields: contractFields(block, manual.relativePath),
        // Some protocols describe their follow-up/GET actions in prose.
        // Keep those wire identifiers, without comparing arbitrary prose tokens.
        referencedActions: [...new Set(
          inlineCodeTokens(block).filter((token) =>
            /^(?:com|net|android|scanemul)\.[\w.]+$/.test(token)),
        )],
      });
    })
    .sort();
}

function contractFields(block, relativePath) {
  const fields = [];
  for (const line of block.split("\n")) {
    const field =
      /^\s*[-*]\s+\*\*(Action|Response action|Target package|Extra|Delivery|전송 방식)\*\*:\s*(.*)$/.exec(line);
    if (field) {
      const label = field[1] === "전송 방식" ? "Delivery" : field[1];
      const value = label === "Delivery"
        ? (/\bordered\s+broadcast\b/i.test(field[2])
          ? "ordered" : field[2].trim().toLowerCase())
        : inlineCodeTokens(field[2]);
      fields.push(JSON.stringify({ label, value }));
    } else if (/^\s*\|/.test(line)) {
      const cells = line.trim().replace(/^\||\|$/g, "").split("|")
        .map((cell) => cell.trim());
      const tokens = cells.map(inlineCodeTokens);
      // Translated headings and descriptions are prose. Code in table cells
      // describes wire keys, types and literal values; retain its cell mapping.
      if (tokens.some((cell) => cell.length > 0)) {
        if (cells.length === 4 && tokens[0].length > 0) {
          if (!cells[2]) {
            fail(`${relativePath}: Broadcast Extra table has no required/optional status: ${tokens[0].join(", ")}`);
          }
          tokens[2] = /^(O|Yes|필수)$/i.test(cells[2]) ? "required"
            : /^(X|No|선택)$/i.test(cells[2]) ? "optional" : "conditional";
        }
        fields.push(JSON.stringify({ table: tokens }));
      }
    }
  }
  return fields.sort();
}

function declaredConstants(relativePath, expression, repoRoot) {
  const constants = new Map();
  const content = source(relativePath, repoRoot).content;

  for (const match of content.matchAll(expression)) {
    constants.set(match[1], match[2]);
  }
  return constants;
}

function requiredConstant(constants, name, relativePath) {
  const value = constants.get(name);
  if (!value) {
    fail(`${relativePath}: action constant is missing: ${name}`);
  }
  return value;
}

function sameSet(actual, expected, description) {
  const actualValues = [...actual].sort();
  const expectedValues = [...expected].sort();

  if (JSON.stringify(actualValues) !== JSON.stringify(expectedValues)) {
    fail(
      `${description} differ:\n` +
        `  actual:   ${actualValues.join(", ")}\n` +
        `  expected: ${expectedValues.join(", ")}`,
    );
  }
}

function sdkActionContract(repoRoot = REPO_ROOT) {
  const kotlin = {};
  for (const [key, relativePath] of Object.entries(KOTLIN_ACTION_SOURCES)) {
    kotlin[key] = declaredConstants(
      relativePath,
      /const val\s+([A-Z0-9_]+)(?:\s*:\s*\w+)?\s*=\s*"([^"]+)"/g,
      repoRoot,
    );
  }
  const csharp = declaredConstants(
    CSHARP_ACTION_SOURCE,
    /const string\s+([A-Za-z0-9_]+)\s*=\s*"([^"]+)"/g,
    repoRoot,
  );

  const kotlinRequests = new Set([
    requiredConstant(kotlin.startUpRequest, "SYSTEM", KOTLIN_ACTION_SOURCES.startUpRequest),
    requiredConstant(kotlin.startUpRequest, "CONFIG", KOTLIN_ACTION_SOURCES.startUpRequest),
    requiredConstant(kotlin.scanEmulRequest, "START_SCAN", KOTLIN_ACTION_SOURCES.scanEmulRequest),
    requiredConstant(kotlin.scanEmulRequest, "STOP_SCAN", KOTLIN_ACTION_SOURCES.scanEmulRequest),
    requiredConstant(
      kotlin.scanEmulRequest,
      "GET_SCANNER_MODULE",
      KOTLIN_ACTION_SOURCES.scanEmulRequest,
    ),
    requiredConstant(
      kotlin.scanEmulRequest,
      "SET_SCANNER_SETTING",
      KOTLIN_ACTION_SOURCES.scanEmulRequest,
    ),
    requiredConstant(
      kotlin.scanEmulRequest,
      "GET_SCANNER_SETTING",
      KOTLIN_ACTION_SOURCES.scanEmulRequest,
    ),
    requiredConstant(kotlin.keyToolRequest, "LEFT_SCAN_WAKEUP", KOTLIN_ACTION_SOURCES.keyToolRequest),
    requiredConstant(kotlin.keyToolRequest, "RIGHT_SCAN_WAKEUP", KOTLIN_ACTION_SOURCES.keyToolRequest),
    requiredConstant(kotlin.keyToolRequest, "CONTROL_FN", KOTLIN_ACTION_SOURCES.keyToolRequest),
    requiredConstant(kotlin.keyToolRequest, "ACTION_SET_KEY", KOTLIN_ACTION_SOURCES.keyToolRequest),
    requiredConstant(kotlin.appCenter, "ACTION_CHANGE_PASSWORD", KOTLIN_ACTION_SOURCES.appCenter),
    requiredConstant(
      kotlin.appCenter,
      "ACTION_SET_KEEP_ADMIN_MODE_ON_SLEEP",
      KOTLIN_ACTION_SOURCES.appCenter,
    ),
  ]);
  const kotlinResponses = new Set([
    requiredConstant(kotlin.startUpResponse, "SYSTEM", KOTLIN_ACTION_SOURCES.startUpResponse),
    requiredConstant(
      kotlin.scanEmulResponse,
      "GET_SCANNER_MODULE",
      KOTLIN_ACTION_SOURCES.scanEmulResponse,
    ),
    requiredConstant(
      kotlin.scanEmulResponse,
      "GET_SCANNER_SETTING",
      KOTLIN_ACTION_SOURCES.scanEmulResponse,
    ),
  ]);

  const csharpRequests = new Set([
    "RequestSystem",
    "RequestConfig",
    "StartScan",
    "StopScan",
    "GetScannerModule",
    "SetScannerSetting",
    "GetScannerSetting",
    "LeftScanWakeUp",
    "RightScanWakeUp",
    "ControlFn",
    "SetKey",
    "ChangePassword",
    "SetKeepAdminModeOnSleep",
  ].map((name) => requiredConstant(csharp, name, CSHARP_ACTION_SOURCE)));
  const csharpResponses = new Set([
    "ResponseSystem",
    "ResponseScannerModule",
    "ResponseScannerSetting",
  ].map((name) => requiredConstant(csharp, name, CSHARP_ACTION_SOURCE)));

  sameSet(csharpRequests, kotlinRequests, "Kotlin and C# SDK request actions");
  sameSet(csharpResponses, kotlinResponses, "Kotlin and C# SDK response actions");

  const startUpRequest = requiredConstant(
    kotlin.startUpRequest,
    "SYSTEM",
    KOTLIN_ACTION_SOURCES.startUpRequest,
  );
  const startUpResponse = requiredConstant(
    kotlin.startUpResponse,
    "SYSTEM",
    KOTLIN_ACTION_SOURCES.startUpResponse,
  );
  const scannerModuleRequest = requiredConstant(
    kotlin.scanEmulRequest,
    "GET_SCANNER_MODULE",
    KOTLIN_ACTION_SOURCES.scanEmulRequest,
  );
  const scannerModuleResponse = requiredConstant(
    kotlin.scanEmulResponse,
    "GET_SCANNER_MODULE",
    KOTLIN_ACTION_SOURCES.scanEmulResponse,
  );
  const scannerSettingSetRequest = requiredConstant(
    kotlin.scanEmulRequest,
    "SET_SCANNER_SETTING",
    KOTLIN_ACTION_SOURCES.scanEmulRequest,
  );
  const scannerSettingGetRequest = requiredConstant(
    kotlin.scanEmulRequest,
    "GET_SCANNER_SETTING",
    KOTLIN_ACTION_SOURCES.scanEmulRequest,
  );
  const scannerSettingResponse = requiredConstant(
    kotlin.scanEmulResponse,
    "GET_SCANNER_SETTING",
    KOTLIN_ACTION_SOURCES.scanEmulResponse,
  );

  return {
    requests: kotlinRequests,
    responses: kotlinResponses,
    responseByRequest: new Map([
      [startUpRequest, startUpResponse],
      [scannerModuleRequest, scannerModuleResponse],
      [scannerSettingSetRequest, scannerSettingResponse],
      [scannerSettingGetRequest, scannerSettingResponse],
    ]),
    responseRequired: new Set([
      scannerModuleRequest,
      scannerSettingGetRequest,
    ]),
  };
}

function validateManualActions(manual, sdkContract) {
  const requestActions = new Set();
  const responseActions = new Set();

  for (const block of directBlocks(manual.content)) {
    const blockRequests = [
      ...block.matchAll(/\*\*Action\*\*:\s*`([^`]+)`/g),
    ].map((match) => match[1]);
    const blockResponses = [
      ...block.matchAll(/\*\*Response action\*\*:\s*`([^`]+)`/g),
    ].map((match) => match[1]);

    blockRequests.forEach((action) => requestActions.add(action));
    blockResponses.forEach((action) => responseActions.add(action));

    for (const requestAction of blockRequests) {
      const expectedResponse = sdkContract.responseByRequest.get(requestAction);
      if (blockResponses.length > 0 && expectedResponse === undefined) {
        fail(
          `${manual.relativePath}: request action must not declare a response action: ${requestAction}`,
        );
      }
      if (
        expectedResponse !== undefined &&
        blockResponses.some((action) => action !== expectedResponse)
      ) {
        fail(
          `${manual.relativePath}: response action does not match ${requestAction}; ` +
            `expected ${expectedResponse}`,
        );
      }
      if (
        sdkContract.responseRequired.has(requestAction) &&
        !blockResponses.includes(expectedResponse)
      ) {
        fail(
          `${manual.relativePath}: required response action is missing for ${requestAction}; ` +
            `expected ${expectedResponse}`,
        );
      }
    }
  }

  sameSet(
    requestActions,
    new Set([...sdkContract.requests, M3OTA_OTA_REQUEST]),
    `${manual.relativePath} and documented request actions`,
  );
  sameSet(
    responseActions,
    sdkContract.responses,
    `${manual.relativePath} and SDK response actions`,
  );
}

function validateCommonExamples(manual) {
  const kotlin = manual.relativePath.includes("M3SDK_Manual_");
  const expected = kotlin
    ? ["Implicit broadcast", "Explicit broadcast", ".setPackage("]
    : ["Implicit broadcast", "Explicit broadcast", ".SetPackage("];

  for (const text of expected) {
    if (!manual.content.includes(text)) {
      fail(`${manual.relativePath}: common Broadcast example is missing ${text}`);
    }
  }
}

function validateRemovedSections(manual) {
  const forbidden = [
    "#### 직접 Broadcast 사용",
    "#### Direct Broadcast Usage",
    "[애플리케이션 고정 해제]",
  ];

  for (const text of forbidden) {
    if (manual.content.includes(text)) {
      fail(`${manual.relativePath}: stale manual content remains: ${text}`);
    }
  }
}

function validateManuals(repoRoot = REPO_ROOT) {
  const manuals = MANUALS.map((relativePath) => source(relativePath, repoRoot));
  const actionContract = sdkActionContract(repoRoot);

  for (const manual of manuals) {
    validateToc(manual);
    validateLeafApiSections(manual);
    validateManualActions(manual, actionContract);
    validateCommonExamples(manual);
    validateRemovedSections(manual);
  }

  const baseline = contractSignatures(manuals[0]);
  for (const manual of manuals.slice(1)) {
    const actual = contractSignatures(manual);
    if (JSON.stringify(actual) !== JSON.stringify(baseline)) {
      fail(
        `${manual.relativePath}: Broadcast action/extra contracts differ from ${manuals[0].relativePath}`,
      );
    }
  }

  const blockCounts = manuals.map((manual) =>
    `${manual.relativePath}=${directBlocks(manual.content).length}`);
  return (
    `Validated ${manuals.length} manuals, ` +
      `Broadcast blocks: ${blockCounts.join(", ")}, ` +
      `${actionContract.requests.size} request actions, and ` +
      `${actionContract.responses.size} response actions.`
  );
}

if (require.main === module) {
  try {
    console.log(validateManuals());
  } catch (error) {
    console.error(error.message);
    process.exitCode = 1;
  }
}

module.exports = { validateManuals };
