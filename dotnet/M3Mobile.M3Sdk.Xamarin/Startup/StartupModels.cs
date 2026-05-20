using System;

namespace M3Sdk.Xamarin.Startup
{
    /// <summary>
    /// Represents a Wi-Fi access point configuration used by <see cref="IStartUpApi.SetAccessPoint" />.
    /// </summary>
    /// <remarks>Use <see cref="CreateBuilder" /> when constructing the configuration fluently.</remarks>
    public sealed class AccessPoint
    {
        /// <summary>
        /// Creates a Wi-Fi access point configuration with the required SSID and security type.
        /// </summary>
        /// <param name="ssid">The Wi-Fi network SSID.</param>
        /// <param name="security">The Wi-Fi security type.</param>
        /// <exception cref="ArgumentNullException"><paramref name="ssid" /> or <paramref name="security" /> is <c>null</c>.</exception>
        public AccessPoint(string ssid, string security)
        {
            if (ssid == null)
                throw new ArgumentNullException(nameof(ssid));
            if (security == null)
                throw new ArgumentNullException(nameof(security));

            Ssid = ssid;
            Security = security;
        }

        /// <summary>
        /// Gets the Wi-Fi network SSID.
        /// </summary>
        public string Ssid { get; private set; }

        /// <summary>
        /// Gets the Wi-Fi security type.
        /// </summary>
        public string Security { get; private set; }

        /// <summary>
        /// Gets or sets the Wi-Fi password.
        /// </summary>
        public string Password { get; set; }

        /// <summary>
        /// Gets or sets whether static IP configuration is enabled.
        /// </summary>
        public bool? EnableStatic { get; set; }

        /// <summary>
        /// Gets or sets the static IP address.
        /// </summary>
        public string IpAddress { get; set; }

        /// <summary>
        /// Gets or sets the static network mask.
        /// </summary>
        public string Mask { get; set; }

        /// <summary>
        /// Gets or sets the static gateway address.
        /// </summary>
        public string Gateway { get; set; }

        /// <summary>
        /// Gets or sets the static DNS address.
        /// </summary>
        public string Dns { get; set; }

        /// <summary>
        /// Gets or sets whether MAC randomization is enabled for this access point.
        /// </summary>
        public bool? MacRandom { get; set; }

        /// <summary>
        /// Gets or sets whether the SSID is hidden.
        /// </summary>
        public bool? HiddenSsid { get; set; }

        /// <summary>
        /// Creates a builder for an <see cref="AccessPoint" /> configuration.
        /// </summary>
        /// <returns>A new access point builder.</returns>
        public static Builder CreateBuilder()
        {
            return new Builder();
        }

        /// <summary>
        /// Fluent builder for <see cref="AccessPoint" />.
        /// </summary>
        public sealed class Builder
        {
            private string _ssid;
            private string _security;
            private string _password;
            private bool? _enableStatic;
            private string _ipAddress;
            private string _mask;
            private string _gateway;
            private string _dns;
            private bool? _macRandom;
            private bool? _hiddenSsid;

            /// <summary>
            /// Creates an empty access point builder.
            /// </summary>
            public Builder()
            {
            }

            /// <summary>
            /// Sets the Wi-Fi network SSID.
            /// </summary>
            /// <param name="ssid">The Wi-Fi network SSID.</param>
            /// <returns>The current builder.</returns>
            public Builder SetSsid(string ssid)
            {
                _ssid = ssid;
                return this;
            }

            /// <summary>
            /// Sets the Wi-Fi security type.
            /// </summary>
            /// <param name="security">The Wi-Fi security type.</param>
            /// <returns>The current builder.</returns>
            public Builder SetSecurity(string security)
            {
                _security = security;
                return this;
            }

            /// <summary>
            /// Sets the Wi-Fi password.
            /// </summary>
            /// <param name="password">The Wi-Fi password.</param>
            /// <returns>The current builder.</returns>
            public Builder SetPassword(string password)
            {
                _password = password;
                return this;
            }

