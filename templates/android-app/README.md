# 다른 Android 앱에 적용

공통 절차는 M3SDK docs/development-workflow.md, docs/onboarding-startup.md, docs/release-notes-manual.md를 기준으로 한다.

1. 이 폴더의 .github/workflows와 AGENTS.md를 새 앱 저장소에 반영한다. 기존 파일이 있으면 병합 검토하며 덮어쓰지 않는다.
2. workflow/app.example.json을 workflow/app.json으로 복사하고 앱/패키지/Gradle 테스트·assemble 작업/변형별 output-metadata.json 경로를 실제 앱으로 바꾼다.
3. M3SDK tools/workflow의 Python 파일들을 앱의 같은 위치에 복사하고, docs의 증거/릴리즈 노트 입력 예시·수동 기록 가이드를 함께 반영한다. 정책 수정 시 SDK와 앱 사본을 동기화한다.
4. 필요 없는 M3Util submodule이 있는 것처럼 두지 않는다. 실제 .gitmodules, checkout 경로, 읽기 권한을 확인하고 해당 앱에 맞게 조정한다.
5. CI 후보의 app manifest를 사용한다. KeyTool 전용 kind/name/기기 정책을 복사하지 않는다.
6. 로컬 정책 테스트와 후보 빌드 → PR CI → 허용된 실기/증거 검증을 거쳐 적용한다. 수동 기록과 매뉴얼 검수도 완료 조건에 포함한다.

StartUp은 이 템플릿을 master/nougat 두 변형으로 구체화한 적용 사례다. 앱 기능 코드와 실제 릴리즈 버전은 템플릿에 포함하지 않는다.
