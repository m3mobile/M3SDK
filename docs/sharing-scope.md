# 공유 workflow 변경 범위

- 앱/SDK 기능 구현과 버전 변경은 포함하지 않는다.
- 기존 KeyTool Back 기능 작업과 사용자 로컬 수정은 원래 체크아웃에 보존한다.
- 공통 lifecycle, 매뉴얼 검수, 릴리즈 노트 수동 인계, 모델별 회귀 판단, 후보/배포/완료 검증을 공유한다.
- StartUp은 master/nougat 빌드/JVM 테스트를 설정했다. SDK는 Maven/NuGet 패키지를 소비하는 양쪽 샘플 CI를 사용한다.
- 실기 테스트·실제 앱/SDK 배포·Excel 원본 편집은 수행하지 않는다.
- 원격 CI 결과와 최초 설정 방법은 PR 및 onboarding-startup.md를 확인한다.
