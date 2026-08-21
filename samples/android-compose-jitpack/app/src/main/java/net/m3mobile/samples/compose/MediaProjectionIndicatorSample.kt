package net.m3mobile.samples.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import net.m3mobile.sdk.M3Mobile

private const val DROID_VNC_PACKAGE = "net.christianbeier.droidvnc_ng"

@Composable
internal fun MediaProjectionIndicatorSample(record: (operation: String, body: String) -> Unit) {
    val context = LocalContext.current
    val sdk = M3Mobile.instance
    var packageNamesText by remember { mutableStateOf(DROID_VNC_PACKAGE) }

    fun request(operation: String, action: () -> Unit) {
        val body = try {
            action()
            context.getString(R.string.request_sent_unverified)
        } catch (error: Throwable) {
            buildString {
                appendLine("FAILED")
                appendLine("type=${error::class.java.name}")
                append("message=${error.message ?: "No message"}")
            }
        }
        record(operation, body)
    }

    fun packageNames(): Array<String> = packageNamesText
        .split(',')
        .map(String::trim)
        .filter(String::isNotEmpty)
        .toTypedArray()

    Text(stringResource(R.string.media_projection_indicator_description))
    Text(stringResource(R.string.media_projection_indicator_requirement))
    OutlinedTextField(
        value = packageNamesText,
        onValueChange = { packageNamesText = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.media_projection_indicator_packages)) },
        supportingText = {
            Text(stringResource(R.string.media_projection_indicator_packages_hint))
        },
        singleLine = true,
    )
    ActionRow(
        first = stringResource(R.string.replace_packages) to {
            val packages = packageNames()
            request("setMediaProjectionIndicatorExemptPackages(${packages.joinToString()})") {
                sdk.setMediaProjectionIndicatorExemptPackages(*packages)
            }
        },
        second = stringResource(R.string.add_packages) to {
            val packages = packageNames()
            request("addMediaProjectionIndicatorExemptPackages(${packages.joinToString()})") {
                sdk.addMediaProjectionIndicatorExemptPackages(*packages)
            }
        },
    )
    ActionRow(
        first = stringResource(R.string.remove_packages) to {
            val packages = packageNames()
            request("removeMediaProjectionIndicatorExemptPackages(${packages.joinToString()})") {
                sdk.removeMediaProjectionIndicatorExemptPackages(*packages)
            }
        },
        second = stringResource(R.string.clear_packages) to {
            request("clearMediaProjectionIndicatorExemptPackages") {
                sdk.clearMediaProjectionIndicatorExemptPackages()
            }
        },
    )
}
