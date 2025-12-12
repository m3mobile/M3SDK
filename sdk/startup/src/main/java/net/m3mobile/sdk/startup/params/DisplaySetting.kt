package net.m3mobile.sdk.startup.params

/**
 * Represents a configuration object for customizing various device display settings.
 *
 * @property enableAutoBrightness Determines whether the device should automatically adjust screen brightness
 * @property brightness The specific brightness level to set if auto-brightness is disabled (1-255)
 * @property enableAutoRotate Determines whether the screen orientation should automatically rotate based on the device's physical orientation
 * @property rotateForce Specifies a forced orientation mode
 * @property enableScreenLock Determines whether the screen lock mechanism is enabled
 * @property sleepMode Defines the duration of inactivity before the screen goes to sleep
 * @property policyControl Controls the visibility of system UI elements, such as the status bar or navigation bar
 * @property showBatteryPercentage Determines whether the battery percentage is explicitly displayed in the status bar
 * @property screenSaverMode Configures when the screen saver should be activated
 * @property screenSaverComponent The component name (package/class) of the specific screen saver application to use
 */
public data class DisplaySetting(
    val enableAutoBrightness: Boolean,
    val brightness: Int,
    val enableAutoRotate: Boolean,
    val rotateForce: RotateForce,
    val enableScreenLock: Boolean,
    val sleepMode: SleepMode,
    val policyControl: PolicyControl,
    val showBatteryPercentage: Boolean,
    val screenSaverMode: ScreenSaverMode,
    val screenSaverComponent: String
)

public enum class SleepMode(internal val value: Int) {
    SECONDS_15(15000),
    SECONDS_30(30000),
    MINUTES_1(60000),
    MINUTES_2(120000),
    MINUTES_5(300000),
    MINUTES_10(600000),
    MINUTES_30(1800000),
    NEVER(Int.MAX_VALUE)
}

public enum class RotateForce(internal val value: Int) {
    DEFAULT(0),
    AUTOMATIC(1),
    LANDSCAPE(2),
    LANDSCAPE_REVERSE(3),
    LANDSCAPE_SENSOR(4),
    PORTRAIT(5),
    PORTRAIT_REVERSE(6),
    PORTRAIT_SENSOR(7)
}

public enum class PolicyControl(internal val value: Int) {
    HIDE_STATUS_BAR(1),
    HIDE_NAVIGATION_BAR(2),
    HIDE_SYSTEM_BAR(3),
    DEFAULT(4)
}

public enum class ScreenSaverMode(internal val value: Int) {
    WHILE_CHARGING(0),
    WHILE_DOCKED(1),
    WHILE_CHARGING_OR_DOCKED(2),
    NEVER(3)
}