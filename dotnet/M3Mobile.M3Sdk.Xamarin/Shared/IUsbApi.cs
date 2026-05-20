using System.Collections.Generic;

namespace M3Sdk.Xamarin.Shared
{
    /// <summary>
    /// USB read API contract exposed by the M3 Mobile SDK.
    /// </summary>
    public interface IUsbApi
    {
        /// <summary>
        /// Gets the currently active USB connection modes.
        /// </summary>
        /// <returns>A list of active USB mode strings, or an empty list when no active mode is reported.</returns>
        IList<string> GetCurrentUsbModes();
    }
}
