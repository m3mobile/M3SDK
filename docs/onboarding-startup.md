# StartUp 담당자에게 M3SDK workflow 전달

이번 공유 대상은 **M3SDK 저장소 한 곳**이다. StartUp과 KeyTool 저장소에 workflow를 반영하거나 PR을 만드는 작업은 포함하지 않는다. 앱 작업은 동료의 기존 브랜치와 절차를 사용한다.

## 동료가 받을 저장소

- 저장소: `m3mobile/M3SDK`, 기본 브랜치 `develop`.
- 공유 PR: https://github.com/m3mobile/M3SDK/pull/37
- 병합 전 공유 브랜치: `ci/sdk-release-workflow`. 병합 후에는 develop의 반영 커밋을 사용한다.
- 읽을 문서: AGENTS.md → docs/development-workflow.md → docs/release-notes-manual.md.
- 기존 앱이나 SDK 체크아웃에 미커밋 변경이 있으면 지우거나 강제로 전환하지 않는다. 별도 폴더/작업 트리에서 시작한다.

```powershell
gh auth status
gh repo clone m3mobile/M3SDK
git -C M3SDK fetch origin
git -C M3SDK switch --track origin/ci/sdk-release-workflow
python M3SDK/tools/workflow/preflight.py --sdk-root M3SDK
```

로그인이 필요하면 동료 본인 계정으로 인증한다. 본인 계정의 읽기/작업 브랜치 권한이 필요하며 다른 사람의 토큰을 복사하지 않는다. 이 안내서만으로 동료 PC의 인증·도구 준비가 확인된 것은 아니다.

## 개인 환경에서 한 번 준비

1. Git, Python 3.10 이상, Java 17, Android SDK를 준비하고 저장소 Gradle wrapper를 사용한다. 필요한 Android API와 Java toolchain은 저장소 설정에 맞춘다. local.properties는 개인 경로이며 커밋하지 않는다.
2. C# 로컬 검증에는 Windows의 클래식 Xamarin.Android MSBuild, .NET 9/10, NuGet CLI, Android workload가 필요하다. 미구성 환경은 Windows CI를 사용하고 로컬 C# 통과라고 기록하지 않는다.
3. workflow/task-scope.example.json을 .workflow.local.json으로 복사해 실제 앱·모델·OS·허용 범위를 입력한다. 기본은 실기/배포 금지다. 모델을 SM24 등으로 추정하지 않는다.
4. Excel 연결 없이 시작할 수 있다. TSV 초안/인계 기록을 만들고, 실제 공유 파일 반영과 확인은 담당자에게 인계한다. 개인 OneDrive 경로·인증·workbook은 Git에 넣지 않는다.

기존 StartUp 체크아웃도 읽기 점검하려면 `python M3SDK/tools/workflow/preflight.py --app-root STARTUP_PATH --sdk-root M3SDK`를 사용한다. 설정·Excel·기기는 변경하지 않는다.

## 작업 순서

1. StartUp 변경 기능·대상 모델/OS·최소 앱 버전·SDK 변경 범위와 허용 작업을 확정한다.
2. 앱 구현·리뷰·테스트 결과를 확보하고, SDK Kotlin/C# API·버전 검사·양쪽 샘플을 수정한다. 앱 저장소 수정/PR 권한은 별도로 확인한다.
3. 앱 매뉴얼과 SDK 국·영문 4종을 기능과 대조하고 생성 PDF를 검수한다.
4. SDK 후보 Maven/NuGet을 양쪽 샘플의 패키지 의존성으로 소비해 검증한다. SDK 프로젝트 직접 참조로 대체하지 않는다.
5. 릴리즈 노트 TSV 초안을 준비한다. Excel 연결이 없으면 수동 인계하고 반영 확인은 pending으로 남긴다.
6. 허용된 후보 실기·모델별 회귀 또는 근거 있는 생략·리뷰 증거를 확보한 뒤 배포 dry-run을 확인한다.
7. 실제 배포가 허용되면 같은 후보를 게시하고, 공개 패키지 빌드·배포 후 실기·기기 복원·Excel 반영을 확인한 뒤 Complete로 간다.

실기 금지이면 해당 증거는 pending이며 배포/완료를 우회하지 않는다. 비적용 항목은 영향 리뷰와 항목별 판단자·참조·사유가 필요하다. 실제 명령·증거 항목은 development-workflow.md를 따른다.

## GitHub에서 적용할 때

- PR CI는 병합 전에도 실행된다. 새 workflow_dispatch 메뉴는 기본 브랜치에 정의가 반영된 뒤 등록된다.
- 후보 검증: Verify SDK Candidate. 배포 전 자동 검증, 격리 Maven/NuGet, 패키지 소비 샘플, 매뉴얼 PDF를 생성한다.
- 배포: Deployment. 기본 dry-run이며 실제 게시에는 별도 허용과 검증 증거가 필요하다.
- 완료: Complete SDK Release. 공개 패키지·배포 후 실기·기록 반영 증거를 요구한다.
- NUGET_API_KEY와 sdk-release environment 보호 정책은 관리자가 확인한다. 기존 secret의 존재와 값/권한의 유효성은 다른 문제다.
- 기본 브랜치 반영 후 실제 CI check 이름으로 리뷰·필수 검사 정책을 설정한다. 아직 등록되지 않은 필수 검사로 다른 PR을 잠그지 않는다.
- 원격 상태 조회: `python tools/workflow/repository-readiness.py --repo m3mobile/M3SDK`. 이 명령은 secret 값과 정책을 변경하지 않는다.

## 에이전트 시작 지시문

> M3SDK의 AGENTS.md, development-workflow.md, onboarding-startup.md를 읽고 작업하세요. 공유/PR 대상은 M3SDK만입니다. StartUp은 관련 앱이므로 코드를 읽고 검증 의존성을 확인하되, 앱 저장소 수정·push·PR은 별도 요청 없이는 하지 마세요. 대상 모델·허용 범위를 확인하고 기존 변경을 보존하세요. Kotlin/C# API·패키지 소비 샘플·매뉴얼·릴리즈 기록을 함께 진행하고 실패는 수정 후 재검증하세요. 실기/배포 미허용 항목은 pending으로 남기고 완료를 우회하지 마세요. Excel 연결이 없으면 수동 인계 파일을 만들고 반영 확인을 별도로 남기세요.

## 다른 앱에 확장할 때

M3SDK의 templates/android-app은 재사용 예시다. 해당 앱 저장소에 자동 적용되는 설정이 아니다. 앱 workflow 적용을 별도로 요청받은 경우에만 앱별 빌드 작업·변형·서브모듈 접근을 맞추고 별도 리뷰한다. CI용 개인 토큰이나 접근 키를 이 공유 작업에 추가할 필요는 없다.
