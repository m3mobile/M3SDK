using System;
using Android.Content;

namespace M3Sdk.Xamarin
{
    /// <summary>
    /// Entry point for creating an M3 Mobile SDK instance.
    /// </summary>
    public static class M3Mobile
    {
        /// <summary>
        /// Creates a native C# SDK instance using the supplied Android context.
        /// Pass <c>Application.Context</c> from a Xamarin.Android application.
        /// </summary>
        /// <param name="context">The Android context used to send broadcasts and read system settings.</param>
        /// <returns>An SDK instance that exposes StartUp, ScanEmul, time, Wi-Fi, and USB APIs.</returns>
        /// <exception cref="ArgumentNullException"><paramref name="context" /> is <c>null</c>.</exception>
        public static IM3Sdk Create(Context context)
        {
            if (context == null)
                throw new ArgumentNullException(nameof(context));

            return new M3Sdk(context);
        }
    }
}
