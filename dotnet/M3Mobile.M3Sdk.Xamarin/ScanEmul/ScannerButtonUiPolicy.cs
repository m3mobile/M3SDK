using System.Collections.Generic;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.ScanEmul
{
    internal static class ScannerButtonUiPolicy
    {
        internal const string DefaultMinimumVersion = "4.15.1";

        internal static readonly ISet<DeviceModel> UnsupportedModels =
            new HashSet<DeviceModel>
            {
                DeviceModel.WD10
            };

        internal static readonly IDictionary<DeviceModel, string> MinimumVersionOverrides =
            new Dictionary<DeviceModel, string>
            {
                { DeviceModel.SM24, "4.14.10" }
            };
    }
}
