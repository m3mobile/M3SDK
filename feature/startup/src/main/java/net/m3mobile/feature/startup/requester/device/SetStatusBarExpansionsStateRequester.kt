package net.m3mobile.feature.startup.requester.device

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal abstract class SetStatusBarExpansionsStateRequester: BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.STATUS_BAR
    override val extras get() = bundleOf(ExtraKey.LOCK_STATUS_BAR_EXPANSIONS to locks)
    protected abstract val locks: Boolean
}

internal class LockStatusBarExpansionRequester(
    override val context: Context
): SetStatusBarExpansionsStateRequester() {

    override val locks = true
}

internal class UnlockStatusBarExpansionRequester(
    override val context: Context
): SetStatusBarExpansionsStateRequester() {

    override val locks = false
}