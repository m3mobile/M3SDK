namespace M3Sdk.Xamarin.KeyTool
{
    /// <summary>
    /// KeyTool companion app API contract implemented through one-way Android broadcasts.
    /// </summary>
    public interface IKeyToolApi
    {
        /// <summary>
        /// Enables Function-key mode on SL20K.
        /// </summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that the setting was applied.</remarks>
        void EnableFn();

        /// <summary>
        /// Disables Function-key mode on SL20K.
        /// </summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that the setting was applied.</remarks>
        void DisableFn();

        /// <summary>
        /// Locks Function-key mode on SL20K.
        /// </summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that the setting was applied.</remarks>
        void LockFn();

        /// <summary>
        /// Assigns a KeyTool function to a physical key on a current KeyTool SL-series, WD10, SM24, or SM25 device.
        /// </summary>
        /// <param name="key">The KeyTool key title.</param>
        /// <param name="function">The KeyTool function title.</param>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that the mapping was applied.</remarks>
        void SetKeyFunction(string key, string function);

        /// <summary>Enables the Home navigation button on SM24 or SM25.</summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that SystemUI applied it.</remarks>
        void EnableHomeButton();

        /// <summary>Disables the Home navigation button on SM24 or SM25.</summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that SystemUI applied it.</remarks>
        void DisableHomeButton();

        /// <summary>Enables the Recent navigation button on SM24 or SM25.</summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that SystemUI applied it.</remarks>
        void EnableRecentButton();

        /// <summary>Disables the Recent navigation button on SM24 or SM25.</summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that SystemUI applied it.</remarks>
        void DisableRecentButton();

        /// <summary>
        /// Enables wake-up by the left scan key on SL20P.
        /// </summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that the setting was applied.</remarks>
        void EnableLeftScanWakeUp();

        /// <summary>
        /// Disables wake-up by the left scan key on SL20P.
        /// </summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that the setting was applied.</remarks>
        void DisableLeftScanWakeUp();

        /// <summary>
        /// Enables wake-up by the right scan key on SL20P.
        /// </summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that the setting was applied.</remarks>
        void EnableRightScanWakeUp();

        /// <summary>
        /// Disables wake-up by the right scan key on SL20P.
        /// </summary>
        /// <remarks>A normal return confirms only that the broadcast was sent, not that the setting was applied.</remarks>
        void DisableRightScanWakeUp();
    }
}
