# SM24 Floating Scanner Button UI API Examples

These APIs wrap the ScanEmul broadcast contract for `scanner_button_ui`.
They do not store settings in M3SDK. ScanEmul is the source of truth.

## Kotlin

```kotlin
import net.m3mobile.feature.scanemul.params.ScannerButtonUiOptions
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSize
import net.m3mobile.sdk.M3Mobile

val options = ScannerButtonUiOptions(
    imagePath = "/sdcard/Download/ScanEmul_Floating_Button_Images/target.png",
    opacityPercent = 80,
    size = ScannerButtonUiSize.LARGE,
)

val setResult = M3Mobile.instance.setScannerButtonUi(options)
if (setResult.saved()) {
    val getResult = M3Mobile.instance.getScannerButtonUi()
    val settings = getResult.settings()
}

val verification = M3Mobile.instance.setAndVerifyScannerButtonUi(options)
val verified = verification.verified()
```

Partial SET keeps omitted values:

```kotlin
M3Mobile.instance.setScannerButtonUi(
    ScannerButtonUiOptions.opacityPercent(100),
)
```

Empty image path selects the default ScanEmul image:

```kotlin
M3Mobile.instance.setScannerButtonUi(
    ScannerButtonUiOptions.imagePath(""),
)
```

## Java

```java
import net.m3mobile.feature.scanemul.params.ScannerButtonUiOptions;
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSize;
import net.m3mobile.sdk.M3Mobile;

ScannerButtonUiOptions options = new ScannerButtonUiOptions(
        "/sdcard/Download/ScanEmul_Floating_Button_Images/target.png",
        80,
        ScannerButtonUiSize.LARGE
);

M3Mobile.INSTANCE.getInstance().setAndVerifyScannerButtonUi(options, (result, error) -> {
    if (error != null) {
        return;
    }

    boolean verified = result.verified();
});
```

Caller-provided request id:

```java
M3Mobile.INSTANCE.getInstance().getScannerButtonUi("my-request-id", (result, error) -> {
    if (error != null) {
        return;
    }

    String rawScanEmulStatus = result.rawStatus();
});
```

Check `result.transportStatus()` for SDK transport errors such as
`SCANEMUL_NOT_INSTALLED`, `FEATURE_NOT_AVAILABLE`, and `TIMEOUT`. Check
`result.status()` and `result.rawStatus()` for ScanEmul status.

## C#

```csharp
using M3Sdk.Xamarin;
using M3Sdk.Xamarin.ScanEmul;

var sdk = M3Mobile.Create(applicationContext);

var options = new ScannerButtonUiOptions(
    imagePath: "/sdcard/Download/ScanEmul_Floating_Button_Images/target.png",
    opacityPercent: 80,
    size: ScannerButtonUiSize.Large);

ScannerButtonUiVerificationResult verification =
    await sdk.SetAndVerifyScannerButtonUiAsync(options);

bool verified = verification.IsVerified;
ScannerButtonUiResult setResult = verification.SetResult;
```

Partial SET and default image:

```csharp
await sdk.SetScannerButtonUiAsync(ScannerButtonUiOptions.ForOpacityPercent(100));
await sdk.SetScannerButtonUiAsync(ScannerButtonUiOptions.ForImagePath(string.Empty));
```

Caller-provided request id:

```csharp
ScannerButtonUiResult result =
    await sdk.GetScannerButtonUiAsync("my-request-id");

string rawScanEmulStatus = result.RawStatus;
ScannerButtonUiTransportStatus transport = result.TransportStatus;
```
