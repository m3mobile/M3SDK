package net.m3mobile.sdk.startup.requester.airplane

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

class TurnOnAirplaneModeRequester(override val context: Context): BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.AIRPLANE
    override val extras = bundleOf(TypeValue.AIRPLANE to true)
}