# M3 SDK 매뉴얼

PDF 다운로드 : [M3SDK_Manual_kr.pdf](https://github.com/user-attachments/files/24117902/M3SDK_Manual_kr.pdf)

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
  - [Device API](#device-api)
    - [미디어 볼륨 설정](#미디어-볼륨-설정)
    - [벨소리 볼륨 설정](#벨소리-볼륨-설정)
    - [알림 볼륨 설정](#알림-볼륨-설정)
    - [알람 볼륨 설정](#알람-볼륨-설정)
    - [진동 모드 활성화](#진동-모드-활성화)
    - [진동 모드 비활성화](#진동-모드-비활성화)
    - [디스플레이 설정](#디스플레이-설정)
    - [시리얼 번호 조회](#시리얼-번호-조회)
  - [Network API](#network-api)
    - [APN 설정](#apn-설정)
  - [Permission API](#permission-api)
    - [권한 부여](#권한-부여)
    - [권한 취소](#권한-취소)
  - [Quick Tile API](#quick-tile-api)
    - [빠른 설정 타일 지정](#빠른-설정-타일-지정)
    - [빠른 설정 타일 초기화](#빠른-설정-타일-초기화)
  - [StartUp Setting API](#startup-setting-api)
    - [StartUp 설정 초기화](#startup-설정-초기화)
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
    implementation("com.github.m3mobile:M3SDK:2.0.0")
}
```

```groovy
// Groovy
dependencies {
    implementation "com.github.m3mobile:M3SDK:2.0.0"
}
```

최신 버전은 릴리즈 문서를 참고해주세요.
- https://github.com/m3mobile/M3SDK/releases

## 기본 사용법 (Basic Usage)

SDK는 애플리케이션 시작 시 자동으로 초기화됩니다. 수동으로 초기화할 필요가 없습니다.

### API 접근

모든 기능은 싱글톤 인스턴스를 통해 접근할 수 있습니다.

```kotlin
import net.m3mobile.feature.startup.M3Mobile

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

    발생할 수 있는 예외는 다음과 같습니다.
    *   `UnsupportedDeviceModelException`: 지원되지 않는 장치 모델에서 API가 호출될 경우 발생합니다.
    *   `UnsatisfiedVersionException`: API가 설치된 장치의 애플리케이션 버전보다 더 높은 버전을 요구할 경우 발생합니다. 예를 들어, StartUp 앱 1.0.0이 설치된 장치에서 @RequiresStartUp("2.0.0")인 메서드를 호출할 경우 발생합니다.
    
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

#### 비행기 모드 끄기

비행기 모드를 끕니다.

*   **필요 StartUp 버전**: `6.3.7` 이상

```kotlin
M3Mobile.instance.turnOffAirplaneMode()
```

---

### App API

애플리케이션을 설치하거나 특정 패키지를 활성화/비활성화합니다.

#### 로컬 APK 설치

로컬 파일 경로에서 APK를 설치합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `filePath` (String): 설치할 .apk 파일의 절대 경로입니다.

```kotlin
M3Mobile.instance.installLocalApk(filePath: String)
```

#### 원격 APK 설치

원격 URL에서 APK를 다운로드하여 설치합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `url` (String): APK 파일의 URL입니다.

```kotlin
M3Mobile.instance.installRemoteApk(url: String)
```

#### 애플리케이션 활성화

지정된 애플리케이션 패키지를 활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `packageName` (String): 활성화할 애플리케이션의 패키지 이름입니다.

```kotlin
M3Mobile.instance.enableApp(packageName: String)
```

#### 애플리케이션 비활성화

지정된 애플리케이션 패키지를 비활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `packageName` (String): 비활성화할 애플리케이션의 패키지 이름입니다.

```kotlin
M3Mobile.instance.disableApp(packageName: String)
```

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

#### 진동 모드 활성화

진동 모드를 활성화합니다. 이 기능을 사용하면 벨소리와 알림 볼륨이 0으로 설정됩니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```kotlin
M3Mobile.instance.enableVibrationMode()
```

#### 진동 모드 비활성화

진동 모드를 비활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```kotlin
M3Mobile.instance.disableVibrationMode()
```

#### 디스플레이 설정

디스플레이 설정을 구성합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `displaySetting` (DisplaySetting): 디스플레이 설정 정보를 담은 객체입니다.

```kotlin
M3Mobile.instance.setDisplaySetting(displaySetting: DisplaySetting)
```

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

#### 권한 취소

대상 애플리케이션 패키지에서 특정 런타임 권한을 취소합니다.

*   **필요 StartUp 버전**: `6.4.17` 이상
*   **매개변수**:
    *   `packageName` (String): 애플리케이션의 패키지 이름입니다.
    *   `permission` (String): 취소할 권한의 전체 이름입니다.

```kotlin
M3Mobile.instance.revokePermission(packageName: String, permission: String)
```

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

#### 빠른 설정 타일 초기화

빠른 설정 타일 구성을 기본 상태로 재설정합니다.

*   **필요 StartUp 버전**: `6.4.1` 이상

```kotlin
M3Mobile.instance.resetQuickTile()
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

#### NTP 서버 설정

자동 시간 동기화를 위한 NTP 서버를 설정합니다. 이 설정은 다음 재부팅 후 적용됩니다.

*   **필요 StartUp 버전**: `6.4.9` 이상
*   **매개변수**:
    *   `host` (String): NTP 서버의 호스트 이름 또는 IP 주소입니다.

```kotlin
M3Mobile.instance.setNtpServer(host: String)
```

#### 시간대 설정

시스템의 기본 시간대를 설정합니다.

*   **필요 StartUp 버전**: `6.5.9` 이상
*   **매개변수**:
    *   `timezone` (String): 시간대 식별자입니다 (예: "Asia/Seoul", "America/New_York").

```kotlin
M3Mobile.instance.setTimeZone(timezone: String)
```

#### NTP 서버 조회

현재 설정된 NTP 서버 주소를 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: NTP 서버 주소 문자열입니다.

```kotlin
M3Mobile.instance.getNtpServer(): String
```

#### NTP 동기화 간격 조회

현재 설정된 NTP 동기화 간격을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: 밀리초(ms) 단위의 NTP 동기화 간격(Int)입니다.

```kotlin
M3Mobile.instance.getNtpInterval(): Int
```

#### 시간대 조회

시스템의 현재 기본 시간대를 조회합니다.

*   **반환값**: 시간대 식별자 문자열입니다.

```kotlin
M3Mobile.instance.getTimeZone(): String
```

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

#### USB 모드를 RNDIS로 설정

USB 연결 모드를 RNDIS(USB 테더링)로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```kotlin
M3Mobile.instance.setUsbModeRndis()
```

#### USB 모드를 MIDI로 설정

USB 연결 모드를 MIDI로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```kotlin
M3Mobile.instance.setUsbModeMidi()
```

#### USB 모드를 PTP로 설정

USB 연결 모드를 PTP(Picture Transfer Protocol)로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```kotlin
M3Mobile.instance.setUsbModePtp()
```

#### USB 데이터 비활성화 (충전 전용)

USB 데이터 연결을 비활성화합니다 (충전 전용).

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```kotlin
M3Mobile.instance.setUsbModeNone()
```

#### 현재 USB 모드 조회

현재 USB 연결 모드를 조회합니다.

*   **반환값**: 현재 활성화된 USB 모드를 나타내는 문자열 목록입니다. 활성화된 모드가 없으면 빈 목록을 반환합니다.

```kotlin
M3Mobile.instance.getCurrentUsbModes(): List<String>
```

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

#### 캡티브 포털 감지 (Captive Portal Detection)

장치가 캡티브 포털(공용 Wi-Fi 로그인 페이지)을 감지할지 여부를 제어합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SL20`

```kotlin
M3Mobile.instance.enableCaptivePortalDetection()
M3Mobile.instance.disableCaptivePortalDetection()
```

#### 주파수 대역 제어 (Frequency Band Control)

Wi-Fi 주파수 대역 사용을 제한합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SM15`, `SL10`, `SL10K`

```kotlin
M3Mobile.instance.allowAllWifiFrequencyBand()          // 모든 대역 허용
M3Mobile.instance.allowOnly2_4GHzWifiFrequencyBand()   // 2.4GHz 대역만 허용
M3Mobile.instance.allowOnly5GHzWifiFrequencyBand()     // 5GHz 대역만 허용
```

#### Wi-Fi 국가 코드 설정

Wi-Fi 국가 코드를 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SL10`, `SL10K`
*   **매개변수**:
    *   `countryCode` (String): ISO 3166-1 alpha-2 국가 코드 (예: "US", "KR").

```kotlin
M3Mobile.instance.setWifiCountry(countryCode: String)
```

#### 개방형 네트워크 알림 (Open Network Notification)

사용 가능한 개방형 Wi-Fi 네트워크가 있을 때 알림을 받을지 여부를 제어합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```kotlin
M3Mobile.instance.enableOpenNetworkNotification()
M3Mobile.instance.disableOpenNetworkNotification()
```

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

#### Wi-Fi 절전 정책 (Wi-Fi Sleep Policy)

Wi-Fi가 언제 절전 모드로 들어갈지 제어합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```kotlin
M3Mobile.instance.setWifiSleepPolicyNever()         // 항상 켜짐 (절전 안 함)
M3Mobile.instance.setWifiSleepPolicyPluggedOnly()   // 충전 중일 때만 켜짐
M3Mobile.instance.setWifiSleepPolicyAlways()        // 화면이 꺼지면 절전 모드 허용
```

#### Wi-Fi 안정성 (Wi-Fi Stability)

Wi-Fi 성능을 최적화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **참고**: Android 13 이상에서는 지원되지 않습니다.

```kotlin
M3Mobile.instance.setWifiStabilityNormal() // 균형 모드
M3Mobile.instance.setWifiStabilityHigh()   // 성능 중심 (배터리 소모 증가)
```

#### Wi-Fi 채널 설정

사용할 Wi-Fi 채널을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SM15`, `SL10`, `SL10K`
*   **매개변수**:
    *   `channels` (vararg Int): 활성화할 채널 목록 (예: 1, 6, 11, 36).

```kotlin
M3Mobile.instance.setWifiChannel(vararg channels: Int)
```

#### 네트워크 관리 (Network Management)

##### 액세스 포인트 설정

Wi-Fi 액세스 포인트(AP)를 구성합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `accessPoint` (AccessPoint): 설정할 Wi-Fi 액세스 포인트 객체입니다. `AccessPoint.builder()`를 사용하여 생성하세요.

```kotlin
M3Mobile.instance.setAccessPoint(accessPoint: AccessPoint)
```

##### 저장된 Wi-Fi 네트워크 초기화

저장된 모든 Wi-Fi 네트워크를 제거합니다.

*   **필요 StartUp 버전**: `6.4.11` 이상

```kotlin
M3Mobile.instance.clearSavedWifiNetworks()
```

##### Wi-Fi 네트워크 제거

특정 Wi-Fi 네트워크를 제거합니다.

*   **필요 StartUp 버전**: `6.4.11` 이상
*   **매개변수**:
    *   `ssid` (String): 제거할 네트워크의 SSID입니다.

```kotlin
M3Mobile.instance.removeWifiNetwork(ssid: String)
```

#### 기기별 Wi-Fi 설정 (Device Specific Wi-Fi Settings)

다음 함수들은 특정 기기 모델에서만 지원됩니다.

##### 로밍 임계값 조회

현재 설정된 Wi-Fi 로밍 임계값을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: 음수 `Int` 값의 로밍 임계값입니다.

```kotlin
M3Mobile.instance.getRoamingThreshold(): Int
```

##### 로밍 델타값 조회

현재 설정된 Wi-Fi 로밍 델타값을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: `Int` 형태의 로밍 델타값입니다.

```kotlin
M3Mobile.instance.getRoamingDelta(): Int
```

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

##### Wi-Fi 국가 코드 조회

현재 설정된 Wi-Fi 국가 코드를 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: `String` 형태의 국가 코드입니다.

```kotlin
M3Mobile.instance.getWifiCountryCode(): String
```