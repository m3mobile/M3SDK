package net.m3mobile.sdk.startup.api

import net.m3mobile.sdk.startup.params.QuickTile
import net.m3mobile.sdk.startup.params.QuickTileId

interface QuickTileApi {

    /**
     * Sets quick tiles to the system UI.
     *
     * @param quickTile The [QuickTile] to add
     */
    fun setQuickTiles(vararg quickTile: QuickTile)

    /**
     * Resets the quick tiles configuration to the default state.
     */
    fun resetQuickTile()
}