using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Android.Content;
using M3Sdk.Xamarin.Internal;
using M3Sdk.Xamarin.ScanEmul;
using M3Sdk.Xamarin.Shared;
using M3Sdk.Xamarin.Startup;

namespace M3Sdk.Xamarin
{
    /// <summary>
    /// Native C# facade over the M3 Mobile StartUp, ScanEmul, Time, Wifi, and Usb APIs.
    /// </summary>
    public sealed class M3Sdk : IM3Sdk
    {
        private readonly StartUpApi _startUp;
        private readonly ScanEmulApi _scanEmul;
        private readonly TimeApi _time;
        private readonly WifiApi _wifi;
        private readonly UsbApi _usb;
        private bool _disposed;

        internal M3Sdk(Context context)
        {
            var appContext = context.ApplicationContext ?? context;
            var guard = new M3SdkGuard(appContext);

            _startUp = new StartUpApi(appContext, guard);
            _scanEmul = new ScanEmulApi(appContext, guard);
            _time = new TimeApi(appContext, guard);
            _wifi = new WifiApi(appContext, guard);
            _usb = new UsbApi(appContext);
        }

        /// <inheritdoc />
        public IStartUpApi StartUp
        {
            get { return _startUp; }
        }

        /// <inheritdoc />
        public IScanEmulApi ScanEmul
        {
            get { return _scanEmul; }
        }

        /// <inheritdoc />
        public ITimeApi Time
        {
            get { return _time; }
        }

        /// <inheritdoc />
        public IWifiApi Wifi
        {
            get { return _wifi; }
        }

        /// <inheritdoc />
        public IUsbApi Usb
        {
            get { return _usb; }
        }

        /// <inheritdoc />
        public event EventHandler<ScanResultEventArgs> ScanResultReceived
        {
            add
            {
                ThrowIfDisposed();
                _scanEmul.ScanResultReceived += value;
            }
            remove
            {
                _scanEmul.ScanResultReceived -= value;
            }
        }

        /// <inheritdoc />
        public event EventHandler<GS1ParsedEventArgs> GS1ParsedReceived
        {
            add
            {
                ThrowIfDisposed();
                _scanEmul.GS1ParsedReceived += value;
            }
            remove
            {
                _scanEmul.GS1ParsedReceived -= value;
            }
        }

        /// <inheritdoc />
        public event EventHandler<DigitalLinkParsedEventArgs> DigitalLinkParsedReceived
        {
            add
            {
                ThrowIfDisposed();
                _scanEmul.DigitalLinkParsedReceived += value;
            }
            remove
            {
                _scanEmul.DigitalLinkParsedReceived -= value;
            }
        }

        /// <inheritdoc />
        public void TurnOnAirplaneMode()
        {
            ThrowIfDisposed();
            _startUp.TurnOnAirplaneMode();
        }

        /// <inheritdoc />
        public void TurnOffAirplaneMode()
        {
            ThrowIfDisposed();
            _startUp.TurnOffAirplaneMode();
        }

        /// <inheritdoc />
        public void InstallLocalApk(string filePath)
        {
            ThrowIfDisposed();
            _startUp.InstallLocalApk(filePath);
        }

        /// <inheritdoc />
        public void InstallRemoteApk(string url)
        {
            ThrowIfDisposed();
            _startUp.InstallRemoteApk(url);
        }

        /// <inheritdoc />
        public void EnableApp(string packageName)
        {
            ThrowIfDisposed();
            _startUp.EnableApp(packageName);
        }

        /// <inheritdoc />
        public void DisableApp(string packageName)
        {
            ThrowIfDisposed();
            _startUp.DisableApp(packageName);
        }

        /// <inheritdoc />
        public void RunApp(string packageName)
        {
            ThrowIfDisposed();
            _startUp.RunApp(packageName);
        }

        /// <inheritdoc />
        public void RunAndPinApp(string packageName)
        {
            ThrowIfDisposed();
            _startUp.RunAndPinApp(packageName);
        }

        /// <inheritdoc />
        public void SetMediaVolume(int value)
        {
            ThrowIfDisposed();
            _startUp.SetMediaVolume(value);
        }

        /// <inheritdoc />
        public void SetRingtoneVolume(int value)
        {
            ThrowIfDisposed();
            _startUp.SetRingtoneVolume(value);
        }

        /// <inheritdoc />
        public void SetNotificationVolume(int value)
        {
            ThrowIfDisposed();
            _startUp.SetNotificationVolume(value);
        }

