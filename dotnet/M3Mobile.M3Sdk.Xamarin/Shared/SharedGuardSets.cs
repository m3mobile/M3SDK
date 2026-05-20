using System.Collections.Generic;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.Shared
{
    internal static class SharedGuardSets
    {
        internal static readonly ISet<DeviceModel> Us20Us30 =
            new HashSet<DeviceModel> { DeviceModel.US20, DeviceModel.US30 };
    }
}
