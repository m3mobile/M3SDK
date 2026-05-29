using System;
using Android.OS;

namespace M3Sdk.Xamarin.Internal
{
    internal enum DeviceModel
    {
        Unknown,
        SM10,
        SM10LTE,
        SM15,
        SM20,
        SM20_U,
        SM30,
        SM24,
        SM25,
        TN15,
        TN15_OREO,
        TX15,
        UL20_OREO,
        UL20_PIE,
        UX20_Q,
        UL20_A10,
        UL20F,
        UL30,
        TL20,
        US20,
        US30,
        SL10,
        SL10K,
        SL20,
        SL20P,
        SL20K,
        SL25,
        PC10,
        WD10
    }

    internal static class DeviceModelDetector
    {
        internal static DeviceModel Current
        {
            get { return Detect(Build.Model ?? string.Empty, (int)Build.VERSION.SdkInt); }
        }

        private static DeviceModel Detect(string model, int sdkInt)
        {
            if (Contains(model, "M3SM10_LTE"))
                return DeviceModel.SM10LTE;
            if (Contains(model, "M3SM10"))
                return DeviceModel.SM10;
            if (Contains(model, "M3SM15"))
                return DeviceModel.SM15;
            if (Contains(model, "M3UL20") || Contains(model, "M3US20"))
            {
                if (sdkInt == 28)
                    return DeviceModel.UL20_PIE;
                if (sdkInt == 29)
                    return DeviceModel.UX20_Q;
                return DeviceModel.UL20_OREO;
            }
            if (Contains(model, "UL30"))
                return DeviceModel.UL30;
            if (Contains(model, "TN15"))
                return DeviceModel.TN15;
            if (Contains(model, "TX15"))
                return DeviceModel.TX15;
            if (Contains(model, "TL20"))
                return DeviceModel.TL20;
            if (Contains(model, "SL20"))
            {
                if (Contains(model, "K"))
                    return DeviceModel.SL20K;
                if (Contains(model, "P"))
                    return DeviceModel.SL20P;
                return DeviceModel.SL20;
            }
            if (Contains(model, "SM20"))
                return sdkInt == 34 ? DeviceModel.SM20_U : DeviceModel.SM20;
            if (Contains(model, "PC10"))
                return DeviceModel.PC10;
            if (Contains(model, "US30"))
                return DeviceModel.US30;
            if (Contains(model, "SM30"))
                return DeviceModel.SM30;
            if (Contains(model, "SL25"))
                return DeviceModel.SL25;
            if (Contains(model, "WD10"))
                return DeviceModel.WD10;
            if (Contains(model, "SM24"))
                return DeviceModel.SM24;
            if (Contains(model, "SM25"))
                return DeviceModel.SM25;

            return DeviceModel.Unknown;
        }

        private static bool Contains(string source, string value)
        {
            return source.IndexOf(value, StringComparison.Ordinal) >= 0;
        }
    }
}
