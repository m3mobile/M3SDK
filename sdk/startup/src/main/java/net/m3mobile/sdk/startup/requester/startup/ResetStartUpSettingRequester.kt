package net.m3mobile.sdk.startup.requester.startup

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.FinishRequiredBroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class ResetStartUpSettingRequester(override val context: Context): FinishRequiredBroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.OPTION
    override val extras = bundleOf("option_reset" to true)
}