        /// <inheritdoc />
        public void SetAlarmVolume(int value)
        {
            ThrowIfDisposed();
            _startUp.SetAlarmVolume(value);
        }

        /// <inheritdoc />
        public void EnableVibrationMode()
        {
            ThrowIfDisposed();
            _startUp.EnableVibrationMode();
        }

        /// <inheritdoc />
        public void DisableVibrationMode()
        {
            ThrowIfDisposed();
            _startUp.DisableVibrationMode();
        }

        /// <inheritdoc />
        public void SetDisplaySetting(DisplaySetting displaySetting)
        {
            ThrowIfDisposed();
            _startUp.SetDisplaySetting(displaySetting);
        }

        /// <inheritdoc />
        public Task<string> GetSerialNumberAsync()
        {
            return GetSerialNumberAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetSerialNumberAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _startUp.GetSerialNumberAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetSerialNumber(M3RequestCallback<string> callback)
        {
            ThrowIfDisposed();
            return _startUp.GetSerialNumber(callback);
        }

        /// <inheritdoc />
        public Task<string> GetBluetoothMacAsync()
        {
            return GetBluetoothMacAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetBluetoothMacAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _startUp.GetBluetoothMacAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetBluetoothMac(M3RequestCallback<string> callback)
        {
            ThrowIfDisposed();
            return _startUp.GetBluetoothMac(callback);
        }

        /// <inheritdoc />
        public void LockStatusBarExpansion()
        {
            ThrowIfDisposed();
            _startUp.LockStatusBarExpansion();
        }

        /// <inheritdoc />
        public void UnlockStatusBarExpansion()
        {
            ThrowIfDisposed();
            _startUp.UnlockStatusBarExpansion();
        }

        /// <inheritdoc />
        public void SetLanguage(string language, string country)
        {
            ThrowIfDisposed();
            _startUp.SetLanguage(language, country);
        }

        /// <inheritdoc />
        public void SetApn(Apn apn)
        {
            ThrowIfDisposed();
            _startUp.SetApn(apn);
        }

        /// <inheritdoc />
        public void EnableNfc()
        {
            ThrowIfDisposed();
            _startUp.EnableNfc();
        }

        /// <inheritdoc />
        public void DisableNfc()
        {
            ThrowIfDisposed();
            _startUp.DisableNfc();
        }

        /// <inheritdoc />
        public void GrantPermission(string packageName, string permission)
        {
            ThrowIfDisposed();
            _startUp.GrantPermission(packageName, permission);
        }

        /// <inheritdoc />
        public void RevokePermission(string packageName, string permission)
        {
            ThrowIfDisposed();
            _startUp.RevokePermission(packageName, permission);
        }

        /// <inheritdoc />
        public void SetQuickTiles(params QuickTile[] quickTiles)
        {
            ThrowIfDisposed();
            _startUp.SetQuickTiles(quickTiles);
        }

        /// <inheritdoc />
        public void ResetQuickTile()
        {
            ThrowIfDisposed();
            _startUp.ResetQuickTile();
        }

        /// <inheritdoc />
        public void ResetStartUpSetting()
        {
            ThrowIfDisposed();
            _startUp.ResetStartUpSetting();
        }

        /// <inheritdoc />
        public void SetDateTime(DateTime dateTime)
        {
            ThrowIfDisposed();
            _startUp.SetDateTime(dateTime);
        }

        /// <inheritdoc />
        public void SetDateTime(DateTimeOffset dateTime)
        {
            ThrowIfDisposed();
            _startUp.SetDateTime(dateTime);
        }

        /// <inheritdoc />
        public void SetDateTime(int year, int month, int day, int hour, int minute, int second)
        {
            ThrowIfDisposed();
            _startUp.SetDateTime(year, month, day, hour, minute, second);
        }

        /// <inheritdoc />
        public void SetNtpServer(string host)
        {
            ThrowIfDisposed();
            _startUp.SetNtpServer(host);
        }

        /// <inheritdoc />
        public void SetTimeZone(string timezone)
        {
            ThrowIfDisposed();
            _startUp.SetTimeZone(timezone);
        }

        /// <inheritdoc />
        public void SetUsbModeMtp()
        {
            ThrowIfDisposed();
            _startUp.SetUsbModeMtp();
        }

        /// <inheritdoc />
        public void SetUsbModeRndis()
        {
            ThrowIfDisposed();
            _startUp.SetUsbModeRndis();
        }

        /// <inheritdoc />
        public void SetUsbModeMidi()
        {
            ThrowIfDisposed();
            _startUp.SetUsbModeMidi();
        }

        /// <inheritdoc />
        public void SetUsbModePtp()
        {
            ThrowIfDisposed();
            _startUp.SetUsbModePtp();
        }

        /// <inheritdoc />
        public void SetUsbModeNone()
        {
            ThrowIfDisposed();
            _startUp.SetUsbModeNone();
        }

        /// <inheritdoc />
        public Task<string> GetWifiMacAsync()
        {
            return GetWifiMacAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetWifiMacAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _startUp.GetWifiMacAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetWifiMac(M3RequestCallback<string> callback)
        {
            ThrowIfDisposed();
            return _startUp.GetWifiMac(callback);
        }

        /// <inheritdoc />
        public Task<FactoryWifiMacResult> GetFactoryWifiMacAsync()
        {
            return GetFactoryWifiMacAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<FactoryWifiMacResult> GetFactoryWifiMacAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _startUp.GetFactoryWifiMacAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetFactoryWifiMac(M3RequestCallback<FactoryWifiMacResult> callback)
        {
            ThrowIfDisposed();
            return _startUp.GetFactoryWifiMac(callback);
        }

        /// <inheritdoc />
        public void EnableCaptivePortalDetection()
        {
            ThrowIfDisposed();
            _startUp.EnableCaptivePortalDetection();
        }

        /// <inheritdoc />
        public void DisableCaptivePortalDetection()
        {
            ThrowIfDisposed();
            _startUp.DisableCaptivePortalDetection();
        }

        /// <inheritdoc />
        public void AllowAllWifiFrequencyBand()
        {
            ThrowIfDisposed();
            _startUp.AllowAllWifiFrequencyBand();
        }

        /// <inheritdoc />
        public void AllowOnly2_4GHzWifiFrequencyBand()
        {
            ThrowIfDisposed();
            _startUp.AllowOnly2_4GHzWifiFrequencyBand();
        }

        /// <inheritdoc />
        public void AllowOnly5GHzWifiFrequencyBand()
        {
            ThrowIfDisposed();
            _startUp.AllowOnly5GHzWifiFrequencyBand();
        }

        /// <inheritdoc />
        public void SetWifiCountry(string countryCode)
        {
            ThrowIfDisposed();
            _startUp.SetWifiCountry(countryCode);
        }

        /// <inheritdoc />
        public void EnableOpenNetworkNotification()
        {
            ThrowIfDisposed();
            _startUp.EnableOpenNetworkNotification();
        }

        /// <inheritdoc />
        public void DisableOpenNetworkNotification()
        {
            ThrowIfDisposed();
            _startUp.DisableOpenNetworkNotification();
        }

        /// <inheritdoc />
        public void SetRoamingTrigger(int index)
        {
            ThrowIfDisposed();
            _startUp.SetRoamingTrigger(index);
        }

        /// <inheritdoc />
        public void SetRoamingDelta(int index)
        {
            ThrowIfDisposed();
            _startUp.SetRoamingDelta(index);
        }

        /// <inheritdoc />
        public void SetWifiSleepPolicyNever()
        {
            ThrowIfDisposed();
            _startUp.SetWifiSleepPolicyNever();
        }

        /// <inheritdoc />
        public void SetWifiSleepPolicyPluggedOnly()
        {
            ThrowIfDisposed();
            _startUp.SetWifiSleepPolicyPluggedOnly();
        }

        /// <inheritdoc />
        public void SetWifiSleepPolicyAlways()
        {
            ThrowIfDisposed();
            _startUp.SetWifiSleepPolicyAlways();
        }

        /// <inheritdoc />
        public void SetWifiStabilityNormal()
        {
            ThrowIfDisposed();
            _startUp.SetWifiStabilityNormal();
        }

        /// <inheritdoc />
        public void SetWifiStabilityHigh()
        {
            ThrowIfDisposed();
            _startUp.SetWifiStabilityHigh();
        }

        /// <inheritdoc />
        public void SetWifiChannel(params int[] channels)
        {
            ThrowIfDisposed();
            _startUp.SetWifiChannel(channels);
        }

        /// <inheritdoc />
        public void SetAccessPoint(AccessPoint accessPoint)
        {
            ThrowIfDisposed();
            _startUp.SetAccessPoint(accessPoint);
        }

        /// <inheritdoc />
        public void ClearSavedWifiNetworks()
        {
            ThrowIfDisposed();
            _startUp.ClearSavedWifiNetworks();
        }

        /// <inheritdoc />
        public void RemoveWifiNetwork(string ssid)
        {
            ThrowIfDisposed();
            _startUp.RemoveWifiNetwork(ssid);
        }

        /// <inheritdoc />
        public void StartScan()
        {
            ThrowIfDisposed();
            _scanEmul.StartScan();
        }

        /// <inheritdoc />
        public void StopScan()
        {
            ThrowIfDisposed();
            _scanEmul.StopScan();
        }

        /// <inheritdoc />
        public Task<string> GetScannerTypeAsync()
        {
            return GetScannerTypeAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetScannerTypeAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScannerTypeAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScannerType(M3RequestCallback<string> callback)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScannerType(callback);
        }

        /// <inheritdoc />
        public Task<int> GetScannerStatusAsync()
        {
            return GetScannerStatusAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<int> GetScannerStatusAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScannerStatusAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScannerStatus(M3RequestCallback<int> callback)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScannerStatus(callback);
        }

        /// <inheritdoc />
        public void RegisterOnScanResultListener(IOnScanResultListener listener)
        {
            ThrowIfDisposed();
            _scanEmul.RegisterOnScanResultListener(listener);
        }

        /// <inheritdoc />
        public IDisposable RegisterOnScanResultListener(Action<ScanResult> listener)
        {
            ThrowIfDisposed();
            return _scanEmul.RegisterOnScanResultListener(listener);
        }

        /// <inheritdoc />
        public void UnregisterOnScanResultListener(IOnScanResultListener listener)
        {
            ThrowIfDisposed();
            _scanEmul.UnregisterOnScanResultListener(listener);
        }

        /// <inheritdoc />
        public void RegisterOnGS1ParsedListener(IOnGS1ParsedListener listener)
        {
            ThrowIfDisposed();
            _scanEmul.RegisterOnGS1ParsedListener(listener);
        }

        /// <inheritdoc />
        public IDisposable RegisterOnGS1ParsedListener(Action<IList<GS1ParsedData>> listener)
        {
            ThrowIfDisposed();
            return _scanEmul.RegisterOnGS1ParsedListener(listener);
        }

        /// <inheritdoc />
        public void UnregisterOnGS1ParsedListener(IOnGS1ParsedListener listener)
        {
            ThrowIfDisposed();
            _scanEmul.UnregisterOnGS1ParsedListener(listener);
        }

        /// <inheritdoc />
        public void RegisterOnDigitalLinkParsedListener(IOnDigitalLinkParsedListener listener)
        {
            ThrowIfDisposed();
            _scanEmul.RegisterOnDigitalLinkParsedListener(listener);
        }

        /// <inheritdoc />
        public IDisposable RegisterOnDigitalLinkParsedListener(Action<IList<DigitalLinkParsedData>> listener)
        {
            ThrowIfDisposed();
            return _scanEmul.RegisterOnDigitalLinkParsedListener(listener);
        }

        /// <inheritdoc />
        public void UnregisterOnDigitalLinkParsedListener(IOnDigitalLinkParsedListener listener)
        {
            ThrowIfDisposed();
            _scanEmul.UnregisterOnDigitalLinkParsedListener(listener);
        }

        /// <inheritdoc />
        public void SetScanSound(ScanSound sound)
        {
            ThrowIfDisposed();
            _scanEmul.SetScanSound(sound);
        }

        /// <inheritdoc />
        public void EnableScanVibration()
        {
            ThrowIfDisposed();
            _scanEmul.EnableScanVibration();
        }

        /// <inheritdoc />
        public void DisableScanVibration()
        {
            ThrowIfDisposed();
            _scanEmul.DisableScanVibration();
        }

        /// <inheritdoc />
        public void EnableScanLed()
        {
            ThrowIfDisposed();
            _scanEmul.EnableScanLed();
        }

        /// <inheritdoc />
        public void DisableScanLed()
        {
            ThrowIfDisposed();
            _scanEmul.DisableScanLed();
        }

        /// <inheritdoc />
        public void SetScanLedTime(int timeMillis)
        {
            ThrowIfDisposed();
            _scanEmul.SetScanLedTime(timeMillis);
        }

        /// <inheritdoc />
        public void SetScannerReadMode(ReadMode mode)
        {
            ThrowIfDisposed();
            _scanEmul.SetScannerReadMode(mode);
        }

        /// <inheritdoc />
        public void SetScanResultOutputMode(OutputMode mode)
        {
            ThrowIfDisposed();
            _scanEmul.SetScanResultOutputMode(mode);
        }

        /// <inheritdoc />
        public void SetScanResultEndCharacter(EndCharacter endCharacter)
        {
            ThrowIfDisposed();
            _scanEmul.SetScanResultEndCharacter(endCharacter);
        }

        /// <inheritdoc />
        public void SetScanResultPrefix(string prefix)
        {
            ThrowIfDisposed();
            _scanEmul.SetScanResultPrefix(prefix);
        }

        /// <inheritdoc />
        public void SetScanResultPostfix(string postfix)
        {
            ThrowIfDisposed();
            _scanEmul.SetScanResultPostfix(postfix);
        }

        /// <inheritdoc />
        public Task<string> GetScanResultPrefixAsync()
        {
            return GetScanResultPrefixAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetScanResultPrefixAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScanResultPrefixAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScanResultPrefix(M3RequestCallback<string> callback)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScanResultPrefix(callback);
        }

        /// <inheritdoc />
        public Task<string> GetScanResultPostfixAsync()
        {
            return GetScanResultPostfixAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetScanResultPostfixAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScanResultPostfixAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScanResultPostfix(M3RequestCallback<string> callback)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScanResultPostfix(callback);
        }

        /// <inheritdoc />
        public Task<EndCharacter> GetScanResultEndCharacterAsync()
        {
            return GetScanResultEndCharacterAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<EndCharacter> GetScanResultEndCharacterAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScanResultEndCharacterAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScanResultEndCharacter(M3RequestCallback<EndCharacter> callback)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScanResultEndCharacter(callback);
        }

        /// <inheritdoc />
        public Task<OutputMode> GetScanResultOutputModeAsync()
        {
            return GetScanResultOutputModeAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<OutputMode> GetScanResultOutputModeAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScanResultOutputModeAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScanResultOutputMode(M3RequestCallback<OutputMode> callback)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScanResultOutputMode(callback);
        }

        /// <inheritdoc />
        public Task<bool> IsScannerProfileEnabledAsync()
        {
            return IsScannerProfileEnabledAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<bool> IsScannerProfileEnabledAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _scanEmul.IsScannerProfileEnabledAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable IsScannerProfileEnabled(M3RequestCallback<bool> callback)
        {
            ThrowIfDisposed();
            return _scanEmul.IsScannerProfileEnabled(callback);
        }

        /// <inheritdoc />
        public Task<ReadMode> GetScannerReadModeAsync()
        {
            return GetScannerReadModeAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<ReadMode> GetScannerReadModeAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScannerReadModeAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScannerReadMode(M3RequestCallback<ReadMode> callback)
        {
            ThrowIfDisposed();
            return _scanEmul.GetScannerReadMode(callback);
        }

        /// <inheritdoc />
        public string GetNtpServer()
        {
            ThrowIfDisposed();
            return _time.GetNtpServer();
        }

        /// <inheritdoc />
        public int GetNtpInterval()
        {
            ThrowIfDisposed();
            return _time.GetNtpInterval();
        }

        /// <inheritdoc />
        public string GetTimeZone()
        {
            ThrowIfDisposed();
            return _time.GetTimeZone();
        }

        /// <inheritdoc />
        public int GetRoamingThreshold()
        {
            ThrowIfDisposed();
            return _wifi.GetRoamingThreshold();
        }

        /// <inheritdoc />
        public int GetRoamingDelta()
        {
            ThrowIfDisposed();
            return _wifi.GetRoamingDelta();
        }

        /// <inheritdoc />
        public int GetWifiFrequencyBand()
        {
            ThrowIfDisposed();
            return _wifi.GetWifiFrequencyBand();
        }

        /// <inheritdoc />
        public string GetWifiCountryCode()
        {
            ThrowIfDisposed();
            return _wifi.GetWifiCountryCode();
        }

        /// <inheritdoc />
        public IList<string> GetCurrentUsbModes()
        {
            ThrowIfDisposed();
            return _usb.GetCurrentUsbModes();
        }

        /// <inheritdoc />
        public void Dispose()
        {
            if (_disposed)
                return;

            _disposed = true;
            _scanEmul.Dispose();
        }

        private void ThrowIfDisposed()
        {
            if (_disposed)
                throw new ObjectDisposedException(GetType().FullName);
        }
    }
}
