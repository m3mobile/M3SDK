using System;
using System.Collections.Generic;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.KeyTool
{
    internal sealed class KeySettingRequest
    {
        private readonly IDictionary<string, object> _extras;

        private KeySettingRequest(
            string packageName,
            string action,
            bool ordered,
            IDictionary<string, object> extras)
        {
            PackageName = packageName;
            Action = action;
            Ordered = ordered;
            _extras = new Dictionary<string, object>(extras);
        }

        internal string PackageName { get; private set; }

        internal string Action { get; private set; }

        internal bool Ordered { get; private set; }

        internal IEnumerable<KeyValuePair<string, object>> Extras
        {
            get { return _extras; }
        }

        internal object Extra(string key)
        {
            object value;
            return _extras.TryGetValue(key, out value) ? value : null;
        }

        internal static KeySettingRequest ForKeyFunction(
            string keyName,
            string function,
            bool? wakeUpEnabled = null)
        {
            if (string.IsNullOrWhiteSpace(keyName))
                throw new ArgumentException("A KeyTool key title is required.", nameof(keyName));
            if (string.IsNullOrWhiteSpace(function))
                throw new ArgumentException("A KeyTool function title is required.", nameof(function));

            return Unified(keyName, function, wakeUpEnabled);
        }

        internal static KeySettingRequest ForScanWakeUp(
            DeviceModel model,
            string keyName,
            bool enabled)
        {
            if (keyName != Constants.KeyTool.LeftScan && keyName != Constants.KeyTool.RightScan)
                throw new ArgumentException("Unsupported scan key: " + keyName, nameof(keyName));

            return model == DeviceModel.SM24
                ? Unified(keyName, null, enabled)
                : LegacyWakeUp(keyName, enabled);
        }

        private static KeySettingRequest Unified(
            string keyName,
            string function,
            bool? wakeUpEnabled)
        {
            if (function == null && !wakeUpEnabled.HasValue)
                throw new ArgumentException("At least one KeyTool setting is required.");

            var extras = new Dictionary<string, object>
            {
                { Constants.KeyTool.ExtraKeyTitle, keyName }
            };
            if (function != null)
                extras.Add(Constants.KeyTool.ExtraKeyFunction, function);
            if (wakeUpEnabled.HasValue)
                extras.Add(Constants.KeyTool.ExtraKeyWakeUp, wakeUpEnabled.Value);

            return new KeySettingRequest(
                Constants.KeyTool.Sl20PackageName,
                Constants.KeyTool.SetKey,
                true,
                extras);
        }

        private static KeySettingRequest LegacyWakeUp(string keyName, bool enabled)
        {
            return new KeySettingRequest(
                Constants.KeyTool.WakeUpPackageName,
                keyName == Constants.KeyTool.LeftScan
                    ? Constants.KeyTool.LeftScanWakeUp
                    : Constants.KeyTool.RightScanWakeUp,
                false,
                new Dictionary<string, object>
                {
                    { Constants.KeyTool.ExtraWakeUpEnabled, enabled }
                });
        }

        public override string ToString()
        {
            return "KeySettingRequest(PackageName=" + PackageName +
                   ", Action=" + Action +
                   ", Extras=" + string.Join(", ", _extras.Keys) + ")";
        }
    }
}
