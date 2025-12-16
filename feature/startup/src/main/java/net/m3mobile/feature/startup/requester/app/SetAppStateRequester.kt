package net.m3mobile.feature.startup.requester.app

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

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
        ExtraKey.SET_APP_STATE_PACKAGE_NAME to packageName,
        ExtraKey.SET_APP_STATE to true
    )
}

internal class DisableAppRequester(
    override val context: Context,
    packageName: String
) : SetAppStateRequester() {

    override val extras = bundleOf(
        ExtraKey.SET_APP_STATE_PACKAGE_NAME to packageName,
        ExtraKey.SET_APP_STATE to false
    )
}