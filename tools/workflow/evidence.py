"""Offline artifact/evidence gate. Never connects to an Android device."""
import argparse
import hashlib
import json
import re
from pathlib import Path


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def require(condition, message):
    if not condition:
        raise ValueError(message)


def record(value, label):
    require(isinstance(value, dict), f"{label}: record required")
    require(value.get("status") == "passed", f"{label}: passed required")
    for key in ("by", "reference"):
        require(isinstance(value.get(key), str) and value[key].strip(), f"{label}.{key}: required")


def applicable_record(data, value, label):
    """An omitted route needs an explicit reviewed scope decision, never a fake pass."""
    if isinstance(value, dict) and value.get('status') == 'not_applicable':
        require(label in data['impact_review'].get('not_applicable_checks', []),
                f'{label}: reviewed exclusion required')
        require(all(isinstance(value.get(k), str) and value[k].strip()
                    for k in ('by', 'reference', 'reason')), f'{label}: exclusion proof required')
    else:
        record(value, label)


def manifest(root, name, sha, run, version):
    require(re.fullmatch(r"[0-9a-f]{40}", sha), "Full source SHA required")
    require(re.fullmatch(r"[0-9]+", run), "Numeric candidate run ID required")
    require(re.fullmatch(r"[0-9]+\.[0-9]+\.[0-9]+(?:-[0-9A-Za-z.-]+)?", version), "Invalid candidate version")
    files = {}
    for path in sorted(root.rglob("*")):
        require(not path.is_symlink(), f"Symlink forbidden: {path}")
        if path.is_file() and not path.name.startswith("candidate-"):
            files[path.relative_to(root).as_posix()] = digest(path)
    require(files, "Empty candidate")
    output = root / f"candidate-{name}.json"
    output.write_text(json.dumps(dict(source_sha=sha, candidate_run_id=run, version=version,
                                      files=files), indent=2) + "\n", encoding="utf-8")


