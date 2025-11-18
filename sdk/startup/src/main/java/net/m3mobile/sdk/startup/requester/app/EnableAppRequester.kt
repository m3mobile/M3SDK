package net.m3mobile.sdk.startup.requester.app

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal abstract class SetAppStateRequester: BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.APPLICATION
}

internal class EnableAppRequester(
    override val context: Context,
    packageName: String
) : SetAppStateRequester() {

    override val extras = bundleOf(
        "package_name" to packageName,
        "enable" to true
    )
}

internal class DisableAppRequester(
    override val context: Context,
    packageName: String
) : SetAppStateRequester() {

    override val extras = bundleOf(
        "package_name" to packageName,
        "enable" to false
    )
}