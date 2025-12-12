package net.m3mobile.sdk.startup.api

import net.m3mobile.core.RequiresStartUp
import net.m3mobile.sdk.startup.params.QuickTile
import net.m3mobile.sdk.startup.params.QuickTileId

public interface QuickTileApi {

    /**
     * Sets quick tiles to the system UI.
     *
     * StartUp version `6.4.1` or later is required.
     *
     * @param quickTile The [QuickTile] to add
     */
    @RequiresStartUp("6.4.1")
    public fun setQuickTiles(vararg quickTile: QuickTile)

    /**
     * Resets the quick tiles configuration to the default state.
     *
     * StartUp version `6.4.1` or later is required.
     */
    @RequiresStartUp("6.4.1")
    public fun resetQuickTile()
}