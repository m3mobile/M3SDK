package net.m3mobile.samples.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import net.m3mobile.feature.scanemul.listener.OnScanResultListener
import net.m3mobile.feature.startup.params.QuickTile
import net.m3mobile.feature.startup.params.QuickTileId
import net.m3mobile.sdk.M3Mobile

private const val STARTUP_PACKAGE = "com.m3.startup"
private const val SCANEMUL_PACKAGE = "net.m3mobile.app.scanemul"
private const val KEYTOOL_SL20_PACKAGE = "com.m3.keytoolsl20"
private const val KEYTOOL_WAKE_UP_PACKAGE = "net.m3.keytool"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CategoryListScreen { category ->
                        startActivity(CategoryActivity.intent(this, category))
                    }
                }
            }
        }
    }
}

@Composable
internal fun CategoryScreen(category: SampleCategory) {
    val context = LocalContext.current
    val sdk = M3Mobile.instance
    val scope = rememberCoroutineScope()
    val results = remember { mutableStateMapOf<String, String>() }
    var packageName by remember { mutableStateOf(context.packageName) }
    var keyTitle by remember { mutableStateOf("Left Scan") }
    var functionTitle by remember { mutableStateOf("Volume Up") }
    val requestSentUnverified = stringResource(R.string.request_sent_unverified)
    val executionTrace = remember { ExecutionTrace() }

    fun record(key: String, operation: String, body: String) {
        results[key] = executionTrace.message(operation, body)
    }

    val scanListener = remember {
        OnScanResultListener { result ->
            scope.launch {
                record(
                    "scanner",
                    "scanResult",
                    "SUCCESS\nbarcode=${result.barcode}\ntype=${result.type}"
                )
            }
        }
    }

    DisposableEffect(category, sdk) {
        if (category == SampleCategory.SCANNER) {
            val body = try {
                sdk.registerOnScanResultListener(scanListener)
                "LISTENING"
            } catch (error: Throwable) {
                failure(context, error)
            }
            record("scanner", "registerOnScanResultListener", body)
            onDispose { runCatching { sdk.unregisterOnScanResultListener(scanListener) } }
        } else {
            onDispose {}
        }
    }

    fun oneWay(
        key: String,
        operation: String,
        action: () -> Unit
    ) {
        val body = try {
            action()
            requestSentUnverified
        } catch (error: Throwable) {
            failure(context, error)
        }
        record(key, operation, body)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(category.titleRes), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.screen_subtitle, BuildConfig.M3_SDK_VERSION))

        SampleCard(
            category = SampleCategory.DEVICE_INFO,
            selectedCategory = category,
            title = stringResource(R.string.device_info),
            result = results["info"] ?: environment(context)
        ) {
            SdkActionButton(onClick = { record("info", "environment", environment(context)) }) {
                Text(stringResource(R.string.refresh))
            }
        }

        SampleCard(SampleCategory.AIRPLANE_MODE, category, stringResource(R.string.airplane_mode), results["airplane"]) {
            ActionRow(
                first = stringResource(R.string.turn_on) to {
                    oneWay("airplane", "turnOnAirplaneMode") { sdk.turnOnAirplaneMode() }
                    results["airplane"] = results["airplane"] +
                        "\nobserved=${Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1}"
                },
                second = stringResource(R.string.turn_off) to {
                    oneWay("airplane", "turnOffAirplaneMode") { sdk.turnOffAirplaneMode() }
                    results["airplane"] = results["airplane"] +
                        "\nobserved=${Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1}"
                }
            )
        }

        SampleCard(SampleCategory.APPLICATION, category, stringResource(R.string.application), results["app"]) {
            OutlinedTextField(
                value = packageName,
                onValueChange = { packageName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.package_name)) },
                singleLine = true
            )
            SdkActionButton(onClick = { oneWay("app", "runApp(package=$packageName)") { sdk.runApp(packageName) } }) {
                Text(stringResource(R.string.run_application))
            }
        }

        SampleCard(SampleCategory.DEVICE, category, stringResource(R.string.device), results["device"]) {
            SdkActionButton(onClick = {
                scope.launch {
                    val body = try {
                        "SUCCESS\n${sdk.getSerialNumber()}"
                    } catch (error: Throwable) {
                        failure(context, error)
                    }
                    record("device", "getSerialNumber", body)
                }
            }) { Text(stringResource(R.string.get_serial)) }
        }

        SampleCard(SampleCategory.LANGUAGE, category, stringResource(R.string.language), results["language"]) {
            ActionRow(
                first = stringResource(R.string.english) to { oneWay("language", "setLanguage(en, US)") { sdk.setLanguage("en", "US") } },
                second = stringResource(R.string.korean) to { oneWay("language", "setLanguage(ko, KR)") { sdk.setLanguage("ko", "KR") } }
            )
        }

        SampleCard(SampleCategory.NETWORK, category, stringResource(R.string.network), results["network"]) {
            ActionRow(
                first = stringResource(R.string.enable_nfc) to {
                    oneWay("network", "enableNfc") { sdk.enableNfc() }
                    results["network"] = results["network"] + "\nobserved=${NfcAdapter.getDefaultAdapter(context)?.isEnabled}"
                },
                second = stringResource(R.string.disable_nfc) to {
                    oneWay("network", "disableNfc") { sdk.disableNfc() }
                    results["network"] = results["network"] + "\nobserved=${NfcAdapter.getDefaultAdapter(context)?.isEnabled}"
                }
            )
        }

        SampleCard(SampleCategory.PERMISSION, category, stringResource(R.string.permission), results["permission"]) {
            SdkActionButton(onClick = {
                oneWay("permission", "grantPermission(package=${context.packageName}, CAMERA)") {
                    sdk.grantPermission(context.packageName, Manifest.permission.CAMERA)
                }
                val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
                results["permission"] = results["permission"] + "\nobserved=$granted"
            }) { Text(stringResource(R.string.grant_camera)) }
        }

        SampleCard(SampleCategory.QUICK_TILE, category, stringResource(R.string.quick_tile), results["quickTile"]) {
            SdkActionButton(onClick = {
                oneWay("quickTile", "setQuickTiles(id=WIFI, title=Wi-Fi)") {
                    sdk.setQuickTiles(QuickTile(QuickTileId.WIFI, "Wi-Fi"))
                }
            }) { Text(stringResource(R.string.set_quick_tile)) }
        }

        SampleCard(SampleCategory.SCANNER, category, stringResource(R.string.scanner), results["scanner"])

        SampleCard(SampleCategory.STARTUP_SETTING, category, stringResource(R.string.startup_setting), results["startupSetting"]) {
            SdkActionButton(onClick = { oneWay("startupSetting", "resetStartUpSetting") { sdk.resetStartUpSetting() } }) {
                Text(stringResource(R.string.reset_startup))
            }
        }

        SampleCard(SampleCategory.TIME, category, stringResource(R.string.time), results["time"]) {
            SdkActionButton(onClick = {
                val body = try {
                    "SUCCESS\n${sdk.getTimeZone()}"
                } catch (error: Throwable) {
                    failure(context, error)
                }
                record("time", "getTimeZone", body)
            }) { Text(stringResource(R.string.get_timezone)) }
        }

        SampleCard(SampleCategory.USB, category, stringResource(R.string.usb), results["usb"]) {
            SdkActionButton(onClick = {
                val body = try {
                    "SUCCESS\n${sdk.getCurrentUsbModes().joinToString()}"
                } catch (error: Throwable) {
                    failure(context, error)
                }
                record("usb", "getCurrentUsbModes", body)
            }) { Text(stringResource(R.string.get_usb_modes)) }
        }

        SampleCard(SampleCategory.WIFI, category, stringResource(R.string.wifi), results["wifi"]) {
            SdkActionButton(onClick = {
                scope.launch {
                    val body = try {
                        val result = sdk.getFactoryWifiMac()
                        if (result.success) {
                            "SUCCESS\n${result.macAddress}"
                        } else {
                            failure(context, IllegalStateException("StartUp error=${result.errorMessage}"))
                        }
                    } catch (error: Throwable) {
                        failure(context, error)
                    }
                    record("wifi", "getFactoryWifiMac", body)
                }
            }) { Text(stringResource(R.string.get_factory_wifi_mac)) }
        }

        SampleCard(SampleCategory.KEYTOOL, category, stringResource(R.string.keytool), results["keytool"]) {
            Text(stringResource(R.string.keytool_warning))
            Text(
                stringResource(R.string.key_mapping),
                style = MaterialTheme.typography.titleMedium
            )
            Text(stringResource(R.string.key_mapping_description))
            OutlinedTextField(
                value = keyTitle,
                onValueChange = { keyTitle = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.key_title)) },
                singleLine = true
            )
            OutlinedTextField(
                value = functionTitle,
                onValueChange = { functionTitle = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.function_title)) },
                singleLine = true
            )
            SdkActionButton(onClick = {
                oneWay("keytool", "setKeyFunction(key=$keyTitle, function=$functionTitle)") {
                    sdk.setKeyFunction(keyTitle, functionTitle)
                }
            }) {
                Text(stringResource(R.string.set_key_function))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.navigation_buttons),
                style = MaterialTheme.typography.titleMedium
            )
            Text(stringResource(R.string.navigation_buttons_description))
            Text(stringResource(R.string.home_button), style = MaterialTheme.typography.titleSmall)
            ActionRow(
                first = stringResource(R.string.enable) to {
                    oneWay("keytool", "enableHomeButton") { sdk.enableHomeButton() }
                },
                second = stringResource(R.string.disable) to {
                    oneWay("keytool", "disableHomeButton") { sdk.disableHomeButton() }
                }
            )
            Text(stringResource(R.string.recent_button), style = MaterialTheme.typography.titleSmall)
            ActionRow(
                first = stringResource(R.string.enable) to {
                    oneWay("keytool", "enableRecentButton") { sdk.enableRecentButton() }
                },
                second = stringResource(R.string.disable) to {
                    oneWay("keytool", "disableRecentButton") { sdk.disableRecentButton() }
                }
            )
        }
    }
}

