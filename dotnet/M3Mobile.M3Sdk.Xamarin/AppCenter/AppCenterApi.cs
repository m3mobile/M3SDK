using System;
using Android.Content;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.AppCenter
{
    /// <summary>
    /// AppCenter API surface implemented by one-way native Android broadcasts.
    /// </summary>
    public sealed class AppCenterApi : IAppCenterApi
    {
        private readonly Context _context;
        private readonly M3SdkGuard _guard;

        internal AppCenterApi(Context context, M3SdkGuard guard)
        {
            _context = context;
            _guard = guard;
        }

        /// <inheritdoc />
        public void ChangeKioskAdminPassword(string currentPassword, string newPassword)
        {
            AppCenterPasswordPolicy.AssertCurrentPassword(currentPassword);
            AppCenterPasswordPolicy.AssertNewPassword(newPassword);
            _guard.AssertAppCenterVersion("ChangeKioskAdminPassword");
            Send(AppCenterBroadcastRequest.PasswordChange(currentPassword, newPassword));
        }

        /// <inheritdoc />
        public void SetKeepAdminModeOnSleep(bool enabled)
        {
            _guard.AssertAppCenterVersion("SetKeepAdminModeOnSleep");
            Send(AppCenterBroadcastRequest.KeepAdminModeOnSleep(enabled));
        }

        private void Send(AppCenterBroadcastRequest request)
        {
            var intent = new Intent(request.Action);
            intent.SetPackage(request.PackageName);

            foreach (var extra in request.Extras)
            {
                var key = extra.Key;
                var value = extra.Value;

                if (value is string)
                    intent.PutExtra(key, (string)value);
                else if (value is bool)
                    intent.PutExtra(key, (bool)value);
                else if (value is int)
                    intent.PutExtra(key, (int)value);
                else
                    throw new ArgumentException("Unsupported AppCenter extra type for " + key + ".");
            }

            _context.SendBroadcast(intent);
        }
    }
}
