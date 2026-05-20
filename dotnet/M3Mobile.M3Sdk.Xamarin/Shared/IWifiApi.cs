namespace M3Sdk.Xamarin.Shared
{
    /// <summary>
    /// Wifi read API contract exposed by the M3 Mobile SDK.
    /// </summary>
    public interface IWifiApi
    {
        /// <summary>
        /// Gets the current Wi-Fi roaming threshold value.
        /// </summary>
        /// <returns>The roaming threshold as a negative integer.</returns>
        /// <remarks>Supported on US20 and US30.</remarks>
        int GetRoamingThreshold();

        /// <summary>
        /// Gets the current Wi-Fi roaming delta value.
        /// </summary>
        /// <returns>The roaming delta.</returns>
        /// <remarks>Supported on US20 and US30.</remarks>
        int GetRoamingDelta();

        /// <summary>
        /// Gets the current preferred Wi-Fi frequency band value.
        /// </summary>
        /// <returns><c>0</c> for automatic, <c>1</c> for 5 GHz only, or <c>2</c> for 2.4 GHz only.</returns>
        /// <remarks>Supported on US20 and US30.</remarks>
        int GetWifiFrequencyBand();

        /// <summary>
        /// Gets the current Wi-Fi country code.
        /// </summary>
        /// <returns>The country code, or an empty string when none is configured.</returns>
        /// <remarks>Supported on US20 and US30.</remarks>
        string GetWifiCountryCode();
    }
}
