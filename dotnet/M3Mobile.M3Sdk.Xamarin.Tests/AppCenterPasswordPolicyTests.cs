using System;
using M3Sdk.Xamarin.AppCenter;
using Xunit;

namespace M3Sdk.Xamarin.Tests
{
    public sealed class AppCenterPasswordPolicyTests
    {
        [Fact]
        public void CurrentPasswordRejectsOnlyEmptyString()
        {
            Assert.Throws<ArgumentException>(() => AppCenterPasswordPolicy.AssertCurrentPassword(string.Empty));

            AppCenterPasswordPolicy.AssertCurrentPassword(" ");
        }

        [Fact]
        public void NewPasswordRejectsOutOfRangeLength()
        {
            Assert.Throws<ArgumentException>(() => AppCenterPasswordPolicy.AssertNewPassword(Text(3)));
            Assert.Throws<ArgumentException>(() => AppCenterPasswordPolicy.AssertNewPassword(Text(21)));
        }

        [Fact]
        public void NewPasswordAcceptsBoundaryLength()
        {
            AppCenterPasswordPolicy.AssertNewPassword(Text(4));
            AppCenterPasswordPolicy.AssertNewPassword(Text(20));
        }

        private static string Text(int length)
        {
            return new string('p', length);
        }
    }
}
