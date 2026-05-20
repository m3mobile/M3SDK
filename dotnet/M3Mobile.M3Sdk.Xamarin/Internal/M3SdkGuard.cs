using System;
using System.Collections.Generic;
using Android.Content;
using Android.Content.PM;

namespace M3Sdk.Xamarin.Internal
{
    internal sealed class M3SdkGuard
    {
        private readonly Context _context;
        private readonly bool _strictMode;

        /// <summary>
        /// Creates a guard that reads strict-mode configuration from the consuming app manifest.
        /// </summary>
        /// <param name="context">The Android context used to inspect app metadata and installed package versions.</param>
        internal M3SdkGuard(Context context)
        {
            _context = context;
            _strictMode = ReadStrictMode(context);
        }

        /// <summary>
        /// Gets whether strict-mode version and model checks are enabled.
        /// </summary>
        internal bool IsStrictModeEnabled
        {
            get { return _strictMode; }
        }

        /// <summary>
        /// Verifies that the installed StartUp app satisfies the required version when strict mode is enabled.
        /// </summary>
        /// <param name="methodName">The public SDK method being guarded.</param>
        /// <param name="requiredVersion">The minimum required StartUp app version.</param>
        internal void AssertStartUpVersion(string methodName, string requiredVersion)
        {
            AssertStartUpVersion(methodName, requiredVersion, null);
        }

        /// <summary>
        /// Verifies that the installed StartUp app satisfies the required version, applying model-specific overrides when present.
        /// </summary>
        /// <param name="methodName">The public SDK method being guarded.</param>
        /// <param name="requiredVersion">The default minimum required StartUp app version.</param>
        /// <param name="modelVersionOverrides">Optional minimum versions keyed by device model.</param>
        internal void AssertStartUpVersion(
            string methodName,
            string requiredVersion,
            IDictionary<DeviceModel, string> modelVersionOverrides)
        {
            AssertAppVersion(
                methodName,
                Constants.StartUp.AppName,
                Constants.StartUp.PackageName,
                GetEffectiveVersion(requiredVersion, modelVersionOverrides));
        }

        /// <summary>
        /// Verifies that the installed ScanEmul app satisfies the required version when strict mode is enabled.
        /// </summary>
        /// <param name="methodName">The public SDK method being guarded.</param>
        /// <param name="requiredVersion">The minimum required ScanEmul app version.</param>
        internal void AssertScanEmulVersion(string methodName, string requiredVersion)
        {
            AssertAppVersion(methodName, Constants.ScanEmul.AppName, Constants.ScanEmul.PackageName, requiredVersion);
        }

        /// <summary>
        /// Verifies that the current device model is accepted for the guarded API when strict mode is enabled.
        /// </summary>
        /// <param name="methodName">The public SDK method being guarded.</param>
        /// <param name="supportedModels">Optional allow-list of supported models.</param>
        /// <param name="unsupportedModels">Optional deny-list of unsupported models.</param>
        internal void AssertDeviceSupport(
            string methodName,
            ISet<DeviceModel> supportedModels,
            ISet<DeviceModel> unsupportedModels)
        {
            if (!ShouldInspect())
                return;

            var current = DeviceModelDetector.Current;

            if (supportedModels != null && supportedModels.Count > 0)
            {
                if (supportedModels.Contains(current))
                    return;

                ThrowUnsupported(methodName, current, supportedModels);
            }

            if (unsupportedModels == null || unsupportedModels.Count == 0)
                return;

            if (!unsupportedModels.Contains(current))
                return;

            ThrowUnsupported(methodName, current, BuildSupportedSet(unsupportedModels));
        }

        private void ThrowUnsupported(string methodName, DeviceModel current, ISet<DeviceModel> supportedModels)
        {
            throw new UnsupportedDeviceModelException(
                "\"" + methodName + "\" is not available on the current device (" + current + "). " +
                "Supported models are: " + string.Join(", ", supportedModels) + ".");
        }

        private void AssertAppVersion(string methodName, string appName, string packageName, string requiredVersion)
        {
            if (!ShouldInspect())
                return;

            var currentVersion = GetAppVersionName(packageName);
            if (string.IsNullOrEmpty(currentVersion))
            {
                throw new UnsatisfiedVersionException(
                    "\"" + methodName + "\" is not available because " + appName + " is not installed. " +
                    "Required " + appName + " version is '" + requiredVersion + "'.");
            }

            if (!IsVersionAtLeast(currentVersion, requiredVersion))
            {
                throw new UnsatisfiedVersionException(
                    "\"" + methodName + "\" is not available on the current " + appName + " version '" + currentVersion + "'. " +
                    "Required " + appName + " version is '" + requiredVersion + "'.");
            }
        }

        private string GetAppVersionName(string packageName)
        {
            try
            {
                var packageInfo = _context.PackageManager.GetPackageInfo(packageName, (PackageInfoFlags)0);
                return packageInfo.VersionName;
            }
            catch (PackageManager.NameNotFoundException)
            {
                return null;
            }
        }

        private bool ShouldInspect()
        {
            return _strictMode && DeviceModelDetector.Current != DeviceModel.Unknown;
        }

        private static string GetEffectiveVersion(
            string requiredVersion,
            IDictionary<DeviceModel, string> modelVersionOverrides)
        {
            if (modelVersionOverrides == null)
                return requiredVersion;

            string overrideVersion;
            return modelVersionOverrides.TryGetValue(DeviceModelDetector.Current, out overrideVersion)
                ? overrideVersion
                : requiredVersion;
        }

        private static ISet<DeviceModel> BuildSupportedSet(ISet<DeviceModel> unsupportedModels)
        {
            var supported = new HashSet<DeviceModel>();
            foreach (DeviceModel model in Enum.GetValues(typeof(DeviceModel)))
            {
                if (!unsupportedModels.Contains(model))
                    supported.Add(model);
            }

            return supported;
        }

        private static bool ReadStrictMode(Context context)
        {
            try
            {
                var appInfo = context.PackageManager.GetApplicationInfo(context.PackageName, PackageInfoFlags.MetaData);
                return appInfo.MetaData != null &&
                       appInfo.MetaData.GetBoolean(Constants.Core.StrictModeMetadataName, false);
            }
            catch (Exception)
            {
                return false;
            }
        }

        private static bool IsVersionAtLeast(string currentVersion, string requiredVersion)
        {
            var current = ToVersionParts(currentVersion);
            var required = ToVersionParts(requiredVersion);

            for (var i = 0; i < 3; i++)
            {
                if (current[i] != required[i])
                    return current[i] > required[i];
            }

            return true;
        }

        private static int[] ToVersionParts(string version)
        {
            var parts = new[] { 0, 0, 0 };
            if (version == null)
                return parts;

            var normalized = version.Split('-')[0];
            var tokens = normalized.Split('.');
            for (var i = 0; i < parts.Length && i < tokens.Length; i++)
            {
                int value;
                if (int.TryParse(tokens[i], out value))
                    parts[i] = value;
            }

            return parts;
        }
    }
}
