# M3 SDK Android Compose sample

This is a standalone Kotlin and Jetpack Compose application. It intentionally does not include or
reference any local M3 SDK project module.

Build it only with an SDK version that is already available from JitPack:

```powershell
.\gradlew -p samples\android-compose-jitpack :app:assembleDebug "-Pm3SdkVersion=RELEASED_VERSION"
```

Replace `RELEASED_VERSION` with the released tag being verified. The build fails when `m3SdkVersion` is omitted
so that a developer cannot accidentally test local SDK sources.

`MainActivity` lists the SDK manual categories. `CategoryActivity` shows only the selected
category and its representative verification function. Scanner registration is active only while
the Scanner category is open.

Both screens apply the system safe-drawing insets, including the bottom navigation area.
Every SDK action clears text-field focus and hides the software keyboard before running so the
result is not covered.
Every executed operation writes `operation`, `attempt`, and `at` before its result. Repeating
the same API or changing its arguments therefore always produces a visibly new result. KeyTool
results include the requested key and function values.

The App screen exposes local and remote APK installation, same-version reinstall, and launch after
install. The result records only option values, not the full file path or URL. StartUp handles the
request asynchronously, so the screen reports `REQUEST_SENT_UNVERIFIED`; verify the StartUp
notification and logs, installed package, and launched screen.

The Wi-Fi screen exposes `Enable Wi-Fi` and `Disable Wi-Fi` buttons that call
`setWifiEnabled(true)` and `setWifiEnabled(false)`. Verify the device Wi-Fi state after calling
them on SM24 with StartUp 6.8.3 or later.

The `PROJECT_MEDIA` screen accepts any installed target package name and calls
`allowProjectMedia(packageName)`. It includes live presets for DroidVNC-NG, a deliberately missing
package, and an empty package. On SM24 these cover `SUCCESS`, `TARGET_NOT_INSTALLED`, and
`INVALID_TARGET`; updated StartUp builds on other models return `UNSUPPORTED_DEVICE`. A separate,
clearly labelled preview area renders the condition and recommended action for all seven statuses
without calling StartUp. `PERMISSION_DENIED`, `APP_OP_UNAVAILABLE`, and `APPLY_FAILED` should be
verified through unit tests and previews rather than by damaging a test device system configuration.

The application enables M3 SDK strict mode and shows the following for every operation:

- `SUCCESS` when a response or observable value is available.
- `REQUEST_SENT_UNVERIFIED` for one-way SDK broadcasts that provide no acknowledgement.
- `FAILED` with the exception type, message, device model, Android version, SDK dependency version,
  and installed companion-app versions.

The KeyTool screen defaults to `Left Scan` and `Volume Up`, demonstrates the SM24 three-argument
`setKeyFunction` overload that sends mapping and Wake-Up together, exposes left/right Scan Key
Wake-Up controls for SL20P and SM24, and exposes Home and Recent controls for SM24 and SM25. SM24
Wake-Up requires KeyTool 1.3.8 or later; 1.3.9 or later is recommended. Confirm changes with the
physical buttons and restore the required device state after verification.

Default UI resources are English. Only Korean localization is included under `values-ko`.