def validate(root, evidence, kind, stage, version, run):
    data = json.loads(evidence.read_text(encoding="utf-8-sig"))
    names = ["android", "dotnet"] if kind == "m3sdk" else [kind]
    manifests = {}
    source_sha = None
    declared_files = {}
    for name in names:
        path = root / f"candidate-{name}.json"
        item = json.loads(path.read_text(encoding="utf-8"))
        require(item["version"] == version, "Candidate version mismatch")
        require(str(item["candidate_run_id"]) == run, "Candidate run mismatch")
        require(re.fullmatch(r"[0-9a-f]{40}", item["source_sha"]), "Invalid source SHA")
        source_sha = source_sha or item["source_sha"]
        require(item["source_sha"] == source_sha, "Mixed source commits")
        require(item["files"], "Empty manifest")
        for relative, expected in item["files"].items():
            require(relative not in declared_files, f"Overlapping candidate artifacts: {relative}")
            declared_files[relative] = expected
            target = (root / relative).resolve()
            require(target.is_relative_to(root.resolve()), "Artifact path escapes root")
            require(target.is_file() and digest(target) == expected, f"Artifact changed/missing: {relative}")
        manifests[path.name] = digest(path)
    actual = {p.relative_to(root).as_posix() for p in root.rglob('*') if p.is_file()}
    require(actual == set(declared_files) | set(manifests), "Unexpected files in candidate artifacts")
    require(data.get("candidate_manifests") == manifests, "Evidence must bind every candidate manifest SHA256")
    require(data.get("source_sha") == source_sha, "Evidence source SHA mismatch")
    require(data.get("version") == version and str(data.get("candidate_run_id")) == run,
            "Evidence version/run mismatch")
    record(data.get("code_review"), "code_review")
    record(data.get("app_verification"), "app_verification")
    record(data.get("impact_review"), "impact_review")
    required = ["ui", "broadcast", "persistence", "restart_reboot"]
    if kind == "m3sdk":
        required += ["kotlin_package", "csharp_package"]
    for case in required:
        label = f"pre_release.{case}"
        if case in ('kotlin_package', 'csharp_package'):
            record(data.get('pre_release', {}).get(case), label)
        else:
            applicable_record(data, data.get('pre_release', {}).get(case), label)
    manuals = ['app']
    if kind == 'm3sdk':
        manuals += ['kotlin_ko', 'kotlin_en', 'csharp_ko', 'csharp_en', 'pdf_visual_review']
    for manual in manuals:
        applicable_record(data, data.get('manuals', {}).get(manual), f'manuals.{manual}')
    record(data.get('release_notes', {}).get('draft'), 'release_notes.draft')
    devices = data.get("devices", [])
    require(devices and all(all(isinstance(d.get(k), str) and d[k].strip()
                               for k in ("model", "os", "app_version")) for d in devices),
            "Device model/OS/companion-app versions required")
    regressions = data.get("regression", [])
    require(regressions, "Per-model regression decision required")
    planned_models = data["impact_review"].get("regression_models")
    require(isinstance(planned_models, list) and planned_models and
            all(isinstance(model, str) and model.strip() for model in planned_models),
            "Reviewed regression model list required")
    require(len(planned_models) == len(set(planned_models)), "Duplicate reviewed regression models")
    require(len(regressions) == len(planned_models) and
            {d.get('model') for d in regressions} == set(planned_models),
            "Regression results must cover the reviewed model list exactly")
    for decision in regressions:
        require(all(isinstance(decision.get(k), str) and decision[k].strip()
                    for k in ("model", "by", "reference")),
                "Regression model/reviewer/reference required")
        require(decision.get("status") in ("passed", "skipped"), "Regression pending/failed")
        if decision["status"] == "skipped":
            require(isinstance(decision.get("reason"), str) and decision["reason"].strip() and
                    all(isinstance(decision.get(k), list) and decision[k] and
                        all(isinstance(item, str) and item.strip() for item in decision[k])
                        for k in ("affected_paths", "test_coverage")),
                    "Regression skip needs reason, affected paths and test evidence")
        else:
            require(decision['model'] in {d['model'] for d in devices},
                    "Passed device regression needs model/OS/app version evidence")
    if stage == "complete":
        record(data.get("published_packages"), "published_packages")
        for case in (["kotlin_device", "csharp_device"] if kind == "m3sdk" else ["app_device"]):
            record(data.get("post_release", {}).get(case), f"post_release.{case}")
        record(data.get("device_restored"), "device_restored")
        excel = data.get('release_notes', {}).get('excel')
        applicable_record(data, excel, 'release_notes.excel')
        if excel['status'] == 'passed':
            require(all(isinstance(excel.get(k), str) and excel[k].strip()
                        for k in ('workbook', 'sheet', 'range', 'verification')),
                    'Excel completion needs workbook, sheet, range and confirmation evidence')
    print(f"{kind}: {stage} evidence passed; source={source_sha}; candidate={run}")
    return source_sha


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    commands = parser.add_subparsers(dest="command", required=True)
    create = commands.add_parser("manifest")
    create.add_argument("--name", choices=["android", "dotnet", "keytool", "app"], required=True)
    create.add_argument("--source-sha", required=True)
    for command in (create, commands.add_parser("validate")):
        command.add_argument("--root", type=Path, required=True)
        command.add_argument("--run-id", required=True)
        command.add_argument("--version", required=True)
    check = commands.choices["validate"]
    check.add_argument("--evidence", type=Path, required=True)
    check.add_argument("--kind", choices=["m3sdk", "keytool", "app"], required=True)
    check.add_argument("--stage", choices=["pre", "complete"], default="pre")
    args = parser.parse_args()
    if args.command == "manifest":
        manifest(args.root, args.name, args.source_sha, args.run_id, args.version)
    else:
        validate(args.root, args.evidence, args.kind, args.stage, args.version, args.run_id)


if __name__ == "__main__":
    main()
