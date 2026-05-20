using System;
using System.Collections.Generic;
using System.Globalization;
using System.Threading;
using System.Threading.Tasks;
using Android.Content;
using Android.OS;
using M3Sdk.Xamarin.Internal;
using Org.Json;

namespace M3Sdk.Xamarin.Startup
{
    /// <summary>
    /// StartUp app API surface implemented by native Android broadcasts.
    /// </summary>
    public sealed class StartUpApi : IStartUpApi
    {
        private static readonly ISet<DeviceModel> UsbSupportedModels =
            new HashSet<DeviceModel> { DeviceModel.US20, DeviceModel.US30 };

        private static readonly ISet<DeviceModel> UnsupportedSl20 =
            new HashSet<DeviceModel> { DeviceModel.SL20 };

        private static readonly ISet<DeviceModel> UnsupportedLegacyWifi =
            new HashSet<DeviceModel> { DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K };

        private static readonly ISet<DeviceModel> UnsupportedSl10 =
            new HashSet<DeviceModel> { DeviceModel.SL10, DeviceModel.SL10K };

        private static readonly IDictionary<DeviceModel, string> BluetoothMacVersionOverrides =
            new Dictionary<DeviceModel, string> { { DeviceModel.UL30, "6.5.31" } };

        private readonly Context _context;
        private readonly M3SdkGuard _guard;

        internal StartUpApi(Context context, M3SdkGuard guard)
        {
            _context = context;
            _guard = guard;
        }

        /// <inheritdoc />
        public void TurnOnAirplaneMode()
        {
            GuardStartUp("TurnOnAirplaneMode", "6.3.7");
            SendSystem(Constants.StartUp.TypeAirplane, BooleanExtra(Constants.StartUp.TypeAirplane, true));
        }

        /// <inheritdoc />
        public void TurnOffAirplaneMode()
        {
            GuardStartUp("TurnOffAirplaneMode", "6.3.7");
            SendSystem(Constants.StartUp.TypeAirplane, BooleanExtra(Constants.StartUp.TypeAirplane, false));
        }

        /// <inheritdoc />
        public void InstallLocalApk(string filePath)
        {
            if (filePath == null)
                throw new ArgumentNullException(nameof(filePath));

            GuardStartUp("InstallLocalApk", "6.2.14");
            var extras = new Bundle();
            extras.PutInt(Constants.StartUp.ExtraInstallApkType, Constants.StartUp.InstallLocalApk);
            extras.PutString(Constants.StartUp.ExtraInstallLocalApkPath, filePath);
            SendSystem(Constants.StartUp.TypeInstallApk, extras);
        }

        /// <inheritdoc />
        public void InstallRemoteApk(string url)
        {
            if (url == null)
                throw new ArgumentNullException(nameof(url));

            GuardStartUp("InstallRemoteApk", "6.2.14");
            var extras = new Bundle();
            extras.PutInt(Constants.StartUp.ExtraInstallApkType, Constants.StartUp.InstallRemoteApk);
            extras.PutString(Constants.StartUp.ExtraInstallRemoteApkPath, url);
            SendSystem(Constants.StartUp.TypeInstallApk, extras);
        }

        /// <inheritdoc />
        public void EnableApp(string packageName)
        {
            SetAppState("EnableApp", packageName, true);
        }

        /// <inheritdoc />
        public void DisableApp(string packageName)
        {
            SetAppState("DisableApp", packageName, false);
        }

        /// <inheritdoc />
        public void SetMediaVolume(int value)
        {
            SetVolume("SetMediaVolume", Constants.StartUp.ExtraVolumeMedia, value);
        }

        /// <inheritdoc />
        public void SetRingtoneVolume(int value)
        {
            SetVolume("SetRingtoneVolume", Constants.StartUp.ExtraVolumeRingtone, value);
        }

        /// <inheritdoc />
        public void SetNotificationVolume(int value)
        {
            SetVolume("SetNotificationVolume", Constants.StartUp.ExtraVolumeNotification, value);
        }

