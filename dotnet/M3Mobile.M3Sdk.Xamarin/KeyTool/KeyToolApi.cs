using System;
using System.Collections.Generic;
using Android.Content;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.KeyTool
{
    /// <summary>
    /// KeyTool API surface implemented by one-way native Android broadcasts.
    /// </summary>
    public sealed class KeyToolApi : IKeyToolApi
    {
        private static readonly ISet<DeviceModel> FnModels =
            new HashSet<DeviceModel> { DeviceModel.SL20K };

        private static readonly ISet<DeviceModel> KeyModels =
            new HashSet<DeviceModel>
            {
                DeviceModel.SL20,
                DeviceModel.SL20K,
                DeviceModel.SL20P,
                DeviceModel.SL25,
                DeviceModel.WD10,
                DeviceModel.SM24,
                DeviceModel.SM25
            };

        private static readonly ISet<DeviceModel> NavigationButtonModels =
            new HashSet<DeviceModel> { DeviceModel.SM24, DeviceModel.SM25 };

        private static readonly ISet<DeviceModel> WakeUpModels =
            new HashSet<DeviceModel> { DeviceModel.SL20P, DeviceModel.SM24 };

        private static readonly ISet<DeviceModel> CombinedKeySettingModels =
            new HashSet<DeviceModel> { DeviceModel.SM24 };

        private readonly Context _context;
        private readonly M3SdkGuard _guard;

        internal KeyToolApi(Context context, M3SdkGuard guard)
        {
            _context = context;
            _guard = guard;
        }

        /// <inheritdoc />
        public void EnableFn()
        {
            GuardSl20("EnableFn", FnModels, KeyToolVersionPolicy.FnMinimumVersion);
            SendFnState(Constants.KeyTool.EnableFn);
        }

        /// <inheritdoc />
        public void DisableFn()
        {
            GuardSl20("DisableFn", FnModels, KeyToolVersionPolicy.FnMinimumVersion);
            SendFnState(Constants.KeyTool.DisableFn);
        }

        /// <inheritdoc />
        public void LockFn()
        {
            GuardSl20("LockFn", FnModels, KeyToolVersionPolicy.FnMinimumVersion);
            SendFnState(Constants.KeyTool.LockFn);
        }

        /// <inheritdoc />
        public void SetKeyFunction(string key, string function)
        {
            SetKeyFunction(
                key,
                function,
                null,
                KeyModels,
                KeyToolVersionPolicy.KeyFunctionMinimumVersion(DeviceModelDetector.Current));
        }

        /// <inheritdoc />
        public void SetKeyFunction(string key, string function, bool wakeUpEnabled)
        {
            SetKeyFunction(
                key,
                function,
                wakeUpEnabled,
                CombinedKeySettingModels,
                KeyToolVersionPolicy.ScanWakeUpMinimumVersion);
        }

        /// <inheritdoc />
        public void EnableHomeButton()
        {
            SetNavigationButton("EnableHomeButton", Constants.KeyTool.Home, Constants.KeyTool.Default);
        }

        /// <inheritdoc />
        public void DisableHomeButton()
        {
            SetNavigationButton("DisableHomeButton", Constants.KeyTool.Home, Constants.KeyTool.Disable);
        }

        /// <inheritdoc />
        public void EnableRecentButton()
        {
            SetNavigationButton("EnableRecentButton", Constants.KeyTool.Recent, Constants.KeyTool.Default);
        }

        /// <inheritdoc />
        public void DisableRecentButton()
        {
            SetNavigationButton("DisableRecentButton", Constants.KeyTool.Recent, Constants.KeyTool.Disable);
        }

        /// <inheritdoc />
        public void EnableLeftScanWakeUp()
        {
            SetScanWakeUp("EnableLeftScanWakeUp", Constants.KeyTool.LeftScan, true);
        }

        /// <inheritdoc />
        public void DisableLeftScanWakeUp()
        {
            SetScanWakeUp("DisableLeftScanWakeUp", Constants.KeyTool.LeftScan, false);
        }

        /// <inheritdoc />
        public void EnableRightScanWakeUp()
        {
            SetScanWakeUp("EnableRightScanWakeUp", Constants.KeyTool.RightScan, true);
        }

        /// <inheritdoc />
        public void DisableRightScanWakeUp()
        {
            SetScanWakeUp("DisableRightScanWakeUp", Constants.KeyTool.RightScan, false);
        }

        private void GuardSl20(
            string methodName,
            ISet<DeviceModel> supportedModels,
            string requiredVersion)
        {
            _guard.AssertDeviceSupport(methodName, supportedModels, null);
            _guard.AssertKeyToolAppVersion(
                methodName,
                Constants.KeyTool.Sl20AppName,
                Constants.KeyTool.Sl20PackageName,
                DeviceModelDetector.Current,
                requiredVersion);
        }

        private void GuardScanWakeUp(string methodName)
        {
            _guard.AssertDeviceSupport(methodName, WakeUpModels, null);
            if (DeviceModelDetector.Current == DeviceModel.SM24)
            {
                _guard.AssertKeyToolAppVersion(
                    methodName,
                    Constants.KeyTool.Sl20AppName,
                    Constants.KeyTool.Sl20PackageName,
                    DeviceModel.SM24,
                    KeyToolVersionPolicy.ScanWakeUpMinimumVersion);
            }
            else
            {
                _guard.AssertCompanionAppAvailable(
                    methodName,
                    Constants.KeyTool.WakeUpAppName,
                    Constants.KeyTool.WakeUpPackageName);
            }
        }

        private void SendFnState(int state)
        {
            var intent = new Intent(Constants.KeyTool.ControlFn);
            intent.PutExtra(Constants.KeyTool.ExtraFnState, state);
            _context.SendBroadcast(intent);
        }

        private void SetScanWakeUp(string methodName, string keyName, bool enabled)
        {
            GuardScanWakeUp(methodName);
            Send(KeySettingRequest.ForScanWakeUp(DeviceModelDetector.Current, keyName, enabled));
        }

        private void SetNavigationButton(string methodName, string key, string function)
        {
            GuardSl20(
                methodName,
                NavigationButtonModels,
                KeyToolVersionPolicy.NavigationMinimumVersion);
            Send(KeySettingRequest.ForKeyFunction(key, function));
        }

        private void SetKeyFunction(
            string key,
            string function,
            bool? wakeUpEnabled,
            ISet<DeviceModel> supportedModels,
            string requiredVersion)
        {
            if (string.IsNullOrWhiteSpace(key))
                throw new ArgumentException("A KeyTool key title is required.", nameof(key));
            if (string.IsNullOrWhiteSpace(function))
                throw new ArgumentException("A KeyTool function title is required.", nameof(function));

            GuardSl20("SetKeyFunction", supportedModels, requiredVersion);
            Send(KeySettingRequest.ForKeyFunction(key, function, wakeUpEnabled));
        }

        private void Send(KeySettingRequest request)
        {
            var intent = new Intent(request.Action);
            intent.SetPackage(request.PackageName);

            foreach (var extra in request.Extras)
            {
                if (extra.Value is string)
                    intent.PutExtra(extra.Key, (string)extra.Value);
                else if (extra.Value is bool)
                    intent.PutExtra(extra.Key, (bool)extra.Value);
                else
                    throw new ArgumentException("Unsupported KeyTool extra type for " + extra.Key + ".");
            }

            if (request.Ordered)
                _context.SendOrderedBroadcast(intent, null);
            else
                _context.SendBroadcast(intent);
        }
    }
}
