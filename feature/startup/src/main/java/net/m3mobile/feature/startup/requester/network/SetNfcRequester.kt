package net.m3mobile.feature.startup.requester.network

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal abstract class SetNfcRequester(): BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.NFC
    override val extras get() = bundleOf(ExtraKey.ENABLE_NFC to enable)
    protected abstract val enable: Boolean
}

internal class EnableNfcRequester(override val context: Context): SetNfcRequester() {

    override val enable = true
}

internal class DisableNfcRequester(override val context: Context): SetNfcRequester() {

    override val enable = false
}