            /// <summary>
            /// Sets whether static IP configuration is enabled.
            /// </summary>
            /// <param name="enable">Whether to enable static IP configuration.</param>
            /// <returns>The current builder.</returns>
            public Builder SetEnableStatic(bool enable)
            {
                _enableStatic = enable;
                return this;
            }

            /// <summary>
            /// Sets the static IP address.
            /// </summary>
            /// <param name="ipAddress">The static IP address.</param>
            /// <returns>The current builder.</returns>
            public Builder SetIpAddress(string ipAddress)
            {
                _ipAddress = ipAddress;
                return this;
            }

            /// <summary>
            /// Sets the static network mask.
            /// </summary>
            /// <param name="mask">The static network mask.</param>
            /// <returns>The current builder.</returns>
            public Builder SetMask(string mask)
            {
                _mask = mask;
                return this;
            }

            /// <summary>
            /// Sets the static gateway address.
            /// </summary>
            /// <param name="gateway">The static gateway address.</param>
            /// <returns>The current builder.</returns>
            public Builder SetGateway(string gateway)
            {
                _gateway = gateway;
                return this;
            }

            /// <summary>
            /// Sets the static DNS address.
            /// </summary>
            /// <param name="dns">The static DNS address.</param>
            /// <returns>The current builder.</returns>
            public Builder SetDns(string dns)
            {
                _dns = dns;
                return this;
            }

            /// <summary>
            /// Sets whether MAC randomization is enabled for this access point.
            /// </summary>
            /// <param name="macRandom">Whether MAC randomization is enabled.</param>
            /// <returns>The current builder.</returns>
            public Builder SetMacRandom(bool macRandom)
            {
                _macRandom = macRandom;
                return this;
            }

            /// <summary>
            /// Sets whether the SSID is hidden.
            /// </summary>
            /// <param name="hiddenSsid">Whether the SSID is hidden.</param>
            /// <returns>The current builder.</returns>
            public Builder SetHiddenSsid(bool hiddenSsid)
            {
                _hiddenSsid = hiddenSsid;
                return this;
            }

            /// <summary>
            /// Builds an <see cref="AccessPoint" /> from the current builder values.
            /// </summary>
            /// <returns>The configured access point.</returns>
            /// <exception cref="ArgumentNullException">A required SSID or security value was not set.</exception>
            public AccessPoint Build()
            {
                var accessPoint = new AccessPoint(_ssid, _security)
                {
                    Password = _password,
                    EnableStatic = _enableStatic,
                    IpAddress = _ipAddress,
                    Mask = _mask,
                    Gateway = _gateway,
                    Dns = _dns,
                    MacRandom = _macRandom,
                    HiddenSsid = _hiddenSsid
                };

                return accessPoint;
            }
        }
    }

    /// <summary>
    /// Represents an Access Point Name (APN) configuration for mobile network connections.
    /// </summary>
    /// <remarks>Use <see cref="CreateBuilder" /> when constructing the configuration fluently.</remarks>
    public sealed class Apn
    {
        /// <summary>
        /// Creates an APN configuration with the required fields.
        /// </summary>
        /// <param name="name">The APN display name.</param>
        /// <param name="url">The APN address or URL.</param>
        /// <param name="mcc">The mobile country code.</param>
        /// <param name="mnc">The mobile network code.</param>
        /// <param name="type">The APN type.</param>
        /// <exception cref="ArgumentNullException">A required APN value is <c>null</c>.</exception>
        public Apn(string name, string url, string mcc, string mnc, string type)
        {
            if (name == null)
                throw new ArgumentNullException(nameof(name));
            if (url == null)
                throw new ArgumentNullException(nameof(url));
            if (mcc == null)
                throw new ArgumentNullException(nameof(mcc));
            if (mnc == null)
                throw new ArgumentNullException(nameof(mnc));
            if (type == null)
                throw new ArgumentNullException(nameof(type));

            Name = name;
            Url = url;
            Mcc = mcc;
            Mnc = mnc;
            Type = type;
        }

        /// <summary>
        /// Gets the APN display name.
        /// </summary>
        public string Name { get; private set; }

