# M3 SDK .NET for Android sample

This is a standalone native .NET for Android application targeting `net10.0-android`. Xamarin is
out of support; the sample uses the current SDK-style .NET Android project format while consuming
the package published to NuGet Gallery. The APK targets `android-arm64`, which matches the supported
M3 devices used for release verification.

Build it only with an SDK version that is already available from NuGet Gallery:

```powershell
dotnet build samples\dotnet-android-nuget\M3SdkPublishedSample.csproj -p:M3SdkVersion=RELEASED_VERSION
```

Replace `RELEASED_VERSION` with the released package being verified. Restore fails with an explicit message
when `M3SdkVersion` is omitted.

For local SDK verification before the NuGet package is published, build with the local project reference:

```powershell
dotnet build samples\dotnet-android-nuget\M3SdkPublishedSample.csproj -p:UseLocalM3Sdk=true -p:TargetFramework=net9.0-android --source https://api.nuget.org/v3/index.json
```

`EmbedAssembliesIntoApk` is enabled so that the generated Debug APK is standalone and can be
installed with `adb install` without the .NET Fast Deployment directory.

`MainActivity` lists the SDK manual categories. `CategoryActivity` initializes the SDK and shows
only the selected category and its representative verification function. Scanner registration is
active only while the Scanner category is open.

Both screens apply the current system-bar insets, including the bottom navigation area.
Every SDK action clears text-field focus and hides the software keyboard before running. The result
is kept after the controls and scrolled into view after the action finishes.
Every executed operation writes `operation`, `attempt`, and `at` before its result. Repeating
the same API or changing its arguments therefore always produces a visibly new result. KeyTool
results include the requested key and function values.

The App screen exposes local and remote APK installation, same-version reinstall, and launch after
install. The result records only option values, not the full file path or URL. StartUp handles the
request asynchronously, so the screen reports `REQUEST_SENT_UNVERIFIED`; verify the StartUp
notification and logs, installed package, and launched screen.

The Wi-Fi screen exposes `Enable Wi-Fi` and `Disable Wi-Fi` buttons that call
`SetWifiEnabled(true)` and `SetWifiEnabled(false)`. Verify the device Wi-Fi state after calling
them on SM24 with StartUp 6.8.3 or later.

The `PROJECT_MEDIA` screen accepts any installed target package name and calls
`AllowProjectMediaAsync(packageName)`. It includes live presets for DroidVNC-NG, a deliberately
missing package, and an empty package. On SM24 these cover `Success`, `TargetNotInstalled`, and
`InvalidTarget`; updated StartUp builds on other models return `UnsupportedDevice`. A separate,
clearly labelled preview area renders the condition and recommended action for all seven statuses
without calling StartUp. `PermissionDenied`, `AppOpUnavailable`, and `ApplyFailed` should be
verified through unit tests and previews rather than by damaging a test device system configuration.

The application enables M3 SDK strict mode and shows:

- `SUCCESS` for APIs with a response or observable value.
- `REQUEST_SENT_UNVERIFIED` for one-way broadcasts with no acknowledgement.
- `FAILED` with exception, device, Android, SDK package, and companion-app version details.

The KeyTool screen defaults to `Left Scan` and `Volume Up`, demonstrates the SM24 three-argument
`SetKeyFunction` overload that sends mapping and Wake-Up together, exposes left/right Scan Key
Wake-Up controls for SL20P and SM24, and exposes Home and Recent controls for SM24 and SM25. SM24
Wake-Up requires KeyTool 1.3.8 or later; 1.3.9 or later is recommended. Confirm changes with the
physical buttons and restore the required device state after verification.

Default UI resources are English. Only Korean localization is included under `Resources/values-ko`.
