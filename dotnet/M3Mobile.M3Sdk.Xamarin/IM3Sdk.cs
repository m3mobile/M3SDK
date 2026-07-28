using System;
using M3Sdk.Xamarin.AppCenter;
using M3Sdk.Xamarin.KeyTool;
using M3Sdk.Xamarin.ScanEmul;
using M3Sdk.Xamarin.Shared;
using M3Sdk.Xamarin.Startup;

namespace M3Sdk.Xamarin
{
    /// <summary>
    /// Public M3 Mobile SDK contract exposed to Xamarin.Android consumers.
    /// </summary>
    public interface IM3Sdk : IStartUpApi, IScanEmulApi, IKeyToolApi, IAppCenterApi, ITimeApi, IWifiApi, IUsbApi, IDisposable
    {
        /// <summary>
        /// Gets the StartUp app API group.
        /// </summary>
        IStartUpApi StartUp { get; }

        /// <summary>
        /// Gets the ScanEmul app API group.
        /// </summary>
        IScanEmulApi ScanEmul { get; }

        /// <summary>
        /// Gets the KeyTool app API group.
        /// </summary>
        IKeyToolApi KeyTool { get; }

        /// <summary>
        /// Gets the AppCenter app API group.
        /// </summary>
        IAppCenterApi AppCenter { get; }

        /// <summary>
        /// Gets the time read API group.
        /// </summary>
        ITimeApi Time { get; }

        /// <summary>
        /// Gets the Wi-Fi read API group.
        /// </summary>
        IWifiApi Wifi { get; }

        /// <summary>
        /// Gets the USB read API group.
        /// </summary>
        IUsbApi Usb { get; }
    }
}
