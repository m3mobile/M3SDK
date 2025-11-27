package net.m3mobile.sdk.startup.requester.device

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue
import net.m3mobile.sdk.startup.params.DisplaySetting

internal class SetDisplaySettingRequester(
    override val context: Context,
    displaySetting: DisplaySetting
): BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.DISPLAY
    override val extras = buildDisplaySettingExtras(displaySetting)

    private fun buildDisplaySettingExtras(displaySetting: DisplaySetting) = bundleOf(
        "display_auto_brightness" to displaySetting.enableAutoBrightness,
        "display_brightness_step" to displaySetting.brightness,
        "display_auto_rotate" to displaySetting.enableAutoRotate,
        "display_disable_screen_lock" to !displaySetting.enableScreenLock,
        "display_sleep" to displaySetting.sleepMode.value,
        "display_policy_control" to displaySetting.policyControl.value,
        "display_battery_percentage" to if (displaySetting.showBatteryPercentage) 1 else 2,
        "display_screensaver_mode" to displaySetting.screenSaverMode.value,
        "display_screensaver_component" to displaySetting.screenSaverComponent,
        "display_rotate_force" to displaySetting.rotateForce
    )
}