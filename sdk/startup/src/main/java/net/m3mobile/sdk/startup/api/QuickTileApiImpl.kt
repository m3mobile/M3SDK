package net.m3mobile.sdk.startup.api

import android.content.Context
import net.m3mobile.sdk.startup.params.QuickTile
import net.m3mobile.sdk.startup.requester.quick.SetQuickTileRequester
import net.m3mobile.sdk.startup.requester.quick.ResetQuickTileRequester

internal class QuickTileApiImpl(private val context: Context): QuickTileApi {

    override fun setQuickTiles(vararg quickTile: QuickTile) {
        SetQuickTileRequester(context, *quickTile).request()
    }

    override fun resetQuickTile() {
        ResetQuickTileRequester(context).request()
    }
}