package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.params.QuickTile
import net.m3mobile.feature.startup.requester.quick.SetQuickTileRequester
import net.m3mobile.feature.startup.requester.quick.ResetQuickTileRequester

internal class StartUpQuickTileApiImpl(private val context: Context): StartUpQuickTileApi {

    override fun setQuickTiles(vararg quickTile: QuickTile) {
        SetQuickTileRequester(context, *quickTile).request()
    }

    override fun resetQuickTile() {
        ResetQuickTileRequester(context).request()
    }
}