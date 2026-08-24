# 배포 패키지 샘플 검증 가이드

## 목적

샘플 앱은 SDK 저장소의 로컬 모듈이 아니라 패키징된 SDK를 검증한다. 개발, 선택적 Alpha, 정식 검증 단계에서 같은 샘플 소스를 사용하고 package feed와 버전만 외부 설정으로 변경한다.

- Android 샘플의 정식 기본값: JitPack `com.github.m3mobile:M3SDK:<version>`.
- C# 샘플의 정식 기본값: NuGet Gallery `M3Mobile.M3Sdk.Xamarin:<version>`.
- 로컬·Alpha 검증은 임시 Gradle init script 또는 `NuGet.Config`로 feed만 바꾸며 샘플 소스는 수정하지 않는다.
- 두 샘플 모두 버전을 명시하지 않으면 빌드가 실패한다.
- `ProjectReference`, `project(":sdk")`, `projects.sdk`를 금지한다.
- 로컬 경로, PAT, Alpha feed 설정은 저장소에 커밋하지 않는다.

## 샘플 범위

| 구분 | 대표 기능 | 결과 판정 |
|---|---|---|
| 장치/SDK 정보 | Model, Android, 샘플 앱, SDK, StartUp, ScanEmul, KeyTool 버전 | 화면 표시 |
| Airplane Mode | 켜기/끄기와 상태 확인 | 요청 상태 + 관찰값 |
| App | 패키지명 입력 후 Run Application | 단방향 요청 |
| Device | Get Serial Number | 응답값 |
| Language | English/Korean 변경 | 단방향 요청 |
| Network | NFC 켜기/끄기와 상태 확인 | 요청 상태 + 관찰값 |
| Permission | 샘플 앱 카메라 권한 부여와 실제 권한 확인 | 요청 상태 + 관찰값 |
| PROJECT_MEDIA | 패키지 직접 입력, DroidVNC, 미설치, 빈 대상 실제 호출과 7개 상태 UI 미리보기 | StartUp 상태 코드 또는 통신 예외 |
| MediaProjection 녹화 표시 | 예외 패키지 목록 대체·추가·삭제·전체 삭제 | 단방향 요청 + 실제 녹화 표시 확인 |
| Quick Tile | Wi-Fi Quick Tile 설정 | 단방향 요청 |
| Scanner | Scan Result Listener | 수신값 |
| StartUp Setting | Reset StartUp Settings | 단방향 요청 |
| Time | Get Timezone | 응답값 |
| USB | Get Current USB Modes | 응답값 |
| Wi-Fi | Get Factory Wi-Fi MAC, Set Wi-Fi Enabled true/false | MAC 응답값 또는 StartUp 오류, Wi-Fi 활성화/비활성화 실제 확인 |
| AppCenter | Change Admin Password, Keep Admin Mode On Sleep true/false | `REQUEST_SENT_UNVERIFIED` + AppCenter 2.2.0 이상에서 실제 동작 확인 |
| KeyTool | Set Key Function, 매핑+Wake-Up 오버로드, Scan Key Wake-Up, Home/Recent 활성화·비활성화 | `REQUEST_SENT_UNVERIFIED` + 실제 버튼/Wake-Up 확인 |

기본 UI 리소스는 영어이며 한국어(`values-ko`)만 추가한다.

## 결과 상태

- `SUCCESS`: SDK 응답 또는 시스템 관찰값으로 결과를 확인했다.
- `REQUEST_SENT_UNVERIFIED`: 단방향 broadcast를 보냈으나 대상 앱의 처리 응답은 없다.
- `FAILED`: 예외가 발생했다. 예외 타입과 메시지, 모델, Android, SDK 버전, 대상 앱 버전을 함께 표시한다.
- KeyTool의 정상 반환을 `SUCCESS`로 표시하지 않는다. 물리 키, Home/Recent 또는 Wake-Up 동작을 장치에서 확인해야 한다.
- 텍스트 입력 후 모든 SDK 실행 버튼은 포커스를 해제하고 소프트 키보드를 닫아 결과 영역을 가리지 않게 한다.

