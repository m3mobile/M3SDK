using M3Sdk.Xamarin.AppCenter;
using M3Sdk.Xamarin.Internal;
using Xunit;

namespace M3Sdk.Xamarin.Tests
{
    public sealed class AppCenterBroadcastRequestTests
    {
        [Fact]
        public void PasswordChangeUsesAppCenterContract()
        {
            var currentPassword = Text(5, 'c');
            var newPassword = Text(6, 'n');

            var request = AppCenterBroadcastRequest.PasswordChange(currentPassword, newPassword);

            Assert.Equal(Constants.AppCenter.PackageName, request.PackageName);
            Assert.Equal(Constants.AppCenter.ChangePassword, request.Action);
            Assert.Equal(currentPassword, request.Extra(Constants.AppCenter.ExtraCurrentPassword));
            Assert.Equal(newPassword, request.Extra(Constants.AppCenter.ExtraNewPassword));
            Assert.Equal(true, request.Extra(Constants.AppCenter.ExtraEncryptionEnabled));
            Assert.DoesNotContain(currentPassword, request.ToString());
            Assert.DoesNotContain(newPassword, request.ToString());
        }

        [Fact]
        public void KeepAdminModeOnSleepUsesIntPolicy()
        {
            var enabled = AppCenterBroadcastRequest.KeepAdminModeOnSleep(true);
            var disabled = AppCenterBroadcastRequest.KeepAdminModeOnSleep(false);

            Assert.Equal(Constants.AppCenter.SetKeepAdminModeOnSleep, enabled.Action);
            Assert.Equal(
                Constants.AppCenter.EnableKeepAdminModeOnSleep,
                enabled.Extra(Constants.AppCenter.ExtraKeepAdminModeOnSleep));
            Assert.Equal(
                Constants.AppCenter.DisableKeepAdminModeOnSleep,
                disabled.Extra(Constants.AppCenter.ExtraKeepAdminModeOnSleep));
            Assert.IsType<int>(enabled.Extra(Constants.AppCenter.ExtraKeepAdminModeOnSleep));
            Assert.IsType<int>(disabled.Extra(Constants.AppCenter.ExtraKeepAdminModeOnSleep));
        }

        [Fact]
        public void PasswordValuesAreNotTrimmed()
        {
            var currentPassword = " " + Text(4, 'c') + " ";
            var newPassword = " " + Text(4, 'n') + " ";

            var request = AppCenterBroadcastRequest.PasswordChange(currentPassword, newPassword);

            Assert.Equal(currentPassword, request.Extra(Constants.AppCenter.ExtraCurrentPassword));
            Assert.Equal(newPassword, request.Extra(Constants.AppCenter.ExtraNewPassword));
        }

        private static string Text(int length, char character)
        {
            return new string(character, length);
        }
    }
}
