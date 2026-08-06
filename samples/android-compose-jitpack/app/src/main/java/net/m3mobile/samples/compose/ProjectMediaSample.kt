package net.m3mobile.samples.compose

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.m3mobile.feature.startup.params.ProjectMediaResult
import net.m3mobile.feature.startup.params.ProjectMediaStatus
import net.m3mobile.sdk.M3Mobile

private const val DROID_VNC_PACKAGE = "net.christianbeier.droidvnc_ng"
private const val MISSING_PACKAGE = "net.m3mobile.missing.projectmedia"

@Composable
internal fun ProjectMediaSample(record: (operation: String, body: String) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sdk = M3Mobile.instance
    var packageName by remember { mutableStateOf(DROID_VNC_PACKAGE) }
    var running by remember { mutableStateOf(false) }

    fun request(operation: String, targetPackage: String) {
        if (running) return
        scope.launch {
            running = true
            val body = try {
                projectMediaText(
                    context,
                    context.getString(R.string.project_media_live_result),
                    targetPackage,
                    sdk.allowProjectMedia(targetPackage),
                )
            } catch (error: Throwable) {
                buildString {
                    appendLine(context.getString(R.string.project_media_transport_failure))
                    appendLine("package=$targetPackage")
                    appendLine("type=${error::class.java.name}")
                    append("message=${error.message ?: "No message"}")
                }
            }
            record(operation, body)
            running = false
        }
    }

    Text(stringResource(R.string.project_media_description))
    Text(stringResource(R.string.project_media_requirement))
    OutlinedTextField(
        value = packageName,
        onValueChange = { packageName = it },
        modifier = Modifier.fillMaxWidth(),
        enabled = !running,
        label = { Text(stringResource(R.string.project_media_target_package)) },
        singleLine = true,
    )
    SdkActionButton(
        onClick = { request("allowProjectMedia(package=$packageName)", packageName) },
        modifier = Modifier.fillMaxWidth(),
        enabled = !running,
    ) {
        Text(stringResource(R.string.project_media_run_entered))
    }
    ActionRow(
        first = stringResource(R.string.project_media_test_droid_vnc) to {
            packageName = DROID_VNC_PACKAGE
            request("allowProjectMedia(DroidVNC)", DROID_VNC_PACKAGE)
        },
        second = stringResource(R.string.project_media_test_missing) to {
            packageName = MISSING_PACKAGE
            request("allowProjectMedia(missingPackage)", MISSING_PACKAGE)
        },
    )
    SdkActionButton(
        onClick = {
            packageName = ""
            request("allowProjectMedia(blankPackage)", "")
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !running,
    ) {
        Text(stringResource(R.string.project_media_test_invalid))
    }

    Text(stringResource(R.string.project_media_status_preview_title))
    Text(stringResource(R.string.project_media_status_preview_description))
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        ProjectMediaStatus.entries.forEach { status ->
            val content = status.content()
            SdkActionButton(
                onClick = {
                    record(
                        "previewProjectMediaStatus(${status.name})",
                        projectMediaText(
                            context,
                            context.getString(R.string.project_media_preview_result),
                            packageName,
                            ProjectMediaResult(
                                status,
                                if (status == ProjectMediaStatus.SUCCESS) "" else
                                    context.getString(content.conditionRes),
                            ),
                        ),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !running,
            ) {
                Text("${status.name} (${status.code})")
            }
        }
    }
}

private fun projectMediaText(
    context: android.content.Context,
    source: String,
    packageName: String,
    result: ProjectMediaResult,
): String {
    val content = result.status.content()
    return buildString {
        appendLine(source)
        appendLine("status=${result.status.name}")
        appendLine("code=${result.status.code}")
        appendLine("successful=${result.successful}")
        appendLine("package=${packageName.ifEmpty { "<empty>" }}")
        appendLine("errorMessage=${result.errorMessage.ifEmpty { "<empty>" }}")
        appendLine("condition=${context.getString(content.conditionRes)}")
        appendLine("nextAction=${context.getString(content.actionRes)}")
        append("device=${Build.MODEL}")
    }
}

internal data class ProjectMediaStatusContent(
    @StringRes val conditionRes: Int,
    @StringRes val actionRes: Int,
)

internal fun ProjectMediaStatus.content(): ProjectMediaStatusContent = when (this) {
    ProjectMediaStatus.SUCCESS -> ProjectMediaStatusContent(
        R.string.project_media_success_condition,
        R.string.project_media_success_action,
    )
    ProjectMediaStatus.UNSUPPORTED_DEVICE -> ProjectMediaStatusContent(
        R.string.project_media_unsupported_condition,
        R.string.project_media_unsupported_action,
    )
    ProjectMediaStatus.TARGET_NOT_INSTALLED -> ProjectMediaStatusContent(
        R.string.project_media_not_installed_condition,
        R.string.project_media_not_installed_action,
    )
    ProjectMediaStatus.INVALID_TARGET -> ProjectMediaStatusContent(
        R.string.project_media_invalid_condition,
        R.string.project_media_invalid_action,
    )
    ProjectMediaStatus.PERMISSION_DENIED -> ProjectMediaStatusContent(
        R.string.project_media_permission_condition,
        R.string.project_media_permission_action,
    )
    ProjectMediaStatus.APP_OP_UNAVAILABLE -> ProjectMediaStatusContent(
        R.string.project_media_app_op_condition,
        R.string.project_media_app_op_action,
    )
    ProjectMediaStatus.APPLY_FAILED -> ProjectMediaStatusContent(
        R.string.project_media_apply_condition,
        R.string.project_media_apply_action,
    )
}
