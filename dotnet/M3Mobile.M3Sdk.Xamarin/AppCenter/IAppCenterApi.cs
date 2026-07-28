namespace M3Sdk.Xamarin.AppCenter
{
    /// <summary>
    /// AppCenter kiosk API contract implemented through one-way Android broadcasts.
    /// </summary>
    public interface IAppCenterApi
    {
        /// <summary>
        /// Changes the AppCenter kiosk administrator password.
        /// </summary>
        /// <param name="currentPassword">The current kiosk administrator password.</param>
        /// <param name="newPassword">The new kiosk administrator password. Length must be 4 to 20 characters.</param>
        /// <remarks>
        /// Requires AppCenter version <c>2.2.0</c> or later. A normal return only confirms that
        /// Android accepted the broadcast; it does not confirm that AppCenter changed the password.
        /// </remarks>
        void ChangeKioskAdminPassword(string currentPassword, string newPassword);

        /// <summary>
        /// Sets whether AppCenter keeps administrator mode while the screen is off.
        /// </summary>
        /// <param name="enabled"><c>true</c> to keep administrator mode during screen off, otherwise <c>false</c>.</param>
        /// <remarks>
        /// Requires AppCenter version <c>2.2.0</c> or later. A normal return only confirms that
        /// Android accepted the broadcast.
        /// </remarks>
        void SetKeepAdminModeOnSleep(bool enabled);
    }
}
