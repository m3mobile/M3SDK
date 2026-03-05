## 고객 가이드: Bluetooth MAC 주소 조회 API

### 개요
디바이스의 Bluetooth MAC 주소를 조회하는 API입니다. 전체 모델을 지원합니다.

### SDK 버전
- **SDK:** `2.2.0`
- **Gradle 의존성:** `implementation 'com.github.m3mobile:M3SDK:2.2.0'`

### 지원 모델
- 전체 모델 (UL30, SL20, SL20P, SL20K, SL25, US20, US30, UL20, PC10, WD10 등)

### 필요 조건
- StartUp 앱 v6.5.31 이상 (UL30), v6.5.35 이상 (기타 모델)

### API

| 메서드 | 반환 타입 | 설명 |
|---|---|---|
| `M3Mobile.instance.getBluetoothMac()` | `String` | Bluetooth MAC 주소 (형식: XX:XX:XX:XX:XX:XX) |

### 사용 방법

**방법 1: suspend 함수 (코루틴)**
```kotlin
val mac = M3Mobile.instance.getBluetoothMac()
```

**방법 2: Callback**
```kotlin
M3Mobile.instance.getBluetoothMac(object : RequestCallback<String> {
    override fun onResponse(result: String) {
        // result = "XX:XX:XX:XX:XX:XX"
    }
    override fun onError(error: Throwable) {
        // 에러 처리
    }
})
```

### 비고
- AwaitableBroadcastRequester의 Android 13+ RECEIVER_EXPORTED 플래그 수정이 포함되어, 기존 Awaitable 계열 API(getSerialNumber, getWifiMac 등)의 Android 13+ 안정성도 개선되었습니다.
