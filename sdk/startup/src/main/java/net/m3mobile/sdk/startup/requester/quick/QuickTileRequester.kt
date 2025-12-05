package net.m3mobile.sdk.startup.requester.quick

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.FinishRequiredBroadcastRequester
import net.m3mobile.sdk.startup.constants.ExtraKey
import net.m3mobile.sdk.startup.constants.ExtraValue
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue
import org.json.JSONArray
import org.json.JSONObject
import net.m3mobile.sdk.startup.params.QuickTile

internal abstract class QuickTileRequester: FinishRequiredBroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.QUICK_TILE
}

internal class SetQuickTileRequester(
    override val context: Context,
    vararg quickTile: QuickTile
) : QuickTileRequester() {

    override val extras = bundleOf(
        ExtraKey.QUICK_TILE_ACTION to ExtraValue.ADD_QUICK_TILE,
        ExtraKey.QUICK_TILE_ITEMS to buildQuickTilesJsonString(*quickTile)
    )

    private fun buildQuickTilesJsonString(vararg quickTiles: QuickTile): String {
        return JSONArray().apply {
            quickTiles.forEach { (id, name) ->
                put(JSONObject().apply {
                    put(ExtraKey.QUICK_TILE_ID, id.value)
                    put(ExtraKey.QUICK_TILE_DISPLAY_NAME, name)
                })
            }
        }.toString()
    }
}

internal class ResetQuickTileRequester(override val context: Context) : QuickTileRequester() {

    override val extras = bundleOf(
        ExtraKey.QUICK_TILE_ACTION to ExtraValue.RESET_QUICK_TILE,
        ExtraKey.QUICK_TILE_ITEMS to ExtraValue.EMPTY_QUICK_TILE_ITEMS
    )
}