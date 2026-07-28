using M3Sdk.Xamarin.Internal;
using M3Sdk.Xamarin.KeyTool;
using Xunit;

namespace M3Sdk.Xamarin.Tests
{
    public sealed class KeyToolVersionPolicyTests
    {
        [Fact]
        public void KeyFunctionMinimumVersionsMatchDeviceIntroduction()
        {
            Assert.Equal("1.2.6", KeyToolVersionPolicy.KeyFunctionMinimumVersion(DeviceModel.SL20));
            Assert.Equal("1.2.6", KeyToolVersionPolicy.KeyFunctionMinimumVersion(DeviceModel.SL20K));
            Assert.Equal("1.2.6", KeyToolVersionPolicy.KeyFunctionMinimumVersion(DeviceModel.SL20P));
            Assert.Equal("1.2.6", KeyToolVersionPolicy.KeyFunctionMinimumVersion(DeviceModel.SL25));
            Assert.Equal("1.2.6", KeyToolVersionPolicy.KeyFunctionMinimumVersion(DeviceModel.WD10));
            Assert.Equal("1.3.8", KeyToolVersionPolicy.KeyFunctionMinimumVersion(DeviceModel.SM24));
            Assert.Equal("1.3.16", KeyToolVersionPolicy.KeyFunctionMinimumVersion(DeviceModel.SM25));
        }

        [Theory]
        [InlineData("1.2.5", "1.2.6", false)]
        [InlineData("1.2.6", "1.2.6", true)]
        [InlineData("1.3.7", "1.3.8", false)]
        [InlineData("1.3.8", "1.3.8", true)]
        [InlineData("1.3.15", "1.3.16", false)]
        [InlineData("1.3.16", "1.3.16", true)]
        [InlineData("1.4.0", "1.4.1", false)]
        [InlineData("1.4.1", "1.4.1", true)]
        [InlineData("1.4.1_alpha", "1.4.1", true)]
        [InlineData("1.3.4F", "1.3.4", true)]
        [InlineData("1.4.0AD", "1.4.0", true)]
        [InlineData("1.4.0AD", "1.4.1", false)]
        public void VersionComparisonUsesNumericPrefixes(
            string currentVersion,
            string requiredVersion,
            bool expected)
        {
            Assert.Equal(
                expected,
                KeyToolVersionPolicy.VersionSatisfied(currentVersion, requiredVersion));
        }
    }
}
