using System;

namespace M3Sdk.Xamarin.AppCenter
{
    internal static class AppCenterPasswordPolicy
    {
        private const int MinNewPasswordLength = 4;
        private const int MaxNewPasswordLength = 20;

        internal static void AssertCurrentPassword(string currentPassword)
        {
            if (string.IsNullOrEmpty(currentPassword))
                throw new ArgumentException("Current AppCenter kiosk admin password is required.", nameof(currentPassword));
        }

        internal static void AssertNewPassword(string newPassword)
        {
            if (newPassword == null ||
                newPassword.Length < MinNewPasswordLength ||
                newPassword.Length > MaxNewPasswordLength)
            {
                throw new ArgumentException(
                    "New AppCenter kiosk admin password length must be 4 to 20 characters.",
                    nameof(newPassword));
            }
        }
    }
}