## 작업 및 배포 순서

1. SDK 기능, 공개 API 스냅샷, 매뉴얼, 두 샘플 소스를 같은 PR에서 변경한다.
2. 로컬 임시 Maven/NuGet package를 같은 두 샘플이 외부 dependency로 restore/build/install하고 대상 장치에서 runtime 동작을 확인한다.
3. PR에서 Android SDK 테스트와 API 호환성 검사를 실행하고 Xamarin Release build 결과를 확인한다.
4. 아래 위험 기준에 해당할 때만 GitHub Packages Alpha를 동일한 샘플 소스로 검증한다. 생략 근거는 PR 테스트 결과에 기록한다.
5. Deployment workflow를 `dry_run=true`로 실행해 문서, Android 산출물, Xamarin nupkg를 검증한다.
6. dry run 성공 후 같은 버전으로 `dry_run=false`를 실행해 태그, NuGet 패키지, GitHub Release를 생성한다.
7. Deployment workflow가 GitHub Release 생성 직후 `Verify Published Samples`를 `workflow_dispatch`로 명시 실행한다. `GITHUB_TOKEN`으로 만든 Release는 일반 `published` 이벤트 workflow를 연쇄 실행하지 않기 때문이다.
8. Android job은 해당 태그를 JitPack에서 받아 Compose 샘플 APK를 빌드한다.
9. C# job은 같은 버전을 NuGet Gallery에서 받아 `net10.0-android` 샘플 APK를 빌드한다.
10. 두 빌드가 통과한 후 workflow APK를 M3 장치에 설치해 아래 수동 검증을 수행한다.
11. 공개 패키지와 실기기 검증이 모두 성공한 뒤에만 고객 공지와 안정 버전 승격을 진행한다.

## Alpha 적용 기준

Alpha는 기본 필수 단계가 아니다. 다음 중 하나에 해당할 때 수행한다.

- 기존 public API signature 변경 또는 binary/source compatibility 위험이 있다.
- Maven/NuGet package 구조, target framework, build targets 또는 배포 도구가 변경된다.
- 대표 모델의 로컬 패키지 실기기 검증이 부족하거나 companion app 최소 버전 영향이 크다.
- 권한·보안·시스템 설정 또는 여러 모듈에 걸친 변경으로 실패 영향이 크다.

다음 조건을 모두 충족한 additive API 변경은 Alpha를 생략할 수 있다.

- 기존 public API를 변경하지 않고 새 API만 추가했다.
- 로컬 임시 package를 두 공식 샘플이 외부 dependency로 restore/build했다.
- 대상 모델에서 install/runtime 동작과 실패 진단 UI를 확인했다.
- PR CI와 Deployment dry run을 통과했다.
- 정식 배포 후 고객 공지 전에 JitPack/NuGet Gallery 패키지를 재검증할 수 있다.

## 장치 검증 체크리스트

- 샘플 화면의 SDK 의존성 버전이 릴리스 태그와 같은가.
- StartUp, ScanEmul, KeyTool 패키지와 버전이 예상값으로 표시되는가.
- 응답형 API가 실제 값을 표시하는가.
- 단방향 API가 `REQUEST_SENT_UNVERIFIED`로 표시되는가.
- 실패 시 예외 타입, 메시지, 모델, Android 및 대상 앱 버전을 한 화면에서 확인할 수 있는가.
- SM24에서 DroidVNC 프리셋 호출 결과가 `SUCCESS(0)`이고 실제 AppOps가 `allow`인가.
- SM24에서 미설치 패키지와 빈 패키지 프리셋이 각각 `TARGET_NOT_INSTALLED(2)`, `INVALID_TARGET(3)`를 반환하는가.
- 최신 StartUp을 설치한 비 SM24에서 호출 결과가 `UNSUPPORTED_DEVICE(1)`인가.
- 두 샘플의 상태 미리보기에서 0~6 전체 상태의 의미와 대응 방법이 표시되며 실제 StartUp 미호출임을 명시하는가.
- SM24와 StartUp 6.8.7 이상에서 MediaProjection 녹화 표시 예외 목록의 대체·추가·삭제·전체 삭제 요청이 `REQUEST_SENT_UNVERIFIED`로 표시되는가.
- 예외 패키지로 새 MediaProjection 세션을 시작했을 때 상태 표시줄 녹화 표시가 숨겨지고, 예외 삭제 후 세션을 종료·재시작하면 다시 표시되는가.
- 실행 중인 MediaProjection 세션에서 예외 목록을 변경했을 때 표시가 즉시 갱신되지 않을 수 있으며,
  SM24 알림창을 열었다 닫으면 현재 세션의 표시가 갱신될 수 있는가.
