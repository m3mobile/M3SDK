package net.m3mobile.sdk.startup.requester.device

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.ExtraKey
import net.m3mobile.sdk.startup.constants.ExtraValue
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue
import net.m3mobile.sdk.startup.params.DisplaySetting

internal class SetDisplaySettingRequester(
    override val context: Context,
    displaySetting: DisplaySetting
): BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.DISPLAY
    override val extras = buildDisplaySettingExtras(displaySetting)

    private fun buildDisplaySettingExtras(displaySetting: DisplaySetting) = bundleOf(
        ExtraKey.SET_DISPLAY_SETTING_AUTO_BRIGHTNESS to displaySetting.enableAutoBrightness,
        ExtraKey.SET_DISPLAY_SETTING_BRIGHTNESS_STEP to displaySetting.brightness,
        ExtraKey.SET_DISPLAY_SETTING_AUTO_ROTATE to displaySetting.enableAutoRotate,
        ExtraKey.SET_DISPLAY_SETTING_DISABLE_SCREEN_LOCK to !displaySetting.enableScreenLock,
        ExtraKey.SET_DISPLAY_SETTING_SLEEP to displaySetting.sleepMode.value,
        ExtraKey.SET_DISPLAY_SETTING_POLICY_CONTROL to displaySetting.policyControl.value,
        ExtraKey.SET_DISPLAY_SETTING_BATTERY_PERCENT to
                if (displaySetting.showBatteryPercentage) ExtraValue.SHOW_BATTERY_PERCENT else ExtraValue.NOT_SHOW_BATTERY_PERCENT,
        ExtraKey.SET_DISPLAY_SETTING_SCREENSAVER_MODE to displaySetting.screenSaverMode.value,
        ExtraKey.SET_DISPLAY_SETTING_SCREENSAVER_COMPONENT to displaySetting.screenSaverComponent,
        ExtraKey.SET_DISPLAY_SETTING_ROTATE_FORCE to displaySetting.rotateForce
    )
}