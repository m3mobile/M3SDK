# M3Mobile.M3Sdk.Xamarin

Native C# Xamarin.Android library for Xamarin.Forms 4.6 Android projects. The NuGet package targets `MonoAndroid7.0` so Android heads targeting `MonoAndroid10.0` or lower can reference it. This module talks directly to M3 Mobile StartUp and ScanEmul apps with Android broadcasts, receivers, `Messenger`, `ServiceConnection`, `Settings.Global`, and USB state intents; it does not bind or reference the Kotlin SDK, AndroidX, Kotlin, coroutines, Java SDK artifacts, or AAR bindings.

## Usage

```csharp
using Android.App;
using M3Sdk.Xamarin;

IM3Sdk m3 = M3Mobile.Create(Application.Context);

m3.SetDateTime(DateTime.Now);
m3.SetDateTime(2026, 5, 19, 10, 30, 0);
m3.SetNtpServer("time.android.com");
m3.StartScan();
m3.SetScanResultPrefix("M3-");
```

Optional reads and callbacks:

```csharp
var prefix = await m3.GetScanResultPrefixAsync();

var request = m3.GetScannerStatus((status, error) =>
{
    if (error != null)
        return;

    // 1: open failed, 2: close failed, 4: open succeeded, 8: close succeeded.
});

request.Cancel();
```

Scan result listener:

```csharp
m3.RegisterOnScanResultListener(result =>
{
    var barcode = result.Barcode;
    var type = result.Type;
});
```

Grouped APIs are also available:

```csharp
using M3Sdk.Xamarin.ScanEmul;

m3.StartUp.SetWifiCountry("KR");
m3.ScanEmul.SetScannerReadMode(ReadMode.Async);
var ntp = m3.Time.GetNtpServer();
var modes = m3.Usb.GetCurrentUsbModes();
```

## Strict Mode

By default, this module matches the Kotlin SDK and does not enforce app version checks. To enable version and model guards, add this meta-data to the consuming Android app manifest:

```xml
<application>
  <meta-data android:name="M3_STRICT_MODE" android:value="true" />
</application>
```

For Android 11 package visibility, the NuGet package includes `build` and `buildTransitive` targets that add a library manifest containing queries for `com.m3.startup` and `net.m3mobile.app.scanemul` to consuming `MonoAndroid11.0` or newer builds. The targets intentionally skip `MonoAndroid10.0` and lower builds because `<queries>` is an Android 11 manifest element; those apps can still reference the package without the Android 11 manifest overlay.

## License

This package is licensed under the Apache License 2.0.