        /// <summary>
        /// Gets the APN address or URL.
        /// </summary>
        public string Url { get; private set; }

        /// <summary>
        /// Gets the mobile country code.
        /// </summary>
        public string Mcc { get; private set; }

        /// <summary>
        /// Gets the mobile network code.
        /// </summary>
        public string Mnc { get; private set; }

        /// <summary>
        /// Gets the APN type.
        /// </summary>
        public string Type { get; private set; }

        /// <summary>
        /// Gets or sets the APN proxy.
        /// </summary>
        public string Proxy { get; set; }

        /// <summary>
        /// Gets or sets the APN proxy port.
        /// </summary>
        public string Port { get; set; }

        /// <summary>
        /// Gets or sets the APN user name.
        /// </summary>
        public string User { get; set; }

        /// <summary>
        /// Gets or sets the APN password.
        /// </summary>
        public string Password { get; set; }

        /// <summary>
        /// Gets or sets the APN server.
        /// </summary>
        public string Server { get; set; }

        /// <summary>
        /// Gets or sets the Multimedia Messaging Service Center URL.
        /// </summary>
        public string Mmsc { get; set; }

        /// <summary>
        /// Gets or sets the MMS proxy.
        /// </summary>
        public string MmsProxy { get; set; }

        /// <summary>
        /// Gets or sets the MMS proxy port.
        /// </summary>
        public string MmsPort { get; set; }

        /// <summary>
        /// Gets or sets the APN authentication type.
        /// </summary>
        public int? AuthType { get; set; }

        /// <summary>
        /// Gets or sets the APN protocol.
        /// </summary>
        public int? Protocol { get; set; }

        /// <summary>
        /// Gets or sets the APN roaming protocol.
        /// </summary>
        public int? Roaming { get; set; }

        /// <summary>
        /// Gets or sets the Mobile Virtual Network Operator type.
        /// </summary>
        public int? Mvno { get; set; }

        /// <summary>
        /// Gets or sets the Mobile Virtual Network Operator value.
        /// </summary>
        public string MvnoValue { get; set; }

        /// <summary>
        /// Creates a builder for an <see cref="Apn" /> configuration.
        /// </summary>
        /// <returns>A new APN builder.</returns>
        public static Builder CreateBuilder()
        {
            return new Builder();
        }

        /// <summary>
        /// Fluent builder for <see cref="Apn" />.
        /// </summary>
        public sealed class Builder
        {
            private string _name;
            private string _url;
            private string _mcc;
            private string _mnc;
            private string _type;
            private string _proxy;
            private string _port;
            private string _user;
            private string _password;
            private string _server;
            private string _mmsc;
            private string _mmsProxy;
            private string _mmsPort;
            private int? _authType;
            private int? _protocol;
            private int? _roaming;
            private int? _mvno;
            private string _mvnoValue;

            /// <summary>
            /// Creates an empty APN builder.
            /// </summary>
            public Builder()
            {
            }

            /// <summary>
            /// Sets the APN display name.
            /// </summary>
            /// <param name="name">The APN display name.</param>
            /// <returns>The current builder.</returns>
            public Builder SetName(string name)
            {
                _name = name;
                return this;
            }

            /// <summary>
            /// Sets the APN address or URL.
            /// </summary>
            /// <param name="url">The APN address or URL.</param>
            /// <returns>The current builder.</returns>
            public Builder SetUrl(string url)
            {
                _url = url;
                return this;
            }

            /// <summary>
            /// Sets the mobile country code.
            /// </summary>
            /// <param name="mcc">The mobile country code.</param>
            /// <returns>The current builder.</returns>
            public Builder SetMcc(string mcc)
            {
                _mcc = mcc;
                return this;
            }

            /// <summary>
            /// Sets the mobile network code.
            /// </summary>
            /// <param name="mnc">The mobile network code.</param>
            /// <returns>The current builder.</returns>
            public Builder SetMnc(string mnc)
            {
                _mnc = mnc;
                return this;
            }

