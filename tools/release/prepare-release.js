#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

function parseArgs(argv) {
  const options = {};

  for (let index = 0; index < argv.length; index += 1) {
    const arg = argv[index];

    switch (arg) {
      case '--version':
        options.version = requireValue(argv, ++index, arg);
        break;
      case '--repo-root':
        options.repoRoot = requireValue(argv, ++index, arg);
        break;
      case '--help':
      case '-h':
        options.help = true;
        break;
      default:
        throw new Error(`Unknown argument: ${arg}`);
    }
  }

  return options;
}

function requireValue(argv, index, flag) {
  const value = argv[index];
  if (!value || value.startsWith('--')) {
    throw new Error(`${flag} requires a value`);
  }
  return value;
}

function printUsage() {
  console.log(`Usage:
  node tools/release/prepare-release.js --version <semver> [--repo-root <path>]
`);
}

function assertReleaseVersion(version) {
  const semver = /^\d+\.\d+\.\d+(?:-[0-9A-Za-z.-]+)?$/;
  if (!semver.test(version)) {
    throw new Error(`Invalid release version: ${version}`);
  }
}

function walkMarkdownFiles(dir) {
  if (!fs.existsSync(dir)) {
    return [];
  }

  const files = [];
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const fullPath = path.join(dir, entry.name);

    if (entry.isDirectory()) {
      files.push(...walkMarkdownFiles(fullPath));
      continue;
    }

    if (entry.isFile() && entry.name.toLowerCase().endsWith('.md')) {
      files.push(fullPath);
    }
  }
  return files;
}

function replaceAll(content, pattern, replacer) {
  return content.replace(pattern, replacer);
}

function updateFile(filePath, transform) {
  if (!fs.existsSync(filePath)) {
    return false;
  }

  const before = fs.readFileSync(filePath, 'utf8');
  const after = transform(before);

  if (before === after) {
    return false;
  }

  fs.writeFileSync(filePath, after, 'utf8');
  return true;
}

function updateMarkdownVersions(repoRoot, version) {
  const docsDir = path.join(repoRoot, 'docs');
  const files = walkMarkdownFiles(docsDir);
  const versionPattern = /\d+\.\d+\.\d+(?:-[0-9A-Za-z.-]+)?/g;
  const changed = [];

  for (const file of files) {
    const didChange = updateFile(file, (content) => {
      let next = content;

      next = replaceAll(
        next,
        new RegExp(`(releases/download/)${versionPattern.source}(/)`, 'g'),
        (_match, prefix, suffix) => `${prefix}${version}${suffix}`,
      );

      next = replaceAll(
        next,
        new RegExp(`(com\\.github\\.m3mobile:M3SDK:)${versionPattern.source}`, 'g'),
        (_match, prefix) => `${prefix}${version}`,
      );

      return next;
    });

    if (didChange) {
      changed.push(path.relative(repoRoot, file));
    }
  }

  return changed;
}

function updateNuGetReadme(repoRoot) {
  const readmePath = path.join(repoRoot, 'dotnet', 'M3Mobile.M3Sdk.Xamarin', 'README.md');

  const didChange = updateFile(readmePath, (content) => content.replace(
    /^NuGet package: .+$/m,
    'NuGet package: [M3Mobile.M3Sdk.Xamarin](https://www.nuget.org/packages/M3Mobile.M3Sdk.Xamarin)',
  ));

  return didChange ? [path.relative(repoRoot, readmePath)] : [];
}

function main() {
  const options = parseArgs(process.argv.slice(2));

  if (options.help) {
    printUsage();
    return;
  }

  if (!options.version) {
    throw new Error('--version is required');
  }

  const repoRoot = path.resolve(options.repoRoot || process.cwd());
  assertReleaseVersion(options.version);

  const changed = [
    ...updateMarkdownVersions(repoRoot, options.version),
    ...updateNuGetReadme(repoRoot),
  ];

  if (changed.length === 0) {
    console.log(`Release metadata already up to date for ${options.version}`);
    return;
  }

  console.log(`Updated release metadata for ${options.version}:`);
  for (const file of changed) {
    console.log(`  - ${file}`);
  }
}

main();
