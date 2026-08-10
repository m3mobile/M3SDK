using System.Collections.Generic;
using M3Sdk.Xamarin.Internal;
using M3Sdk.Xamarin.Startup;
using Xunit;

namespace M3Sdk.Xamarin.Tests
{
    public sealed class StartUpWifiPolicyTests
    {
        [Fact]
        public void SupportsAllStartUpWifiStateChangeModels()
        {
            var expected = new HashSet<DeviceModel>
            {
                DeviceModel.SM20,
                DeviceModel.SM20_U,
                DeviceModel.SL20,
                DeviceModel.SL20P,
                DeviceModel.SL20K,
                DeviceModel.US20,
                DeviceModel.US30,
                DeviceModel.UL20_OREO,
                DeviceModel.UL20_PIE,
                DeviceModel.UX20_Q,
                DeviceModel.UL20_A10,
                DeviceModel.UL20F,
                DeviceModel.UL30,
                DeviceModel.SM24,
                DeviceModel.SM25,
                DeviceModel.PC10,
                DeviceModel.WD10
            };

            Assert.Equal(expected.Count, StartUpWifiPolicy.SupportedModels.Count);
            Assert.Subset(expected, StartUpWifiPolicy.SupportedModels);
        }

        [Fact]
        public void RequiresStartUp685ByDefaultAnd683OnSm24()
        {
            Assert.Equal("6.8.5", StartUpWifiPolicy.DefaultMinimumVersion);
            Assert.Equal(
                new Dictionary<DeviceModel, string> { { DeviceModel.SM24, "6.8.3" } },
                StartUpWifiPolicy.MinimumVersionOverrides);
        }
    }
}
