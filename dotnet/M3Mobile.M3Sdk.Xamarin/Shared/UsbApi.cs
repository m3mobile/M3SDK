using System.Collections.Generic;
using Android.Content;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.Shared
{
    /// <summary>
    /// USB read API implementation backed by the Android USB state broadcast.
    /// </summary>
    public sealed class UsbApi : IUsbApi
    {
        private static readonly string[] UsbModes = { "mtp", "ptp", "midi", "rndis", "ncm", "adb" };

        private readonly Context _context;

        internal UsbApi(Context context)
        {
            _context = context;
        }

        /// <inheritdoc />
        public IList<string> GetCurrentUsbModes()
        {
            var result = new List<string>();
            var intent = _context.RegisterReceiver(null, new IntentFilter(Constants.Shared.UsbStateAction));
            if (intent == null)
                return result;

            foreach (var mode in UsbModes)
            {
                if (intent.GetBooleanExtra(mode, false))
                    result.Add(mode);
            }

            return result;
        }
    }
}
