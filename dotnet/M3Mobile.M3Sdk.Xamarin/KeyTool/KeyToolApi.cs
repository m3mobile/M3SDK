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
            new HashSet<DeviceModel> { DeviceModel.SL20P };

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
            GuardSl20("EnableFn", FnModels);
            SendFnState(Constants.KeyTool.EnableFn);
        }

        /// <inheritdoc />
        public void DisableFn()
        {
            GuardSl20("DisableFn", FnModels);
            SendFnState(Constants.KeyTool.DisableFn);
        }

        /// <inheritdoc />
        public void LockFn()
        {
            GuardSl20("LockFn", FnModels);
            SendFnState(Constants.KeyTool.LockFn);
        }

        /// <inheritdoc />
        public void SetKeyFunction(string key, string function)
        {
            if (string.IsNullOrWhiteSpace(key))
                throw new ArgumentException("A KeyTool key title is required.", nameof(key));
            if (string.IsNullOrWhiteSpace(function))
                throw new ArgumentException("A KeyTool function title is required.", nameof(function));

            GuardSl20("SetKeyFunction", KeyModels);
            var intent = new Intent(Constants.KeyTool.SetKey);
            intent.PutExtra(Constants.KeyTool.ExtraKeyTitle, key);
            intent.PutExtra(Constants.KeyTool.ExtraKeyFunction, function);
            _context.SendBroadcast(intent);
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
            SetWakeUp("EnableLeftScanWakeUp", Constants.KeyTool.LeftScanWakeUp, true);
        }

        /// <inheritdoc />
        public void DisableLeftScanWakeUp()
        {
            SetWakeUp("DisableLeftScanWakeUp", Constants.KeyTool.LeftScanWakeUp, false);
        }

        /// <inheritdoc />
        public void EnableRightScanWakeUp()
        {
            SetWakeUp("EnableRightScanWakeUp", Constants.KeyTool.RightScanWakeUp, true);
        }

        /// <inheritdoc />
        public void DisableRightScanWakeUp()
        {
            SetWakeUp("DisableRightScanWakeUp", Constants.KeyTool.RightScanWakeUp, false);
        }

        private void GuardSl20(string methodName, ISet<DeviceModel> supportedModels)
        {
            _guard.AssertDeviceSupport(methodName, supportedModels, null);
            _guard.AssertCompanionAppAvailable(
                methodName,
                Constants.KeyTool.Sl20AppName,
                Constants.KeyTool.Sl20PackageName);
        }

        private void GuardWakeUp(string methodName)
        {
            _guard.AssertDeviceSupport(methodName, WakeUpModels, null);
            _guard.AssertCompanionAppAvailable(
                methodName,
                Constants.KeyTool.WakeUpAppName,
                Constants.KeyTool.WakeUpPackageName);
        }

        private void SendFnState(int state)
        {
            var intent = new Intent(Constants.KeyTool.ControlFn);
            intent.PutExtra(Constants.KeyTool.ExtraFnState, state);
            _context.SendBroadcast(intent);
        }

        private void SetWakeUp(string methodName, string action, bool enabled)
        {
            GuardWakeUp(methodName);
            var intent = new Intent(action);
            intent.PutExtra(Constants.KeyTool.ExtraWakeUpEnabled, enabled);
            _context.SendBroadcast(intent);
        }

        private void SetNavigationButton(string methodName, string key, string function)
        {
            GuardSl20(methodName, NavigationButtonModels);
            var intent = new Intent(Constants.KeyTool.SetKey);
            intent.PutExtra(Constants.KeyTool.ExtraKeyTitle, key);
            intent.PutExtra(Constants.KeyTool.ExtraKeyFunction, function);
            _context.SendBroadcast(intent);
        }
    }
}
