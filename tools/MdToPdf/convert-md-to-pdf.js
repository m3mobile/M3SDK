#!/usr/bin/env node

const fs = require('fs');
const os = require('os');
const path = require('path');
const { spawnSync } = require('child_process');
const { preprocessMarkdown } = require('./preprocess-markdown');

const TOOL_DIR = __dirname;

function printUsage() {
  console.log(`Usage:
  node convert-md-to-pdf.js --input <file-or-dir> --output <dir-or-pdf> [options]

Options:
  --input <path>       Markdown file or directory to convert. Can be repeated.
  --output <path>      Output directory, or a .pdf path when input is one file.
  --repo-root <path>   Git repository root. Defaults to nearest parent with .git.
  --temp <path>        Temp directory. Defaults to <repo-root>/build/md-to-pdf.
  --version <version>  Append _v<version> to generated PDF filenames.
  --config <path>      md-to-pdf config. Defaults to tools/MdToPdf/.md-to-pdf.json.
  --basedir <path>     md-to-pdf base directory. Defaults to tools/MdToPdf.
  --clean             Remove the output directory before writing PDFs.
  --no-preprocess     Skip TOC/version-history preprocessing.
  --help              Show this help.

Examples:
  node tools/MdToPdf/convert-md-to-pdf.js --input docs --output build/release/manuals --clean
  node tools/MdToPdf/convert-md-to-pdf.js --input docs/M3SDK_Manual_en.md --output build/release/manuals
`);
}

