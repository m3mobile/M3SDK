package net.m3mobile.feature.startup.params

/**
 * Represents the configuration for a Quick Settings Tile.
 *
 * @property id A unique identifier for the tile
 * @property name The display name of the tile to be shown in the Quick Settings UI
 * @see QuickTileId
 */
public data class QuickTile(
    val id: QuickTileId,
    val name: String,
)

public enum class QuickTileId(internal val value: String) {
    WIFI("wifi"),
    BLUETOOTH("bt"),
    FLASHLIGHT("flashlight"),
    DO_NOT_DISTURB("dnd"),
    AUTO_ROTATION("rotation"),
    BATTERY_SAVER("battery"),
    AIRPLANE_MODE("airplane"),
    NIGHT_LIGHT("night"),
    SCREEN_RECORD("screenrecord"),
    QR_CODE_SCANNER("qr_code_scanner"),
    ALARM("alarm"),
    DEVICE_CONTROLS("controls"),
    WALLET("wallet"),
    SCREEN_CAST("cast"),
    LOCATION("location"),
    HOTSPOT("hotspot"),
    COLOR_INVERSION("inversion"),
    DATA_SAVER("saver"),
    DARK_THEME("dark"),
}