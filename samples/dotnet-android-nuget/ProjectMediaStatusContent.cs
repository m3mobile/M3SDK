using M3Sdk.Xamarin.Startup;

namespace M3SdkPublishedSample;

internal sealed class ProjectMediaStatusContent
{
    internal ProjectMediaStatusContent(int conditionResource, int actionResource)
    {
        ConditionResource = conditionResource;
        ActionResource = actionResource;
    }

    internal int ConditionResource { get; }

    internal int ActionResource { get; }
}

internal static class ProjectMediaStatusContentMap
{
    internal static ProjectMediaStatusContent Content(this ProjectMediaStatus status) => status switch
    {
        ProjectMediaStatus.Success => new(
            Resource.String.project_media_success_condition,
            Resource.String.project_media_success_action),
        ProjectMediaStatus.UnsupportedDevice => new(
            Resource.String.project_media_unsupported_condition,
            Resource.String.project_media_unsupported_action),
        ProjectMediaStatus.TargetNotInstalled => new(
            Resource.String.project_media_not_installed_condition,
            Resource.String.project_media_not_installed_action),
        ProjectMediaStatus.InvalidTarget => new(
            Resource.String.project_media_invalid_condition,
            Resource.String.project_media_invalid_action),
        ProjectMediaStatus.PermissionDenied => new(
            Resource.String.project_media_permission_condition,
            Resource.String.project_media_permission_action),
        ProjectMediaStatus.AppOpUnavailable => new(
            Resource.String.project_media_app_op_condition,
            Resource.String.project_media_app_op_action),
        ProjectMediaStatus.ApplyFailed => new(
            Resource.String.project_media_apply_condition,
            Resource.String.project_media_apply_action),
        _ => throw new ArgumentOutOfRangeException(nameof(status), status, null)
    };
}
