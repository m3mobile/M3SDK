#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

INPUT_PATH="$REPO_ROOT/docs"
OUTPUT_PATH="$REPO_ROOT/build/release/manuals"
EXTRA_ARGS=()

while [[ $# -gt 0 ]]; do
  case "$1" in
    --input|-i)
      INPUT_PATH="$2"
      shift 2
      ;;
    --output|-o)
      OUTPUT_PATH="$2"
      shift 2
      ;;
    *)
      EXTRA_ARGS+=("$1")
      shift
      ;;
  esac
done

node "$SCRIPT_DIR/convert-md-to-pdf.js" \
  --input "$INPUT_PATH" \
  --output "$OUTPUT_PATH" \
  --repo-root "$REPO_ROOT" \
  --temp "$REPO_ROOT/build/md-to-pdf" \
  --clean \
  "${EXTRA_ARGS[@]}"
