using Android.Content;
using Android.Content.Res;
using Android.Provider;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.Shared
{
    /// <summary>
    /// Time read API implementation backed by Android system settings.
    /// </summary>
    public sealed class TimeApi : ITimeApi
    {
        private readonly Context _context;
        private readonly M3SdkGuard _guard;

        internal TimeApi(Context context, M3SdkGuard guard)
        {
            _context = context;
            _guard = guard;
        }

        /// <inheritdoc />
        public string GetNtpServer()
        {
            GuardUs20Us30("GetNtpServer");
            var server = Settings.Global.GetString(_context.ContentResolver, Constants.Shared.NtpServerSettingsKey);
            return string.IsNullOrEmpty(server) ? Constants.Shared.DefaultNtpServer : server;
        }

        /// <inheritdoc />
        public int GetNtpInterval()
        {
            GuardUs20Us30("GetNtpInterval");
            var intervalResId = Resources.System.GetIdentifier(
                Constants.Shared.NtpIntervalResourceName,
                Constants.Shared.AndroidResourceTypeInteger,
                Constants.Shared.AndroidResourcePackage);
            return Resources.System.GetInteger(intervalResId);
        }

        /// <inheritdoc />
        public string GetTimeZone()
        {
            return Java.Util.TimeZone.Default.ID;
        }

        private void GuardUs20Us30(string methodName)
        {
            _guard.AssertDeviceSupport(methodName, SharedGuardSets.Us20Us30, null);
        }
    }
}