            /// <summary>
            /// Sets the APN type.
            /// </summary>
            /// <param name="type">The APN type.</param>
            /// <returns>The current builder.</returns>
            public Builder SetType(string type)
            {
                _type = type;
                return this;
            }

            /// <summary>
            /// Sets the APN proxy.
            /// </summary>
            /// <param name="proxy">The APN proxy.</param>
            /// <returns>The current builder.</returns>
            public Builder SetProxy(string proxy)
            {
                _proxy = proxy;
                return this;
            }

            /// <summary>
            /// Sets the APN proxy port.
            /// </summary>
            /// <param name="port">The APN proxy port.</param>
            /// <returns>The current builder.</returns>
            public Builder SetPort(string port)
            {
                _port = port;
                return this;
            }

            /// <summary>
            /// Sets the APN user name.
            /// </summary>
            /// <param name="user">The APN user name.</param>
            /// <returns>The current builder.</returns>
            public Builder SetUser(string user)
            {
                _user = user;
                return this;
            }

            /// <summary>
            /// Sets the APN password.
            /// </summary>
            /// <param name="password">The APN password.</param>
            /// <returns>The current builder.</returns>
            public Builder SetPassword(string password)
            {
                _password = password;
                return this;
            }

            /// <summary>
            /// Sets the APN server.
            /// </summary>
            /// <param name="server">The APN server.</param>
            /// <returns>The current builder.</returns>
            public Builder SetServer(string server)
            {
                _server = server;
                return this;
            }

            /// <summary>
            /// Sets the Multimedia Messaging Service Center URL.
            /// </summary>
            /// <param name="mmsc">The Multimedia Messaging Service Center URL.</param>
            /// <returns>The current builder.</returns>
            public Builder SetMmsc(string mmsc)
            {
                _mmsc = mmsc;
                return this;
            }

            /// <summary>
            /// Sets the MMS proxy.
            /// </summary>
            /// <param name="mmsProxy">The MMS proxy.</param>
            /// <returns>The current builder.</returns>
            public Builder SetMmsProxy(string mmsProxy)
            {
                _mmsProxy = mmsProxy;
                return this;
            }

            /// <summary>
            /// Sets the MMS proxy port.
            /// </summary>
            /// <param name="mmsPort">The MMS proxy port.</param>
            /// <returns>The current builder.</returns>
            public Builder SetMmsPort(string mmsPort)
            {
                _mmsPort = mmsPort;
                return this;
            }

            /// <summary>
            /// Sets the APN authentication type.
            /// </summary>
            /// <param name="authType">The APN authentication type.</param>
            /// <returns>The current builder.</returns>
            public Builder SetAuthType(int authType)
            {
                _authType = authType;
                return this;
            }

            /// <summary>
            /// Sets the APN protocol.
            /// </summary>
            /// <param name="protocol">The APN protocol.</param>
            /// <returns>The current builder.</returns>
            public Builder SetProtocol(int protocol)
            {
                _protocol = protocol;
                return this;
            }

            /// <summary>
            /// Sets the APN roaming protocol.
            /// </summary>
            /// <param name="roaming">The APN roaming protocol.</param>
            /// <returns>The current builder.</returns>
            public Builder SetRoaming(int roaming)
            {
                _roaming = roaming;
                return this;
            }

            /// <summary>
            /// Sets the Mobile Virtual Network Operator type.
            /// </summary>
            /// <param name="mvno">The Mobile Virtual Network Operator type.</param>
            /// <returns>The current builder.</returns>
            public Builder SetMvno(int mvno)
            {
                _mvno = mvno;
                return this;
            }

            /// <summary>
            /// Sets the Mobile Virtual Network Operator value.
            /// </summary>
            /// <param name="mvnoValue">The Mobile Virtual Network Operator value.</param>
            /// <returns>The current builder.</returns>
            public Builder SetMvnoValue(string mvnoValue)
            {
                _mvnoValue = mvnoValue;
                return this;
            }

