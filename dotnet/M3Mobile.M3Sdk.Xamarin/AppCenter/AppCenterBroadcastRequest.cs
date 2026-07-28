using System.Collections.Generic;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.AppCenter
{
    internal sealed class AppCenterBroadcastRequest
    {
        private readonly IDictionary<string, object> _extras;

        private AppCenterBroadcastRequest(string action, IDictionary<string, object> extras)
        {
            PackageName = Constants.AppCenter.PackageName;
            Action = action;
            _extras = new Dictionary<string, object>(extras);
        }

        internal string PackageName { get; private set; }

        internal string Action { get; private set; }

        internal IEnumerable<KeyValuePair<string, object>> Extras
        {
            get { return _extras; }
        }

        internal object Extra(string key)
        {
            object value;
            return _extras.TryGetValue(key, out value) ? value : null;
        }

        internal static AppCenterBroadcastRequest PasswordChange(
            string currentPassword,
            string newPassword)
        {
            return new AppCenterBroadcastRequest(
                Constants.AppCenter.ChangePassword,
                new Dictionary<string, object>
                {
                    { Constants.AppCenter.ExtraCurrentPassword, currentPassword },
                    { Constants.AppCenter.ExtraNewPassword, newPassword },
                    { Constants.AppCenter.ExtraEncryptionEnabled, true }
                });
        }

        internal static AppCenterBroadcastRequest KeepAdminModeOnSleep(bool enabled)
        {
            return new AppCenterBroadcastRequest(
                Constants.AppCenter.SetKeepAdminModeOnSleep,
                new Dictionary<string, object>
                {
                    {
                        Constants.AppCenter.ExtraKeepAdminModeOnSleep,
                        enabled
                            ? Constants.AppCenter.EnableKeepAdminModeOnSleep
                            : Constants.AppCenter.DisableKeepAdminModeOnSleep
                    }
                });
        }

        public override string ToString()
        {
            return "AppCenterBroadcastRequest(PackageName=" + PackageName +
                   ", Action=" + Action +
                   ", Extras=" + string.Join(", ", _extras.Keys) + ")";
        }
    }
}
