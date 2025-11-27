package net.m3mobile.sdk.startup.requester.quick

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.FinishRequiredBroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue
import org.json.JSONArray
import org.json.JSONObject
import net.m3mobile.sdk.startup.params.QuickTile
import net.m3mobile.sdk.startup.params.QuickTileId

internal abstract class QuickTileRequester: FinishRequiredBroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.QUICK_TILE
}

internal class AddQuickTileRequester(
    override val context: Context,
    vararg quickTile: QuickTile
) : QuickTileRequester() {

    override val extras = bundleOf(
        "quick_tile_action" to "add",
        "quick_tile_items" to buildQuickTilesJsonString(*quickTile)
    )

    private fun buildQuickTilesJsonString(vararg quickTiles: QuickTile): String {
        return JSONArray().apply {
            quickTiles.forEach { (id, name, position) ->
                put(JSONObject().apply {
                    put("tile_id", id.value)
                    put("tile_name", name)
                    put("position", position)
                })
            }
        }.toString()
    }
}

internal class RemoveQuickTileRequester(
    override val context: Context,
    vararg quickTileId: QuickTileId
) : QuickTileRequester() {

    override val extras = bundleOf(
        "quick_tile_action" to "remove",
        "quick_tile_items" to buildQuickTileIdsJsonString(*quickTileId)
    )

    private fun buildQuickTileIdsJsonString(vararg quickTileId: QuickTileId): String {
        return JSONArray().apply {
            quickTileId.forEach { id ->
                put(JSONObject().apply {
                    put("tile_id", id.value)
                })
            }
        }.toString()
    }
}

internal class ResetQuickTileRequester(override val context: Context) : QuickTileRequester() {

    override val extras = bundleOf(
        "quick_tile_action" to "reset",
        "quick_tile_items" to "[]"
    )
}