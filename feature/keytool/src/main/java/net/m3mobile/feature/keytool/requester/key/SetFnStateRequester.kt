package net.m3mobile.feature.keytool.requester.key

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.keytool.constants.RequestAction

internal abstract class SetFnStateRequester: BroadcastRequester() {

    override val requestAction = RequestAction.CONTROL_FN
    override val extras = bundleOf("fn_state" to state)
    protected abstract val state: Int
}

internal class DisableFnRequester(override val context: Context): SetFnStateRequester() {

    override val state = 0
}

internal class EnableFnRequester(override val context: Context): SetFnStateRequester() {

    override val state = 1
}

internal class LockFnRequester(override val context: Context): SetFnStateRequester() {

    override val state = 2
}