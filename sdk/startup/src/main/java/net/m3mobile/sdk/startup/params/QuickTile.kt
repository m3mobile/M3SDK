package net.m3mobile.sdk.startup.params

/**
 * Represents the configuration for a Quick Settings Tile.
 *
 * @param id A unique identifier for the tile
 * @param name The display name of the tile to be shown in the Quick Settings UI
 * @param position The index for the tile's placement within the panel
 * @see QuickTileId
 */
data class QuickTile(
    val id: QuickTileId,
    val name: String,
    val position: Int
)

enum class QuickTileId(internal val value: String) {
    WIFI("wifi"),
    BLUETOOTH("bluetooth"),
    AIRPLANE_MODE("airplane_mode"),
    BATTERY_SAVER("battery_saver"),
    LOCATION("location"),
    DO_NOT_DISTURB("do_not_disturb"),
    BRIGHTNESS("screen_brightness"),
    SOUND("sound"),
    ROTATION("screen_rotation"),
    FLASHLIGHT("flashlight"),
    NFC("nfc"),
    HOTSPOT("hotspot"),
    CAST("cast"),
    NIGHT_LIGHT("night_light")
}