            /// <summary>
            /// Builds an <see cref="Apn" /> from the current builder values.
            /// </summary>
            /// <returns>The configured APN.</returns>
            /// <exception cref="ArgumentNullException">A required APN value was not set.</exception>
            public Apn Build()
            {
                var apn = new Apn(_name, _url, _mcc, _mnc, _type)
                {
                    Proxy = _proxy,
                    Port = _port,
                    User = _user,
                    Password = _password,
                    Server = _server,
                    Mmsc = _mmsc,
                    MmsProxy = _mmsProxy,
                    MmsPort = _mmsPort,
                    AuthType = _authType,
                    Protocol = _protocol,
                    Roaming = _roaming,
                    Mvno = _mvno,
                    MvnoValue = _mvnoValue
                };

                return apn;
            }
        }
    }

    /// <summary>
    /// Represents device display settings to apply through StartUp.
    /// </summary>
    public sealed class DisplaySetting
    {
        /// <summary>
        /// Creates a display settings configuration.
        /// </summary>
        /// <param name="enableAutoBrightness">Whether the device should automatically adjust screen brightness.</param>
        /// <param name="brightness">The brightness level used when auto-brightness is disabled. Kotlin KDoc defines the supported range as <c>1</c> to <c>255</c>.</param>
        /// <param name="enableAutoRotate">Whether screen orientation should automatically rotate based on physical orientation.</param>
        /// <param name="rotateForce">The forced orientation mode.</param>
        /// <param name="enableScreenLock">Whether the screen lock mechanism is enabled.</param>
        /// <param name="sleepMode">The inactivity duration before the screen goes to sleep.</param>
        /// <param name="policyControl">The visibility policy for system UI elements.</param>
        /// <param name="showBatteryPercentage">Whether battery percentage is explicitly displayed in the status bar.</param>
        /// <param name="screenSaverMode">When the screen saver is activated.</param>
        /// <param name="screenSaverComponent">The component name, in package/class form, of the screen saver application.</param>
        public DisplaySetting(
            bool enableAutoBrightness,
            int brightness,
            bool enableAutoRotate,
            RotateForce rotateForce,
            bool enableScreenLock,
            SleepMode sleepMode,
            PolicyControl policyControl,
            bool showBatteryPercentage,
            ScreenSaverMode screenSaverMode,
            string screenSaverComponent)
        {
            EnableAutoBrightness = enableAutoBrightness;
            Brightness = brightness;
            EnableAutoRotate = enableAutoRotate;
            RotateForce = rotateForce;
            EnableScreenLock = enableScreenLock;
            SleepMode = sleepMode;
            PolicyControl = policyControl;
            ShowBatteryPercentage = showBatteryPercentage;
            ScreenSaverMode = screenSaverMode;
            ScreenSaverComponent = screenSaverComponent;
        }

        /// <summary>
        /// Gets whether the device automatically adjusts screen brightness.
        /// </summary>
        public bool EnableAutoBrightness { get; private set; }

        /// <summary>
        /// Gets the brightness level used when auto-brightness is disabled.
        /// </summary>
        public int Brightness { get; private set; }

        /// <summary>
        /// Gets whether screen orientation automatically rotates based on physical orientation.
        /// </summary>
        public bool EnableAutoRotate { get; private set; }

        /// <summary>
        /// Gets the forced orientation mode.
        /// </summary>
        public RotateForce RotateForce { get; private set; }

        /// <summary>
        /// Gets whether the screen lock mechanism is enabled.
        /// </summary>
        public bool EnableScreenLock { get; private set; }

        /// <summary>
        /// Gets the inactivity duration before the screen goes to sleep.
        /// </summary>
        public SleepMode SleepMode { get; private set; }

        /// <summary>
        /// Gets the visibility policy for system UI elements.
        /// </summary>
        public PolicyControl PolicyControl { get; private set; }

        /// <summary>
        /// Gets whether battery percentage is explicitly displayed in the status bar.
        /// </summary>
        public bool ShowBatteryPercentage { get; private set; }

        /// <summary>
        /// Gets when the screen saver is activated.
        /// </summary>
        public ScreenSaverMode ScreenSaverMode { get; private set; }

