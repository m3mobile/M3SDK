package net.m3mobile.feature.startup.requester.language

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal class SetLanguageRequester(
    override val context: Context,
    language: String,
    country: String
): BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.LANGUAGE
    override val extras = bundleOf(ExtraKey.LANGUAGE_TAG to "$language-$country")
}