function parseArgs(argv) {
  const options = {
    inputs: [],
    clean: false,
    preprocess: true,
  };

  for (let index = 0; index < argv.length; index += 1) {
    const arg = argv[index];

    switch (arg) {
      case '--help':
      case '-h':
        options.help = true;
        break;
      case '--input':
      case '-i':
        options.inputs.push(requireValue(argv, ++index, arg));
        break;
      case '--output':
      case '-o':
        options.output = requireValue(argv, ++index, arg);
        break;
      case '--repo-root':
        options.repoRoot = requireValue(argv, ++index, arg);
        break;
      case '--temp':
        options.temp = requireValue(argv, ++index, arg);
        break;
      case '--version':
        options.version = requireValue(argv, ++index, arg);
        break;
      case '--config':
        options.config = requireValue(argv, ++index, arg);
        break;
      case '--basedir':
        options.basedir = requireValue(argv, ++index, arg);
        break;
      case '--clean':
        options.clean = true;
        break;
      case '--no-preprocess':
        options.preprocess = false;
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

function findRepoRoot(startDir) {
  let current = path.resolve(startDir);

  while (true) {
    if (fs.existsSync(path.join(current, '.git'))) {
      return current;
    }

    const parent = path.dirname(current);
    if (parent === current) {
      return path.resolve(startDir);
    }
    current = parent;
  }
}

function ensureDirectory(dirPath) {
  fs.mkdirSync(dirPath, { recursive: true });
}

function removeDirectory(dirPath) {
  if (fs.existsSync(dirPath)) {
    fs.rmSync(dirPath, { recursive: true, force: true });
  }
}

function collectMarkdownFiles(inputPath) {
  const absoluteInput = path.resolve(inputPath);
  const stat = fs.statSync(absoluteInput);

  if (stat.isFile()) {
    if (absoluteInput.toLowerCase().endsWith('.md')) {
      return [{ file: absoluteInput, root: path.dirname(absoluteInput) }];
    }
    return [];
  }

  const files = [];
  walkDirectory(absoluteInput, (file) => {
    if (file.toLowerCase().endsWith('.md')) {
      files.push({ file, root: absoluteInput });
    }
  });
  return files;
}

function walkDirectory(dir, onFile) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const fullPath = path.join(dir, entry.name);

    if (entry.isDirectory()) {
      if (['.git', '.gradle', 'build', 'node_modules', 'output'].includes(entry.name)) {
        continue;
      }
      walkDirectory(fullPath, onFile);
      continue;
    }

    if (entry.isFile()) {
      onFile(fullPath);
    }
  }
}

function getOutputPath(item, outputPath, totalFiles, version) {
  const absoluteOutput = path.resolve(outputPath);
  const outputIsPdf = absoluteOutput.toLowerCase().endsWith('.pdf');

  if (outputIsPdf) {
    if (totalFiles !== 1) {
      throw new Error('--output can be a .pdf file only when exactly one markdown file is converted');
    }
    return absoluteOutput;
  }

  const relative = path.relative(item.root, item.file);
  const relativePdf = version
    ? relative.replace(/\.md$/i, `_v${version}.pdf`)
    : relative.replace(/\.md$/i, '.pdf');
  return path.join(absoluteOutput, relativePdf);
}

function getTempMarkdownPath(item, tempDir) {
  const relative = path.relative(item.root, item.file);
  const safeName = relative
    .replace(/^[A-Za-z]:/, '')
    .replace(/[\\/]+/g, '__')
    .replace(/[^A-Za-z0-9_.-]/g, '_')
    .replace(/\.md$/i, '-processed.md');
  return path.join(tempDir, safeName);
}

function resolveMdToPdfCommand() {
  const localBin = path.join(TOOL_DIR, 'node_modules', '.bin', os.platform() === 'win32' ? 'md-to-pdf.cmd' : 'md-to-pdf');
  if (fs.existsSync(localBin)) {
    return localBin;
  }
  return 'md-to-pdf';
}

function convertWithMdToPdf(markdownPath, pdfPath, configPath, basedir) {
  ensureDirectory(path.dirname(pdfPath));
  const generatedPdfPath = markdownPath.replace(/\.md$/i, '.pdf');

  if (fs.existsSync(generatedPdfPath)) {
    fs.rmSync(generatedPdfPath, { force: true });
  }

  const command = resolveMdToPdfCommand();
  const args = [
    markdownPath,
    '--config-file',
    configPath,
    '--basedir',
    basedir,
  ];

  const result = spawnSync(command, args, {
    cwd: basedir,
    encoding: 'utf-8',
    shell: os.platform() === 'win32',
  });

  if (result.status !== 0) {
    const stdout = result.stdout ? `\n${result.stdout}` : '';
    const stderr = result.stderr ? `\n${result.stderr}` : '';
    throw new Error(`md-to-pdf failed for ${markdownPath}${stdout}${stderr}`);
  }

  if (!fs.existsSync(generatedPdfPath)) {
    throw new Error(`md-to-pdf completed but did not create ${generatedPdfPath}`);
  }

  if (fs.existsSync(pdfPath)) {
    fs.rmSync(pdfPath, { force: true });
  }

  if (path.resolve(generatedPdfPath) !== path.resolve(pdfPath)) {
    fs.renameSync(generatedPdfPath, pdfPath);
  }
}

async function main() {
  const options = parseArgs(process.argv.slice(2));

  if (options.help) {
    printUsage();
    return;
  }

  if (options.inputs.length === 0) {
    throw new Error('At least one --input path is required');
  }

  if (!options.output) {
    throw new Error('--output is required');
  }

  const repoRoot = path.resolve(options.repoRoot || findRepoRoot(process.cwd()));
  const configPath = path.resolve(options.config || path.join(TOOL_DIR, '.md-to-pdf.json'));
  const basedir = path.resolve(options.basedir || TOOL_DIR);
  const tempDir = path.resolve(options.temp || path.join(repoRoot, 'build', 'md-to-pdf'));

  if (!fs.existsSync(configPath)) {
    throw new Error(`Config file not found: ${configPath}`);
  }

  const items = options.inputs.flatMap((input) => collectMarkdownFiles(input));
  if (items.length === 0) {
    throw new Error(`No markdown files found in: ${options.inputs.join(', ')}`);
  }

  const outputPath = path.resolve(options.output);
  if (options.clean && !outputPath.toLowerCase().endsWith('.pdf')) {
    removeDirectory(outputPath);
  }
  removeDirectory(tempDir);
  ensureDirectory(tempDir);

  console.log(`M3 SDK PDF generation`);
  console.log(`  Repo root: ${repoRoot}`);
  console.log(`  Output:    ${outputPath}`);
  console.log(`  Temp:      ${tempDir}`);
  console.log(`  Files:     ${items.length}`);
  console.log('');

  const failures = [];

  for (const item of items) {
    const pdfPath = getOutputPath(item, outputPath, items.length, options.version);
    const tempMarkdownPath = getTempMarkdownPath(item, tempDir);
    const displayName = path.relative(repoRoot, item.file) || item.file;

    try {
      console.log(`Converting ${displayName}`);
      ensureDirectory(path.dirname(tempMarkdownPath));

      if (options.preprocess) {
        await preprocessMarkdown(item.file, tempMarkdownPath, repoRoot);
      } else {
        fs.copyFileSync(item.file, tempMarkdownPath);
      }

      convertWithMdToPdf(tempMarkdownPath, pdfPath, configPath, basedir);
      console.log(`  OK ${path.relative(repoRoot, pdfPath) || pdfPath}`);
    } catch (error) {
      failures.push({ file: item.file, error });
      console.error(`  FAIL ${error.message}`);
    }
  }

  removeDirectory(tempDir);

  if (failures.length > 0) {
    console.error('');
    console.error(`PDF generation failed for ${failures.length} file(s):`);
    for (const failure of failures) {
      console.error(`  - ${failure.file}`);
    }
    process.exit(1);
  }

  console.log('');
  console.log(`PDF generation completed: ${items.length} file(s)`);
}

main().catch((error) => {
  console.error(error.message);
  process.exit(1);
});