        /// <summary>
        /// Gets the component name, in package/class form, of the screen saver application.
        /// </summary>
        public string ScreenSaverComponent { get; private set; }
    }

    /// <summary>
    /// Defines the display sleep timeout sent to StartUp.
    /// </summary>
    public enum SleepMode
    {
        /// <summary>
        /// Turns the screen off after 15 seconds of inactivity.
        /// </summary>
        Seconds15 = 15000,

        /// <summary>
        /// Turns the screen off after 30 seconds of inactivity.
        /// </summary>
        Seconds30 = 30000,

        /// <summary>
        /// Turns the screen off after 1 minute of inactivity.
        /// </summary>
        Minutes1 = 60000,

        /// <summary>
        /// Turns the screen off after 2 minutes of inactivity.
        /// </summary>
        Minutes2 = 120000,

        /// <summary>
        /// Turns the screen off after 5 minutes of inactivity.
        /// </summary>
        Minutes5 = 300000,

        /// <summary>
        /// Turns the screen off after 10 minutes of inactivity.
        /// </summary>
        Minutes10 = 600000,

        /// <summary>
        /// Turns the screen off after 30 minutes of inactivity.
        /// </summary>
        Minutes30 = 1800000,

        /// <summary>
        /// Keeps the screen from sleeping automatically.
        /// </summary>
        Never = int.MaxValue
    }

    /// <summary>
    /// Defines forced display orientation modes.
    /// </summary>
    public enum RotateForce
    {
        /// <summary>
        /// Uses the default orientation behavior.
        /// </summary>
        Default = 0,

        /// <summary>
        /// Uses automatic orientation behavior.
        /// </summary>
        Automatic = 1,

        /// <summary>
        /// Forces landscape orientation.
        /// </summary>
        Landscape = 2,

        /// <summary>
        /// Forces reverse landscape orientation.
        /// </summary>
        LandscapeReverse = 3,

        /// <summary>
        /// Uses landscape sensor orientation.
        /// </summary>
        LandscapeSensor = 4,

        /// <summary>
        /// Forces portrait orientation.
        /// </summary>
        Portrait = 5,

        /// <summary>
        /// Forces reverse portrait orientation.
        /// </summary>
        PortraitReverse = 6,

        /// <summary>
        /// Uses portrait sensor orientation.
        /// </summary>
        PortraitSensor = 7
    }

    /// <summary>
    /// Defines which system UI bars StartUp should hide or show.
    /// </summary>
    public enum PolicyControl
    {
        /// <summary>
        /// Hides the status bar.
        /// </summary>
        HideStatusBar = 1,

        /// <summary>
        /// Hides the navigation bar.
        /// </summary>
        HideNavigationBar = 2,

        /// <summary>
        /// Hides both status and navigation bars.
        /// </summary>
        HideSystemBar = 3,

        /// <summary>
        /// Uses the default system UI visibility policy.
        /// </summary>
        Default = 4
    }

    /// <summary>
    /// Defines when the screen saver should activate.
    /// </summary>
    public enum ScreenSaverMode
    {
        /// <summary>
        /// Activates the screen saver while charging.
        /// </summary>
        WhileCharging = 0,

        /// <summary>
        /// Activates the screen saver while docked.
        /// </summary>
        WhileDocked = 1,

        /// <summary>
        /// Activates the screen saver while charging or docked.
        /// </summary>
        WhileChargingOrDocked = 2,

        /// <summary>
        /// Disables screen saver activation.
        /// </summary>
        Never = 3
    }

    /// <summary>
    /// Represents a Quick Settings tile to add to the system UI.
    /// </summary>
    public sealed class QuickTile
    {
        /// <summary>
        /// Creates a Quick Settings tile configuration.
        /// </summary>
        /// <param name="id">The unique tile identifier.</param>
        /// <param name="name">The display name shown in the Quick Settings UI.</param>
        /// <exception cref="ArgumentNullException"><paramref name="name" /> is <c>null</c>.</exception>
        public QuickTile(QuickTileId id, string name)
        {
            if (name == null)
                throw new ArgumentNullException(nameof(name));

            Id = id;
            Name = name;
        }

