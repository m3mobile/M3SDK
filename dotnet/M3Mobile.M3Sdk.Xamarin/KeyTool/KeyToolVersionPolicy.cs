using System;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.KeyTool
{
    internal static class KeyToolVersionPolicy
    {
        internal const string FnMinimumVersion = "1.2.6";
        internal const string NavigationMinimumVersion = "1.4.1";
        internal const string ScanWakeUpMinimumVersion = "1.3.8";

        internal static string KeyFunctionMinimumVersion(DeviceModel model)
        {
            if (model == DeviceModel.SM24)
                return "1.3.8";
            if (model == DeviceModel.SM25)
                return "1.3.16";
            return "1.2.6";
        }

        internal static bool VersionSatisfied(string currentVersion, string requiredVersion)
        {
            var current = VersionParts(currentVersion);
            var required = VersionParts(requiredVersion);

            for (var i = 0; i < current.Length; i++)
            {
                if (current[i] != required[i])
                    return current[i] > required[i];
            }

            return true;
        }

        private static int[] VersionParts(string version)
        {
            var parts = new[] { 0, 0, 0 };
            if (string.IsNullOrEmpty(version))
                return parts;

            var tokens = version.Split('.');
            for (var i = 0; i < parts.Length && i < tokens.Length; i++)
                parts[i] = NumericPrefix(tokens[i]);

            return parts;
        }

        private static int NumericPrefix(string token)
        {
            var length = 0;
            while (length < token.Length && char.IsDigit(token[length]))
                length++;

            int value;
            return length > 0 && int.TryParse(token.Substring(0, length), out value)
                ? value
                : 0;
        }
    }
}
