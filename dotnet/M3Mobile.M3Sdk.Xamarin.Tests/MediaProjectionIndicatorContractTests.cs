using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.Tests
{
    public sealed class MediaProjectionIndicatorContractTests
    {
        [Fact]
        public void UsesStartUp687MediaProjectionIndicatorBroadcastContract()
        {
            Assert.Equal(
                "media_projection_exempt_packages",
                Constants.StartUp.TypeMediaProjectionIndicatorPackages);
            Assert.Equal("packages", Constants.StartUp.ExtraMediaProjectionIndicatorPackages);
            Assert.Equal("mode", Constants.StartUp.ExtraMediaProjectionIndicatorMode);
            Assert.Equal("replace", Constants.StartUp.ReplaceMediaProjectionIndicatorPackages);
            Assert.Equal("append", Constants.StartUp.AppendMediaProjectionIndicatorPackages);
            Assert.Equal("remove", Constants.StartUp.RemoveMediaProjectionIndicatorPackages);
            Assert.Equal("clear", Constants.StartUp.ClearMediaProjectionIndicatorPackages);
        }
    }
}
