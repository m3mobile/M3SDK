using System.Collections.Generic;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.Startup
{
    internal static class StartUpWifiPolicy
    {
        internal const string DefaultMinimumVersion = "6.8.5";

        internal static readonly ISet<DeviceModel> SupportedModels =
            new HashSet<DeviceModel>
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

        internal static readonly IDictionary<DeviceModel, string> MinimumVersionOverrides =
            new Dictionary<DeviceModel, string>
            {
                { DeviceModel.SM24, "6.8.3" }
            };
    }
}
