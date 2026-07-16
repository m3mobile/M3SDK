using Android.Views;

namespace M3SdkPublishedSample;

internal static class SystemBarPadding
{
    internal static void Apply(View view)
    {
        view.SetOnApplyWindowInsetsListener(new InsetAction());
        view.RequestApplyInsets();
    }

    private sealed class InsetAction : Java.Lang.Object, View.IOnApplyWindowInsetsListener
    {
        public WindowInsets OnApplyWindowInsets(View view, WindowInsets insets)
        {
            if (OperatingSystem.IsAndroidVersionAtLeast(30))
            {
                var systemBars = insets.GetInsets(WindowInsets.Type.SystemBars());
                view.SetPadding(systemBars.Left, systemBars.Top, systemBars.Right, systemBars.Bottom);
            }
            else
            {
#pragma warning disable CS0618
                view.SetPadding(
                    insets.SystemWindowInsetLeft,
                    insets.SystemWindowInsetTop,
                    insets.SystemWindowInsetRight,
                    insets.SystemWindowInsetBottom);
#pragma warning restore CS0618
            }
            return insets;
        }
    }
}
