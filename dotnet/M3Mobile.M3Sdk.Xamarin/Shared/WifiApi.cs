using Android.Content;
using Android.Provider;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.Shared
{
    /// <summary>
    /// Wi-Fi read API implementation backed by Android system settings.
    /// </summary>
    public sealed class WifiApi : IWifiApi
    {
        private readonly Context _context;
        private readonly M3SdkGuard _guard;

        internal WifiApi(Context context, M3SdkGuard guard)
        {
            _context = context;
            _guard = guard;
        }

        /// <inheritdoc />
        public int GetRoamingThreshold()
        {
            GuardUs20Us30("GetRoamingThreshold");
            return GetGlobalInt(Constants.Shared.WifiRoamingThresholdSettingsKey, -1);
        }

        /// <inheritdoc />
        public int GetRoamingDelta()
        {
            GuardUs20Us30("GetRoamingDelta");
            return GetGlobalInt(Constants.Shared.WifiRoamingDeltaSettingsKey, -1);
        }

        /// <inheritdoc />
        public int GetWifiFrequencyBand()
        {
            GuardUs20Us30("GetWifiFrequencyBand");
            return GetGlobalInt(Constants.Shared.WifiFrequencyBandSettingsKey, -1);
        }

        /// <inheritdoc />
        public string GetWifiCountryCode()
        {
            GuardUs20Us30("GetWifiCountryCode");
            return Settings.Global.GetString(_context.ContentResolver, Constants.Shared.WifiCountryCodeSettingsKey) ?? string.Empty;
        }

        private int GetGlobalInt(string key, int defaultValue)
        {
            try
            {
                return Settings.Global.GetInt(_context.ContentResolver, key);
            }
            catch
            {
                return defaultValue;
            }
        }

        private void GuardUs20Us30(string methodName)
        {
            _guard.AssertDeviceSupport(methodName, SharedGuardSets.Us20Us30, null);
        }
    }
}