        /// <inheritdoc />
        public void SetAlarmVolume(int value)
        {
            SetVolume("SetAlarmVolume", Constants.StartUp.ExtraVolumeAlarm, value);
        }

        /// <inheritdoc />
        public void EnableVibrationMode()
        {
            SetVibrationMode("EnableVibrationMode", true);
        }

        /// <inheritdoc />
        public void DisableVibrationMode()
        {
            SetVibrationMode("DisableVibrationMode", false);
        }

        /// <inheritdoc />
        public void SetDisplaySetting(DisplaySetting displaySetting)
        {
            if (displaySetting == null)
                throw new ArgumentNullException(nameof(displaySetting));

            GuardStartUp("SetDisplaySetting", "6.2.14");
            var extras = new Bundle();
            extras.PutBoolean(Constants.StartUp.ExtraDisplayAutoBrightness, displaySetting.EnableAutoBrightness);
            extras.PutInt(Constants.StartUp.ExtraDisplayBrightnessStep, displaySetting.Brightness);
            extras.PutBoolean(Constants.StartUp.ExtraDisplayAutoRotate, displaySetting.EnableAutoRotate);
            extras.PutBoolean(Constants.StartUp.ExtraDisplayDisableScreenLock, !displaySetting.EnableScreenLock);
            extras.PutInt(Constants.StartUp.ExtraDisplaySleep, (int)displaySetting.SleepMode);
            extras.PutInt(Constants.StartUp.ExtraDisplayPolicyControl, (int)displaySetting.PolicyControl);
            extras.PutInt(
                Constants.StartUp.ExtraDisplayBatteryPercent,
                displaySetting.ShowBatteryPercentage
                    ? Constants.StartUp.ShowBatteryPercent
                    : Constants.StartUp.NotShowBatteryPercent);
            extras.PutInt(Constants.StartUp.ExtraDisplayScreensaverMode, (int)displaySetting.ScreenSaverMode);
            extras.PutString(Constants.StartUp.ExtraDisplayScreensaverComponent, displaySetting.ScreenSaverComponent);
            extras.PutInt(Constants.StartUp.ExtraDisplayRotateForce, (int)displaySetting.RotateForce);
            SendConfig(Constants.StartUp.TypeDisplay, extras);
        }

