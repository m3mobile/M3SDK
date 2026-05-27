# M3 SDK Xamarin 매뉴얼

NuGet 배포 링크 : [M3Mobile.M3Sdk.Xamarin 2.3.2](https://www.nuget.org/packages/M3Mobile.M3Sdk.Xamarin/2.3.2)


M3 SDK Xamarin 패키지는 Xamarin.Android 애플리케이션에서 M3 Mobile 장치를 구성하고 제어하기 위한 C# API 모음을 제공합니다.

## 목차
- [요구 사항 (Requirements)](#요구-사항-requirements)
- [설치 방법 (Installation)](#설치-방법-installation)
  - [1. NuGet 패키지 설치](#1-nuget-패키지-설치)
  - [2. 패키지 참조 확인](#2-패키지-참조-확인)
- [기본 사용법 (Basic Usage)](#기본-사용법-basic-usage)
  - [API 접근](#api-접근)
  - [비동기 조회 및 콜백](#비동기-조회-및-콜백)
  - [API 그룹 접근](#api-그룹-접근)
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
    - [Wi-Fi 절전 정책 (Wi-Fi Sleep Policy)](#wi-fi-절전-정책-wi-fi-sleep-policy)
    - [Wi-Fi 안정성 (Wi-Fi Stability)](#wi-fi-안정성-wi-fi-stability)
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

*   **MonoAndroid**: 7.0 이상
*   **Visual Studio**: 2017 이상

## 설치 방법 (Installation)

### 1. NuGet 패키지 설치

Visual Studio의 NuGet 패키지 관리자에서 `M3Mobile.M3Sdk.Xamarin`을 검색하여 설치하거나, 패키지 관리자 콘솔에서 다음 명령을 실행합니다.

```powershell
Install-Package M3Mobile.M3Sdk.Xamarin -Version 2.3.2
```

NuGet 패키지 페이지는 문서 상단의 배포 링크에서 확인할 수 있습니다.

### 2. 패키지 참조 확인

프로젝트 파일에서 다음과 같은 패키지 참조를 확인할 수 있습니다.

```xml
<PackageReference Include="M3Mobile.M3Sdk.Xamarin" Version="2.3.2" />
```

## 기본 사용법 (Basic Usage)

Xamarin용 SDK는 Android `Context`를 사용하여 인스턴스를 생성합니다. 애플리케이션에서 하나의 인스턴스를 생성해 재사용하는 방식을 권장합니다.

### API 접근

모든 기능은 `IM3Sdk` 인스턴스를 통해 접근할 수 있습니다.

```csharp
using Android.App;
using M3Sdk.Xamarin;

// 예시: 비행기 모드 켜기
IM3Sdk m3 = M3Mobile.Create(Application.Context);
m3.TurnOnAirplaneMode();
```

사용이 끝난 뒤에는 `Dispose()`를 호출하여 스캔 리스너와 내부 연결을 정리할 수 있습니다.

```csharp
m3.Dispose();
```

### 비동기 조회 및 콜백

값을 반환하는 일부 StartUp/ScanEmul 조회 API는 `Task` 기반 비동기 메서드와 콜백 메서드를 함께 제공합니다. `Async` 메서드는 `CancellationToken` 오버로드도 제공합니다.

콜백 메서드는 `M3RequestCallback<T>` delegate를 사용합니다. 성공 시 `error`는 `null`이고, 실패 시 `result`는 기본값이며 `error`에 예외가 전달됩니다. 콜백 요청은 `IM3Cancelable`을 반환하며, `Cancel()`, `IsCancellationRequested`, `Dispose()`를 제공합니다. 취소된 콜백 요청은 이후 콜백을 호출하지 않습니다.

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task 기반 조회
string serialNumber = await m3.GetSerialNumberAsync();
string serialNumberWithCancel = await m3.GetSerialNumberAsync(cancellationToken);

// 콜백 기반 조회
IM3Cancelable request = m3.GetSerialNumber((result, error) =>
{
    if (error != null)
        return;

    string value = result;
});

request.Cancel();
request.Dispose();
```

### API 그룹 접근

루트 `IM3Sdk` 인스턴스에서 바로 호출할 수도 있고, 기능별 그룹 속성을 통해 호출할 수도 있습니다.

```csharp
using System.Collections.Generic;
using M3Sdk.Xamarin.ScanEmul;

m3.StartUp.SetWifiCountry("KR");
m3.ScanEmul.SetScannerReadMode(ReadMode.Multiple);

string ntpServer = m3.Time.GetNtpServer();
IList<string> usbModes = m3.Usb.GetCurrentUsbModes();
int roamingDelta = m3.Wifi.GetRoamingDelta();
```

`IM3Sdk`는 `StartUp`, `ScanEmul`, `Time`, `Wifi`, `Usb` 그룹을 제공합니다. 각 그룹의 타입은 `IStartUpApi`, `IScanEmulApi`, `ITimeApi`, `IWifiApi`, `IUsbApi`입니다. `M3Mobile.Create(...)`는 공개 루트 구현체 `M3Sdk`를 `IM3Sdk`로 반환하며, 그룹 구현체는 `StartUpApi`, `ScanEmulApi`, `TimeApi`, `WifiApi`, `UsbApi`입니다. `ScanEmul` 그룹은 내부 스캔 연결을 정리하기 위해 `IDisposable`을 구현합니다. 루트 `IM3Sdk.Dispose()`를 호출하면 함께 정리됩니다.

### Strict Mode 및 예외 처리

M3 SDK는 특정 API 호출이 (장치 지원 또는 앱 버전과 같은) 조건을 충족하지 못할 때의 동작 방식에 영향을 미치는 **Strict Mode**를 제공합니다.

**작동 방식:**

*   **활성화된 경우**: Strict Mode가 활성화되면, Java/Kotlin SDK의 다음 조건 구조에 대응되는 API 호출은 조건이 충족되지 않을 경우 예외를 발생시킵니다.
    *   `@SupportedModels`: 해당 API를 이용 가능한 장치 모델들을 명시합니다.
    *   `@UnsupportedModels`: 해당 API 이용이 불가능한 장치 모델들을 명시합니다.
    *   `@RequiresStartUp`: 해당 API를 이용하기 위해서는 특정 버전 이상의 StartUp이 설치되어 있어야 함을 나타냅니다.
    *   `@RequiresScanEmul`: 해당 API를 이용하기 위해서는 특정 버전 이상의 ScanEmul이 설치되어 있어야 함을 나타냅니다.

    발생할 수 있는 예외는 다음과 같습니다.
    *   `UnsupportedDeviceModelException`: 지원되지 않는 장치 모델에서 API가 호출될 경우 발생합니다.
    *   `UnsatisfiedVersionException`: API가 설치된 장치의 StartUp 또는 ScanEmul 애플리케이션 버전보다 더 높은 버전을 요구할 경우 발생합니다.

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

```csharp
m3.TurnOnAirplaneMode();
```

#### 비행기 모드 끄기

비행기 모드를 끕니다.

*   **필요 StartUp 버전**: `6.3.7` 이상

```csharp
m3.TurnOffAirplaneMode();
```

---

### App API

애플리케이션을 설치하거나 특정 패키지를 활성화/비활성화합니다.

#### 로컬 APK 설치

로컬 파일 경로에서 APK를 설치합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `filePath` (string): 설치할 .apk 파일의 절대 경로입니다.

```csharp
m3.InstallLocalApk(filePath);
```

#### 원격 APK 설치

원격 URL에서 APK를 다운로드하여 설치합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `url` (string): APK 파일의 URL입니다.

```csharp
m3.InstallRemoteApk(url);
```

#### 애플리케이션 활성화

지정된 애플리케이션 패키지를 활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `packageName` (string): 활성화할 애플리케이션의 패키지 이름입니다.

```csharp
m3.EnableApp(packageName);
```

#### 애플리케이션 비활성화

지정된 애플리케이션 패키지를 비활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `packageName` (string): 비활성화할 애플리케이션의 패키지 이름입니다.

```csharp
m3.DisableApp(packageName);
```

---

### Device API

볼륨, 디스플레이, 진동 등 다양한 장치 설정을 제어합니다.

#### 미디어 볼륨 설정

미디어 볼륨을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **범위**: 0 ~ 15
*   **매개변수**:
    *   `value` (int): 설정할 볼륨 레벨입니다.

```csharp
m3.SetMediaVolume(value);
```

#### 벨소리 볼륨 설정

벨소리 볼륨을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **범위**:
    *   0 ~ 15: 모델 `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `SL25`, `PC10`
    *   0 ~ 7: 그 외 모든 모델
*   **매개변수**:
    *   `value` (int): 설정할 볼륨 레벨입니다.

```csharp
m3.SetRingtoneVolume(value);
```

#### 알림 볼륨 설정

알림 볼륨을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **범위**: `SetRingtoneVolume`과 동일
*   **매개변수**:
    *   `value` (int): 설정할 볼륨 레벨입니다.

```csharp
m3.SetNotificationVolume(value);
```

#### 알람 볼륨 설정

알람 볼륨을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **범위**:
    *   0 ~ 15: 모델 `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `PC10`
    *   0 ~ 7: 그 외 모든 모델
*   **매개변수**:
    *   `value` (int): 설정할 볼륨 레벨입니다.

```csharp
m3.SetAlarmVolume(value);
```

#### 진동 모드 활성화

진동 모드를 활성화합니다. 이 기능을 사용하면 벨소리와 알림 볼륨이 0으로 설정됩니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```csharp
m3.EnableVibrationMode();
```

#### 진동 모드 비활성화

진동 모드를 비활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```csharp
m3.DisableVibrationMode();
```

#### 디스플레이 설정

디스플레이 설정을 구성합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `displaySetting` (DisplaySetting): 디스플레이 설정 정보를 담은 객체입니다.

```csharp
using System;
using M3Sdk.Xamarin.Startup;

var displaySetting = new DisplaySetting(
    enableAutoBrightness: false,
    brightness: 180,
    enableAutoRotate: true,
    rotateForce: RotateForce.Automatic,
    enableScreenLock: true,
    sleepMode: SleepMode.Minutes5,
    policyControl: PolicyControl.Default,
    showBatteryPercentage: true,
    screenSaverMode: ScreenSaverMode.Never,
    screenSaverComponent: "");

m3.SetDisplaySetting(displaySetting);
```

`DisplaySetting`에서 사용하는 주요 enum은 다음과 같습니다.

*   `SleepMode`: `Seconds15`, `Seconds30`, `Minutes1`, `Minutes2`, `Minutes5`, `Minutes10`, `Minutes30`, `Never`
*   `RotateForce`: `Default`, `Automatic`, `Landscape`, `LandscapeReverse`, `LandscapeSensor`, `Portrait`, `PortraitReverse`, `PortraitSensor`
*   `PolicyControl`: `HideStatusBar`, `HideNavigationBar`, `HideSystemBar`, `Default`
*   `ScreenSaverMode`: `WhileCharging`, `WhileDocked`, `WhileChargingOrDocked`, `Never`

`DisplaySetting`은 `EnableAutoBrightness`, `Brightness`, `EnableAutoRotate`, `RotateForce`, `EnableScreenLock`, `SleepMode`, `PolicyControl`, `ShowBatteryPercentage`, `ScreenSaverMode`, `ScreenSaverComponent` 속성을 제공합니다.

#### 시리얼 번호 조회

장치의 시리얼 번호를 조회합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **반환값**: 시리얼 번호 문자열입니다.

```csharp
using System;
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
string serialNumber = await m3.GetSerialNumberAsync();
string serialNumberWithCancel = await m3.GetSerialNumberAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetSerialNumber((result, error) =>
{
    if (error != null)
        return;

    string serialNumberFromCallback = result;
});
```

#### 상태 표시줄 확장 잠금

상태 표시줄 확장을 잠급니다. 잠긴 경우 사용자는 상태 표시줄을 아래로 내려 알림이나 빠른 설정을 볼 수 없습니다.

*   **필요 StartUp 버전**: `6.4.12` 이상

```csharp
m3.LockStatusBarExpansion();
```

#### 상태 표시줄 확장 잠금 해제

상태 표시줄 확장을 잠금 해제합니다.

*   **필요 StartUp 버전**: `6.4.12` 이상

```csharp
m3.UnlockStatusBarExpansion();
```

#### Bluetooth MAC 주소 조회

장치의 Bluetooth MAC 주소를 조회합니다.

*   **지원 모델**: 전체 모델
*   **필요 StartUp 버전**: `6.5.31` 이상 (UL30), `6.5.35` 이상 (기타 모델)
*   **반환값**: Bluetooth MAC 주소 문자열입니다 (형식: XX:XX:XX:XX:XX:XX).

```csharp
using System;
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
string bluetoothMac = await m3.GetBluetoothMacAsync();
string bluetoothMacWithCancel = await m3.GetBluetoothMacAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetBluetoothMac((result, error) =>
{
    if (error != null)
        return;

    string bluetoothMacFromCallback = result;
});
```

---

### Language API

시스템 언어 설정을 제어합니다.

#### 언어 설정

장치의 시스템 언어와 국가를 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `language` (string): 언어 코드 (예: "en", "ko").
    *   `country` (string): 국가 코드 (예: "US", "KR").

```csharp
m3.SetLanguage("ko", "KR");
```

---

### Network API

모바일 네트워크 설정을 구성합니다.

#### APN 설정

APN(Access Point Name) 구성을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `apn` (Apn): 네트워크 설정 정보를 담고 있는 `Apn` 객체입니다. `Apn.CreateBuilder()`를 사용하여 생성할 수 있습니다.

```csharp
using M3Sdk.Xamarin.Startup;

var apn = Apn.CreateBuilder()
    .SetName("M3")
    .SetUrl("internet")
    .SetMcc("450")
    .SetMnc("08")
    .SetType("default")
    .Build();

m3.SetApn(apn);
```

`Apn`은 생성자 또는 `Apn.Builder`로 생성할 수 있으며, 기본 속성은 `Name`, `Url`, `Mcc`, `Mnc`, `Type`입니다. 선택 값으로 `Proxy`, `Port`, `User`, `Password`, `Server`, `Mmsc`, `MmsProxy`, `MmsPort`, `AuthType`, `Protocol`, `Roaming`, `Mvno`, `MvnoValue`를 설정할 수 있습니다. 빌더 메서드는 `SetName`, `SetUrl`, `SetMcc`, `SetMnc`, `SetType`, `SetProxy`, `SetPort`, `SetUser`, `SetPassword`, `SetServer`, `SetMmsc`, `SetMmsProxy`, `SetMmsPort`, `SetAuthType`, `SetProtocol`, `SetRoaming`, `SetMvno`, `SetMvnoValue`, `Build`입니다.

#### NFC 활성화

NFC(Near Field Communication)를 활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **필요 Android 버전**: Android 11 (R) 이상

```csharp
m3.EnableNfc();
```

#### NFC 비활성화

NFC(Near Field Communication)를 비활성화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **필요 Android 버전**: Android 11 (R) 이상

```csharp
m3.DisableNfc();
```

---

### Permission API

애플리케이션의 런타임 권한을 부여하거나 취소합니다.

#### 권한 부여

대상 애플리케이션 패키지에 특정 런타임 권한을 부여합니다.

*   **필요 StartUp 버전**: `6.4.17` 이상
*   **매개변수**:
    *   `packageName` (string): 애플리케이션의 패키지 이름입니다.
    *   `permission` (string): 부여할 권한의 전체 이름입니다 (예: `android.permission.CAMERA`).

```csharp
m3.GrantPermission("com.example.app", "android.permission.CAMERA");
```

#### 권한 취소

대상 애플리케이션 패키지에서 특정 런타임 권한을 취소합니다.

*   **필요 StartUp 버전**: `6.4.17` 이상
*   **매개변수**:
    *   `packageName` (string): 애플리케이션의 패키지 이름입니다.
    *   `permission` (string): 취소할 권한의 전체 이름입니다.

```csharp
m3.RevokePermission("com.example.app", "android.permission.CAMERA");
```

---

### Quick Tile API

시스템 UI의 빠른 설정 타일(Quick Settings tiles)을 사용자 지정합니다.

#### 빠른 설정 타일 지정

표시할 빠른 설정 타일을 설정합니다.

*   **필요 StartUp 버전**: `6.4.1` 이상
*   **매개변수**:
    *   `quickTiles` (params QuickTile[]): 추가할 하나 이상의 `QuickTile` 객체입니다.

```csharp
using M3Sdk.Xamarin.Startup;

m3.SetQuickTiles(
    new QuickTile(QuickTileId.Wifi, "Wi-Fi"),
    new QuickTile(QuickTileId.Bluetooth, "Bluetooth"),
    new QuickTile(QuickTileId.Flashlight, "Flashlight"));
```

`QuickTile`은 `Id`, `Name` 속성을 제공합니다. `QuickTileId`는 `Wifi`, `Bluetooth`, `Flashlight`, `DoNotDisturb`, `AutoRotation`, `BatterySaver`, `AirplaneMode`, `NightLight`, `ScreenRecord`, `QrCodeScanner`, `Alarm`, `DeviceControls`, `Wallet`, `ScreenCast`, `Location`, `Hotspot`, `ColorInversion`, `DataSaver`, `DarkTheme`을 제공합니다.

#### 빠른 설정 타일 초기화

빠른 설정 타일 구성을 기본 상태로 재설정합니다.

*   **필요 StartUp 버전**: `6.4.1` 이상

```csharp
m3.ResetQuickTile();
```

---

### Scanner API

바코드 스캐너를 제어하고 스캔 환경을 설정합니다.

#### 스캔 시작

스캔 프로세스를 시작합니다.

*   **필요 ScanEmul 버전**: `2.13.0` 이상

```csharp
m3.StartScan();
```

#### 스캔 중지

스캔 프로세스를 중지합니다.

*   **필요 ScanEmul 버전**: `2.13.0` 이상

```csharp
m3.StopScan();
```

#### 스캐너 상태 조회

스캐너의 현재 상태를 조회합니다.

*   **필요 ScanEmul 버전**: `2.13.0` 이상
*   **반환값**: 상태를 나타내는 정수값입니다.
    *   `1`: 열기 실패
    *   `2`: 닫기 실패
    *   `4`: 열기 성공
    *   `8`: 닫기 성공

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
int status = await m3.GetScannerStatusAsync();
int statusWithCancel = await m3.GetScannerStatusAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetScannerStatus((result, error) =>
{
    if (error != null)
        return;

    int scannerStatus = result;
});
```

#### 스캐너 타입 조회

스캐너 하드웨어 타입을 조회합니다.

*   **필요 ScanEmul 버전**: `2.13.0` 이상
*   **반환값**: 스캐너 타입 문자열입니다.

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
string scannerType = await m3.GetScannerTypeAsync();
string scannerTypeWithCancel = await m3.GetScannerTypeAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetScannerType((result, error) =>
{
    if (error != null)
        return;

    string scannerTypeFromCallback = result;
});
```

#### 스캔 결과 리스너 (Scan Result Listener)

스캔 결과를 수신하기 위한 리스너를 등록하거나 해제합니다.

*   **필요 ScanEmul 버전**: `4.11.0` 이상
*   **매개변수**:
    *   `listener` (IOnScanResultListener 또는 Action<ScanResult>): 스캔 결과를 수신할 리스너입니다.
*   **이벤트 데이터**: `ScanResultEventArgs`
*   **인터페이스 콜백 메서드**: `OnScanResult`
*   **수신 데이터**:
    *   `ScanResult.Barcode`: 디코딩된 바코드 데이터입니다.
    *   `ScanResult.Type`: 스캐너 코드 타입입니다.
    *   `ScanResult.RawData`: 원본 스캐너 데이터 바이트입니다.

```csharp
using System;
using M3Sdk.Xamarin.ScanEmul;

// Event
m3.ScanResultReceived += (sender, args) =>
{
    ScanResult result = args.Result;
    string barcode = result.Barcode;
    string type = result.Type;
};

// Action 등록. 반환된 IDisposable을 Dispose하면 등록이 해제됩니다.
IDisposable registration = m3.RegisterOnScanResultListener(result =>
{
    string barcode = result.Barcode;
});

registration.Dispose();
```

인터페이스 기반 리스너도 사용할 수 있습니다.

```csharp
m3.RegisterOnScanResultListener(listener);
m3.UnregisterOnScanResultListener(listener);
```

#### GS1 파싱 결과 리스너 (GS1 Parsed Listener)

스캔된 바코드를 GS1 형식으로 파싱한 결과를 수신하기 위한 리스너를 등록하거나 해제합니다.

*   **필요 ScanEmul 버전**: `4.11.0` 이상
*   **매개변수**:
    *   `listener` (IOnGS1ParsedListener 또는 Action<IList<GS1ParsedData>>): GS1 파싱 결과를 수신할 리스너입니다.
*   **이벤트 데이터**: `GS1ParsedEventArgs`
*   **인터페이스 콜백 메서드**: `OnGS1Parsed`
*   **수신 데이터**:
    *   `GS1ParsedData.Ai`: Application Identifier입니다.
    *   `GS1ParsedData.Data`: 파싱된 데이터 값입니다.
    *   `GS1ParsedData.Description`: Application Identifier 설명입니다.

```csharp
using System;
using System.Collections.Generic;
using M3Sdk.Xamarin.ScanEmul;

// Event
m3.GS1ParsedReceived += (sender, args) =>
{
    IList<GS1ParsedData> result = args.Result;
};

// Action 등록
IDisposable registration = m3.RegisterOnGS1ParsedListener(result =>
{
    foreach (GS1ParsedData item in result)
    {
        string ai = item.Ai;
        string data = item.Data;
    }
});

registration.Dispose();
```

인터페이스 기반 리스너도 사용할 수 있습니다.

```csharp
m3.RegisterOnGS1ParsedListener(listener);
m3.UnregisterOnGS1ParsedListener(listener);
```

#### 디지털 링크 파싱 결과 리스너 (Digital Link Parsed Listener)

스캔된 바코드에서 디지털 링크를 파싱한 결과를 수신하기 위한 리스너를 등록하거나 해제합니다.

*   **필요 ScanEmul 버전**: `4.11.0` 이상
*   **매개변수**:
    *   `listener` (IOnDigitalLinkParsedListener 또는 Action<IList<DigitalLinkParsedData>>): 디지털 링크 파싱 결과를 수신할 리스너입니다.
*   **이벤트 데이터**: `DigitalLinkParsedEventArgs`
*   **인터페이스 콜백 메서드**: `OnDigitalLinkParsed`
*   **수신 데이터**:
    *   `DigitalLinkParsedData.Ai`: Application Identifier입니다.
    *   `DigitalLinkParsedData.Data`: 파싱된 데이터 값입니다.
    *   `DigitalLinkParsedData.Description`: Application Identifier 설명입니다.

```csharp
using System;
using System.Collections.Generic;
using M3Sdk.Xamarin.ScanEmul;

// Event
m3.DigitalLinkParsedReceived += (sender, args) =>
{
    IList<DigitalLinkParsedData> result = args.Result;
};

// Action 등록
IDisposable registration = m3.RegisterOnDigitalLinkParsedListener(result =>
{
    foreach (DigitalLinkParsedData item in result)
    {
        string ai = item.Ai;
        string data = item.Data;
    }
});

registration.Dispose();
```

인터페이스 기반 리스너도 사용할 수 있습니다.

```csharp
m3.RegisterOnDigitalLinkParsedListener(listener);
m3.UnregisterOnDigitalLinkParsedListener(listener);
```

#### 스캐너 설정 (Scanner Settings)

다양한 스캐너 옵션을 구성합니다. 이 설정은 현재 활성화된 프로필에 적용됩니다.

*   **필요 ScanEmul 버전**: `2.11.0` 이상

##### 피드백 (Feedback)

```csharp
using M3Sdk.Xamarin.ScanEmul;

// 사운드
m3.SetScanSound(ScanSound.Beep);
// Enum: None, Beep, DingDong

// 진동
m3.EnableScanVibration();
m3.DisableScanVibration();

// LED
m3.EnableScanLed();
m3.DisableScanLed();
m3.SetScanLedTime(timeMillis); // 범위: 1 ~ 1000
```

##### 스캔 모드 (Scanning Mode)

```csharp
using System.Threading;
using M3Sdk.Xamarin;
using M3Sdk.Xamarin.ScanEmul;

CancellationToken cancellationToken = CancellationToken.None;

m3.SetScannerReadMode(ReadMode.Multiple);
// Enum: AimingAndRelease, Async, Continue, Multiple, Presentation, Sync

// Task
ReadMode readMode = await m3.GetScannerReadModeAsync();
ReadMode readModeWithCancel = await m3.GetScannerReadModeAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetScannerReadMode((result, error) =>
{
    if (error != null)
        return;

    ReadMode mode = result;
});
```

##### 출력 구성 (Output Configuration)

```csharp
using M3Sdk.Xamarin.ScanEmul;

// 출력 모드
m3.SetScanResultOutputMode(OutputMode.CopyAndPaste);
// Enum: CommitText, CopyAndPaste, CopyToClipboard, KeyEmulation

// 포맷팅
m3.SetScanResultPrefix("Prefix");
m3.SetScanResultPostfix("Postfix");
m3.SetScanResultEndCharacter(EndCharacter.Enter);
// Enum: Enter, KeyboardEnter, KeyboardSpace, KeyboardTab, None, Space, Tab
```

조회 API는 `Task` 방식과 콜백 방식을 모두 제공합니다.

```csharp
using System.Threading;
using M3Sdk.Xamarin;
using M3Sdk.Xamarin.ScanEmul;

CancellationToken cancellationToken = CancellationToken.None;

// Task
OutputMode outputMode = await m3.GetScanResultOutputModeAsync();
OutputMode outputModeWithCancel = await m3.GetScanResultOutputModeAsync(cancellationToken);

string prefix = await m3.GetScanResultPrefixAsync();
string prefixWithCancel = await m3.GetScanResultPrefixAsync(cancellationToken);

string postfix = await m3.GetScanResultPostfixAsync();
string postfixWithCancel = await m3.GetScanResultPostfixAsync(cancellationToken);

EndCharacter endCharacter = await m3.GetScanResultEndCharacterAsync();
EndCharacter endCharacterWithCancel = await m3.GetScanResultEndCharacterAsync(cancellationToken);

// Callback
IM3Cancelable outputModeRequest = m3.GetScanResultOutputMode((result, error) => { });
IM3Cancelable prefixRequest = m3.GetScanResultPrefix((result, error) => { });
IM3Cancelable postfixRequest = m3.GetScanResultPostfix((result, error) => { });
IM3Cancelable endCharacterRequest = m3.GetScanResultEndCharacter((result, error) => { });
```

##### 프로필 상태 (Profile Status)

현재 스캐너 프로필이 활성화되어 있는지 확인합니다.

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
bool enabled = await m3.IsScannerProfileEnabledAsync();
bool enabledWithCancel = await m3.IsScannerProfileEnabledAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.IsScannerProfileEnabled((result, error) =>
{
    if (error != null)
        return;

    bool isEnabled = result;
});
```

---

### StartUp Setting API

StartUp SDK 자체의 설정을 관리합니다.

#### StartUp 설정 초기화

StartUp 설정을 기본값으로 초기화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```csharp
m3.ResetStartUpSetting();
```

---

### Time API

시스템 시간, 시간대 및 NTP 서버 설정을 구성합니다.

#### 날짜 및 시간 설정

장치의 날짜와 시간을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `dateTime` (DateTime): 설정할 날짜와 시간 객체입니다.
    *   `dateTime` (DateTimeOffset): 설정할 날짜와 시간 오프셋 객체입니다.
    *   `year`, `month`, `day`, `hour`, `minute`, `second` (int): 개별 날짜/시간 값입니다.

```csharp
using System;

m3.SetDateTime(DateTime.Now);
m3.SetDateTime(DateTimeOffset.Now);
m3.SetDateTime(2026, 5, 27, 10, 30, 0);
```

#### NTP 서버 설정

자동 시간 동기화를 위한 NTP 서버를 설정합니다. 이 설정은 다음 재부팅 후 적용됩니다.

*   **필요 StartUp 버전**: `6.4.9` 이상
*   **매개변수**:
    *   `host` (string): NTP 서버의 호스트 이름 또는 IP 주소입니다.

```csharp
m3.SetNtpServer("time.android.com");
```

#### 시간대 설정

시스템의 기본 시간대를 설정합니다.

*   **필요 StartUp 버전**: `6.5.9` 이상
*   **매개변수**:
    *   `timezone` (string): 시간대 식별자입니다 (예: "Asia/Seoul", "America/New_York").

```csharp
m3.SetTimeZone("Asia/Seoul");
```

#### NTP 서버 조회

현재 설정된 NTP 서버 주소를 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: NTP 서버 주소 문자열입니다.

```csharp
string ntpServer = m3.GetNtpServer();
```

#### NTP 동기화 간격 조회

현재 설정된 NTP 동기화 간격을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: 밀리초(ms) 단위의 NTP 동기화 간격(int)입니다.

```csharp
int ntpInterval = m3.GetNtpInterval();
```

#### 시간대 조회

시스템의 현재 기본 시간대를 조회합니다.

*   **반환값**: 시간대 식별자 문자열입니다.

```csharp
string timeZone = m3.GetTimeZone();
```

---

### Usb API

장치의 USB 연결 모드를 구성합니다.

#### USB 모드를 MTP로 설정

USB 연결 모드를 MTP(Media Transfer Protocol)로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```csharp
m3.SetUsbModeMtp();
```

#### USB 모드를 RNDIS로 설정

USB 연결 모드를 RNDIS(USB 테더링)로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```csharp
m3.SetUsbModeRndis();
```

#### USB 모드를 MIDI로 설정

USB 연결 모드를 MIDI로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```csharp
m3.SetUsbModeMidi();
```

#### USB 모드를 PTP로 설정

USB 연결 모드를 PTP(Picture Transfer Protocol)로 설정합니다.

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```csharp
m3.SetUsbModePtp();
```

#### USB 데이터 비활성화 (충전 전용)

USB 데이터 연결을 비활성화합니다 (충전 전용).

*   **지원 모델**: `US20`, `US30`
*   **필요 StartUp 버전**: `6.5.10` 이상

```csharp
m3.SetUsbModeNone();
```

#### 현재 USB 모드 조회

현재 USB 연결 모드를 조회합니다.

*   **반환값**: 현재 활성화된 USB 모드를 나타내는 문자열 목록입니다. 활성화된 모드가 없으면 빈 목록을 반환합니다.

```csharp
using System.Collections.Generic;

IList<string> usbModes = m3.GetCurrentUsbModes();
```

---

### Wifi API

Wi-Fi 설정 및 구성을 제어합니다.

#### Wi-Fi MAC 주소 조회

장치의 Wi-Fi MAC 주소를 조회합니다.

*   **필요 StartUp 버전**: `6.4.11` 이상
*   **반환값**: Wi-Fi MAC 주소 문자열입니다.

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
string wifiMac = await m3.GetWifiMacAsync();
string wifiMacWithCancel = await m3.GetWifiMacAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetWifiMac((result, error) =>
{
    if (error != null)
        return;

    string wifiMacFromCallback = result;
});
```

#### 캡티브 포털 감지 (Captive Portal Detection)

장치가 캡티브 포털(공용 Wi-Fi 로그인 페이지)을 감지할지 여부를 제어합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SL20`

```csharp
m3.EnableCaptivePortalDetection();
m3.DisableCaptivePortalDetection();
```

#### 주파수 대역 제어 (Frequency Band Control)

Wi-Fi 주파수 대역 사용을 제한합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SM15`, `SL10`, `SL10K`

```csharp
m3.AllowAllWifiFrequencyBand();          // 모든 대역 허용
m3.AllowOnly2_4GHzWifiFrequencyBand();   // 2.4GHz 대역만 허용
m3.AllowOnly5GHzWifiFrequencyBand();     // 5GHz 대역만 허용
```

#### Wi-Fi 국가 코드 설정

Wi-Fi 국가 코드를 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SL10`, `SL10K`
*   **매개변수**:
    *   `countryCode` (string): ISO 3166-1 alpha-2 국가 코드 (예: "US", "KR").

```csharp
m3.SetWifiCountry("KR");
```

#### 개방형 네트워크 알림 (Open Network Notification)

사용 가능한 개방형 Wi-Fi 네트워크가 있을 때 알림을 받을지 여부를 제어합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```csharp
m3.EnableOpenNetworkNotification();
m3.DisableOpenNetworkNotification();
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

```csharp
m3.SetRoamingTrigger(index);
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

```csharp
m3.SetRoamingDelta(index);
```

#### Wi-Fi 절전 정책 (Wi-Fi Sleep Policy)

Wi-Fi가 언제 절전 모드로 들어갈지 제어합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상

```csharp
m3.SetWifiSleepPolicyNever();         // 항상 켜짐 (절전 안 함)
m3.SetWifiSleepPolicyPluggedOnly();   // 충전 중일 때만 켜짐
m3.SetWifiSleepPolicyAlways();        // 화면이 꺼지면 절전 모드 허용
```

#### Wi-Fi 안정성 (Wi-Fi Stability)

Wi-Fi 성능을 최적화합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **참고**: Android 13 이상에서는 지원되지 않습니다.

```csharp
m3.SetWifiStabilityNormal(); // 균형 모드
m3.SetWifiStabilityHigh();   // 성능 중심 (배터리 소모 증가)
```

#### Wi-Fi 채널 설정

사용할 Wi-Fi 채널을 설정합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **미지원 모델**: `SM15`, `SL10`, `SL10K`
*   **매개변수**:
    *   `channels` (params int[]): 활성화할 채널 목록 (예: 1, 6, 11, 36).

```csharp
m3.SetWifiChannel(1, 6, 11, 36);
```

#### 네트워크 관리 (Network Management)

##### 액세스 포인트 설정

Wi-Fi 액세스 포인트(AP)를 구성합니다.

*   **필요 StartUp 버전**: `6.2.14` 이상
*   **매개변수**:
    *   `accessPoint` (AccessPoint): 설정할 Wi-Fi 액세스 포인트 객체입니다. `AccessPoint.CreateBuilder()`를 사용하여 생성할 수 있습니다.

```csharp
using M3Sdk.Xamarin.Startup;

var accessPoint = AccessPoint.CreateBuilder()
    .SetSsid("M3-WiFi")
    .SetSecurity("WPA2")
    .SetPassword("password")
    .SetHiddenSsid(false)
    .Build();

m3.SetAccessPoint(accessPoint);
```

`AccessPoint`는 생성자 또는 `AccessPoint.Builder`로 생성할 수 있으며, 필수 속성은 `Ssid`, `Security`입니다. 선택 값으로 `Password`, `EnableStatic`, `IpAddress`, `Mask`, `Gateway`, `Dns`, `MacRandom`, `HiddenSsid`를 설정할 수 있습니다. 빌더 메서드는 `SetSsid`, `SetSecurity`, `SetPassword`, `SetEnableStatic`, `SetIpAddress`, `SetMask`, `SetGateway`, `SetDns`, `SetMacRandom`, `SetHiddenSsid`, `Build`입니다.

##### 저장된 Wi-Fi 네트워크 초기화

저장된 모든 Wi-Fi 네트워크를 제거합니다.

*   **필요 StartUp 버전**: `6.4.11` 이상

```csharp
m3.ClearSavedWifiNetworks();
```

##### Wi-Fi 네트워크 제거

특정 Wi-Fi 네트워크를 제거합니다.

*   **필요 StartUp 버전**: `6.4.11` 이상
*   **매개변수**:
    *   `ssid` (string): 제거할 네트워크의 SSID입니다.

```csharp
m3.RemoveWifiNetwork("M3-WiFi");
```

#### 기기별 Wi-Fi 설정 (Device Specific Wi-Fi Settings)

다음 함수들은 특정 기기 모델에서만 지원됩니다.

##### 로밍 임계값 조회

현재 설정된 Wi-Fi 로밍 임계값을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: 음수 `int` 값의 로밍 임계값입니다.

```csharp
int roamingThreshold = m3.GetRoamingThreshold();
```

##### 로밍 델타값 조회

현재 설정된 Wi-Fi 로밍 델타값을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: `int` 형태의 로밍 델타값입니다.

```csharp
int roamingDelta = m3.GetRoamingDelta();
```

##### Wi-Fi 주파수 대역 조회

현재 선호하는 Wi-Fi 주파수 대역 설정을 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: 주파수 대역을 나타내는 `int` 값입니다:
    *   `0`: 자동 (Automatic)
    *   `1`: 2.4 GHz 전용
    *   `2`: 5 GHz 전용

```csharp
int frequencyBand = m3.GetWifiFrequencyBand();
```

##### Wi-Fi 국가 코드 조회

현재 설정된 Wi-Fi 국가 코드를 조회합니다.

*   **지원 모델**: `US20`, `US30`
*   **반환값**: `string` 형태의 국가 코드입니다. 설정값이 없으면 빈 문자열을 반환합니다.

```csharp
string countryCode = m3.GetWifiCountryCode();
```
