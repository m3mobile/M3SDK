# STARTUP-106: getBluetoothMac() API 구현

## 개요

디바이스의 Bluetooth MAC 주소를 조회하는 `getBluetoothMac()` API를 `StartUpDeviceApi`에 추가했다.
전체 디바이스 모델을 지원하며, StartUp 앱 **v6.5.31** 이상(UL30), **v6.5.35** 이상(기타 모델)이 필요하다.
`getSerialNumber()`와 동일한 `AwaitableBroadcastRequester` request-response 패턴을 따른다.

## 수정 내역

### 신규 파일

| 파일 | 설명 |
|------|------|
| `feature/startup/.../requester/bluetooth/GetBluetoothMacRequester.kt` | `AwaitableBroadcastRequester<String>` 구현체. Action: `SYSTEM`, TypeValue: `get_bluetooth_mac` |

### 변경 파일

| 파일 | 변경 내용 |
|------|----------|
| `feature/startup/.../constants/TypeValue.kt` | `GET_BLUETOOTH_MAC = "get_bluetooth_mac"` 상수 추가 |
| `feature/startup/.../api/StartUpDeviceApi.kt` | `suspend fun getBluetoothMac(): String` + `fun getBluetoothMac(callback): Job` 선언. `@RequiresStartUp("6.5.31")` (전체 모델 지원) |
| `feature/startup/.../api/StartUpDeviceApiImpl.kt` | 두 오버로드 구현. Requester 호출 + callback 래핑 |
| `feature/startup/api/startup.api` | API 덤프 갱신 (`apiDump`). `StartUpDeviceApiDeviceSupportMapSource` KSP 생성 클래스 포함 |
| `core/.../requester/AwaitableBroadcastRequester.kt` | **버그 픽스** — Android 13+ `RECEIVER_EXPORTED` 플래그 추가 (아래 상세) |

### 데이터 흐름

```
Caller
  -> StartUpDeviceApi.getBluetoothMac()
    -> ApiProxyFactory (dynamic proxy: @RequiresStartUp 검증)
      -> StartUpDeviceApiImpl.getBluetoothMac()
        -> GetBluetoothMacRequester(context).fetch()
             1. BroadcastReceiver 등록 (ResponseAction.SYSTEM)
             2. 요청 broadcast 전송 (RequestAction.SYSTEM, typeKey="setting", typeValue="get_bluetooth_mac")
             3. suspendCancellableCoroutine + withTimeout(3000ms) 대기
             4. 응답 수신: intent.getStringExtra("get_bluetooth_mac") -> String
```

## 테스트 결과 (UL30F 실기기)

| 항목 | 값 |
|------|-----|
| **기기** | UL30F (시리얼 `2592476`, Build.MODEL `M3UL30`, Android 14 API 34) |
| **StartUp 버전** | 6.5.31 (debug APK) |
| **getBluetoothMac()** | `E8:78:29:79:56:36` 반환 — **성공** |
| **getSerialNumber()** | `2592476` 반환 — 정상 (비교 검증용) |

### 테스트 중 발견 이슈 및 해결

| 순서 | 증상 | 원인 | 해결 |
|------|------|------|------|
| 1 | `Error: null` (UI) | StartUp 앱 v6.5.29 — `get_bluetooth_mac` 미지원 | v6.5.31로 업그레이드 |
| 2 | `UndeclaredThrowableException` | Dynamic Proxy가 checked exception 래핑 | root cause 확인 → 아래 #3 |
| 3 | `RemoteException: registerReceiverWithFeature` | Android 13+에서 `registerReceiver()` 호출 시 `RECEIVER_EXPORTED` 플래그 누락 | `AwaitableBroadcastRequester` 수정 (아래 상세) |
| 4 | `TimeoutCancellationException: 3000ms` | StartUp 앱이 비활성 상태 | StartUp 앱 재실행 후 정상 동작 |

## RECEIVER_EXPORTED 버그 픽스 (core 모듈)

### 문제

`AwaitableBroadcastRequester.fetch()`에서 response receiver를 등록할 때:

```kotlin
// 수정 전 — Android 13+ 에서 RemoteException 발생
context.registerReceiver(receiver, filter)
```

Android 13 (API 33, TIRAMISU) 이상에서는 `RECEIVER_EXPORTED` 또는 `RECEIVER_NOT_EXPORTED` 플래그가 필수다.

### 수정

```kotlin
// 수정 후
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
} else {
    context.registerReceiver(receiver, filter)
}
```

### 영향 범위

`AwaitableBroadcastRequester`는 **모든 request-response API**의 기반 클래스이므로, `getBluetoothMac()` 뿐 아니라 `getSerialNumber()`, `getWifiMac()` 등 응답을 기다리는 전체 API에 영향한다.

## 동기 확인 사항

### 1. RECEIVER_EXPORTED 이슈 — 기존 고객사 영향

`AwaitableBroadcastRequester`에 `RECEIVER_EXPORTED` 플래그가 없었으므로, **Android 13 이상** 기기에서 `getSerialNumber()`, `getWifiMac()` 등 응답형 API를 사용한 고객이 있었는지 확인 필요.

> 고객 앱의 `targetSdk < 33`이면 이 제한이 적용되지 않아 우연히 동작했을 가능성 있음.

### 2. getBluetoothMac() extra key

StartUp 앱이 응답 intent에 `get_bluetooth_mac`을 extra key로 사용하는 것이 확정된 스펙인지 확인. 현재 코드:

```kotlin
// GetBluetoothMacRequester.kt
override fun getExtra(intent: Intent): String? {
    return intent.getStringExtra(typeValue)  // typeValue = "get_bluetooth_mac"
}
```

StartUp 앱 소스의 response broadcast와 일치하는지 공식 확인 필요.

## 커밋 전략

| 커밋 | 범위 | 메시지 |
|------|------|--------|
| 1 | core 모듈 | `fix: AwaitableBroadcastRequester Android 13+ RECEIVER_EXPORTED 플래그 추가` |
| 2 | feature/startup | `feat: getBluetoothMac() API 추가 (STARTUP-106)` |

`app/` 모듈 변경 (MainActivity 테스트 UI, build.gradle.kts sdk 의존성)은 임시 테스트 코드이므로 커밋 제외 대상.
