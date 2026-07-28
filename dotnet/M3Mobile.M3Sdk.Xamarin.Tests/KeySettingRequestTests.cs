using M3Sdk.Xamarin.Internal;
using M3Sdk.Xamarin.KeyTool;
using Xunit;

namespace M3Sdk.Xamarin.Tests
{
    public sealed class KeySettingRequestTests
    {
        [Fact]
        public void FunctionOnlyUsesUnifiedContractWithoutWakeUp()
        {
            var request = KeySettingRequest.ForKeyFunction(
                Constants.KeyTool.LeftScan,
                "Scan");

            Assert.Equal(Constants.KeyTool.Sl20PackageName, request.PackageName);
            Assert.Equal(Constants.KeyTool.SetKey, request.Action);
            Assert.True(request.Ordered);
            Assert.Equal(Constants.KeyTool.LeftScan, request.Extra(Constants.KeyTool.ExtraKeyTitle));
            Assert.Equal("Scan", request.Extra(Constants.KeyTool.ExtraKeyFunction));
            Assert.Null(request.Extra(Constants.KeyTool.ExtraKeyWakeUp));
        }

        [Fact]
        public void FunctionAndWakeUpUseOneUnifiedContract()
        {
            var request = KeySettingRequest.ForKeyFunction(
                Constants.KeyTool.LeftScan,
                "Scan",
                true);

            Assert.Equal(Constants.KeyTool.Sl20PackageName, request.PackageName);
            Assert.Equal(Constants.KeyTool.SetKey, request.Action);
            Assert.True(request.Ordered);
            Assert.Equal(Constants.KeyTool.LeftScan, request.Extra(Constants.KeyTool.ExtraKeyTitle));
            Assert.Equal("Scan", request.Extra(Constants.KeyTool.ExtraKeyFunction));
            Assert.Equal(true, request.Extra(Constants.KeyTool.ExtraKeyWakeUp));
            Assert.Null(request.Extra(Constants.KeyTool.ExtraWakeUpEnabled));
        }

        [Fact]
        public void LegacyLeftEnableUsesLegacyContract()
        {
            var request = KeySettingRequest.ForScanWakeUp(
                DeviceModel.SL20P,
                Constants.KeyTool.LeftScan,
                true);

            Assert.Equal(Constants.KeyTool.WakeUpPackageName, request.PackageName);
            Assert.Equal(Constants.KeyTool.LeftScanWakeUp, request.Action);
            Assert.False(request.Ordered);
            Assert.Equal(true, request.Extra(Constants.KeyTool.ExtraWakeUpEnabled));
            Assert.Null(request.Extra(Constants.KeyTool.ExtraKeyWakeUp));
        }

        [Fact]
        public void LegacyRightDisableUsesLegacyContract()
        {
            var request = KeySettingRequest.ForScanWakeUp(
                DeviceModel.SL20P,
                Constants.KeyTool.RightScan,
                false);

            Assert.Equal(Constants.KeyTool.RightScanWakeUp, request.Action);
            Assert.Equal(false, request.Extra(Constants.KeyTool.ExtraWakeUpEnabled));
        }

        [Fact]
        public void UnifiedLeftEnableUsesSetKeyContract()
        {
            var request = KeySettingRequest.ForScanWakeUp(
                DeviceModel.SM24,
                Constants.KeyTool.LeftScan,
                true);

            Assert.Equal(Constants.KeyTool.Sl20PackageName, request.PackageName);
            Assert.Equal(Constants.KeyTool.SetKey, request.Action);
            Assert.True(request.Ordered);
            Assert.Equal(Constants.KeyTool.LeftScan, request.Extra(Constants.KeyTool.ExtraKeyTitle));
            Assert.Equal(true, request.Extra(Constants.KeyTool.ExtraKeyWakeUp));
            Assert.Null(request.Extra(Constants.KeyTool.ExtraWakeUpEnabled));
            Assert.Null(request.Extra(Constants.KeyTool.ExtraKeyFunction));
        }

        [Fact]
        public void UnifiedRightDisableUsesSetKeyContract()
        {
            var request = KeySettingRequest.ForScanWakeUp(
                DeviceModel.SM24,
                Constants.KeyTool.RightScan,
                false);

            Assert.Equal(Constants.KeyTool.RightScan, request.Extra(Constants.KeyTool.ExtraKeyTitle));
            Assert.Equal(false, request.Extra(Constants.KeyTool.ExtraKeyWakeUp));
            Assert.Null(request.Extra(Constants.KeyTool.ExtraKeyFunction));
        }

        [Fact]
        public void UnsupportedModelKeepsLegacyCompatibility()
        {
            var request = KeySettingRequest.ForScanWakeUp(
                DeviceModel.Unknown,
                Constants.KeyTool.LeftScan,
                true);

            Assert.Equal(Constants.KeyTool.WakeUpPackageName, request.PackageName);
            Assert.Equal(Constants.KeyTool.LeftScanWakeUp, request.Action);
        }
    }
}
