"""Legacy app-template human evidence gate. SDK delivery uses run-provenance.py."""
import argparse
import json
from pathlib import Path
from artifacts import digest, manifest, require, validate_files


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


def validate(root, evidence, kind, stage, version, run):
    require(kind in ('app', 'keytool'), 'SDK delivery does not accept human evidence')
    data = json.loads(evidence.read_text(encoding="utf-8-sig"))
    identity = validate_files(root, kind, version, run)
    manifests, source_sha = identity['candidate_manifests'], identity['source_sha']
    require(data.get("candidate_manifests") == manifests, "Evidence must bind every candidate manifest SHA256")
    require(data.get("source_sha") == source_sha, "Evidence source SHA mismatch")
    require(data.get("version") == version and str(data.get("candidate_run_id")) == run,
            "Evidence version/run mismatch")
    record(data.get("code_review"), "code_review")
    record(data.get("app_verification"), "app_verification")
    record(data.get("impact_review"), "impact_review")
    required = ["ui", "broadcast", "persistence", "restart_reboot"]
    for case in required:
        label = f"pre_release.{case}"
        applicable_record(data, data.get('pre_release', {}).get(case), label)
    manuals = ['app']
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
        for case in ["app_device"]:
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
    check.add_argument("--kind", choices=["keytool", "app"], required=True)
    check.add_argument("--stage", choices=["pre", "complete"], default="pre")
    args = parser.parse_args()
    if args.command == "manifest":
        manifest(args.root, args.name, args.source_sha, args.run_id, args.version)
    else:
        validate(args.root, args.evidence, args.kind, args.stage, args.version, args.run_id)


if __name__ == "__main__":
    main()
