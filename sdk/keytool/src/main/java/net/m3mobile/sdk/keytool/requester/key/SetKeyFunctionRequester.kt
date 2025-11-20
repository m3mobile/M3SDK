package net.m3mobile.sdk.keytool.requester.key

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.keytool.constants.RequestAction

internal class SetKeyFunctionRequester(
    override val context: Context,
    key: String,
    function: String
): BroadcastRequester() {

    override val requestAction = RequestAction.ACTION_SET_KEY
    override val extras = bundleOf("key_title" to key, "key_function" to function)
}