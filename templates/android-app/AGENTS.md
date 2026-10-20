# Android 앱 공통 작업 지침

- docs/development-workflow.md를 먼저 읽고 실제 앱·저장소·변형·지원 모델을 확인한다.
- 실기 테스트/실제 배포 허용 범위는 시작 시 확정한다. 미허용이면 pending으로 남기며 완료 조건을 우회하지 않는다.
- 앱·M3SDK·Kotlin/C# 소비 샘플·매뉴얼·Excel 기록을 함께 추적한다.
- workflow/app.json은 해당 앱의 JVM 테스트/assemble 작업과 APK metadata 위치로 수정한다.
- 회귀 생략과 적용 제외는 리뷰된 구체적 근거가 필요하다. 요청 전송은 실제 적용 성공이 아니다.
- Excel 연결이 없으면 docs/release-notes-manual.md에 따라 입력용 TSV를 제공하고 반영 확인까지 미완료로 남긴다.
