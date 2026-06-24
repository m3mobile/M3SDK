namespace M3Sdk.Xamarin.Internal
{
    internal static class Constants
    {
        internal static class Core
        {
            internal const string StrictModeMetadataName = "M3_STRICT_MODE";
        }

        internal static class StartUp
        {
            internal const string AppName = "StartUp";
            internal const string PackageName = "com.m3.startup";

            internal const string RequestSystem = "com.android.server.startupservice.system";
            internal const string RequestConfig = "com.android.server.startupservice.config";
            internal const string RequestConfigFinish = "com.android.server.startupservice.config.fin";
            internal const string ResponseSystem = "com.android.server.startupservice.system.response";

            internal const string TypeSetting = "setting";

            internal const string TypeBluetoothMac = "get_bluetooth_mac";
            internal const string TypeSerialNumber = "get_serial";
            internal const string TypeWifiMac = "get_wifi_mac";
            internal const string TypeFactoryWifiMac = "get_factory_wifi_mac";
            internal const string TypeFactoryWifiMacSuccess = "get_factory_wifi_mac_success";
            internal const string TypeFactoryWifiMacErrorMessage = "get_factory_wifi_mac_error_message";
            internal const string TypeAirplane = "airplane";
            internal const string TypeInstallApk = "apk_install";
            internal const string TypeApplication = "application";
            internal const string TypePermission = "permission";
            internal const string TypeDateTime = "datetime";
            internal const string TypeNtp = "ntp";
            internal const string TypeTimeZone = "timezone";
            internal const string TypeUsbSetting = "usb_setting";
            internal const string TypeCaptivePortal = "captive_portal";
            internal const string TypeWifiFrequencyBand = "wifi_freq_band";
            internal const string TypeWifiCountryCode = "wifi_country_code";
            internal const string TypeWifiOpenNotification = "wifi_open_noti";
            internal const string TypeVolume = "volume";
            internal const string TypeWifiRoamingTrigger = "wifi_roam_trigger";
            internal const string TypeWifiRoamingDelta = "wifi_roam_delta";
            internal const string TypeWifiSleep = "wifi_sleep";
            internal const string TypeWifiStability = "wifi_stability";
            internal const string TypeWifiChannel = "wifi_channel";
            internal const string TypeApn = "apn";
            internal const string TypeAccessPoint = "access_point";
            internal const string TypeClearWifi = "remove_all_wifi";
            internal const string TypeRemoveWifi = "remove_wifi_by_ssid";
            internal const string TypeDisplay = "display";
            internal const string TypeQuickTile = "quick_tile";
            internal const string TypeOption = "option";
            internal const string TypeLanguage = "language";
            internal const string TypeNfc = "nfc";
            internal const string TypeStatusBar = "status_bar";

            internal const string ExtraInstallApkType = "type";
            internal const string ExtraInstallLocalApkPath = "path";
            internal const string ExtraInstallRemoteApkPath = "url";
            internal const string ExtraSetAppStatePackageName = "package_name";
            internal const string ExtraSetAppState = "enable";
            internal const string ExtraSetAppAutoRun = "auto_run";
            internal const string ExtraSetAppPin = "pin_app";
            internal const string ExtraDisplayAutoBrightness = "display_auto_brightness";
            internal const string ExtraDisplayBrightnessStep = "display_brightness_step";
            internal const string ExtraDisplayAutoRotate = "display_auto_rotate";
            internal const string ExtraDisplayDisableScreenLock = "display_disable_screen_lock";
            internal const string ExtraDisplaySleep = "display_sleep";
            internal const string ExtraDisplayPolicyControl = "display_policy_control";
            internal const string ExtraDisplayBatteryPercent = "display_battery_percentage";
            internal const string ExtraDisplayScreensaverMode = "display_screensaver_mode";
            internal const string ExtraDisplayScreensaverComponent = "display_screensaver_component";
            internal const string ExtraDisplayRotateForce = "display_rotate_force";
            internal const string ExtraEnableVolumeVibrator = "volume_vibrator";
            internal const string ExtraVolumeAlarm = "volume_alarm";
            internal const string ExtraVolumeMedia = "volume_media";
            internal const string ExtraVolumeNotification = "volume_notification";
            internal const string ExtraVolumeRingtone = "volume_ringtone";
            internal const string ExtraApnName = "apn_name";
            internal const string ExtraApnUrl = "apn_url";
            internal const string ExtraApnMcc = "apn_mcc";
            internal const string ExtraApnMnc = "apn_mnc";
            internal const string ExtraApnType = "apn_type";
            internal const string ExtraApnProxy = "apn_proxy";
            internal const string ExtraApnPort = "apn_port";
            internal const string ExtraApnUser = "apn_user";
            internal const string ExtraApnServer = "apn_server";
            internal const string ExtraApnMmsc = "apn_mmsc";
            internal const string ExtraApnMmsProxy = "apn_mms_proxy";
            internal const string ExtraApnMmsPort = "apn_mms_port";
            internal const string ExtraApnAuthType = "apn_auth_type";
            internal const string ExtraApnProtocol = "apn_protocol";
            internal const string ExtraApnRoaming = "apn_roaming";
            internal const string ExtraApnMvno = "apn_mvno";
            internal const string ExtraApnMvnoValue = "apn_mvno_value";
            internal const string ExtraPermissionPackage = "package";
            internal const string ExtraPermissionName = "permission";
            internal const string ExtraPermissionMode = "permission_mode";
            internal const string ExtraQuickTileAction = "quick_tile_action";
            internal const string ExtraQuickTileItems = "quick_tile_items";
            internal const string ExtraQuickTileId = "id";
            internal const string ExtraQuickTileDisplayName = "displayName";
            internal const string ExtraResetStartUpSetting = "option_reset";
            internal const string ExtraDate = "date";
            internal const string ExtraTime = "time";
            internal const string ExtraNtpServer = "ntp_server";
            internal const string ExtraTimeZone = "timezone";
            internal const string ExtraUsbMode = "usb_mode";
            internal const string ExtraWifiSsid = "ssid";
            internal const string ExtraAccessPointSsid = "ssid";
            internal const string ExtraAccessPointSecurity = "security";
            internal const string ExtraAccessPointPassword = "password";
            internal const string ExtraAccessPointStaticEnable = "static_enable";
            internal const string ExtraAccessPointIpAddress = "ip_address";
            internal const string ExtraAccessPointMask = "mask";
            internal const string ExtraAccessPointGateway = "gateway";
            internal const string ExtraAccessPointDns = "dns";
            internal const string ExtraAccessPointMacRandom = "mac_random";
            internal const string ExtraAccessPointHiddenSsid = "hidden_ssid";
            internal const string ExtraCaptivePortalDetection = "value";
            internal const string ExtraFrequencyBand = "value";
            internal const string ExtraOpenNetworkNotification = "value";
            internal const string ExtraRoamingDelta = "value";
            internal const string ExtraRoamingThreshold = "value";
            internal const string ExtraWifiChannels = "value";
            internal const string ExtraWifiCountry = "value";
            internal const string ExtraWifiSleepPolicy = "value";
            internal const string ExtraWifiStability = "value";
            internal const string ExtraLanguageTag = "language_value";
            internal const string ExtraEnableNfc = "nfc_on";
            internal const string ExtraLockStatusBarExpansions = "prevent";

            internal const int InstallLocalApk = 0;
            internal const int InstallRemoteApk = 1;
            internal const int ShowBatteryPercent = 1;
            internal const int NotShowBatteryPercent = 2;
            internal const int RevokePermission = 2;
            internal const int GrantPermission = 1;
            internal const string AddQuickTile = "add";
            internal const string ResetQuickTile = "reset";
            internal const string EmptyQuickTileItems = "[]";
            internal const string UsbModeMtp = "mtp";
            internal const string UsbModeRndis = "rndis";
            internal const string UsbModeMidi = "midi";
            internal const string UsbModePtp = "ptp";
            internal const string UsbModeNone = "none";
            internal const int EnableCaptivePortalDetection = 1;
            internal const int DisableCaptivePortalDetection = 0;
            internal const int AllowOnly5GHzFrequencyBand = 2;
            internal const int AllowOnly24GHzFrequencyBand = 1;
            internal const int AllowAllFrequencyBand = 0;
            internal const int EnableOpenNetworkNotification = 1;
            internal const int DisableOpenNetworkNotification = 0;
            internal const int WifiSleepNever = 0;
            internal const int WifiSleepPluggedOnly = 1;
            internal const int WifiSleepAlways = 2;
            internal const int WifiNormalStability = 1;
            internal const int WifiHighStability = 2;
        }

        internal static class ScanEmul
        {
            internal const string AppName = "ScanEmul";
            internal const string PackageName = "net.m3mobile.app.scanemul";

            internal const string StartScan = "android.intent.action.M3SCANNER_BUTTON_DOWN";
            internal const string StopScan = "android.intent.action.M3SCANNER_BUTTON_UP";
            internal const string GetScannerModule = "com.android.server.scannerservice.m3onoff.ison";
            internal const string SetScannerSetting = "com.android.server.scannerservice.settingchange";
            internal const string GetScannerSetting = "com.android.server.scannerservice.getsetting";

            internal const string ResponseScannerModule = "scanemul.action.status";
            internal const string ResponseScannerSetting = "com.android.server.scannerservice.setting";

            internal const string TypeSetting = "setting";
            internal const string TypeSound = "sound";
            internal const string TypeVibration = "vibration";
            internal const string TypeLed = "led";
            internal const string TypeLedTime = "led_time";
            internal const string TypeReadMode = "read_mode";
            internal const string TypeOutputMode = "output_mode";
            internal const string TypeEndCharacter = "end_char";
            internal const string TypePrefix = "prefix";
            internal const string TypePostfix = "postfix";

            internal const string ExtraSound = "sound_mode";
            internal const string ExtraVibration = "vibration_value";
            internal const string ExtraLed = "led_value";
            internal const string ExtraLedTime = "led_time_value";
            internal const string ExtraReadMode = "read_mode_value";
            internal const string ExtraOutputMode = "output_mode_value";
            internal const string ExtraEndCharacter = "end_char_value";
            internal const string ExtraPrefix = "prefix_value";
            internal const string ExtraPostfix = "postfix_value";

            internal const string ResponsePrefix = "m3scanner_prefix";
            internal const string ResponsePostfix = "m3scanner_postfix";
            internal const string ResponseEndCharacter = "m3scanner_endchar";
            internal const string ResponseOutputMode = "m3scanner_output_mode";
            internal const string ResponseProfileEnabled = "is_enable";
            internal const string ResponseReadMode = "m3scanner_read_mode";
            internal const string ResponseScannerModuleType = "m3scanner_module_type";
            internal const string ResponseScannerStatus = "scanemul.extra.status";

            internal const string ConnectionAction = "net.m3mobile.m3sdk.connection";
            internal const string MessageBarcode = "m3scannerdata";
            internal const string MessageCodeType = "m3scanner_code_type";
            internal const string MessageRawData = "m3scannerdata_raw";
            internal const string MessageParsedList = "list";
            internal const string MessageParsedAi = "ai";
            internal const string MessageParsedData = "data";
            internal const string MessageParsedDescription = "description";

            internal const int MessageDecodeComplete = 1;
            internal const int MessageGs1ParsingComplete = 2;
            internal const int MessageDigitalLinkParsingComplete = 3;
        }

        internal static class Shared
        {
            internal const string NtpServerSettingsKey = "ntp_server";
            internal const string DefaultNtpServer = "time.android.com";
            internal const string NtpIntervalResourceName = "config_ntpPollingInterval";
            internal const string AndroidResourceTypeInteger = "integer";
            internal const string AndroidResourcePackage = "android";
            internal const string UsbStateAction = "android.hardware.usb.action.USB_STATE";
            internal const string WifiRoamingThresholdSettingsKey = "wifi_roam_trigger";
            internal const string WifiRoamingDeltaSettingsKey = "wifi_roam_delta";
            internal const string WifiFrequencyBandSettingsKey = "wifi_frequency_band";
            internal const string WifiCountryCodeSettingsKey = "wifi_country_code";
        }
    }
}
