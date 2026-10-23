# 다른 Android 앱에 적용

앱 저장소 적용을 별도로 요청받은 경우에만 아래 절차를 수행한다. M3SDK 공유만 요청받았다면 앱 저장소에 수정·push·PR을 만들지 않는다.

SDK 패키지 검증은 M3SDK docs/development-workflow.md를 참고한다. M3SDK의 수동 JSON 제거를 앱의 실기·증거·완료 정책 변경으로 적용하지 않는다. 이 폴더의 release-evidence-template.json과 기존 앱 정책을 사용한다.

1. 이 폴더의 .github/workflows와 AGENTS.md를 새 앱 저장소에 반영한다. 기존 파일이 있으면 병합 검토하며 덮어쓰지 않는다.
2. workflow/app.example.json을 workflow/app.json으로 복사하고 앱/패키지/Gradle 테스트·assemble 작업/변형별 output-metadata.json 경로를 실제 앱으로 바꾼다.
3. M3SDK tools/workflow의 Python 파일들(공통 artifacts.py 포함)을 앱의 같은 위치에 복사한다. 이 폴더의 release-evidence-template.json은 앱 docs에 복사하고, 릴리즈 노트 안내는 docs/release-note-input.example.json과 docs/release-notes-manual.md를 참고한다. SDK 배포 workflow를 앱에 그대로 적용하지 말고 앱 이름·명령·저장소·기존 증거 정책에 맞춘다.
4. 필요 없는 M3Util submodule이 있는 것처럼 두지 않는다. 실제 .gitmodules, checkout 경로, 읽기 권한을 확인하고 해당 앱에 맞게 조정한다.
5. CI 후보의 app manifest를 사용한다. KeyTool 전용 kind/name/기기 정책을 복사하지 않는다.
6. 로컬 정책 테스트와 후보 빌드 → PR CI → 허용된 실기/증거 검증을 거쳐 적용한다. 수동 기록과 매뉴얼 검수도 완료 조건에 포함한다.

StartUp에 별도로 적용할 경우 master/nougat 두 변형의 JVM 테스트와 Release 빌드를 지정한다. 앱 기능 코드와 실제 릴리즈 버전은 템플릿에 포함하지 않는다.
