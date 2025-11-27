package net.m3mobile.sdk.startup.api

import net.m3mobile.sdk.startup.params.QuickTile
import net.m3mobile.sdk.startup.params.QuickTileId

interface QuickTileApi {

    /**
     * Adds quick tiles to the system UI.
     *
     * @param quickTile The [QuickTile] to add
     */
    fun addQuickTiles(vararg quickTile: QuickTile)

    /**
     * Removes quick tiles from the system UI.
     *
     * @param quickTileId The [QuickTileId]s to be removed
     */
    fun removeQuickTiles(vararg quickTileId: QuickTileId)

    /**
     * Resets the quick tiles configuration to the default state.
     */
    fun resetQuickTile()
}