        /// <inheritdoc />
        public Task<string> GetSerialNumberAsync()
        {
            return GetSerialNumberAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetSerialNumberAsync(CancellationToken cancellationToken)
        {
            GuardStartUp("GetSerialNumber", "6.2.14");
            return new StartUpStringRequester(_context, Constants.StartUp.TypeSerialNumber).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetSerialNumber(M3RequestCallback<string> callback)
        {
            return CallbackRunner.Run(GetSerialNumberAsync, callback);
        }

        /// <inheritdoc />
        public Task<string> GetBluetoothMacAsync()
        {
            return GetBluetoothMacAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetBluetoothMacAsync(CancellationToken cancellationToken)
        {
            GuardStartUp("GetBluetoothMac", "6.5.35", null, null, BluetoothMacVersionOverrides);
            return new StartUpStringRequester(_context, Constants.StartUp.TypeBluetoothMac).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetBluetoothMac(M3RequestCallback<string> callback)
        {
            return CallbackRunner.Run(GetBluetoothMacAsync, callback);
        }

        /// <inheritdoc />
        public void LockStatusBarExpansion()
        {
            SetStatusBarExpansion("LockStatusBarExpansion", true);
        }

        /// <inheritdoc />
        public void UnlockStatusBarExpansion()
        {
            SetStatusBarExpansion("UnlockStatusBarExpansion", false);
        }

        /// <inheritdoc />
        public void SetLanguage(string language, string country)
        {
            if (language == null)
                throw new ArgumentNullException(nameof(language));
            if (country == null)
                throw new ArgumentNullException(nameof(country));

            GuardStartUp("SetLanguage", "6.2.14");
            SendConfig(
                Constants.StartUp.TypeLanguage,
                StringExtra(Constants.StartUp.ExtraLanguageTag, language + "-" + country));
        }

        /// <inheritdoc />
        public void SetApn(Apn apn)
        {
            if (apn == null)
                throw new ArgumentNullException(nameof(apn));

            GuardStartUp("SetApn", "6.2.14");
            new StartUpFinishRequester(_context, Constants.StartUp.TypeApn, BuildApnExtras(apn)).Request();
        }

        /// <inheritdoc />
        public void EnableNfc()
        {
            SetNfc("EnableNfc", true);
        }

        /// <inheritdoc />
        public void DisableNfc()
        {
            SetNfc("DisableNfc", false);
        }

        /// <inheritdoc />
        public void GrantPermission(string packageName, string permission)
        {
            SetPermission("GrantPermission", packageName, permission, Constants.StartUp.GrantPermission);
        }

        /// <inheritdoc />
        public void RevokePermission(string packageName, string permission)
        {
            SetPermission("RevokePermission", packageName, permission, Constants.StartUp.RevokePermission);
        }

        /// <inheritdoc />
        public void SetQuickTiles(params QuickTile[] quickTiles)
        {
            GuardStartUp("SetQuickTiles", "6.4.1");
            new StartUpFinishRequester(_context, Constants.StartUp.TypeQuickTile, BuildQuickTileExtras(quickTiles)).Request();
        }

        /// <inheritdoc />
        public void ResetQuickTile()
        {
            GuardStartUp("ResetQuickTile", "6.4.1");
            var extras = new Bundle();
            extras.PutString(Constants.StartUp.ExtraQuickTileAction, Constants.StartUp.ResetQuickTile);
            extras.PutString(Constants.StartUp.ExtraQuickTileItems, Constants.StartUp.EmptyQuickTileItems);
            new StartUpFinishRequester(_context, Constants.StartUp.TypeQuickTile, extras).Request();
        }

        /// <inheritdoc />
        public void ResetStartUpSetting()
        {
            GuardStartUp("ResetStartUpSetting", "6.2.14");
            new StartUpFinishRequester(
                _context,
                Constants.StartUp.TypeOption,
                BooleanExtra(Constants.StartUp.ExtraResetStartUpSetting, true)).Request();
        }

        /// <inheritdoc />
        public void SetDateTime(DateTime dateTime)
        {
            GuardStartUp("SetDateTime", "6.2.14");
            SendSystem(Constants.StartUp.TypeDateTime, BuildDateTimeExtras(dateTime));
        }

        /// <inheritdoc />
        public void SetDateTime(DateTimeOffset dateTime)
        {
            SetDateTime(dateTime.LocalDateTime);
        }

        /// <inheritdoc />
        public void SetDateTime(int year, int month, int day, int hour, int minute, int second)
        {
            SetDateTime(new DateTime(year, month, day, hour, minute, second));
        }

        /// <inheritdoc />
        public void SetNtpServer(string host)
        {
            if (host == null)
                throw new ArgumentNullException(nameof(host));

            GuardStartUp("SetNtpServer", "6.4.9");
            SendSystem(Constants.StartUp.TypeNtp, StringExtra(Constants.StartUp.ExtraNtpServer, host));
        }

        /// <inheritdoc />
        public void SetTimeZone(string timezone)
        {
            if (timezone == null)
                throw new ArgumentNullException(nameof(timezone));

            GuardStartUp("SetTimeZone", "6.5.9");
            SendSystem(Constants.StartUp.TypeTimeZone, StringExtra(Constants.StartUp.ExtraTimeZone, timezone));
        }

        /// <inheritdoc />
        public void SetUsbModeMtp()
        {
            SetUsbMode("SetUsbModeMtp", Constants.StartUp.UsbModeMtp);
        }

        /// <inheritdoc />
        public void SetUsbModeRndis()
        {
            SetUsbMode("SetUsbModeRndis", Constants.StartUp.UsbModeRndis);
        }

        /// <inheritdoc />
        public void SetUsbModeMidi()
        {
            SetUsbMode("SetUsbModeMidi", Constants.StartUp.UsbModeMidi);
        }

        /// <inheritdoc />
        public void SetUsbModePtp()
        {
            SetUsbMode("SetUsbModePtp", Constants.StartUp.UsbModePtp);
        }

        /// <inheritdoc />
        public void SetUsbModeNone()
        {
            SetUsbMode("SetUsbModeNone", Constants.StartUp.UsbModeNone);
        }

        /// <inheritdoc />
        public Task<string> GetWifiMacAsync()
        {
            return GetWifiMacAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetWifiMacAsync(CancellationToken cancellationToken)
        {
            GuardStartUp("GetWifiMac", "6.4.11");
            return new StartUpStringRequester(_context, Constants.StartUp.TypeWifiMac).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetWifiMac(M3RequestCallback<string> callback)
        {
            return CallbackRunner.Run(GetWifiMacAsync, callback);
        }

        /// <inheritdoc />
        public void EnableCaptivePortalDetection()
        {
            GuardStartUp(
                "EnableCaptivePortalDetection",
                "6.2.14",
                null,
                UnsupportedSl20,
                null);
            SendConfig(
                Constants.StartUp.TypeCaptivePortal,
                IntExtra(Constants.StartUp.ExtraCaptivePortalDetection, Constants.StartUp.EnableCaptivePortalDetection));
        }

        /// <inheritdoc />
        public void DisableCaptivePortalDetection()
        {
            GuardStartUp(
                "DisableCaptivePortalDetection",
                "6.2.14",
                null,
                UnsupportedSl20,
                null);
            SendConfig(
                Constants.StartUp.TypeCaptivePortal,
                IntExtra(Constants.StartUp.ExtraCaptivePortalDetection, Constants.StartUp.DisableCaptivePortalDetection));
        }

        /// <inheritdoc />
        public void AllowAllWifiFrequencyBand()
        {
            SetWifiFrequencyBand("AllowAllWifiFrequencyBand", Constants.StartUp.AllowAllFrequencyBand);
        }

        /// <inheritdoc />
        public void AllowOnly2_4GHzWifiFrequencyBand()
        {
            SetWifiFrequencyBand("AllowOnly2_4GHzWifiFrequencyBand", Constants.StartUp.AllowOnly24GHzFrequencyBand);
        }

        /// <inheritdoc />
        public void AllowOnly5GHzWifiFrequencyBand()
        {
            SetWifiFrequencyBand("AllowOnly5GHzWifiFrequencyBand", Constants.StartUp.AllowOnly5GHzFrequencyBand);
        }

        /// <inheritdoc />
        public void SetWifiCountry(string countryCode)
        {
            if (countryCode == null)
                throw new ArgumentNullException(nameof(countryCode));

            GuardStartUp("SetWifiCountry", "6.2.14", null, UnsupportedSl10, null);
            SendConfig(Constants.StartUp.TypeWifiCountryCode, StringExtra(Constants.StartUp.ExtraWifiCountry, countryCode));
        }

        /// <inheritdoc />
        public void EnableOpenNetworkNotification()
        {
            SetOpenNetworkNotification("EnableOpenNetworkNotification", Constants.StartUp.EnableOpenNetworkNotification);
        }

        /// <inheritdoc />
        public void DisableOpenNetworkNotification()
        {
            SetOpenNetworkNotification("DisableOpenNetworkNotification", Constants.StartUp.DisableOpenNetworkNotification);
        }

        /// <inheritdoc />
        public void SetRoamingTrigger(int index)
        {
            GuardStartUp("SetRoamingTrigger", "6.2.14", null, UnsupportedSl10, null);
            SendConfig(Constants.StartUp.TypeWifiRoamingTrigger, StringExtra(Constants.StartUp.ExtraRoamingThreshold, index.ToString(CultureInfo.InvariantCulture)));
        }

        /// <inheritdoc />
        public void SetRoamingDelta(int index)
        {
            GuardStartUp("SetRoamingDelta", "6.2.14", null, UnsupportedSl10, null);
            SendConfig(Constants.StartUp.TypeWifiRoamingDelta, StringExtra(Constants.StartUp.ExtraRoamingDelta, index.ToString(CultureInfo.InvariantCulture)));
        }

        /// <inheritdoc />
        public void SetWifiSleepPolicyNever()
        {
            SetWifiSleepPolicy("SetWifiSleepPolicyNever", Constants.StartUp.WifiSleepNever);
        }

        /// <inheritdoc />
        public void SetWifiSleepPolicyPluggedOnly()
        {
            SetWifiSleepPolicy("SetWifiSleepPolicyPluggedOnly", Constants.StartUp.WifiSleepPluggedOnly);
        }

        /// <inheritdoc />
        public void SetWifiSleepPolicyAlways()
        {
            SetWifiSleepPolicy("SetWifiSleepPolicyAlways", Constants.StartUp.WifiSleepAlways);
        }

        /// <inheritdoc />
        public void SetWifiStabilityNormal()
        {
            SetWifiStability("SetWifiStabilityNormal", Constants.StartUp.WifiNormalStability);
        }

        /// <inheritdoc />
        public void SetWifiStabilityHigh()
        {
            SetWifiStability("SetWifiStabilityHigh", Constants.StartUp.WifiHighStability);
        }

        /// <inheritdoc />
        public void SetWifiChannel(params int[] channels)
        {
            GuardStartUp("SetWifiChannel", "6.2.14", null, UnsupportedLegacyWifi, null);
            var channelStrings = new string[channels == null ? 0 : channels.Length];
            for (var i = 0; i < channelStrings.Length; i++)
                channelStrings[i] = channels[i].ToString(CultureInfo.InvariantCulture);

            var extras = new Bundle();
            extras.PutStringArray(Constants.StartUp.ExtraWifiChannels, channelStrings);
            SendConfig(Constants.StartUp.TypeWifiChannel, extras);
        }

        /// <inheritdoc />
        public void SetAccessPoint(AccessPoint accessPoint)
        {
            if (accessPoint == null)
                throw new ArgumentNullException(nameof(accessPoint));

            GuardStartUp("SetAccessPoint", "6.2.14");
            SendSystem(Constants.StartUp.TypeAccessPoint, BuildAccessPointExtras(accessPoint));
        }

        /// <inheritdoc />
        public void ClearSavedWifiNetworks()
        {
            GuardStartUp("ClearSavedWifiNetworks", "6.4.11");
            SendSystem(Constants.StartUp.TypeClearWifi, null);
        }

        /// <inheritdoc />
        public void RemoveWifiNetwork(string ssid)
        {
            if (ssid == null)
                throw new ArgumentNullException(nameof(ssid));

            GuardStartUp("RemoveWifiNetwork", "6.4.11");
            SendSystem(Constants.StartUp.TypeRemoveWifi, StringExtra(Constants.StartUp.ExtraWifiSsid, ssid));
        }

        private void SetAppState(string methodName, string packageName, bool enabled)
        {
            if (packageName == null)
                throw new ArgumentNullException(nameof(packageName));

            GuardStartUp(methodName, "6.2.14");
            var extras = new Bundle();
            extras.PutString(Constants.StartUp.ExtraSetAppStatePackageName, packageName);
            extras.PutBoolean(Constants.StartUp.ExtraSetAppState, enabled);
            SendSystem(Constants.StartUp.TypeApplication, extras);
        }

        private void SetVolume(string methodName, string extraKey, int value)
        {
            GuardStartUp(methodName, "6.2.14");
            SendConfig(Constants.StartUp.TypeVolume, IntExtra(extraKey, value));
        }

        private void SetVibrationMode(string methodName, bool enabled)
        {
            GuardStartUp(methodName, "6.2.14");
            SendConfig(Constants.StartUp.TypeVolume, BooleanExtra(Constants.StartUp.ExtraEnableVolumeVibrator, enabled));
        }

        private void SetStatusBarExpansion(string methodName, bool locked)
        {
            GuardStartUp(methodName, "6.4.12");
            SendSystem(Constants.StartUp.TypeStatusBar, BooleanExtra(Constants.StartUp.ExtraLockStatusBarExpansions, locked));
        }

        private void SetNfc(string methodName, bool enabled)
        {
            GuardStartUp(methodName, "6.2.14");
            SendConfig(Constants.StartUp.TypeNfc, BooleanExtra(Constants.StartUp.ExtraEnableNfc, enabled));
        }

        private void SetPermission(string methodName, string packageName, string permission, int mode)
        {
            if (packageName == null)
                throw new ArgumentNullException(nameof(packageName));
            if (permission == null)
                throw new ArgumentNullException(nameof(permission));

            GuardStartUp(methodName, "6.4.17");
            var extras = new Bundle();
            extras.PutString(Constants.StartUp.ExtraPermissionPackage, packageName);
            extras.PutString(Constants.StartUp.ExtraPermissionName, permission);
            extras.PutInt(Constants.StartUp.ExtraPermissionMode, mode);
            SendSystem(Constants.StartUp.TypePermission, extras);
        }

        private void SetUsbMode(string methodName, string mode)
        {
            GuardStartUp(methodName, "6.5.10", UsbSupportedModels, null, null);
            SendSystem(Constants.StartUp.TypeUsbSetting, StringExtra(Constants.StartUp.ExtraUsbMode, mode));
        }

        private void SetWifiFrequencyBand(string methodName, int value)
        {
            GuardStartUp(methodName, "6.2.14", null, UnsupportedLegacyWifi, null);
            SendConfig(Constants.StartUp.TypeWifiFrequencyBand, IntExtra(Constants.StartUp.ExtraFrequencyBand, value));
        }

        private void SetOpenNetworkNotification(string methodName, int value)
        {
            GuardStartUp(methodName, "6.2.14");
            SendConfig(Constants.StartUp.TypeWifiOpenNotification, IntExtra(Constants.StartUp.ExtraOpenNetworkNotification, value));
        }

        private void SetWifiSleepPolicy(string methodName, int value)
        {
            GuardStartUp(methodName, "6.2.14");
            SendConfig(Constants.StartUp.TypeWifiSleep, IntExtra(Constants.StartUp.ExtraWifiSleepPolicy, value));
        }

        private void SetWifiStability(string methodName, int value)
        {
            GuardStartUp(methodName, "6.2.14");
            SendConfig(Constants.StartUp.TypeWifiStability, IntExtra(Constants.StartUp.ExtraWifiStability, value));
        }

        private void SendSystem(string typeValue, Bundle extras)
        {
            new StartUpBroadcastRequester(_context, Constants.StartUp.RequestSystem, typeValue, extras).Request();
        }

        private void SendConfig(string typeValue, Bundle extras)
        {
            new StartUpBroadcastRequester(_context, Constants.StartUp.RequestConfig, typeValue, extras).Request();
        }

        private void GuardStartUp(string methodName, string version)
        {
            GuardStartUp(methodName, version, null, null, null);
        }

        private void GuardStartUp(
            string methodName,
            string version,
            ISet<DeviceModel> supportedModels,
            ISet<DeviceModel> unsupportedModels,
            IDictionary<DeviceModel, string> modelVersionOverrides)
        {
            _guard.AssertDeviceSupport(methodName, supportedModels, unsupportedModels);
            _guard.AssertStartUpVersion(methodName, version, modelVersionOverrides);
        }

        private static Bundle BooleanExtra(string key, bool value)
        {
            var extras = new Bundle();
            extras.PutBoolean(key, value);
            return extras;
        }

        private static Bundle IntExtra(string key, int value)
        {
            var extras = new Bundle();
            extras.PutInt(key, value);
            return extras;
        }

        private static Bundle StringExtra(string key, string value)
        {
            var extras = new Bundle();
            extras.PutString(key, value);
            return extras;
        }

        private static Bundle BuildDateTimeExtras(DateTime dateTime)
        {
            var extras = new Bundle();
            extras.PutString(Constants.StartUp.ExtraDate, dateTime.ToString("yyyy-MM-dd", CultureInfo.InvariantCulture));
            extras.PutString(Constants.StartUp.ExtraTime, dateTime.ToString("HH:mm:ss", CultureInfo.InvariantCulture));
            return extras;
        }

        private static Bundle BuildApnExtras(Apn apn)
        {
            var extras = new Bundle();
            extras.PutString(Constants.StartUp.ExtraApnName, apn.Name);
            extras.PutString(Constants.StartUp.ExtraApnUrl, apn.Url);
            extras.PutString(Constants.StartUp.ExtraApnMcc, apn.Mcc);
            extras.PutString(Constants.StartUp.ExtraApnMnc, apn.Mnc);
            extras.PutString(Constants.StartUp.ExtraApnType, apn.Type);

            PutOptionalString(extras, Constants.StartUp.ExtraApnProxy, apn.Proxy);
            PutOptionalString(extras, Constants.StartUp.ExtraApnPort, apn.Port);
            PutOptionalString(extras, Constants.StartUp.ExtraApnUser, apn.User);
            if (apn.Password != null)
                extras.PutString(Constants.StartUp.ExtraApnServer, apn.Server);
            PutOptionalString(extras, Constants.StartUp.ExtraApnMmsc, apn.Mmsc);
            PutOptionalString(extras, Constants.StartUp.ExtraApnMmsProxy, apn.MmsProxy);
            PutOptionalString(extras, Constants.StartUp.ExtraApnMmsPort, apn.MmsPort);
            PutOptionalInt(extras, Constants.StartUp.ExtraApnAuthType, apn.AuthType);
            PutOptionalInt(extras, Constants.StartUp.ExtraApnProtocol, apn.Protocol);
            PutOptionalInt(extras, Constants.StartUp.ExtraApnRoaming, apn.Roaming);
            PutOptionalInt(extras, Constants.StartUp.ExtraApnMvno, apn.Mvno);
            PutOptionalString(extras, Constants.StartUp.ExtraApnMvnoValue, apn.MvnoValue);

            return extras;
        }

        private static Bundle BuildAccessPointExtras(AccessPoint accessPoint)
        {
            var extras = new Bundle();
            extras.PutString(Constants.StartUp.ExtraAccessPointSsid, accessPoint.Ssid);
            extras.PutString(Constants.StartUp.ExtraAccessPointSecurity, accessPoint.Security);
            PutOptionalString(extras, Constants.StartUp.ExtraAccessPointPassword, accessPoint.Password);
            PutOptionalBool(extras, Constants.StartUp.ExtraAccessPointStaticEnable, accessPoint.EnableStatic);
            PutOptionalString(extras, Constants.StartUp.ExtraAccessPointIpAddress, accessPoint.IpAddress);
            PutOptionalString(extras, Constants.StartUp.ExtraAccessPointMask, accessPoint.Mask);
            PutOptionalString(extras, Constants.StartUp.ExtraAccessPointGateway, accessPoint.Gateway);
            PutOptionalString(extras, Constants.StartUp.ExtraAccessPointDns, accessPoint.Dns);
            PutOptionalBool(extras, Constants.StartUp.ExtraAccessPointMacRandom, accessPoint.MacRandom);
            PutOptionalBool(extras, Constants.StartUp.ExtraAccessPointHiddenSsid, accessPoint.HiddenSsid);
            return extras;
        }

        private static Bundle BuildQuickTileExtras(QuickTile[] quickTiles)
        {
            var extras = new Bundle();
            extras.PutString(Constants.StartUp.ExtraQuickTileAction, Constants.StartUp.AddQuickTile);
            extras.PutString(Constants.StartUp.ExtraQuickTileItems, BuildQuickTilesJsonString(quickTiles));
            return extras;
        }

        private static string BuildQuickTilesJsonString(QuickTile[] quickTiles)
        {
            var jsonArray = new JSONArray();
            if (quickTiles == null)
                return jsonArray.ToString();

            foreach (var quickTile in quickTiles)
            {
                if (quickTile == null)
                    continue;

                var jsonObject = new JSONObject();
                jsonObject.Put(Constants.StartUp.ExtraQuickTileId, quickTile.Id.ToRequestValue());
                jsonObject.Put(Constants.StartUp.ExtraQuickTileDisplayName, quickTile.Name);
                jsonArray.Put(jsonObject);
            }

            return jsonArray.ToString();
        }

        private static void PutOptionalString(Bundle extras, string key, string value)
        {
            if (value != null)
                extras.PutString(key, value);
        }

        private static void PutOptionalInt(Bundle extras, string key, int? value)
        {
            if (value.HasValue)
                extras.PutInt(key, value.Value);
        }

        private static void PutOptionalBool(Bundle extras, string key, bool? value)
        {
            if (value.HasValue)
                extras.PutBoolean(key, value.Value);
        }

        private sealed class StartUpBroadcastRequester : BroadcastRequester
        {
            private readonly string _requestAction;
            private readonly string _typeValue;
            private readonly Bundle _extras;

            internal StartUpBroadcastRequester(Context context, string requestAction, string typeValue, Bundle extras)
                : base(context)
            {
                _requestAction = requestAction;
                _typeValue = typeValue;
                _extras = extras;
            }

            protected override string RequestAction
            {
                get { return _requestAction; }
            }

            protected override string TypeKey
            {
                get { return Constants.StartUp.TypeSetting; }
            }

            protected override string TypeValue
            {
                get { return _typeValue; }
            }

            protected override Bundle CreateExtras()
            {
                return _extras;
            }
        }

        private sealed class StartUpFinishRequester : FinishRequiredBroadcastRequester
        {
            private readonly string _typeValue;
            private readonly Bundle _extras;

            internal StartUpFinishRequester(Context context, string typeValue, Bundle extras)
                : base(context)
            {
                _typeValue = typeValue;
                _extras = extras;
            }

            protected override string RequestAction
            {
                get { return Constants.StartUp.RequestConfig; }
            }

            protected override string TypeKey
            {
                get { return Constants.StartUp.TypeSetting; }
            }

            protected override string TypeValue
            {
                get { return _typeValue; }
            }

            protected override Bundle CreateExtras()
            {
                return _extras;
            }
        }

        private sealed class StartUpStringRequester : AwaitableBroadcastRequester<string>
        {
            private readonly string _typeValue;

            internal StartUpStringRequester(Context context, string typeValue)
                : base(context)
            {
                _typeValue = typeValue;
            }

            protected override string RequestAction
            {
                get { return Constants.StartUp.RequestSystem; }
            }

            protected override string ResponseAction
            {
                get { return Constants.StartUp.ResponseSystem; }
            }

            protected override string TypeKey
            {
                get { return Constants.StartUp.TypeSetting; }
            }

            protected override string TypeValue
            {
                get { return _typeValue; }
            }

            protected override string GetExtra(Intent intent)
            {
                var value = intent.GetStringExtra(_typeValue);
                if (value == null)
                    throw new InvalidOperationException("Failed to get extra '" + _typeValue + "' or type mismatch.");
                return value;
            }
        }
    }
}
