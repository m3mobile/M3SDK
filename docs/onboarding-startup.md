# StartUp + M3SDK 최초 설정과 작업 시작

## 사용할 저장소

- 앱: `m3mobile/Android-App-StartUp` (기본 main)
- SDK/공통 절차: `m3mobile/M3SDK` (기본 develop)
- 이번 공유 브랜치: 두 저장소의 `codex/shared-release-workflow`. 병합 전에는 이 브랜치를 명시적으로 사용한다. 후보 코드와 설정의 기준 커밋은 PR에서 확인한다.
- StartUp의 사내 origin과 GitHub remote는 별개다. 사내 서버에만 push한 변경은 GitHub CI에 자동 반영되지 않는다.

```powershell
gh repo clone m3mobile/Android-App-StartUp
gh repo clone m3mobile/M3SDK
git -C Android-App-StartUp fetch origin
git -C Android-App-StartUp switch --track origin/codex/shared-release-workflow
git -C M3SDK fetch origin
git -C M3SDK switch --track origin/codex/shared-release-workflow
```

기존 체크아웃은 변경을 지우거나 강제로 전환하지 않는다. 별도 작업 트리에서 시작한다. 두 저장소의 AGENTS.md와 development-workflow.md를 먼저 읽는다.

## 개인 환경에서 한 번 준비

1. GitHub 계정의 private StartUp/M3Util 읽기 및 작업 브랜치 권한을 확인한다. 다른 사람의 토큰/계정을 복사하지 않는다.
2. Git, Python 3.10 이상, Java 17, Android SDK/API 36을 준비하고 Gradle은 저장소 wrapper를 사용한다. local.properties는 개인 경로로 만들고 커밋하지 않는다.
3. M3SDK 로컬 C# 검증에는 Windows의 클래식 Xamarin.Android MSBuild, .NET 9/10, NuGet CLI, Android workload가 필요하다. 준비되지 않았다면 Windows CI에서 검증하고 로컬 C# 통과라고 기록하지 않는다.
4. `workflow/task-scope.example.json`을 `.workflow.local.json`으로 복사해 작업할 모델과 실기·배포 허용 범위를 확정한다. 기본은 실기/배포 금지다. 모델을 SM24 등으로 추정하지 않는다.
5. Excel 연결은 선택 사항이다. 없는 상태로 시작하고 수동 입력용 초안을 인계할 수 있다. 실제 파일·시트·작성자 확인과 반영은 별도 완료 조건이다.

```powershell
python M3SDK/tools/workflow/preflight.py --app-root Android-App-StartUp --sdk-root M3SDK
```

도구 유무와 저장소 SHA만 확인하며 설정·Excel·기기를 변경하지 않는다.

## StartUp의 앱별 설정

`workflow/app.json`은 com.m3.startup과 master/nougat 두 변형을 지정한다. 각 변형의 JVM 테스트와 signed Release를 모두 생성하고 output-metadata.json에서 실제 APK 이름·버전을 읽는다. 두 변형 버전이 다르거나 파일/서명이 없으면 실패한다. 이 경로는 install/connectedAndroidTest/기기 조작을 실행하지 않는다.

```powershell
cd Android-App-StartUp
python tools/workflow/run-app-checks.py
python -m unittest discover -s tools/workflow -p 'test_*.py'
```

서버 CI의 private M3Util 접근은 관리자 설정의 read-only deploy key를 사용한다. 동료 개인의 Excel 인증이나 개인 PAT를 CI secret으로 올릴 필요는 없다.

## 작업 순서

1. 대상 모델/OS·기능·변경 범위·앱 및 SDK 최소 버전·허용 작업을 확인한다.
2. 앱 구현/코드 리뷰 → JVM 테스트 → 허용된 대상 기기 검증 → 모델별 회귀 또는 근거 있는 생략을 수행한다.
3. M3SDK Kotlin/C# API·모델/최소 버전 검사·양쪽 샘플을 함께 수정한다.
4. 앱 매뉴얼과 SDK 국·영문 4종을 구현과 대조하고 생성 PDF를 검수한다.
5. 후보 Maven/NuGet을 패키지 의존성으로 양쪽 샘플에서 검증한다. 프로젝트 직접 참조로 대체하지 않는다.
6. 릴리즈 노트 TSV 초안을 생성한다. Excel 연결이 없으면 담당자 입력을 기다리되, 개발/자동 테스트는 계속한다.
7. 허용된 후보 실기와 증거가 준비되면 dry-run을 확인한다. 실제 배포는 별도 허용 범위에서만 수행한다.
8. 배포 후 공개 패키지 검증과 실제 동작을 확인하고, 기기 설정 및 Excel 기록 반영까지 확인한 뒤 Complete 단계로 간다.

실기 금지이면 해당 증거는 pending이고 배포/완료를 우회하지 않는다. 비적용 항목은 impact_review의 검토 목록과 항목별 판단자·근거·사유가 있어야 한다. 실패는 원인 수정 후 재검증한다. 사람의 인증/직접 조작이 필요한 경우만 요청한다.

## 공유와 원격 설정

workflow는 기본 브랜치 병합 전에도 PR CI로 검증할 수 있다. 새로운 workflow_dispatch 메뉴는 정의가 기본 브랜치에 반영된 뒤 등록된다. 실제 배포 workflow를 최초 빌드 확인용으로 실행하지 않는다.

- 앱 CI: 후보 빌드와 actionlint. SDK CI: Android 및 C# 패키지 소비 빌드, API/계약 검사, workflow 검사.
- 관리자: M3Util read-only deploy key와 앱 repo secret `M3UTIL_DEPLOY_KEY`, SDK의 NuGet secret, 지원되는 branch/environment 정책을 준비한다.
- private 저장소의 branch protection이 요금제 제한이면 적용 완료라고 쓰지 않는다. PR 리뷰·증거 gate를 유지하고 제한을 기록한다.
- SDK 보호 정책은 기본 브랜치에 새 workflow가 들어간 뒤 실제 check 이름으로 적용한다. 아직 없는 필수 check로 다른 작업을 잠그지 않는다.

## 에이전트 시작 지시문

> StartUp과 M3SDK의 AGENTS.md, 공통 절차와 onboarding-startup.md를 읽고 작업해 주세요. 먼저 변경 상태와 대상 모델·허용 범위를 확인하고 기존 변경을 보존하세요. 앱·SDK·샘플·매뉴얼·릴리즈 기록을 함께 진행하고, 실패는 수정 후 재검증하세요. 실기/배포 미허용 항목은 pending으로 남기고 임의로 완료하지 마세요. Excel 연결이 없으면 수동 인계 파일을 만들고 반영 확인을 별도로 남기세요. 진행 상황을 알리되 권한·인증·사람의 조작이 필요한 경우에만 도움을 요청하세요.

원격 준비 상태는 `python tools/workflow/repository-readiness.py --repo OWNER/REPO`로 조회한다. secret 값이나 정책은 변경하지 않는다. 기본 브랜치 반영 전에는 candidate_registered_on_default=false가 정상이며 PR CI로 먼저 검증한다.
