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

The application enables M3 SDK strict mode and shows the following for every operation:

- `SUCCESS` when a response or observable value is available.
- `REQUEST_SENT_UNVERIFIED` for one-way SDK broadcasts that provide no acknowledgement.
- `FAILED` with the exception type, message, device model, Android version, SDK dependency version,
  and installed companion-app versions.

The KeyTool screen defaults to `Left Scan` and `Volume Up`, and separately exposes Home and Recent
enable/disable controls for SM24 and SM25. KeyTool `Set Key Function` and navigation controls change
device configuration. Confirm the values and restore the required device state after verification.

Default UI resources are English. Only Korean localization is included under `values-ko`.
