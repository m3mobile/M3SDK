namespace M3Sdk.Xamarin.ScanEmul
{
    public enum ScannerButtonUiSize
    {
        ExtraSmall,
        Small,
        Medium,
        Large,
        ExtraLarge,
        Unknown
    }

    public enum ScannerButtonUiStatus
    {
        Applied,
        SavedServiceNotReady,
        SavedButtonNotVisible,
        UnsupportedDevice,
        InvalidRequest,
        InvalidImage,
        SaveFailed,
        Busy,
        Unknown
    }

    public enum ScannerButtonUiTransportStatus
    {
        Ok,
        ScanEmulNotInstalled,
        FeatureNotAvailable,
        Timeout,
        SendFailed,
        InvalidSdkRequest
    }

    public sealed class ScannerButtonUiOptions
    {
        public const int MinOpacityPercent = 20;
        public const int MaxOpacityPercent = 100;

        public ScannerButtonUiOptions(
            string imagePath = null,
            int? opacityPercent = null,
            ScannerButtonUiSize? size = null)
        {
            ImagePath = imagePath;
            OpacityPercent = opacityPercent;
            Size = size;
        }

        public string ImagePath { get; }

        public int? OpacityPercent { get; }

        public ScannerButtonUiSize? Size { get; }

        public bool IsEmpty
        {
            get { return ImagePath == null && OpacityPercent == null && Size == null; }
        }

        public bool IsValid
        {
            get
            {
                return !IsEmpty &&
                       (!OpacityPercent.HasValue ||
                        (OpacityPercent.Value >= MinOpacityPercent &&
                         OpacityPercent.Value <= MaxOpacityPercent)) &&
                       Size != ScannerButtonUiSize.Unknown;
            }
        }

        public static ScannerButtonUiOptions ForImagePath(string imagePath)
        {
            return new ScannerButtonUiOptions(imagePath: imagePath);
        }

        public static ScannerButtonUiOptions ForOpacityPercent(int opacityPercent)
        {
            return new ScannerButtonUiOptions(opacityPercent: opacityPercent);
        }

        public static ScannerButtonUiOptions ForSize(ScannerButtonUiSize size)
        {
            return new ScannerButtonUiOptions(size: size);
        }
    }

    public sealed class ScannerButtonUiSettings
    {
        public ScannerButtonUiSettings(string imagePath, int opacityPercent, ScannerButtonUiSize size)
        {
            ImagePath = imagePath ?? string.Empty;
            OpacityPercent = opacityPercent;
            Size = size;
        }

        public string ImagePath { get; }

        public int OpacityPercent { get; }

        public ScannerButtonUiSize Size { get; }

        public bool HasCustomImage
        {
            get { return ImagePath.Length > 0; }
        }

        public override bool Equals(object obj)
        {
            var other = obj as ScannerButtonUiSettings;
            return other != null &&
                   ImagePath == other.ImagePath &&
                   OpacityPercent == other.OpacityPercent &&
                   Size == other.Size;
        }

        public override int GetHashCode()
        {
            unchecked
            {
                var hash = ImagePath.GetHashCode();
                hash = (hash * 397) ^ OpacityPercent;
                hash = (hash * 397) ^ Size.GetHashCode();
                return hash;
            }
        }
    }

    public sealed class ScannerButtonUiResult
    {
        public ScannerButtonUiResult(
            string requestId,
            ScannerButtonUiTransportStatus transportStatus,
            bool success,
            ScannerButtonUiStatus status,
            string rawStatus,
            bool runtimeApplied,
            ScannerButtonUiSettings settings)
        {
            RequestId = requestId ?? string.Empty;
            TransportStatus = transportStatus;
            Success = success;
            Status = status;
            RawStatus = rawStatus;
            RuntimeApplied = runtimeApplied;
            Settings = settings;
        }

        public string RequestId { get; }

        public ScannerButtonUiTransportStatus TransportStatus { get; }

        public bool Success { get; }

        public ScannerButtonUiStatus Status { get; }

        public string RawStatus { get; }

        public bool RuntimeApplied { get; }

        public ScannerButtonUiSettings Settings { get; }

        public bool IsSaved
        {
            get
            {
                return TransportStatus == ScannerButtonUiTransportStatus.Ok &&
                       Success &&
                       (Status == ScannerButtonUiStatus.Applied ||
                        Status == ScannerButtonUiStatus.SavedServiceNotReady ||
                        Status == ScannerButtonUiStatus.SavedButtonNotVisible);
            }
        }
    }

    public sealed class ScannerButtonUiVerificationResult
    {
        public ScannerButtonUiVerificationResult(
            ScannerButtonUiResult setResult,
            ScannerButtonUiResult getResult)
        {
            SetResult = setResult;
            GetResult = getResult;
        }

        public ScannerButtonUiResult SetResult { get; }

        public ScannerButtonUiResult GetResult { get; }

        public bool IsVerified
        {
            get
            {
                return SetResult != null &&
                       GetResult != null &&
                       SetResult.IsSaved &&
                       GetResult.IsSaved &&
                       SetResult.Settings != null &&
                       SetResult.Settings.Equals(GetResult.Settings);
            }
        }
    }
}
