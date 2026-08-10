# M3 SDK 매뉴얼

PDF 다운로드 : [M3SDK_Manual_kr_v2.3.12.pdf](https://github.com/m3mobile/M3SDK/releases/download/2.3.12/M3SDK_Manual_kr_v2.3.12.pdf)


M3 SDK는 M3 Mobile 장치를 구성하고 제어하기 위한 API 모음을 제공합니다.

## 목차
- [요구 사항 (Requirements)](#요구-사항-requirements)
- [설치 방법 (Installation)](#설치-방법-installation)
  - [1. JitPack 저장소 추가](#1-jitpack-저장소-추가)
  - [2. 의존성 추가 (Add Dependency)](#2-의존성-추가-add-dependency)
- [기본 사용법 (Basic Usage)](#기본-사용법-basic-usage)
  - [API 접근](#api-접근)
  - [Strict Mode 및 예외 처리](#strict-mode-및-예외-처리)
- [API](#api)
  - [Airplane Mode API](#airplane-mode-api)
    - [비행기 모드 켜기](#비행기-모드-켜기)
    - [비행기 모드 끄기](#비행기-모드-끄기)
  - [App API](#app-api)
    - [로컬 APK 설치](#로컬-apk-설치)
    - [원격 APK 설치](#원격-apk-설치)
    - [애플리케이션 활성화](#애플리케이션-활성화)
    - [애플리케이션 비활성화](#애플리케이션-비활성화)
    - [애플리케이션 실행](#애플리케이션-실행)
    - [애플리케이션 실행 및 고정](#애플리케이션-실행-및-고정)
  - [Device API](#device-api)
    - [미디어 볼륨 설정](#미디어-볼륨-설정)
    - [벨소리 볼륨 설정](#벨소리-볼륨-설정)
    - [알림 볼륨 설정](#알림-볼륨-설정)
    - [알람 볼륨 설정](#알람-볼륨-설정)
    - [진동 모드 활성화](#진동-모드-활성화)
    - [진동 모드 비활성화](#진동-모드-비활성화)
    - [디스플레이 설정](#디스플레이-설정)
    - [시리얼 번호 조회](#시리얼-번호-조회)
    - [상태 표시줄 확장 잠금](#상태-표시줄-확장-잠금)
    - [상태 표시줄 확장 잠금 해제](#상태-표시줄-확장-잠금-해제)
    - [Bluetooth MAC 주소 조회](#bluetooth-mac-주소-조회)
  - [Language API](#language-api)
    - [언어 설정](#언어-설정)
  - [Network API](#network-api)
    - [APN 설정](#apn-설정)
    - [NFC 활성화](#nfc-활성화)
    - [NFC 비활성화](#nfc-비활성화)
  - [Permission API](#permission-api)
    - [권한 부여](#권한-부여)
    - [권한 취소](#권한-취소)
    - [애플리케이션 PROJECT_MEDIA 허용](#애플리케이션-projectmedia-허용)
  - [Quick Tile API](#quick-tile-api)
    - [빠른 설정 타일 지정](#빠른-설정-타일-지정)
    - [빠른 설정 타일 초기화](#빠른-설정-타일-초기화)
  - [Scanner API](#scanner-api)
    - [스캔 시작](#스캔-시작)
    - [스캔 중지](#스캔-중지)
    - [스캐너 상태 조회](#스캐너-상태-조회)
    - [스캐너 타입 조회](#스캐너-타입-조회)
    - [스캔 결과 리스너](#스캔-결과-리스너-scan-result-listener)
    - [GS1 파싱 결과 리스너](#gs1-파싱-결과-리스너-gs1-parsed-listener)
    - [디지털 링크 파싱 결과 리스너](#디지털-링크-파싱-결과-리스너-digital-link-parsed-listener)
    - [스캐너 설정 (Scanner Settings)](#스캐너-설정-scanner-settings)
    - [플로팅 스캐너 버튼 UI](#플로팅-스캐너-버튼-ui)
  - [StartUp Setting API](#startup-setting-api)
    - [StartUp 설정 초기화](#startup-설정-초기화)
  - [KeyTool API](#keytool-api)
    - [Function 키 모드 제어](#function-키-모드-제어)
    - [키 기능 설정](#키-기능-설정)
    - [Home 및 Recent 버튼 제어](#home-및-recent-버튼-제어)
    - [스캔 키 Wake-Up 제어](#스캔-키-wake-up-제어)
  - [AppCenter Kiosk API](#appcenter-kiosk-api)
    - [키오스크 관리자 비밀번호 변경](#키오스크-관리자-비밀번호-변경)
    - [화면 OFF 시 관리자 모드 유지](#화면-off-시-관리자-모드-유지)
  - [Time API](#time-api)
    - [날짜 및 시간 설정](#날짜-및-시간-설정)
    - [NTP 서버 설정](#ntp-서버-설정)
    - [시간대 설정](#시간대-설정)
    - [NTP 서버 조회](#ntp-서버-조회)
    - [NTP 동기화 간격 조회](#ntp-동기화-간격-조회)
    - [시간대 조회](#시간대-조회)
  - [Usb API](#usb-api)
    - [USB 모드를 MTP로 설정](#usb-모드를-mtp로-설정)
    - [USB 모드를 RNDIS로 설정](#usb-모드를-rndis로-설정)
    - [USB 모드를 MIDI로 설정](#usb-모드를-midi로-설정)
    - [USB 모드를 PTP로 설정](#usb-모드를-ptp로-설정)
    - [USB 데이터 비활성화 (충전 전용)](#usb-데이터-비활성화-충전-전용)
    - [현재 USB 모드 조회](#현재-usb-모드-조회)
  - [Wifi API](#wifi-api)
    - [Wi-Fi MAC 주소 조회](#wi-fi-mac-주소-조회)
    - [Factory Wi-Fi MAC 주소 조회](#factory-wi-fi-mac-주소-조회)
    - [Wi-Fi 활성화 상태 설정](#wi-fi-활성화-상태-설정)
    - [캡티브 포털 감지 (Captive Portal Detection)](#캡티브-포털-감지-captive-portal-detection)
    - [주파수 대역 제어 (Frequency Band Control)](#주파수-대역-제어-frequency-band-control)
    - [Wi-Fi 국가 코드 설정](#wi-fi-국가-코드-설정)
    - [개방형 네트워크 알림 (Open Network Notification)](#개방형-네트워크-알림-open-network-notification)
    - [로밍 구성 (Roaming Configuration)](#로밍-구성-roaming-configuration)
      - [로밍 트리거 설정](#로밍-트리거-설정)
      - [로밍 델타 설정](#로밍-델타-설정)
    - [Wi-Fi 절전 정책 (Wi-Fi 절전 정책)](#wi-fi-절전-정책-wi-fi-sleep-policy)
    - [Wi-Fi 안정성 (Wi-Fi 안정성)](#wi-fi-안정성-wi-fi-stability)
    - [Wi-Fi 채널 설정](#wi-fi-채널-설정)
    - [네트워크 관리 (Network Management)](#네트워크-관리-network-management)
      - [액세스 포인트 설정](#액세스-포인트-설정)
      - [저장된 Wi-Fi 네트워크 초기화](#저장된-wi-fi-네트워크-초기화)
      - [Wi-Fi 네트워크 제거](#wi-fi-네트워크-제거)
    - [기기별 Wi-Fi 설정 (Device Specific Wi-Fi Settings)](#기기별-wi-fi-설정-device-specific-wi-fi-settings)
      - [로밍 임계값 조회](#로밍-임계값-조회)
      - [로밍 델타값 조회](#로밍-델타값-조회)
      - [Wi-Fi 주파수 대역 조회](#wi-fi-주파수-대역-조회)
      - [Wi-Fi 국가 코드 조회](#wi-fi-국가-코드-조회)


## 요구 사항 (Requirements)

*   **Min SDK**: 24 (Android 7.0)
*   **Kotlin 버전**: 1.8
*   **JDK 버전**: 1.8

## 설치 방법 (Installation)

### 1. JitPack 저장소 추가

프로젝트의 `settings.gradle.kts` (또는 루트 `build.gradle`) 파일에 JitPack 저장소를 추가합니다.

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") } // 이 줄을 추가하세요
    }
}
```

### 2. 의존성 추가 (Add Dependency)

모듈 수준의 `build.gradle`에 의존성을 추가합니다.

```kotlin
// Kotlin
dependencies {
    implementation("com.github.m3mobile:M3SDK:2.3.12")
}
```

```groovy
// Groovy
dependencies {
    implementation "com.github.m3mobile:M3SDK:2.3.12"
}
```

최신 버전은 릴리즈 문서를 참고해주세요.
- https://github.com/m3mobile/M3SDK/releases

## 기본 사용법 (Basic Usage)

SDK는 애플리케이션 시작 시 자동으로 초기화됩니다. 수동으로 초기화할 필요가 없습니다.

### API 접근

모든 기능은 싱글톤 인스턴스를 통해 접근할 수 있습니다.

```kotlin
import net.m3mobile.sdk.M3Mobile

// 예시: 비행기 모드 켜기
M3Mobile.instance.turnOnAirplaneMode()
```

### Strict Mode 및 예외 처리

M3 SDK는 특정 API 호출이 (장치 지원 또는 앱 버전과 같은) 조건을 충족하지 못할 때의 동작 방식에 영향을 미치는 **Strict Mode**를 제공합니다.

**작동 방식:**

*   **활성화된 경우**: Strict Mode가 활성화되면, 다음 어노테이션이 붙은 API 호출은 조건이 충족되지 않을 경우 예외를 발생시킵니다:
    *   `@SupportedModels`: 해당 API를 이용 가능한 장치 모델들을 명시합니다.
    *   `@UnsupportedModels`: 해당 API 이용이 불가능한 장치 모델들을 명시합니다.
    *   `@RequiresStartUp`: 해당 API를 이용하기 위해서는 특정 버전 이상의 StartUp이 설치되어 있어야 함을 나타냅니다.
    *   `@RequiresScanEmul`: 해당 API를 이용하기 위해서는 특정 버전 이상의 ScanEmul이 설치되어 있어야 함을 나타냅니다.

    발생할 수 있는 예외는 다음과 같습니다.
    *   `KeyToolAppUnavailableException`: API에 필요한 KeyTool 앱이 설치되어 있지 않거나 앱에서 확인할 수 없을 때 발생합니다. KeyTool 호출은 단방향 broadcast이므로 이 검사는 Strict Mode 설정과 관계없이 수행됩니다.

    *   `UnsupportedDeviceModelException`: 지원되지 않는 장치 모델에서 API가 호출될 경우 발생합니다.
    *   `UnsatisfiedVersionException`: API가 설치된 장치의 StartUp, ScanEmul, AppCenter 또는 KeyTool 애플리케이션 버전보다 더 높은 버전을 요구할 경우 발생합니다. 예를 들어, StartUp 앱 1.0.0이 설치된 장치에서 @RequiresStartUp("2.0.0")인 메서드를 호출할 경우 발생합니다. AppCenter Kiosk 및 `com.m3.keytoolsl20` 기반 KeyTool API의 버전 검사는 Strict Mode 설정과 관계없이 항상 수행됩니다.
    
*   **비활성화된 경우**: 이 모드에서는 필요한 조건을 충족하지 못하는 API 호출은 **자동으로 무시**됩니다. 예외가 발생하지 않으므로 애플리케이션은 중단 없이 계속 실행됩니다.

**Strict Mode 활성화:**

Strict Mode는 기본적으로 비활성화 상태입니다.

Strict Mode를 활성화하려면 애플리케이션의 `AndroidManifest.xml` 파일 내 `<application>` 태그 안에 다음 `<meta-data>` 태그를 추가하세요.

```xml
<application ...>
    <meta-data
        android:name="M3_STRICT_MODE"
        android:value="true" />
</application>
```

개발 및 테스트 중에는 잠재적인 문제를 조기에 발견하기 위해 Strict Mode를 활성화하는 것이 좋습니다. 프로덕션 환경에서는 애플리케이션의 오류 처리 전략에 따라 자동 무시 또는 명시적인 예외 처리 중 어떤 것이 더 적합한지 고려하세요.


**직접 Broadcast 공통 전송 예제**

각 API의 `직접 Broadcast` 표에 표시된 action, target package, typed extra를 다음 형식에 대입합니다. AirWatch, SOTI 등 MDM 콘솔에서는 동일 값을 각 Broadcast 입력 필드에 설정하세요.

```kotlin
// Implicit broadcast
val implicitRequest = Intent("ACTION_FROM_THIS_MANUAL")
    .putExtra("extra_key", "extra_value")
context.sendBroadcast(implicitRequest)

// Explicit broadcast
val explicitRequest = Intent("ACTION_FROM_THIS_MANUAL")
    .setPackage("TARGET_PACKAGE_FROM_THIS_MANUAL")
    .putExtra("extra_key", true)
context.sendBroadcast(explicitRequest)
```
---

## API

### Airplane Mode API

장치의 비행기 모드를 제어합니다.

#### 비행기 모드 켜기

비행기 모드를 켭니다.

*   **필요 StartUp 버전**: `6.3.7` 이상

```kotlin
M3Mobile.instance.turnOnAirplaneMode()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra      | 타입        | 필수 | 값          |
|------------|-----------|----|------------|
| `setting`  | `String`  | O  | `airplane` |
| `airplane` | `Boolean` | O  | `true`     |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 비행기 모드 끄기

비행기 모드를 끕니다.

*   **필요 StartUp 버전**: `6.3.7` 이상

```kotlin
M3Mobile.instance.turnOffAirplaneMode()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra      | 타입        | 필수 | 값          |
|------------|-----------|----|------------|
| `setting`  | `String`  | O  | `airplane` |
| `airplane` | `Boolean` | O  | `false`    |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


---

### App API

애플리케이션을 설치하거나 특정 패키지를 활성화/비활성화하고, 앱 실행 및 화면 고정을 제어합니다.

#### 로컬 APK 설치

로컬 파일 경로에서 APK를 설치합니다. 기존 1인자 overload는 원래 동작을 유지합니다.
추가 overload로 동일 `versionCode` APK 재설치와 설치 성공 후 대상 앱 실행을 선택할 수 있습니다.

*   **필요 StartUp 버전**:
    *   경로만 전달: `6.2.14` 이상
    *   `allowSameVersionUpdate`: `6.8.1` 이상
    *   `launchAfterInstall`: `6.8.2` 이상
*   **매개변수**:
    *   `filePath` (String): 설치할 .apk 파일의 절대 경로입니다.
    *   `allowSameVersionUpdate` (Boolean): APK `versionCode`가 설치된 앱과 같을 때 재설치합니다.
    *   `launchAfterInstall` (Boolean): 설치에 성공한 경우에만 설치된 앱을 실행합니다.

```kotlin
M3Mobile.instance.installLocalApk(filePath)
M3Mobile.instance.installLocalApk(filePath, allowSameVersionUpdate = true)
M3Mobile.instance.installLocalApk(
    filePath,
    allowSameVersionUpdate = true,
    launchAfterInstall = true
)
```

StartUp 6.8.1에서 동일 버전 재설치만 필요하면 2인자 overload를 사용합니다.
3인자 overload는 `launchAfterInstall`이 `false`여도 항상 StartUp 6.8.2 이상이 필요합니다.

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra                       | 타입        | 필수 | 값                     |
|-----------------------------|-----------|----|-----------------------|
| `setting`                   | `String`  | O  | `apk_install`         |
| `type`                      | `Int`     | O  | `0`                   |
| `path`                      | `String`  | O  | APK 절대 경로             |
| `allow_same_version_update` | `Boolean` | X  | 동일 versionCode 재설치 여부 |
| `launch_after_install`      | `Boolean` | X  | 설치 성공 후 실행 여부         |

두 선택 extra는 생략하면 `false`로 처리합니다.

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 원격 APK 설치

원격 URL에서 APK를 다운로드하여 설치합니다. 로컬 APK 설치와 동일하게 동일 버전 재설치와
설치 후 실행 옵션을 제공합니다.

*   **필요 StartUp 버전**:
    *   URL만 전달: `6.2.14` 이상
    *   `allowSameVersionUpdate`: `6.8.1` 이상
    *   `launchAfterInstall`: `6.8.2` 이상
*   **매개변수**:
    *   `url` (String): APK 파일의 URL입니다.
    *   `allowSameVersionUpdate` (Boolean): APK `versionCode`가 설치된 앱과 같을 때 재설치합니다.
    *   `launchAfterInstall` (Boolean): 설치에 성공한 경우에만 설치된 앱을 실행합니다.

```kotlin
M3Mobile.instance.installRemoteApk(url)
M3Mobile.instance.installRemoteApk(url, allowSameVersionUpdate = true)
M3Mobile.instance.installRemoteApk(
    url,
    allowSameVersionUpdate = true,
    launchAfterInstall = true
)
```

APK 설치 요청은 단방향 broadcast입니다. 정상 반환은 M3SDK가 요청을 보냈다는 의미이며,
다운로드·설치·실행 성공을 보장하지 않습니다. StartUp은 `PackageInstaller`가 성공을 반환하고
설치된 package name을 제공한 경우에만 앱을 실행합니다. 다운로드나 설치 실패, package name 누락,
동일 버전 설치 생략 시에는 앱을 실행하지 않습니다. StartUp 알림과 로그, 설치된 패키지 및
실행 화면을 확인하세요.

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra                       | 타입        | 필수 | 값                     |
|-----------------------------|-----------|----|-----------------------|
| `setting`                   | `String`  | O  | `apk_install`         |
| `type`                      | `Int`     | O  | `1`                   |
| `url`                       | `String`  | O  | APK 다운로드 URL          |
| `allow_same_version_update` | `Boolean` | X  | 동일 versionCode 재설치 여부 |
| `launch_after_install`      | `Boolean` | X  | 설치 성공 후 실행 여부         |

두 선택 extra는 생략하면 `false`로 처리합니다.

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 애플리케이션 활성화

지정된 애플리케이션 패키지를 활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `packageName` (String): 활성화할 애플리케이션의 패키지 이름입니다.

```kotlin
M3Mobile.instance.enableApp(packageName: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra          | 타입        | 필수 | 값             |
|----------------|-----------|----|---------------|
| `setting`      | `String`  | O  | `application` |
| `package_name` | `String`  | O  | 대상 앱 패키지      |
| `enable`       | `Boolean` | O  | `true`        |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 애플리케이션 비활성화

지정된 애플리케이션 패키지를 비활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `packageName` (String): 비활성화할 애플리케이션의 패키지 이름입니다.

```kotlin
M3Mobile.instance.disableApp(packageName: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra          | 타입        | 필수 | 값             |
|----------------|-----------|----|---------------|
| `setting`      | `String`  | O  | `application` |
| `package_name` | `String`  | O  | 대상 앱 패키지      |
| `enable`       | `Boolean` | O  | `false`       |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

#### 애플리케이션 실행

지정된 애플리케이션 패키지를 활성화한 뒤 실행합니다.

*   **필요 StartUp 버전**: `6.8.0` 이상
*   **매개변수**:
    *   `packageName` (String): 활성화하고 실행할 애플리케이션의 패키지 이름입니다.
*   **참고**:
    *   이 API는 앱을 실행하기 전에 패키지를 자동으로 활성화합니다. `enableApp`을 먼저 호출할 필요가 없습니다.
    *   `enableApp`은 패키지 활성화만 수행하며 앱을 실행하지 않습니다.

```kotlin
M3Mobile.instance.runApp(packageName: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra          | 타입        | 필수 | 값             |
|----------------|-----------|----|---------------|
| `setting`      | `String`  | O  | `application` |
| `package_name` | `String`  | O  | 대상 앱 패키지      |
| `enable`       | `Boolean` | O  | `true`        |
| `auto_run`     | `Boolean` | O  | `true`        |
| `pin_app`      | `Boolean` | O  | `false`       |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 애플리케이션 실행 및 고정

지정된 애플리케이션 패키지를 활성화한 뒤 실행하고 화면에 고정합니다.

*   **필요 StartUp 버전**: `6.8.0` 이상
*   **매개변수**:
    *   `packageName` (String): 활성화, 실행, 고정할 애플리케이션의 패키지 이름입니다.
*   **사용 전제**:
    *   OS/StartUp 환경에서 Screen Pinning 기능이 활성화되어 있어야 합니다.
*   **참고**:
    *   이 API는 앱을 실행하고 고정하기 전에 패키지를 자동으로 활성화합니다. `enableApp`을 먼저 호출할 필요가 없습니다.
    *   StartUp 6.8.0은 앱 고정 해제용 SDK API 또는 broadcast API를 제공하지 않습니다. 고정된 앱에서 수동으로 빠져나오려면 홈 버튼을 10회 연속으로 누르세요.

```kotlin
M3Mobile.instance.runAndPinApp(packageName: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra          | 타입        | 필수 | 값             |
|----------------|-----------|----|---------------|
| `setting`      | `String`  | O  | `application` |
| `package_name` | `String`  | O  | 대상 앱 패키지      |
| `enable`       | `Boolean` | O  | `true`        |
| `auto_run`     | `Boolean` | O  | `true`        |
| `pin_app`      | `Boolean` | O  | `true`        |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.



---

### Device API

볼륨, 디스플레이, 진동 등 다양한 장치 설정을 제어합니다.

#### 미디어 볼륨 설정

미디어 볼륨을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **범위**: 0 ~ 15
*   **매개변수**:
    *   `value` (Int): 설정할 볼륨 레벨입니다.

```kotlin
M3Mobile.instance.setMediaVolume(value: Int)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra          | 타입       | 필수 | 값             |
|----------------|----------|----|---------------|
| `setting`      | `String` | O  | `volume`      |
| `volume_media` | `Int`    | O  | SDK에 전달한 볼륨 값 |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 벨소리 볼륨 설정

벨소리 볼륨을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **범위**:
    *   0 ~ 15: 모델 `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `SL25`, `PC10`
    *   0 ~ 7: 그 외 모든 모델
*   **매개변수**:
    *   `value` (Int): 설정할 볼륨 레벨입니다.

```kotlin
M3Mobile.instance.setRingtoneVolume(value: Int)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra             | 타입       | 필수 | 값             |
|-------------------|----------|----|---------------|
| `setting`         | `String` | O  | `volume`      |
| `volume_ringtone` | `Int`    | O  | SDK에 전달한 볼륨 값 |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 알림 볼륨 설정

알림 볼륨을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **필요 Android 버전**: Android 14 (UpsideDownCake) 이상
*   **범위**: `setRingtoneVolume`과 동일
*   **매개변수**:
    *   `value` (Int): 설정할 볼륨 레벨입니다.

```kotlin
M3Mobile.instance.setNotificationVolume(value: Int)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra                 | 타입       | 필수 | 값             |
|-----------------------|----------|----|---------------|
| `setting`             | `String` | O  | `volume`      |
| `volume_notification` | `Int`    | O  | SDK에 전달한 볼륨 값 |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 알람 볼륨 설정

알람 볼륨을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **범위**:
    *   0 ~ 15: 모델 `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `PC10`
    *   0 ~ 7: 그 외 모든 모델
*   **매개변수**:
    *   `value` (Int): 설정할 볼륨 레벨입니다.

```kotlin
M3Mobile.instance.setAlarmVolume(value: Int)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra          | 타입       | 필수 | 값             |
|----------------|----------|----|---------------|
| `setting`      | `String` | O  | `volume`      |
| `volume_alarm` | `Int`    | O  | SDK에 전달한 볼륨 값 |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 진동 모드 활성화

진동 모드를 활성화합니다. 이 기능을 사용하면 벨소리와 알림 볼륨이 0으로 설정됩니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```kotlin
M3Mobile.instance.enableVibrationMode()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra             | 타입        | 필수 | 값        |
|-------------------|-----------|----|----------|
| `setting`         | `String`  | O  | `volume` |
| `volume_vibrator` | `Boolean` | O  | `true`   |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 진동 모드 비활성화

진동 모드를 비활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```kotlin
M3Mobile.instance.disableVibrationMode()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra             | 타입        | 필수 | 값        |
|-------------------|-----------|----|----------|
| `setting`         | `String`  | O  | `volume` |
| `volume_vibrator` | `Boolean` | O  | `false`  |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 디스플레이 설정

디스플레이 설정을 구성합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `displaySetting` (DisplaySetting): 디스플레이 설정 정보를 담은 객체입니다.

```kotlin
M3Mobile.instance.setDisplaySetting(displaySetting: DisplaySetting)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra                           | 타입        | 필수 | 값                            |
|---------------------------------|-----------|----|------------------------------|
| `setting`                       | `String`  | O  | `display`                    |
| `display_auto_brightness`       | `Boolean` | X  | 자동 밝기                        |
| `display_brightness_step`       | `Int`     | X  | `1..255`                     |
| `display_auto_rotate`           | `Boolean` | X  | 자동 회전                        |
| `display_rotate_force`          | `Int`     | X  | `0..7`                       |
| `display_disable_screen_lock`   | `Boolean` | X  | 화면 잠금 비활성화 여부                |
| `display_sleep`                 | `Int`     | X  | 화면 꺼짐 시간(ms) 또는 `2147483647` |
| `display_policy_control`        | `Int`     | X  | `1..4`                       |
| `display_battery_percentage`    | `Int`     | X  | 표시 `1`, 숨김 `2`               |
| `display_screensaver_mode`      | `Int`     | X  | `0..3`                       |
| `display_screensaver_component` | `String`  | X  | component name               |

`setting` 외 extra는 변경할 항목만 보냅니다. 생략한 항목은 현재 시스템 값을 유지합니다.

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 시리얼 번호 조회

장치의 시리얼 번호를 조회합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **반환값**: 시리얼 번호 문자열입니다.

```kotlin
// 코루틴 (for kotlin)
M3Mobile.instance.getSerialNumber(): String

// 콜백 (for java)
M3Mobile.instance.getSerialNumber(callback: RequestCallback<String>): Job
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `get_serial` |

*   **Response action**: `com.android.server.startupservice.system.response`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `get_serial` | `String` | 시리얼 번호 |

> 일반 MDM 웹 콘솔은 응답 Broadcast를 수신하지 못할 수 있습니다. 결과가 필요하면 응답 action을 수신하는 Android 애플리케이션을 사용하세요.


#### 상태 표시줄 확장 잠금

상태 표시줄 확장을 잠급니다. 잠긴 경우 사용자는 상태 표시줄을 아래로 내려 알림이나 빠른 설정을 볼 수 없습니다.

*   **필요 StartUp 버전**: `6.4.12` 이상

```kotlin
M3Mobile.instance.lockStatusBarExpansion()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `status_bar` |
| `prevent` | `Boolean` | O | `true` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 상태 표시줄 확장 잠금 해제

상태 표시줄 확장을 잠금 해제합니다.

*   **필요 StartUp 버전**: `6.4.12` 이상

```kotlin
M3Mobile.instance.unlockStatusBarExpansion()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `status_bar` |
| `prevent` | `Boolean` | O | `false` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### Bluetooth MAC 주소 조회

장치의 Bluetooth MAC 주소를 조회합니다.

*   **지원 모델**: 전체 모델
*   **필요 StartUp 버전**: `6.5.31` 이상 (UL30), `6.5.35` 이상 (기타 모델)
*   **반환값**: Bluetooth MAC 주소 문자열입니다 (형식: XX:XX:XX:XX:XX:XX).

```kotlin
// 코루틴 (for kotlin)
M3Mobile.instance.getBluetoothMac(): String

// 콜백 (for java)
M3Mobile.instance.getBluetoothMac(callback: RequestCallback<String>): Job
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `get_bluetooth_mac` |

*   **Response action**: `com.android.server.startupservice.system.response`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `get_bluetooth_mac` | `String` | Bluetooth MAC 주소 |

> 일반 MDM 웹 콘솔은 응답 Broadcast를 수신하지 못할 수 있습니다. 결과가 필요하면 응답 action을 수신하는 Android 애플리케이션을 사용하세요.


---

### Language API

시스템 언어 설정을 제어합니다.

#### 언어 설정

장치의 시스템 언어와 국가를 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `language` (String): 언어 코드 (예: "en", "ko").
    *   `country` (String): 국가 코드 (예: "US", "KR").

```kotlin
M3Mobile.instance.setLanguage(language: String, country: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `language` |
| `language_value` | `String` | O | `language-country` 형식, 예: `ko-KR` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


---

### Network API

모바일 네트워크 설정을 구성합니다.

#### APN 설정

APN(Access Point Name) 구성을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `apn` (Apn): 네트워크 설정 정보를 담고 있는 `Apn` 객체입니다. `Apn.builder()`를 사용하여 생성하세요.

```kotlin
M3Mobile.instance.setApn(apn: Apn)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `apn` |
| `apn_name` | `String` | O | APN 이름 |
| `apn_url` | `String` | O | APN URL |
| `apn_mcc` | `String` | O | MCC |
| `apn_mnc` | `String` | O | MNC |
| `apn_type` | `String` | O | APN type |
| `apn_proxy` | `String` | X | Proxy |
| `apn_port` | `String` | X | Port |
| `apn_user` | `String` | X | User |
| `apn_password` | `String` | X | Password |
| `apn_server` | `String` | X | Server |
| `apn_mmsc` | `String` | X | MMSC |
| `apn_mms_proxy` | `String` | X | MMS proxy |
| `apn_mms_port` | `String` | X | MMS port |
| `apn_auth_type` | `Int` | X | Auth type |
| `apn_protocol` | `Int` | X | Protocol |
| `apn_roaming` | `Int` | X | Roaming protocol |
| `apn_mvno` | `Int` | X | MVNO type |
| `apn_mvno_value` | `String` | X | MVNO value |

선택 `String` extra는 생략하면 `null`, 선택 `Int` extra는 생략하면 `0`으로 처리합니다.

설정 요청 직후 `com.android.server.startupservice.config.fin` Broadcast를 추가로 보내야 합니다.

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### NFC 활성화

NFC(Near Field Communication)를 활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **필요 Android 버전**: Android 11 (R) 이상

```kotlin
M3Mobile.instance.enableNfc()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `nfc` |
| `nfc_on` | `Boolean` | O | `true` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### NFC 비활성화

NFC(Near Field Communication)를 비활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **필요 Android 버전**: Android 11 (R) 이상

```kotlin
M3Mobile.instance.disableNfc()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `nfc` |
| `nfc_on` | `Boolean` | O | `false` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


---

### Permission API

애플리케이션의 런타임 권한을 부여하거나 취소합니다.

#### 권한 부여

대상 애플리케이션 패키지에 특정 런타임 권한을 부여합니다.

*   **필요 StartUp 버전**: `6.4.17` 이상
*   **매개변수**:
    *   `packageName` (String): 애플리케이션의 패키지 이름입니다.
    *   `permission` (String): 부여할 권한의 전체 이름입니다 (예: `android.permission.CAMERA`).

```kotlin
M3Mobile.instance.grantPermission(packageName: String, permission: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `permission` |
| `package` | `String` | O | 대상 앱 패키지 |
| `permission` | `String` | O | Android permission |
| `permission_mode` | `Int` | O | `1` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 권한 취소

대상 애플리케이션 패키지에서 특정 런타임 권한을 취소합니다.

*   **필요 StartUp 버전**: `6.4.17` 이상
*   **매개변수**:
    *   `packageName` (String): 애플리케이션의 패키지 이름입니다.
    *   `permission` (String): 취소할 권한의 전체 이름입니다.

```kotlin
M3Mobile.instance.revokePermission(packageName: String, permission: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `permission` |
| `package` | `String` | O | 대상 앱 패키지 |
| `permission` | `String` | O | Android permission |
| `permission_mode` | `Int` | O | `2` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 애플리케이션 PROJECT_MEDIA 허용

설치된 화면 캡처 애플리케이션에 Android `PROJECT_MEDIA` AppOps를 허용합니다. 호출자가
패키지명을 전달하므로 DroidVNC-NG에 한정되지 않습니다.

*   **필요 StartUp 버전**: `6.8.4` 이상
*   **지원 모델**: 현재 `SM24`에서 `SUCCESS`를 반환하며, 다른 모델은 `UNSUPPORTED_DEVICE`를 반환합니다.
*   **매개변수**: `packageName`은 이미 설치된 애플리케이션의 정확한 패키지명입니다.
*   **호출 시점**: 고객 앱 시작 시 화면 캡처 앱 실행 또는 연결 전에 호출합니다. 반복 호출해도 안전합니다.

```kotlin
val result = M3Mobile.instance.allowProjectMedia("net.christianbeier.droidvnc_ng")
when (result.status) {
    ProjectMediaStatus.SUCCESS -> Log.i("ProjectMedia", "PROJECT_MEDIA 허용 완료")
    else -> Log.e("ProjectMedia", "${result.status}: ${result.errorMessage}")
}
```

취소 가능한 `Job`을 반환하는 callback 방식도 제공합니다.

```kotlin
val request = M3Mobile.instance.allowProjectMedia(packageName) { result, error ->
    when {
        error != null -> Log.e("ProjectMedia", "StartUp 통신 실패", error)
        result != null -> Log.i("ProjectMedia", "${result.status} (${result.status.code})")
    }
}
```

`ProjectMediaResult`는 StartUp의 기능 처리 결과입니다. 예외 또는 callback의 `error`는 Broadcast
실패나 응답 시간 초과 같은 통신 실패이며 `ProjectMediaStatus`가 없습니다. 알 수 없는 응답 코드는
`APPLY_FAILED`로 정규화하고 원본 코드를 `errorMessage`에 기록합니다.

| 코드 | `ProjectMediaStatus` | 의미 | 대응 방법 |
|---:|---|---|---|
| 0 | `SUCCESS` | StartUp이 허용을 적용하고 `MODE_ALLOWED`까지 확인했습니다. | 화면 캡처 앱을 실행하거나 연결합니다. |
| 1 | `UNSUPPORTED_DEVICE` | 현재 모델에서 지원하지 않습니다. | SM24 또는 해당 모델을 명시적으로 지원하는 StartUp을 사용합니다. |
| 2 | `TARGET_NOT_INSTALLED` | 요청한 패키지가 설치되어 있지 않습니다. | 앱을 설치하고 정확한 패키지명으로 재시도합니다. |
| 3 | `INVALID_TARGET` | 패키지명이 비어 있거나 패키지와 UID가 일치하지 않습니다. | 패키지명과 조회된 UID를 확인합니다. |
| 4 | `PERMISSION_DENIED` | 현재 시스템 권한으로 StartUp이 AppOps를 제어할 수 없습니다. | 펌웨어 서명, shared UID, AppOps 권한을 확인합니다. |
| 5 | `APP_OP_UNAVAILABLE` | 필요한 AppOps API를 사용할 수 없습니다. | Android Framework와 StartUp 호환성을 확인합니다. |
| 6 | `APPLY_FAILED` | 적용 실패 또는 조회 결과가 `MODE_ALLOWED`가 아닙니다. | StartUp 로그와 패키지, UID, AppOps 상태를 확인합니다. |

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: `com.m3.startup`

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `project_media` |
| `project_media_package` | `String` | O | 설치된 대상 패키지명 |
| `project_media_messenger` | `Messenger` | O | 결과를 받을 Messenger |

결과는 `project_media_messenger`를 통해 반환됩니다. `Message.what`에는
`ProjectMediaStatus` 코드가 전달되며, `project_media_error_message`에는 실패 상세 내용이
전달될 수 있습니다.

---

### Quick Tile API

시스템 UI의 빠른 설정 타일(Quick Settings tiles)을 사용자 지정합니다.

#### 빠른 설정 타일 지정

표시할 빠른 설정 타일을 설정합니다.

*   **필요 StartUp 버전**: `6.4.1` 이상
*   **매개변수**:
    *   `quickTile` (vararg QuickTile): 추가할 하나 이상의 `QuickTile` 객체입니다.

```kotlin
M3Mobile.instance.setQuickTiles(vararg quickTile: QuickTile)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `quick_tile` |
| `quick_tile_action` | `String` | O | `add` |
| `quick_tile_items` | `String` | O | JSON 배열 문자열 |

설정 요청 직후 `com.android.server.startupservice.config.fin` Broadcast를 추가로 보내야 합니다.

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 빠른 설정 타일 초기화

빠른 설정 타일 구성을 기본 상태로 재설정합니다.

*   **필요 StartUp 버전**: `6.4.1` 이상

```kotlin
M3Mobile.instance.resetQuickTile()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `quick_tile` |
| `quick_tile_action` | `String` | O | `reset` |
| `quick_tile_items` | `String` | O | `[]` |

설정 요청 직후 `com.android.server.startupservice.config.fin` Broadcast를 추가로 보내야 합니다.

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


---

### Scanner API

바코드 스캐너를 제어하고 스캔 환경을 설정합니다.

#### 스캔 시작

스캔 프로세스를 시작합니다.

*   **필요 ScanEmul 버전**: `2.13.0` 이상

```kotlin
M3Mobile.instance.startScan()
```

**직접 Broadcast**

*   **Action**: `android.intent.action.M3SCANNER_BUTTON_DOWN`
*   **Target package**: 지정하지 않음 (implicit broadcast)
*   **Extra**: 없음

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 스캔 중지

스캔 프로세스를 중지합니다.

*   **필요 ScanEmul 버전**: `2.13.0` 이상

```kotlin
M3Mobile.instance.stopScan()
```

**직접 Broadcast**

*   **Action**: `android.intent.action.M3SCANNER_BUTTON_UP`
*   **Target package**: 지정하지 않음 (implicit broadcast)
*   **Extra**: 없음

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 스캐너 상태 조회

스캐너의 현재 상태를 조회합니다.

*   **필요 ScanEmul 버전**: `2.13.0` 이상
*   **반환값**: 상태를 나타내는 정수값입니다.
    *   `1`: 열기 실패
    *   `2`: 닫기 실패
    *   `4`: 열기 성공
    *   `8`: 닫기 성공

```kotlin
// 코루틴
M3Mobile.instance.getScannerStatus(): Int

// 콜백
M3Mobile.instance.getScannerStatus(callback: RequestCallback<Int>): Job
```

**직접 Broadcast**

*   **Action**: `com.android.server.scannerservice.m3onoff.ison`
*   **Target package**: 지정하지 않음 (implicit broadcast)
*   **Extra**: 없음

*   **Response action**: `scanemul.action.status`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `scanemul.extra.status` | `Int` | `1`, `2`, `4`, `8` |

> 일반 MDM 웹 콘솔은 응답 Broadcast를 수신하지 못할 수 있습니다. 결과가 필요하면 응답 action을 수신하는 Android 애플리케이션을 사용하세요.


#### 스캐너 타입 조회

스캐너 하드웨어 타입을 조회합니다.

*   **필요 ScanEmul 버전**: `2.13.0` 이상
*   **반환값**: 스캐너 타입 문자열입니다.

```kotlin
// 코루틴
M3Mobile.instance.getScannerType(): String

// 콜백
M3Mobile.instance.getScannerType(callback: RequestCallback<String>): Job
```

**직접 Broadcast**

*   **Action**: `com.android.server.scannerservice.m3onoff.ison`
*   **Target package**: 지정하지 않음 (implicit broadcast)
*   **Extra**: 없음

*   **Response action**: `scanemul.action.status`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `m3scanner_module_type` | `String` | 스캐너 모듈 타입 |

> 일반 MDM 웹 콘솔은 응답 Broadcast를 수신하지 못할 수 있습니다. 결과가 필요하면 응답 action을 수신하는 Android 애플리케이션을 사용하세요.


#### 스캔 결과 리스너 (Scan Result Listener)

스캔 결과를 수신하기 위한 리스너를 등록하거나 해제합니다.

*   **필요 ScanEmul 버전**: `4.11.0` 이상
*   **매개변수**:
    *   `listener` (OnScanResultListener): 스캔 결과를 수신할 리스너 객체입니다.

```kotlin
// 등록
M3Mobile.instance.registerOnScanResultListener(listener: OnScanResultListener)

// 해제
M3Mobile.instance.unregisterOnScanResultListener(listener: OnScanResultListener)
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. ScanEmul message connection으로 결과를 수신하므로 SDK 또는 수신 애플리케이션이 필요합니다.


#### GS1 파싱 결과 리스너 (GS1 Parsed Listener)

스캔된 바코드를 GS1 형식으로 파싱한 결과를 수신하기 위한 리스너를 등록하거나 해제합니다.

*   **필요 ScanEmul 버전**: `4.11.0` 이상
*   **매개변수**:
    *   `listener` (OnGS1ParsedListener): GS1 파싱 결과를 수신할 리스너 객체입니다.

```kotlin
// 등록
M3Mobile.instance.registerOnGS1ParsedListener(listener: OnGS1ParsedListener)

// 해제
M3Mobile.instance.unregisterOnGS1ParsedListener(listener: OnGS1ParsedListener)
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. ScanEmul message connection으로 결과를 수신하므로 SDK 또는 수신 애플리케이션이 필요합니다.


#### 디지털 링크 파싱 결과 리스너 (Digital Link Parsed Listener)

스캔된 바코드에서 디지털 링크를 파싱한 결과를 수신하기 위한 리스너를 등록하거나 해제합니다.

*   **필요 ScanEmul 버전**: `4.11.0` 이상
*   **매개변수**:
    *   `listener` (OnDigitalLinkParsedListener): 디지털 링크 파싱 결과를 수신할 리스너 객체입니다.

```kotlin
// 등록
M3Mobile.instance.registerOnDigitalLinkParsedListener(listener: OnDigitalLinkParsedListener)

// 해제
M3Mobile.instance.unregisterOnDigitalLinkParsedListener(listener: OnDigitalLinkParsedListener)
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. ScanEmul message connection으로 결과를 수신하므로 SDK 또는 수신 애플리케이션이 필요합니다.


#### 스캐너 설정 (Scanner Settings)

다양한 스캐너 옵션을 구성합니다. 이 설정은 현재 활성화된 프로필에 적용됩니다.

*   **필요 ScanEmul 버전**: `2.11.0` 이상

##### 피드백 (Feedback)

```kotlin
// 사운드
M3Mobile.instance.setScanSound(ScanSound.BEEP) 
// Enum: NONE, BEEP, DING_DONG

// 진동
M3Mobile.instance.enableScanVibration()
M3Mobile.instance.disableScanVibration()

// LED
M3Mobile.instance.enableScanLed()
M3Mobile.instance.disableScanLed()
M3Mobile.instance.setScanLedTime(timeMillis: Int) // 범위: 1 ~ 1000
```

**직접 Broadcast**

*   **Action**: `com.android.server.scannerservice.settingchange`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| 작업 | `setting` | Extra | 타입 | 값 |
|---|---|---|---|---|
| 사운드 | `sound` | `sound_mode` | `Int` | 없음 `0`, BEEP `1`, DING_DONG `2` |
| 진동 | `vibration` | `vibration_value` | `Int` | 비활성화 `0`, 활성화 `1` |
| LED | `led` | `led_value` | `Int` | 비활성화 `0`, 활성화 `1` |
| LED 시간 | `led_time` | `led_time_value` | `Int` | `1..1000` ms |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


##### 스캔 모드 (Scanning Mode)

```kotlin
M3Mobile.instance.setScannerReadMode(ReadMode.MULTIPLE)
// Enum: AIMING_AND_RELEASE, ASYNC, CONTINUE, MULTIPLE, PRESENTATION, SYNC

// 조회
M3Mobile.instance.getScannerReadMode()
```

**직접 Broadcast**

*   **Action**: `com.android.server.scannerservice.settingchange`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| 작업 | `setting` | Extra | 타입 | 값 |
|---|---|---|---|---|
| 읽기 모드 SET | `read_mode` | `read_mode_value` | `Int` | ASYNC `0`, SYNC `1`, CONTINUE `2`, MULTIPLE `3`, PRESENTATION `4`, AIMING_AND_RELEASE `5` |

*   **Response action**: `com.android.server.scannerservice.setting`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `m3scanner_read_mode` | `Int` | 현재 읽기 모드 `0..5` |

> SET 요청은 단방향입니다. GET 결과가 필요하면 응답 action을 수신하는 Android 애플리케이션이 필요하며 일반 MDM 웹 콘솔에서는 결과를 받을 수 없을 수 있습니다.

GET은 Action `com.android.server.scannerservice.getsetting`를 extra 없이 전송합니다.


##### 출력 구성 (Output Configuration)

```kotlin
// 출력 모드
M3Mobile.instance.setScanResultOutputMode(OutputMode.COPY_AND_PASTE)
// Enum: COMMIT_TEXT, COPY_AND_PASTE, COPY_TO_CLIPBOARD, KEY_EMULATION

// 포맷팅
M3Mobile.instance.setScanResultPrefix("Prefix")
M3Mobile.instance.setScanResultPostfix("Postfix")
M3Mobile.instance.setScanResultEndCharacter(EndCharacter.ENTER)
// Enum: ENTER, KEYBOARD_ENTER, KEYBOARD_SPACE, KEYBOARD_TAB, NONE, SPACE, TAB

// 조회
M3Mobile.instance.getScanResultOutputMode()
M3Mobile.instance.getScanResultPrefix()
M3Mobile.instance.getScanResultPostfix()
M3Mobile.instance.getScanResultEndCharacter()
```

**직접 Broadcast**

*   **Action**: `com.android.server.scannerservice.settingchange`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| 작업 | `setting` | Extra | 타입 | 값 |
|---|---|---|---|---|
| 출력 모드 SET | `output_mode` | `output_mode_value` | `Int` | COPY_AND_PASTE `0`, KEY_EMULATION `1`, COPY_TO_CLIPBOARD `2`, COMMIT_TEXT `3` |
| Prefix SET | `prefix` | `prefix_value` | `String` | Prefix 문자열 |
| Postfix SET | `postfix` | `postfix_value` | `String` | Postfix 문자열 |
| 종료 문자 SET | `end_char` | `end_char_value` | `Int` | ENTER `0`, SPACE `1`, TAB `2`, KEYBOARD_ENTER `3`, KEYBOARD_SPACE `4`, KEYBOARD_TAB `5`, NONE `6` |

*   **Response action**: `com.android.server.scannerservice.setting`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `m3scanner_output_mode` | `Int` | 현재 출력 모드 |
| `m3scanner_prefix` | `String` | 현재 prefix |
| `m3scanner_postfix` | `String` | 현재 postfix |
| `m3scanner_endchar` | `Int` | 현재 종료 문자 |

> SET 요청은 단방향입니다. GET 결과가 필요하면 응답 action을 수신하는 Android 애플리케이션이 필요하며 일반 MDM 웹 콘솔에서는 결과를 받을 수 없을 수 있습니다.

GET은 Action `com.android.server.scannerservice.getsetting`를 extra 없이 전송합니다.


##### 프로필 상태 (Profile Status)

현재 스캐너 프로필이 활성화되어 있는지 확인합니다.

```kotlin
M3Mobile.instance.isScannerProfileEnabled()
```

**직접 Broadcast**

*   **Action**: `com.android.server.scannerservice.getsetting`
*   **Target package**: 지정하지 않음 (implicit broadcast)
*   **Extra**: 없음

*   **Response action**: `com.android.server.scannerservice.setting`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `is_enable` | `Boolean` | 현재 프로필 활성화 여부 |

> 일반 MDM 웹 콘솔은 응답 Broadcast를 수신하지 못할 수 있습니다. 결과가 필요하면 응답 action을 수신하는 Android 애플리케이션을 사용하세요.


#### 플로팅 스캐너 버튼 UI

지원 기기에서 ScanEmul 기본 스캐너 버튼의 이미지, 투명도, 크기를 설정하거나 현재 값을 조회합니다.

*   **지원 모델**: 전체 모델 (`WD10` 제외, `WD10`에는 ScanEmul 앱이 없음)
*   **필요 ScanEmul 버전**: `4.15.1` 이상 (`SM24`는 `4.14.10` 이상)

```kotlin
val options = ScannerButtonUiOptions(
    imagePath = "/sdcard/Download/ScanEmul_Floating_Button_Images/target.png",
    opacityPercent = 80,
    size = ScannerButtonUiSize.LARGE,
)

val result = M3Mobile.instance.setAndVerifyScannerButtonUi(options)
```

**직접 Broadcast**

*   **Action**: `com.android.server.scannerservice.settingchange`
*   **Target package**: `net.m3mobile.app.scanemul`

| 작업 | `setting` | Extra | 타입 | 값 |
|---|---|---|---|---|
| SET | `scanner_button_ui` | `request_id` | `String` | 응답 연결용 ID, 생략 가능 |
| SET | `scanner_button_ui` | `scanner_button_image_path` | `String` | 이미지 절대 경로, 빈 문자열은 기본 이미지, 선택 |
| SET | `scanner_button_ui` | `scanner_button_opacity_percent` | `Int` | `20..100`, 선택 |
| SET | `scanner_button_ui` | `scanner_button_size` | `String` | `extra_small`, `small`, `medium`, `large`, `extra_large`, 선택 |

*   **Response action**: `com.android.server.scannerservice.setting`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `setting` | `String` | `scanner_button_ui` |
| `request_id` | `String` | 요청 ID |
| `success` | `Boolean` | 저장 성공 여부 |
| `status` | `String` | ScanEmul 처리 상태 |
| `runtime_applied` | `Boolean` | 실행 중 UI 반영 여부 |
| `scanner_button_image_path` | `String` | 저장된 이미지 경로 |
| `scanner_button_opacity_percent` | `Int` | 저장된 투명도 |
| `scanner_button_size` | `String` | 저장된 크기 |

ScanEmul은 SET과 GET 모두 response Broadcast를 보냅니다. 일반 MDM 웹 콘솔은 이 응답을 받을 수 없을 수 있으므로, 전송만 성공한 경우 실제 UI 반영을 별도로 확인해야 합니다.

GET 요청은 다음 계약을 사용합니다.

*   **Action**: `com.android.server.scannerservice.getsetting`
*   **Target package**: `net.m3mobile.app.scanemul`

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `scanner_button_ui` |
| `request_id` | `String` | X | 응답 연결용 ID |

`request_id`를 생략하면 응답에도 연결 ID가 포함되지 않습니다. SET에서는 이미지 경로, 투명도, 크기 중 하나 이상을 보내야 하며, 생략한 UI 항목은 현재 값을 유지합니다. ScanEmul `4.15.1` 이상이 필요하며, `SM24`는 `4.14.10` 이상이 필요합니다.

응답을 직접 처리하는 애플리케이션은 다음 response action을 동적 receiver로 등록하고 `request_id`를 비교해야 합니다.

```kotlin
val requestId = UUID.randomUUID().toString()
val receiver = object : BroadcastReceiver() {
    override fun onReceive(receiverContext: Context, intent: Intent) {
        if (intent.getStringExtra("setting") != "scanner_button_ui") return
        if (intent.getStringExtra("request_id") != requestId) return

        val success = intent.getBooleanExtra("success", false)
        val status = intent.getStringExtra("status")
        val runtimeApplied = intent.getBooleanExtra("runtime_applied", false)
        receiverContext.unregisterReceiver(this)
    }
}

val filter = IntentFilter("com.android.server.scannerservice.setting")
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
} else {
    context.registerReceiver(receiver, filter)
}

val request = Intent("com.android.server.scannerservice.getsetting")
    .setPackage("net.m3mobile.app.scanemul")
    .putExtra("setting", "scanner_button_ui")
    .putExtra("request_id", requestId)
context.sendOrderedBroadcast(request, null)

// 응답이 오지 않는 경우 timeout 처리에서 receiver를 해제해야 합니다.
```


---

### StartUp Setting API

StartUp SDK 자체의 설정을 관리합니다.

#### StartUp 설정 초기화

StartUp 설정을 기본값으로 초기화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```kotlin
M3Mobile.instance.resetStartUpSetting()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `option` |
| `option_reset` | `Boolean` | O | `true` |

설정 요청 직후 `com.android.server.startupservice.config.fin` Broadcast를 추가로 보내야 합니다.

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


---

### KeyTool API

KeyTool 앱을 통해 물리 키 설정을 제어합니다. StartUp 및 ScanEmul과 동일하게
`M3Mobile.instance`에서 KeyTool 메서드를 직접 호출합니다.

> **단방향 요청:** KeyTool broadcast는 처리 결과를 응답하지 않습니다. 메서드가 예외 없이
> 반환되었다는 것은 Android가 요청을 받았다는 의미이며, 실제 설정 변경을 보장하지 않습니다.
> 호출 후 물리 키 동작을 직접 확인해야 합니다. 필요한 패키지가 없으면 SDK가 메서드명과
> 패키지 정보를 포함한 `KeyToolAppUnavailableException`을 발생시킵니다. `com.m3.keytoolsl20`
> 기반 API는 Strict Mode와 관계없이 최소 버전도 검사하며, 버전 미달이면 메서드명, 모델,
> 패키지, 현재 버전, 필요 버전을 포함한 `UnsatisfiedVersionException`을 발생시킵니다.

| SDK 기능 | 모델 | 패키지 | 최소 버전 |
|---|---|---|---|
| Function 키 모드 | `SL20K` | `com.m3.keytoolsl20` | `1.2.6` |
| 키 기능 설정 | `SL20`, `SL20K`, `SL20P`, `SL25`, `WD10` | `com.m3.keytoolsl20` | `1.2.6` |
| 키 기능 설정 | `SM24` | `com.m3.keytoolsl20` | `1.3.8` |
| 키 기능 설정 | `SM25` | `com.m3.keytoolsl20` | `1.3.16` |
| 키 기능 설정 + Wake-Up | `SM24` | `com.m3.keytoolsl20` | `1.3.8` |
| Home/Recent 제어 | `SM24`, `SM25` | `com.m3.keytoolsl20` | `1.4.1` |
| 스캔 키 Wake-Up | `SL20P` | `net.m3.keytool` | 버전 미확정, 패키지만 검사 |
| 스캔 키 Wake-Up | `SM24` | `com.m3.keytoolsl20` | `1.3.8` |

SM24의 스캔 키 Wake-Up 최소 버전은 `1.3.8`이며, 서비스 연결 안정화가 포함된 `1.3.9`
이상을 현장 배포 버전으로 권장합니다. `1.4.1_alpha`, `1.3.4F`, `1.4.0AD` 같은 제품별
suffix는 각 숫자 구간 앞쪽의 숫자만 비교합니다.

#### Function 키 모드 제어

Function 키 모드를 활성화, 비활성화 또는 잠금 상태로 변경합니다.

*   **지원 모델**: `SL20K`
*   **필요 패키지**: `com.m3.keytoolsl20` 버전 `1.2.6` 이상

```kotlin
M3Mobile.instance.enableFN()
M3Mobile.instance.disableFN()
M3Mobile.instance.lockFN()
```

**직접 Broadcast**

*   **Action**: `com.m3.keytoolsl20.ACTION_CONTROL_FN_STATE`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `fn_state` | `Int` | O | 비활성화 `0`, 활성화 `1`, 잠금 `2` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

SL20K에서 KeyTool SL20 `1.2.6` 이상이 필요합니다.


#### 키 기능 설정

물리 키 이름에 KeyTool 기능 이름을 할당합니다.

*   **지원 모델**: `SL20`, `SL20K`, `SL20P`, `SL25`, `WD10`, `SM24`, `SM25`
*   **필요 패키지**: `com.m3.keytoolsl20` (`SL20`/`SL20K`/`SL20P`/`SL25`/`WD10`은
    `1.2.6`, `SM24`는 `1.3.8`, `SM25`는 `1.3.16` 이상)
*   **매개변수**:
    *   `key`: KeyTool 키 이름입니다.
    *   `function`: KeyTool 기능 이름입니다.

```kotlin
try {
    M3Mobile.instance.setKeyFunction(
        key = "Left Scan",
        function = "Volume Up"
    )
    // REQUEST_SENT_UNVERIFIED: 장치의 물리 키를 직접 확인합니다.
} catch (error: Exception) {
    Log.e("M3SDK", "KeyTool 요청 실패", error)
}
```

현재 KeyTool 표기인 `Volume Up`, `Volume Down`을 사용합니다. KeyTool 1.4.1은 이전 버전이
저장한 `Volume up`, `Volume down` 값도 읽을 때 현재 표기로 정규화합니다.

SM24에서는 3인자 오버로드로 키 매핑과 Wake-Up 상태를 하나의 `ACTION_SET_KEY` 요청에
포함할 수 있습니다.

```kotlin
M3Mobile.instance.setKeyFunction(
    key = "Left Scan",
    function = "Scan",
    wakeUpEnabled = true,
)
```

이 요청은 `key_title`, `key_function`, `key_wakeup`을 함께 전송합니다. KeyTool은 매핑을
적용한 뒤 Wake-Up을 순서대로 적용하며, 두 작업을 하나의 트랜잭션으로 롤백하지 않습니다.
따라서 정상 반환은 두 설정의 실제 적용 성공을 보장하지 않습니다.

**직접 Broadcast**

*   **Action**: `com.m3.keytoolsl20.ACTION_SET_KEY`
*   **Target package**: `com.m3.keytoolsl20`
*   **전송 방식**: ordered broadcast

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `key_title` | `String` | O | KeyTool 키 제목 |
| `key_function` | `String` | O | KeyTool 기능 제목 |
| `key_wakeup` | `Boolean` | X | SM24에서 같은 요청으로 Wake-Up도 변경할 때 사용 |

`key_wakeup`을 생략하면 기존 Wake-Up 설정을 변경하지 않습니다.

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

최소 KeyTool SL20 버전은 SL20/SL20K/SL20P/SL25/WD10 `1.2.6`, SM24 `1.3.8`, SM25 `1.3.16`입니다. `key_wakeup`을 함께 보내는 overload는 SM24와 `1.3.8` 이상이 필요합니다.


#### Home 및 Recent 버튼 제어

SystemUI의 Home 및 Recent 내비게이션 버튼을 활성화하거나 비활성화합니다. 이 메서드는
KeyTool 1.4.1의 `ACTION_SET_KEY` 요청을 사용하며, 활성화는 `Default`, 비활성화는 `Disable`로 전달합니다.

*   **지원 모델**: `SM24`, `SM25`
*   **필요 패키지**: `com.m3.keytoolsl20` 버전 `1.4.1` 이상

```kotlin
M3Mobile.instance.enableHomeButton()
M3Mobile.instance.disableHomeButton()
M3Mobile.instance.enableRecentButton()
M3Mobile.instance.disableRecentButton()
```

단방향 요청이므로 각 호출 후 실제 내비게이션 버튼 동작을 확인해야 합니다.

**직접 Broadcast**

*   **Action**: `com.m3.keytoolsl20.ACTION_SET_KEY`
*   **Target package**: `com.m3.keytoolsl20`

| 작업 | `key_title` (`String`, 필수) | `key_function` (`String`, 필수) |
|---|---|---|
| Home 활성화 | `Home` | `Default` |
| Home 비활성화 | `Home` | `Disable` |
| Recent 활성화 | `Recent` | `Default` |
| Recent 비활성화 | `Recent` | `Disable` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

ordered broadcast로 전송하며 KeyTool SL20 `1.4.1` 이상이 필요합니다.


#### 스캔 키 Wake-Up 제어

`SL20P` 또는 `SM24`의 왼쪽/오른쪽 스캔 키로 장치를 깨울 수 있는지 제어합니다.

*   **지원 모델**: `SL20P`, `SM24`
*   **SL20P 프로토콜**: `net.m3.keytool`의 기존 `WAKEUP_CONTROL_LEFT` 또는
    `WAKEUP_CONTROL_RIGHT` explicit broadcast
*   **SM24 프로토콜**: `com.m3.keytoolsl20` 버전 `1.3.8` 이상의 `ACTION_SET_KEY`
    explicit broadcast (`1.3.9` 이상 권장)

프로토콜은 설치된 패키지의 우선순위가 아니라 현재 모델로 결정됩니다. SM24는
`net.m3.keytool`이 설치되어 있어도 deprecated `WAKEUP_CONTROL_*`를 사용하지 않으며,
SL20P는 `com.m3.keytoolsl20`이 설치되어 있어도 기존 Legacy 동작을 유지합니다.

```kotlin
M3Mobile.instance.enableLeftScanWakeUp()
M3Mobile.instance.disableLeftScanWakeUp()
M3Mobile.instance.enableRightScanWakeUp()
M3Mobile.instance.disableRightScanWakeUp()
```

배포 패키지 샘플은 설치된 KeyTool 패키지 버전을 표시하며, 단방향 호출을 성공이 아닌
`REQUEST_SENT_UNVERIFIED` 상태로 표시합니다.

**SL20P 왼쪽**

**직접 Broadcast**

*   **Action**: `net.m3.keytool.WAKEUP_CONTROL_LEFT`
*   **Target package**: `net.m3.keytool`

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `wakeup_enable` | `Boolean` | O | `true` 또는 `false` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

SL20P 왼쪽 스캔 키에만 사용합니다.

**SL20P 오른쪽**

**직접 Broadcast**

*   **Action**: `net.m3.keytool.WAKEUP_CONTROL_RIGHT`
*   **Target package**: `net.m3.keytool`

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `wakeup_enable` | `Boolean` | O | `true` 또는 `false` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

SL20P 오른쪽 스캔 키에만 사용합니다.

**SM24**

**직접 Broadcast**

*   **Action**: `com.m3.keytoolsl20.ACTION_SET_KEY`
*   **Target package**: `com.m3.keytoolsl20`
*   **전송 방식**: ordered broadcast

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `key_title` | `String` | O | `Left Scan` 또는 `Right Scan` |
| `key_wakeup` | `Boolean` | O | `true` 또는 `false` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

SM24에서만 사용합니다. KeyTool SL20 `1.3.8` 이상이 필요하고 `1.3.9` 이상을 권장합니다. Wake-Up만 변경할 때 `key_function`을 보내지 않습니다.


---

### AppCenter Kiosk API

AppCenter 키오스크 관리자 기능을 단방향 explicit broadcast로 제어합니다.
메서드는 `M3Mobile.instance`에서 직접 호출할 수 있습니다.

> **단방향 요청:** AppCenter broadcast는 처리 결과를 응답하지 않습니다. 정상 반환은
> Android가 요청을 받았다는 의미일 뿐 AppCenter 적용 성공을 보장하지 않습니다.
> AppCenter `2.2.0` 이상이 설치되어 있고 broadcast를 수신 가능한 상태여야 합니다.
> SDK는 Strict Mode 설정과 관계없이 AppCenter 설치 여부와 버전을 검증합니다.

#### 키오스크 관리자 비밀번호 변경

AppCenter 키오스크 관리자 비밀번호 변경을 요청합니다.

*   **필요 AppCenter 버전**: `2.2.0` 이상
*   **매개변수**:
    *   `currentPassword`: 현재 관리자 비밀번호입니다. 빈 문자열은 거부됩니다.
    *   `newPassword`: 새 관리자 비밀번호입니다. 길이는 4~20자만 허용됩니다.

SDK는 두 비밀번호 값을 trim하지 않습니다. 현재 비밀번호가 잘못되면 AppCenter가 요청을
무시할 수 있으며, SDK는 실제 적용 결과를 확인할 수 없습니다.

```kotlin
M3Mobile.instance.changeKioskAdminPassword(currentPassword, newPassword)
```

**직접 Broadcast**

*   **Action**: `com.m3.appcenter.ACTION_CHANGE_PASSWORD`
*   **Target package**: `com.m3.appcenter`

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `com.m3.appcenter.EXTRA_CURRENT_PASSWORD` | `String` | O | 현재 관리자 비밀번호 |
| `com.m3.appcenter.EXTRA_NEW_PASSWORD` | `String` | O | 새 비밀번호, 4~20자 |
| `com.m3.appcenter.EXTRA_ENCRYPTION_ENABLED` | `Boolean` | O | `true` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

AppCenter `2.2.0` 이상이 필요합니다.


#### 화면 OFF 시 관리자 모드 유지

화면이 꺼질 때 AppCenter 관리자 모드를 유지할지 설정합니다.

*   **필요 AppCenter 버전**: `2.2.0` 이상
*   **매개변수**:
    *   `enabled`: `true`이면 화면 OFF 후 관리자 모드를 유지합니다. `false`이면 기존처럼
        사용자 모드로 돌아가며 관리자 로그인이 다시 필요할 수 있습니다.

재부팅 후 관리자 모드는 유지되지 않습니다.

```kotlin
M3Mobile.instance.setKeepAdminModeOnSleep(true)
M3Mobile.instance.setKeepAdminModeOnSleep(false)
```

**직접 Broadcast**

*   **Action**: `com.m3.appcenter.ACTION_SET_KEEP_ADMIN_MODE_ON_SLEEP`
*   **Target package**: `com.m3.appcenter`

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `com.m3.appcenter.EXTRA_KEEP_ADMIN_MODE_ON_SLEEP` | `Int` | O | 유지 `1`, 해제 `0` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

AppCenter `2.2.0` 이상이 필요합니다.


---

### Time API

시스템 시간, 시간대 및 NTP 서버 설정을 구성합니다.

#### 날짜 및 시간 설정

장치의 날짜와 시간을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `dateTime` (java.time.LocalDateTime): 설정할 날짜와 시간 객체입니다.

```kotlin
M3Mobile.instance.setDateTime(dateTime: LocalDateTime)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `datetime` |
| `date` | `String` | O | `yyyy-MM-dd` |
| `time` | `String` | O | `HH:mm:ss` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### NTP 서버 설정

자동 시간 동기화를 위한 NTP 서버를 설정합니다. 이 설정은 다음 재부팅 후 적용됩니다.

*   **필요 StartUp 버전**: `6.4.9` 이상
*   **매개변수**:
    *   `host` (String): NTP 서버의 호스트 이름 또는 IP 주소입니다.

```kotlin
M3Mobile.instance.setNtpServer(host: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `ntp` |
| `ntp_server` | `String` | O | NTP host |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 시간대 설정

시스템의 기본 시간대를 설정합니다.

*   **필요 StartUp 버전**: `6.5.9` 이상
*   **매개변수**:
    *   `timezone` (String): 시간대 식별자입니다 (예: "Asia/Seoul", "America/New_York").

```kotlin
M3Mobile.instance.setTimeZone(timezone: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `timezone` |
| `timezone` | `String` | O | IANA timezone ID |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### NTP 서버 조회

현재 설정된 NTP 서버 주소를 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: NTP 서버 주소 문자열입니다.

```kotlin
M3Mobile.instance.getNtpServer(): String
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. 이 API는 Android 시스템 설정 또는 sticky system Broadcast를 직접 읽으며 명령 Broadcast를 보내지 않습니다.


#### NTP 동기화 간격 조회

현재 설정된 NTP 동기화 간격을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: 밀리초(ms) 단위의 NTP 동기화 간격(Int)입니다.

```kotlin
M3Mobile.instance.getNtpInterval(): Int
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. 이 API는 Android 시스템 설정 또는 sticky system Broadcast를 직접 읽으며 명령 Broadcast를 보내지 않습니다.


#### 시간대 조회

시스템의 현재 기본 시간대를 조회합니다.

*   **반환값**: 시간대 식별자 문자열입니다.

```kotlin
M3Mobile.instance.getTimeZone(): String
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. 이 API는 Android 시스템 설정 또는 sticky system Broadcast를 직접 읽으며 명령 Broadcast를 보내지 않습니다.


---

### Usb API

장치의 USB 연결 모드를 구성합니다.

#### USB 모드를 MTP로 설정

USB 연결 모드를 MTP(Media Transfer Protocol)로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```kotlin
M3Mobile.instance.setUsbModeMtp()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `mtp` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### USB 모드를 RNDIS로 설정

USB 연결 모드를 RNDIS(USB 테더링)로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```kotlin
M3Mobile.instance.setUsbModeRndis()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `rndis` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### USB 모드를 MIDI로 설정

USB 연결 모드를 MIDI로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```kotlin
M3Mobile.instance.setUsbModeMidi()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `midi` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### USB 모드를 PTP로 설정

USB 연결 모드를 PTP(Picture Transfer Protocol)로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```kotlin
M3Mobile.instance.setUsbModePtp()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `ptp` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### USB 데이터 비활성화 (충전 전용)

USB 데이터 연결을 비활성화합니다 (충전 전용).

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```kotlin
M3Mobile.instance.setUsbModeNone()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `none` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 현재 USB 모드 조회

현재 USB 연결 모드를 조회합니다.

*   **반환값**: 현재 활성화된 USB 모드를 나타내는 문자열 목록입니다. 활성화된 모드가 없으면 빈 목록을 반환합니다.

```kotlin
M3Mobile.instance.getCurrentUsbModes(): List<String>
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. 이 API는 Android 시스템 설정 또는 sticky system Broadcast를 직접 읽으며 명령 Broadcast를 보내지 않습니다.


---

### Wifi API

Wi-Fi 설정 및 구성을 제어합니다.

#### Wi-Fi MAC 주소 조회

장치의 Wi-Fi MAC 주소를 조회합니다.

*   **필요 StartUp 버전**: `6.4.11` 이상
*   **반환값**: Wi-Fi MAC 주소 문자열입니다.

```kotlin
// 코루틴 (for kotlin)
M3Mobile.instance.getWifiMac(): String

// 콜백 (for java)
M3Mobile.instance.getWifiMac(callback: RequestCallback<String>): Job
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `get_wifi_mac` |

*   **Response action**: `com.android.server.startupservice.system.response`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `get_wifi_mac` | `String` | Wi-Fi MAC 주소 |

> 일반 MDM 웹 콘솔은 응답 Broadcast를 수신하지 못할 수 있습니다. 결과가 필요하면 응답 action을 수신하는 Android 애플리케이션을 사용하세요.


#### Factory Wi-Fi MAC 주소 조회

장치의 factory Wi-Fi MAC 주소를 조회합니다. 이 API는 `getWifiMac()`과 다릅니다. `getWifiMac()`은 Android 상태와 Wi-Fi 연결 이력에 따라 현재 MAC 또는 randomized MAC을 반환할 수 있습니다.

Wi-Fi AP에 연결되어 있을 필요는 없지만, Wi-Fi는 켜져 있어야 합니다. Wi-Fi가 꺼져 있으면 StartUp에서 factory Wi-Fi MAC을 반환하지 못할 수 있습니다.

*   **필요 StartUp 버전**: `6.7.3` 이상
*   **반환값**: `FactoryWifiMacResult`

```kotlin
// 코루틴 (for kotlin)
val result = M3Mobile.instance.getFactoryWifiMac()
if (result.success) {
    val mac = result.macAddress
} else {
    val error = result.errorMessage
}
```

```java
// 콜백 (for java)
M3Mobile.INSTANCE.getInstance().getFactoryWifiMac((result, error) -> {
    if (error != null) {
        return;
    }

    if (result.isSuccess()) {
        String mac = result.getMacAddress();
    } else {
        String message = result.getErrorMessage();
    }
});
```

StartUp 직접 broadcast 응답:

```java
BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String mac = intent.getStringExtra("get_factory_wifi_mac");
        boolean success = intent.getBooleanExtra("get_factory_wifi_mac_success", false);
        String error = intent.getStringExtra("get_factory_wifi_mac_error_message");
    }
};
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `get_factory_wifi_mac` |

*   **Response action**: `com.android.server.startupservice.system.response`

| 응답 Extra | 타입 | 값 |
|---|---|---|
| `get_factory_wifi_mac` | `String` | Factory Wi-Fi MAC 주소 |
| `get_factory_wifi_mac_success` | `Boolean` | 조회 성공 여부 |
| `get_factory_wifi_mac_error_message` | `String` | 실패 상세 |

> 일반 MDM 웹 콘솔은 응답 Broadcast를 수신하지 못할 수 있습니다. 결과가 필요하면 응답 action을 수신하는 Android 애플리케이션을 사용하세요.


#### Wi-Fi 활성화 상태 설정

장치의 Wi-Fi를 활성화하거나 비활성화합니다.

이 API는 StartUp에서 처리합니다. Android 10 이상에서는 일반 Android 앱이 Wi-Fi를 직접 제어할 수 없으므로, StartUp이 system 또는 privileged app으로 배포되어 있어야 합니다.

*   **필요 StartUp 버전**: `6.8.5` 이상 (`SM24`는 `6.8.3` 이상)
*   **지원 모델**: `SM20`, `SL20`, `SL20P`, `SL20K`, `US20`, `US30`, `UL20` (`UL20F/W/WF` 포함), `UL30`, `SM24`, `SM25`, `PC10`, `WD10`
*   **매개변수**:
    *   `enabled` (Boolean): `true`이면 Wi-Fi 활성화, `false`이면 Wi-Fi 비활성화

```kotlin
M3Mobile.instance.setWifiEnabled(true)
M3Mobile.instance.setWifiEnabled(false)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `wifi_enabled` |
| `enabled` | `Boolean` | O | `true` 또는 `false` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 캡티브 포털 감지 (Captive Portal Detection)

장치가 캡티브 포털(공용 Wi-Fi 로그인 페이지)을 감지할지 여부를 제어합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SL20`

```kotlin
M3Mobile.instance.enableCaptivePortalDetection()
M3Mobile.instance.disableCaptivePortalDetection()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `captive_portal` |
| `value` | `Int` | O | 활성화 `1`, 비활성화 `0` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 주파수 대역 제어 (Frequency Band Control)

Wi-Fi 주파수 대역 사용을 제한합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SM15`, `SL10`, `SL10K`

```kotlin
M3Mobile.instance.allowAllWifiFrequencyBand()          // 모든 대역 허용
M3Mobile.instance.allowOnly2_4GHzWifiFrequencyBand()   // 2.4GHz 대역만 허용
M3Mobile.instance.allowOnly5GHzWifiFrequencyBand()     // 5GHz 대역만 허용
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `wifi_freq_band` |
| `value` | `Int` | O | 전체 `0`, 2.4 GHz `1`, 5 GHz `2` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### Wi-Fi 국가 코드 설정

Wi-Fi 국가 코드를 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SL10`, `SL10K`
*   **매개변수**:
    *   `countryCode` (String): ISO 3166-1 alpha-2 국가 코드 (예: "US", "KR").

```kotlin
M3Mobile.instance.setWifiCountry(countryCode: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `wifi_country_code` |
| `value` | `String` | O | 2자리 국가 코드 |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 개방형 네트워크 알림 (Open Network Notification)

사용 가능한 개방형 Wi-Fi 네트워크가 있을 때 알림을 받을지 여부를 제어합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```kotlin
M3Mobile.instance.enableOpenNetworkNotification()
M3Mobile.instance.disableOpenNetworkNotification()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `wifi_open_noti` |
| `value` | `Int` | O | 활성화 `1`, 비활성화 `0` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 로밍 구성 (Roaming Configuration)

Wi-Fi 로밍 파라미터를 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SL10`, `SL10K`

##### 로밍 트리거 설정

로밍 스캔을 시작할 신호 강도(RSSI) 임계값을 설정합니다.

*   **인덱스(Index)**:
    *   `0`: -80dBm
    *   `1`: -75dBm
    *   `2`: -70dBm
    *   `3`: -65dBm
    *   `4`: -60dBm

```kotlin
M3Mobile.instance.setRoamingTrigger(index: Int)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `wifi_roam_trigger` |
| `value` | `String` | O | SDK index의 문자열 |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


##### 로밍 델타 설정

새로운 AP로 로밍하기 위해 필요한 최소 신호 차이를 설정합니다.

*   **인덱스(Index)**:
    *   `0`: 30dB
    *   `1`: 25dB
    *   `2`: 20dB
    *   `3`: 15dB
    *   `4`: 10dB
    *   `5`: 5dB
    *   `6`: 0dB

```kotlin
M3Mobile.instance.setRoamingDelta(index: Int)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `wifi_roam_delta` |
| `value` | `String` | O | SDK index의 문자열 |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### Wi-Fi 절전 정책 (Wi-Fi Sleep Policy)

Wi-Fi가 언제 절전 모드로 들어갈지 제어합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```kotlin
M3Mobile.instance.setWifiSleepPolicyNever()         // 항상 켜짐 (절전 안 함)
M3Mobile.instance.setWifiSleepPolicyPluggedOnly()   // 충전 중일 때만 켜짐
M3Mobile.instance.setWifiSleepPolicyAlways()        // 화면이 꺼지면 절전 모드 허용
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `wifi_sleep` |
| `value` | `Int` | O | 안 함 `0`, 충전 중만 `1`, 항상 `2` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### Wi-Fi 안정성 (Wi-Fi Stability)

Wi-Fi 성능을 최적화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **참고**: Android 13 이상에서는 지원되지 않습니다.

```kotlin
M3Mobile.instance.setWifiStabilityNormal() // 균형 모드
M3Mobile.instance.setWifiStabilityHigh()   // 성능 중심 (배터리 소모 증가)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `wifi_stability` |
| `value` | `Int` | O | 일반 `1`, 높음 `2` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### Wi-Fi 채널 설정

사용할 Wi-Fi 채널을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SM15`, `SL10`, `SL10K`
*   **매개변수**:
    *   `channels` (vararg Int): 활성화할 채널 목록 (예: 1, 6, 11, 36).

```kotlin
M3Mobile.instance.setWifiChannel(vararg channels: Int)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `wifi_channel` |
| `value` | `String[]` | O | 채널 번호 문자열 배열 |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 네트워크 관리 (Network Management)

##### 액세스 포인트 설정

Wi-Fi 액세스 포인트(AP)를 구성합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `accessPoint` (AccessPoint): 설정할 Wi-Fi 액세스 포인트 객체입니다. `AccessPoint.builder()`를 사용하여 생성하세요.

```kotlin
M3Mobile.instance.setAccessPoint(accessPoint: AccessPoint)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `access_point` |
| `ssid` | `String` | O | SSID |
| `security` | `Int` | O | 없음 `0`, WEP `1`, WPA/WPA2 PSK `2`, 802.1x EAP `3` |
| `password` | `String` | X | Password |
| `static_enable` | `Boolean` | X | 정적 IP 사용 여부 |
| `ip_address` | `String` | X | IP address |
| `mask` | `String` | X | Subnet mask |
| `gateway` | `String` | X | Gateway |
| `dns` | `String` | X | DNS |
| `mac_random` | `Int` | X | 랜덤 MAC `0`(기본값), 기기 MAC `1` |
| `hidden_ssid` | `Boolean` | X | Hidden SSID |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.

직접 Broadcast의 `security`와 `mac_random`은 StartUp receiver의 wire type인 `Int`를 사용합니다.
선택값을 생략하면 `static_enable=false`, `mac_random=0`, `hidden_ssid=false`로 처리하며 나머지 선택 `String` extra는 `null`로 처리합니다.


##### 저장된 Wi-Fi 네트워크 초기화

저장된 모든 Wi-Fi 네트워크를 제거합니다.

*   **필요 StartUp 버전**: `6.4.11` 이상

```kotlin
M3Mobile.instance.clearSavedWifiNetworks()
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `remove_all_wifi` |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


##### Wi-Fi 네트워크 제거

특정 Wi-Fi 네트워크를 제거합니다.

*   **필요 StartUp 버전**: `6.4.11` 이상
*   **매개변수**:
    *   `ssid` (String): 제거할 네트워크의 SSID입니다.

```kotlin
M3Mobile.instance.removeWifiNetwork(ssid: String)
```

**직접 Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: 지정하지 않음 (implicit broadcast)

| Extra | 타입 | 필수 | 값 |
|---|---|---|---|
| `setting` | `String` | O | `remove_wifi_by_ssid` |
| `ssid` | `String` | O | 제거할 SSID |

> 단방향 요청입니다. Broadcast 전송은 실제 설정 적용 성공을 보장하지 않습니다. MDM에서 적용 상태를 별도로 확인하세요.


#### 기기별 Wi-Fi 설정 (Device Specific Wi-Fi Settings)

다음 함수들은 특정 기기 모델에서만 지원됩니다.

##### 로밍 임계값 조회

현재 설정된 Wi-Fi 로밍 임계값을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: 음수 `Int` 값의 로밍 임계값입니다.

```kotlin
M3Mobile.instance.getRoamingThreshold(): Int
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. 이 API는 Android 시스템 설정 또는 sticky system Broadcast를 직접 읽으며 명령 Broadcast를 보내지 않습니다.


##### 로밍 델타값 조회

현재 설정된 Wi-Fi 로밍 델타값을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: `Int` 형태의 로밍 델타값입니다.

```kotlin
M3Mobile.instance.getRoamingDelta(): Int
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. 이 API는 Android 시스템 설정 또는 sticky system Broadcast를 직접 읽으며 명령 Broadcast를 보내지 않습니다.


##### Wi-Fi 주파수 대역 조회

현재 선호하는 Wi-Fi 주파수 대역 설정을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: 주파수 대역을 나타내는 `Int` 값입니다:
    *   `0`: 자동 (Automatic)
    *   `1`: 5 GHz 전용
    *   `2`: 2.4 GHz 전용

```kotlin
M3Mobile.instance.getWifiFrequencyBand(): Int
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. 이 API는 Android 시스템 설정 또는 sticky system Broadcast를 직접 읽으며 명령 Broadcast를 보내지 않습니다.


##### Wi-Fi 국가 코드 조회

현재 설정된 Wi-Fi 국가 코드를 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: `String` 형태의 국가 코드입니다.

```kotlin
M3Mobile.instance.getWifiCountryCode(): String
```

**직접 Broadcast**

직접 명령 Broadcast를 지원하지 않습니다. 이 API는 Android 시스템 설정 또는 sticky system Broadcast를 직접 읽으며 명령 Broadcast를 보내지 않습니다.
