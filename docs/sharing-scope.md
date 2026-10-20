# 공유 workflow 변경 범위

- 공유/PR 대상은 M3SDK 저장소 한 곳이다.
- Kotlin/C# API·앱 기능 구현과 실제 릴리즈 버전 변경은 포함하지 않는다.
- 원래 체크아웃의 KeyTool Back 기능 등 기존 사용자 변경은 보존한다.
- 구현·리뷰·회귀 판단·패키지 소비 검증·매뉴얼·릴리즈 노트 수동 인계·배포/완료 gate를 공유한다.
- 다른 앱에 적용할 예시는 templates/android-app에 둔다. 앱 저장소 수정·push·PR은 별도 요청이 필요하다.
- 실기 테스트·실제 앱/SDK 배포·Excel 원본 편집은 수행하지 않는다.
- 실제 원격 CI 결과는 공유 PR, 동료 최초 설정은 onboarding-startup.md를 확인한다.
