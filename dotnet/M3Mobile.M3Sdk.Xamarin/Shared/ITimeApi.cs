namespace M3Sdk.Xamarin.Shared
{
    /// <summary>
    /// Time read API contract exposed by the M3 Mobile SDK.
    /// </summary>
    public interface ITimeApi
    {
        /// <summary>
        /// Gets the currently configured NTP server address.
        /// </summary>
        /// <returns>The NTP server address.</returns>
        /// <remarks>Supported on US20 and US30.</remarks>
        string GetNtpServer();

        /// <summary>
        /// Gets the currently configured NTP synchronization interval in milliseconds.
        /// </summary>
        /// <returns>The NTP synchronization interval in milliseconds.</returns>
        /// <remarks>Supported on US20 and US30.</remarks>
        int GetNtpInterval();

        /// <summary>
        /// Gets the current default system time zone.
        /// </summary>
        /// <returns>The time zone identifier.</returns>
        string GetTimeZone();
    }
}
