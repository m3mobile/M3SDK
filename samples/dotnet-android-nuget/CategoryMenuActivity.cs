using Android.App;
using Android.Content;
using Android.OS;
using Android.Views;
using Android.Widget;

namespace M3SdkPublishedSample;

[Activity(Label = "@string/app_name", MainLauncher = true, Exported = true)]
public sealed class MainActivity : Activity
{
    protected override void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);

        var scroll = new ScrollView(this);
        SystemBarPadding.Apply(scroll);
        var content = new LinearLayout(this)
        {
            Orientation = Orientation.Vertical
        };
        content.SetPadding(32, 32, 32, 32);
        scroll.AddView(content);

        var title = new TextView(this)
        {
            Text = GetString(Resource.String.category_list),
            TextSize = 26
        };
        content.AddView(title);

        var subtitle = new TextView(this)
        {
            Text = GetString(Resource.String.select_category)
        };
        subtitle.SetPadding(0, 8, 0, 24);
        content.AddView(subtitle);

        foreach (var category in Enum.GetValues<SampleCategory>())
        {
            var selected = category;
            var button = new Button(this)
            {
                Text = GetString(SampleCategoryCatalog.Title(selected))
            };
            button.Click += (_, _) =>
            {
                var intent = new Intent(this, typeof(CategoryActivity));
                intent.PutExtra(CategoryActivity.CategoryExtra, (int)selected);
                StartActivity(intent);
            };
            content.AddView(button, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MatchParent,
                ViewGroup.LayoutParams.WrapContent)
            {
                BottomMargin = 12
            });
        }

        SetContentView(scroll);
    }
}

internal enum SampleCategory
{
    DeviceInfo,
    AirplaneMode,
    Application,
    Device,
    Language,
    Network,
    Permission,
    ProjectMedia,
    MediaProjectionIndicator,
    QuickTile,
    Scanner,
    StartUpSetting,
    Time,
    Usb,
    Wifi,
    AppCenter,
    KeyTool
}

internal static class SampleCategoryCatalog
{
    internal static int Title(SampleCategory category) => category switch
    {
        SampleCategory.DeviceInfo => Resource.String.device_info,
        SampleCategory.AirplaneMode => Resource.String.airplane_mode,
        SampleCategory.Application => Resource.String.application,
        SampleCategory.Device => Resource.String.device,
        SampleCategory.Language => Resource.String.language,
        SampleCategory.Network => Resource.String.network,
        SampleCategory.Permission => Resource.String.permission,
        SampleCategory.ProjectMedia => Resource.String.project_media,
        SampleCategory.MediaProjectionIndicator => Resource.String.media_projection_indicator,
        SampleCategory.QuickTile => Resource.String.quick_tile,
        SampleCategory.Scanner => Resource.String.scanner,
        SampleCategory.StartUpSetting => Resource.String.startup_setting,
        SampleCategory.Time => Resource.String.time,
        SampleCategory.Usb => Resource.String.usb,
        SampleCategory.Wifi => Resource.String.wifi,
        SampleCategory.AppCenter => Resource.String.appcenter,
        SampleCategory.KeyTool => Resource.String.keytool,
        _ => throw new ArgumentOutOfRangeException(nameof(category), category, null)
    };
}