- 표시 상태를 확실하게 검증할 때 MediaProjection 세션 종료·재시작만 필요하고 기기 재부팅은 필요하지 않은가.
- StartUp 앱 재시작과 기기 재부팅 후 예외 패키지 목록과 실제 표시 숨김 동작이 복원되는가.
- 지원 모델에서 Wi-Fi를 끈 상태로 `setWifiEnabled(true)` 호출 후 실제 Wi-Fi가 켜지는가. StartUp 6.8.5 이상 조건으로 확인하며, SM24는 6.8.3 이상으로 확인한다.
- 지원 모델에서 Wi-Fi를 켠 상태로 `setWifiEnabled(false)` 호출 후 실제 Wi-Fi가 꺼지는가. 지원 모델은 SM20, SL20, SL20P, SL20K, US20, US30, UL20 계열, UL30, SM24, SM25, PC10, WD10이다.
- AppCenter 2.2.0 이상에서 관리자 비밀번호 변경 요청이 `REQUEST_SENT_UNVERIFIED`로 표시되고, 비밀번호 값이 결과 화면에 표시되지 않는가.
- AppCenter 2.2.0 이상에서 화면 OFF 관리자 모드 유지/해제 요청 후 실제 화면 OFF 동작이 기대와 일치하는가.
- KeyTool `Set Key Function` 호출 후 지정한 물리 키가 실제로 변경되었는가.
- SM24에서 3인자 `Set Key Function` 호출 후 `key_function`과 `key_wakeup`이 하나의
  `ACTION_SET_KEY` 요청으로 전달되고, 매핑과 Wake-Up 상태가 모두 적용되는가.
- SM24에서 KeyTool `1.3.8` 이상(`1.3.9` 이상 권장)으로 왼쪽/오른쪽 Scan Wake-Up을
  Enable/Disable 했을 때 `persist.sys.key_scan_left.wakeup` 및
  `persist.sys.key_scan_right.wakeup` 값이 각각 `1`/`0`으로 변경되는가.
- 화면 OFF 상태에서 실제 측면 스캔 버튼을 눌러 Enable 시 Wake-Up되고 Disable 시
  Wake-Up되지 않는가. `adb keyevent`는 물리 버튼 검증을 대체하지 않는다.
- Scan Wake-Up Enable 상태로 재부팅한 뒤 property, 실제 Wake-Up, KeyTool JSON 설정이
  유지되는가.
- 검증 로그에 `KeySettingReceiver`, `WakeUp updated`가 있고 `SecurityException`, crash,
  ANR이 없는가.
- 검증 종료 후 SDK API로 왼쪽/오른쪽 Wake-Up 설정을 시작 전 값으로 복원했는가.
- SM24/SM25에서 KeyTool 1.4.1 이상의 Home/Recent 활성화·비활성화가 실제 버튼에 적용되는가.
- 입력 필드에 소프트 키보드를 연 상태에서 SDK 버튼을 눌렀을 때 키보드가 닫히고 결과가 보이는가.
- 기본 영어 UI와 한국어 UI가 모두 표시되는가.

배포 패키지 smoke build는 패키지 발행 뒤에만 가능하므로 SDK 패키지 발행 자체를 되돌리지 않는다.
대신 외부 공지 또는 안정 버전 승격의 필수 게이트로 사용한다.
검증에 실패하면 고객 공지를 중단하고 동일 버전의 tag 또는 package를 덮어쓰지 않는다. 수정 후 새 patch 버전으로 배포한다.