@Composable
private fun SampleCard(
    category: SampleCategory,
    selectedCategory: SampleCategory,
    title: String,
    result: String? = null,
    content: @Composable () -> Unit = {}
) {
    if (category != selectedCategory) return
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            content()
            Spacer(Modifier.height(2.dp))
            Text(result ?: stringResource(R.string.not_run), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ActionRow(
    first: Pair<String, () -> Unit>,
    second: Pair<String, () -> Unit>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SdkActionButton(onClick = first.second, modifier = Modifier.weight(1f)) {
            Text(first.first)
        }
        SdkActionButton(onClick = second.second, modifier = Modifier.weight(1f)) {
            Text(second.first)
        }
    }
}

@Composable
private fun SdkActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val focus = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    Button(
        onClick = {
            focus.clearFocus(force = true)
            keyboard?.hide()
            onClick()
        },
        modifier = modifier,
        content = content
    )
}

private fun environment(context: Context): String = buildString {
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    appendLine("model=${Build.MODEL}")
    appendLine("android=${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
    appendLine("sample=${packageInfo.versionName}")
    appendLine("m3sdk=${BuildConfig.M3_SDK_VERSION}")
    appendLine("StartUp=${packageVersion(context, STARTUP_PACKAGE)}")
    appendLine("ScanEmul=${packageVersion(context, SCANEMUL_PACKAGE)}")
    appendLine("KeyTool SL20=${packageVersion(context, KEYTOOL_SL20_PACKAGE)}")
    append("KeyTool Wake-Up=${packageVersion(context, KEYTOOL_WAKE_UP_PACKAGE)}")
}

private fun failure(context: Context, error: Throwable): String = buildString {
    appendLine("FAILED")
    appendLine("type=${error::class.java.name}")
    appendLine("message=${error.message ?: "No message"}")
    appendLine("model=${Build.MODEL}")
    appendLine("android=${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
    appendLine("m3sdk=${BuildConfig.M3_SDK_VERSION}")
    appendLine("StartUp=${packageVersion(context, STARTUP_PACKAGE)}")
    appendLine("ScanEmul=${packageVersion(context, SCANEMUL_PACKAGE)}")
    appendLine("KeyTool SL20=${packageVersion(context, KEYTOOL_SL20_PACKAGE)}")
    append("KeyTool Wake-Up=${packageVersion(context, KEYTOOL_WAKE_UP_PACKAGE)}")
}

private fun packageVersion(context: Context, packageName: String): String = try {
    val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        @Suppress("DEPRECATION")
        context.packageManager.getPackageInfo(packageName, 0)
    }
    info.versionName ?: "installed (version unavailable)"
} catch (_: PackageManager.NameNotFoundException) {
    "NOT_INSTALLED_OR_NOT_VISIBLE"
}
