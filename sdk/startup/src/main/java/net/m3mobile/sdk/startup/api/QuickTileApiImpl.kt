package net.m3mobile.sdk.startup.api

import android.content.Context
import net.m3mobile.sdk.startup.requester.quick.AddQuickTileRequester
import net.m3mobile.sdk.startup.params.QuickTile
import net.m3mobile.sdk.startup.params.QuickTileId
import net.m3mobile.sdk.startup.requester.quick.RemoveQuickTileRequester
import net.m3mobile.sdk.startup.requester.quick.ResetQuickTileRequester

class QuickTileApiImpl(private val context: Context): QuickTileApi {

    override fun addQuickTiles(vararg quickTile: QuickTile) {
        AddQuickTileRequester(context, *quickTile).request()
    }

    override fun removeQuickTiles(vararg quickTileId: QuickTileId) {
        RemoveQuickTileRequester(context, *quickTileId).request()
    }

    override fun resetQuickTile() {
        ResetQuickTileRequester(context).request()
    }
}