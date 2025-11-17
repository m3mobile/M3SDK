package net.m3mobile.sdk.startup.requester.time

import android.content.Context
import android.os.Bundle
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class SetDateTimeRequester(
    override val context: Context,
    override val extras: Bundle
): BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.DATETIME
}