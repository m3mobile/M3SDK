using System.Collections.Generic;
using M3Sdk.Xamarin.Internal;
using M3Sdk.Xamarin.ScanEmul;
using Xunit;

namespace M3Sdk.Xamarin.Tests
{
    public sealed class ScannerButtonUiPolicyTests
    {
        [Fact]
        public void SupportsEveryModelExceptWd10()
        {
            var expected = new HashSet<DeviceModel>
            {
                DeviceModel.WD10
            };

            Assert.Equal(expected, ScannerButtonUiPolicy.UnsupportedModels);
        }

        [Fact]
        public void RequiresScanEmul4151ByDefaultAnd41410OnSm24()
        {
            Assert.Equal("4.15.1", ScannerButtonUiPolicy.DefaultMinimumVersion);
            Assert.Equal(
                new Dictionary<DeviceModel, string> { { DeviceModel.SM24, "4.14.10" } },
                ScannerButtonUiPolicy.MinimumVersionOverrides);
        }
    }
}
