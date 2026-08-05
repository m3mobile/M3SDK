using System;

namespace M3Sdk.Xamarin.Startup
{
    /// <summary>
    /// Identifies the StartUp result of a PROJECT_MEDIA allow request.
    /// </summary>
    public enum ProjectMediaStatus
    {
        Success = 0,
        UnsupportedDevice = 1,
        TargetNotInstalled = 2,
        InvalidTarget = 3,
        PermissionDenied = 4,
        AppOpUnavailable = 5,
        ApplyFailed = 6
    }

    /// <summary>
    /// Contains the StartUp result of a PROJECT_MEDIA allow request.
    /// </summary>
    public sealed class ProjectMediaResult
    {
        /// <summary>
        /// Creates a PROJECT_MEDIA result.
        /// </summary>
        public ProjectMediaResult(ProjectMediaStatus status, string errorMessage)
        {
            Status = status;
            ErrorMessage = errorMessage ?? string.Empty;
        }

        /// <summary>
        /// Gets the StartUp feature status.
        /// </summary>
        public ProjectMediaStatus Status { get; private set; }

        /// <summary>
        /// Gets the StartUp error message, or an empty string when successful.
        /// </summary>
        public string ErrorMessage { get; private set; }

        /// <summary>
        /// Gets whether StartUp allowed and verified PROJECT_MEDIA.
        /// </summary>
        public bool IsSuccess
        {
            get { return Status == ProjectMediaStatus.Success; }
        }
    }

    internal static class ProjectMediaContract
    {
        internal static ProjectMediaResult Result(int resultCode, string errorMessage)
        {
            var known = Enum.IsDefined(typeof(ProjectMediaStatus), resultCode);
            var status = known
                ? (ProjectMediaStatus)resultCode
                : ProjectMediaStatus.ApplyFailed;
            var message = !string.IsNullOrWhiteSpace(errorMessage)
                ? errorMessage
                : known ? string.Empty : "Unknown result code: " + resultCode;
            return new ProjectMediaResult(status, message);
        }
    }
}