        /// <summary>
        /// Gets the unique tile identifier.
        /// </summary>
        public QuickTileId Id { get; private set; }

        /// <summary>
        /// Gets the display name shown in the Quick Settings UI.
        /// </summary>
        public string Name { get; private set; }
    }

    /// <summary>
    /// Defines Quick Settings tile identifiers supported by StartUp.
    /// </summary>
    public enum QuickTileId
    {
        /// <summary>
        /// Wi-Fi tile.
        /// </summary>
        Wifi,

        /// <summary>
        /// Bluetooth tile.
        /// </summary>
        Bluetooth,

        /// <summary>
        /// Flashlight tile.
        /// </summary>
        Flashlight,

        /// <summary>
        /// Do Not Disturb tile.
        /// </summary>
        DoNotDisturb,

        /// <summary>
        /// Auto-rotation tile.
        /// </summary>
        AutoRotation,

        /// <summary>
        /// Battery Saver tile.
        /// </summary>
        BatterySaver,

        /// <summary>
        /// Airplane Mode tile.
        /// </summary>
        AirplaneMode,

        /// <summary>
        /// Night Light tile.
        /// </summary>
        NightLight,

        /// <summary>
        /// Screen Record tile.
        /// </summary>
        ScreenRecord,

        /// <summary>
        /// QR Code Scanner tile.
        /// </summary>
        QrCodeScanner,

        /// <summary>
        /// Alarm tile.
        /// </summary>
        Alarm,

        /// <summary>
        /// Device Controls tile.
        /// </summary>
        DeviceControls,

        /// <summary>
        /// Wallet tile.
        /// </summary>
        Wallet,

        /// <summary>
        /// Screen Cast tile.
        /// </summary>
        ScreenCast,

        /// <summary>
        /// Location tile.
        /// </summary>
        Location,

        /// <summary>
        /// Hotspot tile.
        /// </summary>
        Hotspot,

        /// <summary>
        /// Color Inversion tile.
        /// </summary>
        ColorInversion,

        /// <summary>
        /// Data Saver tile.
        /// </summary>
        DataSaver,

        /// <summary>
        /// Dark Theme tile.
        /// </summary>
        DarkTheme
    }

    internal static class StartupModelExtensions
    {
        /// <summary>
        /// Converts a quick settings tile identifier to the StartUp app request value.
        /// </summary>
        /// <param name="id">The quick settings tile identifier.</param>
        /// <returns>The request value expected by the StartUp app.</returns>
        public static string ToRequestValue(this QuickTileId id)
        {
            switch (id)
            {
                case QuickTileId.Wifi:
                    return "wifi";
                case QuickTileId.Bluetooth:
                    return "bt";
                case QuickTileId.Flashlight:
                    return "flashlight";
                case QuickTileId.DoNotDisturb:
                    return "dnd";
                case QuickTileId.AutoRotation:
                    return "rotation";
                case QuickTileId.BatterySaver:
                    return "battery";
                case QuickTileId.AirplaneMode:
                    return "airplane";
                case QuickTileId.NightLight:
                    return "night";
                case QuickTileId.ScreenRecord:
                    return "screenrecord";
                case QuickTileId.QrCodeScanner:
                    return "qr_code_scanner";
                case QuickTileId.Alarm:
                    return "alarm";
                case QuickTileId.DeviceControls:
                    return "controls";
                case QuickTileId.Wallet:
                    return "wallet";
                case QuickTileId.ScreenCast:
                    return "cast";
                case QuickTileId.Location:
                    return "location";
                case QuickTileId.Hotspot:
                    return "hotspot";
                case QuickTileId.ColorInversion:
                    return "inversion";
                case QuickTileId.DataSaver:
                    return "saver";
                case QuickTileId.DarkTheme:
                    return "dark";
                default:
                    throw new ArgumentOutOfRangeException(nameof(id));
            }
        }
    }
}
