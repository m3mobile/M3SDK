using System.Reflection;
using Android.Graphics;
using Android;
using Android.App;
using Android.Content.PM;
using Android.Nfc;
using Android.OS;
using Android.Provider;
using Android.Text;
using Android.Views;
using Android.Views.InputMethods;
using Android.Widget;
using M3Sdk.Xamarin;
using M3Sdk.Xamarin.ScanEmul;
using M3Sdk.Xamarin.Startup;

namespace M3SdkPublishedSample;

[Activity(Label = "@string/app_name", Exported = false)]
public sealed class CategoryActivity : Activity
{
    private const int ImageReadPermissionRequest = 2401;
    internal const string CategoryExtra = "sample_category";
    private const string StartUpPackage = "com.m3.startup";
    private const string ScanEmulPackage = "net.m3mobile.app.scanemul";
    private const string KeyToolSl20Package = "com.m3.keytoolsl20";
    private const string KeyToolWakeUpPackage = "net.m3.keytool";
    private const string AppCenterPackage = "com.m3.appcenter";
    private const string DroidVncPackage = "net.christianbeier.droidvnc_ng";
    private const string MissingProjectMediaPackage = "net.m3mobile.missing.projectmedia";
    private const string DefaultScannerButtonImagePath =
        "/sdcard/Download/ScanEmul_Floating_Button_Images/target.png";

    private IM3Sdk? _sdk;
    private LinearLayout? _content;
    private ScrollView? _scroll;
    private readonly ExecutionTrace _executionTrace = new();

    protected override void OnCreate(Bundle? savedInstanceState)
    {
        base.OnCreate(savedInstanceState);
        Window?.SetSoftInputMode(SoftInput.AdjustResize);
        var categoryValue = Intent?.GetIntExtra(CategoryExtra, -1) ?? -1;
        if (!Enum.IsDefined(typeof(SampleCategory), categoryValue))
        {
            Finish();
            return;
        }
        var category = (SampleCategory)categoryValue;

        _scroll = new ScrollView(this);
        SystemBarPadding.Apply(_scroll);
        _content = new LinearLayout(this)
        {
            Orientation = Orientation.Vertical
        };
        _content.SetPadding(32, 32, 32, 32);
        _scroll.AddView(_content);
        SetContentView(_scroll);

        ShowHeading(category);
        try
        {
            _sdk = M3Mobile.Create(ApplicationContext);
            ShowCategory(category);
        }
        catch (Exception error)
        {
            var section = Section(SampleCategoryCatalog.Title(category));
            section.Result.Text = _executionTrace.Message("M3Mobile.Create", Failure(error));
        }
    }

    private void RequestScannerImageReadPermission()
    {
        var missingPermissions = ScannerImageReadPermissions()
            .Where(permission => CheckSelfPermission(permission) != Permission.Granted)
            .ToArray();
        if (missingPermissions.Length > 0)
            RequestPermissions(missingPermissions, ImageReadPermissionRequest);
    }

    private static string[] ScannerImageReadPermissions()
    {
        return Build.VERSION.SdkInt >= BuildVersionCodes.Tiramisu
            ? new[] { "android.permission.READ_MEDIA_IMAGES" }
            : new[] { Manifest.Permission.ReadExternalStorage };
    }

    protected override void OnDestroy()
    {
        _sdk?.Dispose();
        base.OnDestroy();
    }

    private void ShowHeading(SampleCategory category)
    {
        var title = new TextView(this)
        {
            Text = GetString(SampleCategoryCatalog.Title(category)),
            TextSize = 26
        };
        title.SetPadding(0, 0, 0, 8);
        _content!.AddView(title);

        var subtitle = new TextView(this)
        {
            Text = GetString(Resource.String.screen_title)
        };
        subtitle.SetPadding(0, 0, 0, 24);
        _content.AddView(subtitle);
    }

    private void ShowCategory(SampleCategory category)
    {
        switch (category)
        {
            case SampleCategory.DeviceInfo: DeviceInfo(); break;
            case SampleCategory.AirplaneMode: AirplaneMode(); break;
            case SampleCategory.Application: ApplicationSample(); break;
            case SampleCategory.Device: DeviceSample(); break;
            case SampleCategory.Language: LanguageSample(); break;
            case SampleCategory.Network: NetworkSample(); break;
            case SampleCategory.Permission: PermissionSample(); break;
            case SampleCategory.ProjectMedia: ProjectMediaSample(); break;
            case SampleCategory.QuickTile: QuickTileSample(); break;
            case SampleCategory.Scanner: ScannerSample(); break;
            case SampleCategory.StartUpSetting: StartUpSettingSample(); break;
            case SampleCategory.Time: TimeSample(); break;
            case SampleCategory.Usb: UsbSample(); break;
            case SampleCategory.Wifi: WifiSample(); break;
            case SampleCategory.AppCenter: AppCenterSample(); break;
            case SampleCategory.KeyTool: KeyToolSample(); break;
            default: throw new ArgumentOutOfRangeException(nameof(category), category, null);
        }
    }

    private void DeviceInfo()
    {
        var section = Section(Resource.String.device_info);
        section.Result.Text = _executionTrace.Message("environment", EnvironmentText());
        AddButton(section, Resource.String.refresh, () =>
            section.Result.Text = _executionTrace.Message("environment", EnvironmentText()));
    }

    private void AirplaneMode()
    {
        var section = Section(Resource.String.airplane_mode);
        AddButton(section, Resource.String.turn_on, () =>
        {
            RunOneWay(section, "turnOnAirplaneMode", () => _sdk!.TurnOnAirplaneMode());
            section.Result.Append("\nobserved=" + AirplaneModeEnabled());
        });
        AddButton(section, Resource.String.turn_off, () =>
        {
            RunOneWay(section, "turnOffAirplaneMode", () => _sdk!.TurnOffAirplaneMode());
            section.Result.Append("\nobserved=" + AirplaneModeEnabled());
        });
    }

    private void ApplicationSample()
    {
        var section = Section(Resource.String.application);
        var packageName = TextField(Resource.String.package_name, PackageName ?? string.Empty);
        section.Add(packageName);
        AddButton(section, Resource.String.run_application, () =>
            RunOneWay(section, "runApp(package=" + (packageName.Text ?? string.Empty) + ")", () => _sdk!.RunApp(packageName.Text ?? string.Empty)));

        section.Add(SectionTitle(Resource.String.apk_install));
        section.Add(new TextView(this)
        {
            Text = GetString(Resource.String.apk_install_warning)
        });
        var localApkPath = TextField(Resource.String.local_apk_path, string.Empty);
        var remoteApkUrl = TextField(Resource.String.remote_apk_url, string.Empty);
        localApkPath.InputType = InputTypes.ClassText | InputTypes.TextVariationUri;
        remoteApkUrl.InputType = InputTypes.ClassText | InputTypes.TextVariationUri;
        var allowSameVersionUpdate = new CheckBox(this)
        {
            Text = GetString(Resource.String.allow_same_version_update)
        };
        var launchAfterInstall = new CheckBox(this)
        {
            Text = GetString(Resource.String.launch_after_install)
        };
        section.Add(localApkPath);
        section.Add(remoteApkUrl);
        section.Add(allowSameVersionUpdate);
        section.Add(launchAfterInstall);
        AddButton(section, Resource.String.install_local_apk, () =>
            RunOneWay(
                section,
                "InstallLocalApk(allowSameVersionUpdate=" + allowSameVersionUpdate.Checked +
                    ", launchAfterInstall=" + launchAfterInstall.Checked + ")",
                () => InstallLocalApk(
                    localApkPath.Text ?? string.Empty,
                    allowSameVersionUpdate.Checked,
                    launchAfterInstall.Checked),
                "StartUp=" + PackageVersion(StartUpPackage)));
        AddButton(section, Resource.String.install_remote_apk, () =>
            RunOneWay(
                section,
                "InstallRemoteApk(allowSameVersionUpdate=" + allowSameVersionUpdate.Checked +
                    ", launchAfterInstall=" + launchAfterInstall.Checked + ")",
                () => InstallRemoteApk(
                    remoteApkUrl.Text ?? string.Empty,
                    allowSameVersionUpdate.Checked,
                    launchAfterInstall.Checked),
                "StartUp=" + PackageVersion(StartUpPackage)));
    }

    private void InstallLocalApk(
        string filePath,
        bool allowSameVersionUpdate,
        bool launchAfterInstall)
    {
        if (string.IsNullOrWhiteSpace(filePath))
            throw new ArgumentException(GetString(Resource.String.local_apk_path_required), nameof(filePath));

        if (launchAfterInstall)
            _sdk!.InstallLocalApk(filePath.Trim(), allowSameVersionUpdate, launchAfterInstall);
        else if (allowSameVersionUpdate)
            _sdk!.InstallLocalApk(filePath.Trim(), allowSameVersionUpdate);
        else
            _sdk!.InstallLocalApk(filePath.Trim());
    }

    private void InstallRemoteApk(
        string url,
        bool allowSameVersionUpdate,
        bool launchAfterInstall)
    {
        if (string.IsNullOrWhiteSpace(url))
            throw new ArgumentException(GetString(Resource.String.remote_apk_url_required), nameof(url));

        if (launchAfterInstall)
            _sdk!.InstallRemoteApk(url.Trim(), allowSameVersionUpdate, launchAfterInstall);
        else if (allowSameVersionUpdate)
            _sdk!.InstallRemoteApk(url.Trim(), allowSameVersionUpdate);
        else
            _sdk!.InstallRemoteApk(url.Trim());
    }

    private void DeviceSample()
    {
        var section = Section(Resource.String.device);
        AddAsyncButton(section, Resource.String.get_serial, "getSerialNumber", async () =>
        {
            var serial = await _sdk!.GetSerialNumberAsync();
            return "SUCCESS\n" + serial;
        });
    }

    private void LanguageSample()
    {
        var section = Section(Resource.String.language);
        AddButton(section, Resource.String.english, () =>
            RunOneWay(section, "setLanguage(en, US)", () => _sdk!.SetLanguage("en", "US")));
        AddButton(section, Resource.String.korean, () =>
            RunOneWay(section, "setLanguage(ko, KR)", () => _sdk!.SetLanguage("ko", "KR")));
    }

    private void NetworkSample()
    {
        var section = Section(Resource.String.network);
        AddButton(section, Resource.String.enable_nfc, () =>
        {
            RunOneWay(section, "enableNfc", () => _sdk!.EnableNfc());
            section.Result.Append("\nobserved=" + NfcAdapter.GetDefaultAdapter(this)?.IsEnabled);
        });
        AddButton(section, Resource.String.disable_nfc, () =>
        {
            RunOneWay(section, "disableNfc", () => _sdk!.DisableNfc());
            section.Result.Append("\nobserved=" + NfcAdapter.GetDefaultAdapter(this)?.IsEnabled);
        });
    }

    private void PermissionSample()
    {
        var section = Section(Resource.String.permission);
        AddButton(section, Resource.String.grant_camera, () =>
        {
            RunOneWay(section, "grantPermission(package=" + (PackageName ?? string.Empty) + ", CAMERA)", () => _sdk!.GrantPermission(PackageName ?? string.Empty, Manifest.Permission.Camera));
            section.Result.Append("\nobserved=" +
                (CheckSelfPermission(Manifest.Permission.Camera) == Permission.Granted));
        });
    }

    private void ProjectMediaSample()
    {
        var section = Section(Resource.String.project_media);
        section.Add(new TextView(this)
        {
            Text = GetString(Resource.String.project_media_description) + "\n" +
                GetString(Resource.String.project_media_requirement)
        });
        var packageName = TextField(Resource.String.project_media_target_package, DroidVncPackage);
        section.Add(packageName);
        AddAsyncButton(
            section,
            Resource.String.project_media_run_entered,
            "AllowProjectMediaAsync(enteredPackage)",
            async () => ProjectMediaText(
                GetString(Resource.String.project_media_live_result),
                packageName.Text ?? string.Empty,
                await _sdk!.AllowProjectMediaAsync(packageName.Text ?? string.Empty)));
        AddAsyncButton(
            section,
            Resource.String.project_media_test_droid_vnc,
            "AllowProjectMediaAsync(DroidVNC)",
            async () =>
            {
                packageName.Text = DroidVncPackage;
                return ProjectMediaText(
                    GetString(Resource.String.project_media_live_result),
                    DroidVncPackage,
                    await _sdk!.AllowProjectMediaAsync(DroidVncPackage));
            });
        AddAsyncButton(
            section,
            Resource.String.project_media_test_missing,
            "AllowProjectMediaAsync(missingPackage)",
            async () =>
            {
                packageName.Text = MissingProjectMediaPackage;
                return ProjectMediaText(
                    GetString(Resource.String.project_media_live_result),
                    MissingProjectMediaPackage,
                    await _sdk!.AllowProjectMediaAsync(MissingProjectMediaPackage));
            });
        AddAsyncButton(
            section,
            Resource.String.project_media_test_invalid,
            "AllowProjectMediaAsync(blankPackage)",
            async () =>
            {
                packageName.Text = string.Empty;
                return ProjectMediaText(
                    GetString(Resource.String.project_media_live_result),
                    string.Empty,
                    await _sdk!.AllowProjectMediaAsync(string.Empty));
            });

        section.Add(SectionTitle(Resource.String.project_media_status_preview_title));
        section.Add(new TextView(this)
        {
            Text = GetString(Resource.String.project_media_status_preview_description)
        });
        foreach (var status in Enum.GetValues<ProjectMediaStatus>())
        {
            var previewStatus = status;
            AddButton(section, status + " (" + (int)status + ")", () =>
            {
                var content = previewStatus.Content();
                var errorMessage = previewStatus == ProjectMediaStatus.Success
                    ? string.Empty
                    : GetString(content.ConditionResource);
                section.Result.Text = _executionTrace.Message(
                    "PreviewProjectMediaStatus(" + previewStatus + ")",
                    ProjectMediaText(
                        GetString(Resource.String.project_media_preview_result),
                        packageName.Text ?? string.Empty,
                        new ProjectMediaResult(previewStatus, errorMessage)));
            });
        }
    }

    private void QuickTileSample()
    {
        var section = Section(Resource.String.quick_tile);
        AddButton(section, Resource.String.set_quick_tile, () =>
            RunOneWay(section, "setQuickTiles(id=Wifi, title=Wi-Fi)", () => _sdk!.SetQuickTiles(new QuickTile(QuickTileId.Wifi, "Wi-Fi"))));
    }

    private void ScannerSample()
    {
        RequestScannerImageReadPermission();
        var section = Section(Resource.String.scanner);
        var imagePath = TextField(Resource.String.floating_button_image_path, DefaultScannerButtonImagePath);
        imagePath.InputType = InputTypes.ClassText | InputTypes.TextVariationUri;
        section.Add(imagePath);
        AddAsyncButton(section, Resource.String.set_floating_button_image, "SetAndVerifyScannerButtonUi(imagePath)", async () =>
        {
            var result = await _sdk!.SetAndVerifyScannerButtonUiAsync(
                ScannerButtonUiOptions.ForImagePath((imagePath.Text ?? string.Empty).Trim()));
            return ScannerButtonUiText(result.SetResult) + "\nverified=" + result.IsVerified;
        });
        AddAsyncButton(section, Resource.String.default_floating_button_image, "SetAndVerifyScannerButtonUi(defaultImage)", async () =>
        {
            var result = await _sdk!.SetAndVerifyScannerButtonUiAsync(
                ScannerButtonUiOptions.ForImagePath(string.Empty));
            return ScannerButtonUiText(result.SetResult) + "\nverified=" + result.IsVerified;
        });
        AddAsyncButton(section, Resource.String.get_floating_button, "GetScannerButtonUi", async () =>
            ScannerButtonUiText(await _sdk!.GetScannerButtonUiAsync()));
        AddAsyncButton(section, Resource.String.reset_floating_button, "SetAndVerifyScannerButtonUi(default)", async () =>
        {
            var result = await _sdk!.SetAndVerifyScannerButtonUiAsync(new ScannerButtonUiOptions(
                imagePath: string.Empty,
                opacityPercent: 100,
                size: ScannerButtonUiSize.Medium));
            return ScannerButtonUiText(result.SetResult) + "\nverified=" + result.IsVerified;
        });
        AddAsyncButton(section, Resource.String.floating_button_small_20, "SetAndVerifyScannerButtonUi(small,20)", async () =>
        {
            var result = await _sdk!.SetAndVerifyScannerButtonUiAsync(new ScannerButtonUiOptions(
                opacityPercent: 20,
                size: ScannerButtonUiSize.Small));
            return ScannerButtonUiText(result.SetResult) + "\nverified=" + result.IsVerified;
        });
        AddAsyncButton(section, Resource.String.floating_button_large_100, "SetAndVerifyScannerButtonUi(large,100)", async () =>
        {
            var result = await _sdk!.SetAndVerifyScannerButtonUiAsync(new ScannerButtonUiOptions(
                opacityPercent: 100,
                size: ScannerButtonUiSize.Large));
            return ScannerButtonUiText(result.SetResult) + "\nverified=" + result.IsVerified;
        });
        AddAsyncButton(section, Resource.String.floating_button_invalid_image, "SetScannerButtonUi(invalidImage)", async () =>
            ScannerButtonUiText(await _sdk!.SetScannerButtonUiAsync(
                ScannerButtonUiOptions.ForImagePath("/sdcard/Download/m3sdk_missing_button_image.png"))));

    }

    private void StartUpSettingSample()
    {
        var section = Section(Resource.String.startup_setting);
        AddButton(section, Resource.String.reset_startup, () =>
            RunOneWay(section, "resetStartUpSetting", () => _sdk!.ResetStartUpSetting()));
    }

    private void TimeSample()
    {
        var section = Section(Resource.String.time);
        AddButton(section, Resource.String.get_timezone, () =>
            RunValue(section, "getTimeZone", () => _sdk!.GetTimeZone()));
    }

    private void UsbSample()
    {
        var section = Section(Resource.String.usb);
        AddButton(section, Resource.String.get_usb_modes, () =>
            RunValue(section, "getCurrentUsbModes", () => string.Join(", ", _sdk!.GetCurrentUsbModes())));
    }

    private void WifiSample()
    {
        var section = Section(Resource.String.wifi);
        AddButton(section, Resource.String.enable_wifi, () =>
            RunOneWay(section, "SetWifiEnabled(true)", () => _sdk!.SetWifiEnabled(true)));
        AddButton(section, Resource.String.disable_wifi, () =>
            RunOneWay(section, "SetWifiEnabled(false)", () => _sdk!.SetWifiEnabled(false)));
        AddAsyncButton(section, Resource.String.get_factory_wifi_mac, "getFactoryWifiMac", async () =>
        {
            var result = await _sdk!.GetFactoryWifiMacAsync();
            return result.Success
                ? "SUCCESS\n" + result.MacAddress
                : Failure(new InvalidOperationException("StartUp error=" + result.ErrorMessage));
        });
    }

    private void KeyToolSample()
    {
        var section = Section(Resource.String.keytool);
        var warning = new TextView(this)
        {
            Text = GetString(Resource.String.keytool_warning)
        };
        var mappingTitle = SectionTitle(Resource.String.key_mapping);
        var mappingDescription = new TextView(this)
        {
            Text = GetString(Resource.String.key_mapping_description)
        };
        var key = TextField(Resource.String.key_title, "Left Scan");
        var function = TextField(Resource.String.function_title, "Volume Up");
        section.Add(warning);
        section.Add(mappingTitle);
        section.Add(mappingDescription);
        section.Add(key);
        section.Add(function);
        AddButton(section, Resource.String.set_key_function, () =>
            RunOneWay(section, "setKeyFunction(key=" + (key.Text ?? string.Empty) + ", function=" + (function.Text ?? string.Empty) + ")", () => _sdk!.SetKeyFunction(key.Text ?? string.Empty, function.Text ?? string.Empty)));

        section.Add(SectionTitle(Resource.String.set_key_function_and_wake_up));
        section.Add(new TextView(this)
        {
            Text = GetString(Resource.String.set_key_function_and_wake_up_description)
        });
        AddButton(section, Resource.String.set_and_enable_wake_up, () =>
            RunOneWay(section, "SetKeyFunction(key=" + (key.Text ?? string.Empty) + ", function=" + (function.Text ?? string.Empty) + ", wakeUpEnabled=true)", () => _sdk!.SetKeyFunction(key.Text ?? string.Empty, function.Text ?? string.Empty, true)));
        AddButton(section, Resource.String.set_and_disable_wake_up, () =>
            RunOneWay(section, "SetKeyFunction(key=" + (key.Text ?? string.Empty) + ", function=" + (function.Text ?? string.Empty) + ", wakeUpEnabled=false)", () => _sdk!.SetKeyFunction(key.Text ?? string.Empty, function.Text ?? string.Empty, false)));

        section.Add(SectionTitle(Resource.String.scan_key_wake_up));
        section.Add(new TextView(this)
        {
            Text = GetString(Resource.String.scan_key_wake_up_description)
        });
        section.Add(SectionTitle(Resource.String.left_scan_key));
        AddButton(section, Resource.String.enable, () =>
            RunOneWay(section, "EnableLeftScanWakeUp", () => _sdk!.EnableLeftScanWakeUp()));
        AddButton(section, Resource.String.disable, () =>
            RunOneWay(section, "DisableLeftScanWakeUp", () => _sdk!.DisableLeftScanWakeUp()));
        section.Add(SectionTitle(Resource.String.right_scan_key));
        AddButton(section, Resource.String.enable, () =>
            RunOneWay(section, "EnableRightScanWakeUp", () => _sdk!.EnableRightScanWakeUp()));
        AddButton(section, Resource.String.disable, () =>
            RunOneWay(section, "DisableRightScanWakeUp", () => _sdk!.DisableRightScanWakeUp()));

        section.Add(SectionTitle(Resource.String.navigation_buttons));
        section.Add(new TextView(this)
        {
            Text = GetString(Resource.String.navigation_buttons_description)
        });
        section.Add(SectionTitle(Resource.String.home_button));
        AddButton(section, Resource.String.enable, () =>
            RunOneWay(section, "EnableHomeButton", () => _sdk!.EnableHomeButton()));
        AddButton(section, Resource.String.disable, () =>
            RunOneWay(section, "DisableHomeButton", () => _sdk!.DisableHomeButton()));
        section.Add(SectionTitle(Resource.String.recent_button));
        AddButton(section, Resource.String.enable, () =>
            RunOneWay(section, "EnableRecentButton", () => _sdk!.EnableRecentButton()));
        AddButton(section, Resource.String.disable, () =>
            RunOneWay(section, "DisableRecentButton", () => _sdk!.DisableRecentButton()));
    }

    private void AppCenterSample()
    {
        var section = Section(Resource.String.appcenter);
        section.Add(new TextView(this)
        {
            Text = GetString(Resource.String.appcenter_warning)
        });

        var currentPassword = TextField(Resource.String.current_admin_password, string.Empty);
        var newPassword = TextField(Resource.String.new_admin_password, string.Empty);
        currentPassword.InputType = InputTypes.ClassText | InputTypes.TextVariationPassword;
        newPassword.InputType = InputTypes.ClassText | InputTypes.TextVariationPassword;
        section.Add(currentPassword);
        section.Add(newPassword);

        AddButton(section, Resource.String.change_admin_password, () =>
            RunOneWay(
                section,
                "ChangeKioskAdminPassword",
                () => _sdk!.ChangeKioskAdminPassword(
                    currentPassword.Text ?? string.Empty,
                    newPassword.Text ?? string.Empty),
                "AppCenter=" + PackageVersion(AppCenterPackage)));

        section.Add(SectionTitle(Resource.String.keep_admin_mode_on_sleep));
        AddButton(section, Resource.String.enable, () =>
            RunOneWay(
                section,
                "SetKeepAdminModeOnSleep(true)",
                () => _sdk!.SetKeepAdminModeOnSleep(true),
                "AppCenter=" + PackageVersion(AppCenterPackage)));
        AddButton(section, Resource.String.disable, () =>
            RunOneWay(
                section,
                "SetKeepAdminModeOnSleep(false)",
                () => _sdk!.SetKeepAdminModeOnSleep(false),
                "AppCenter=" + PackageVersion(AppCenterPackage)));
    }

    private SectionView Section(int titleId)
    {
        var layout = new LinearLayout(this)
        {
            Orientation = Orientation.Vertical
        };
        layout.SetPadding(24, 24, 24, 24);

        var title = new TextView(this)
        {
            Text = GetString(titleId),
            TextSize = 20
        };
        var result = new TextView(this)
        {
            Text = GetString(Resource.String.not_run)
        };
        result.SetPadding(0, 16, 0, 0);

        layout.AddView(title);
        layout.AddView(result);
        _content!.AddView(layout, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MatchParent,
            ViewGroup.LayoutParams.WrapContent)
        {
            BottomMargin = 20
        });
        return new SectionView(layout, result);
    }

    private EditText TextField(int hintId, string value)
    {
        var field = new EditText(this)
        {
            Hint = GetString(hintId),
            Text = value
        };
        field.SetSingleLine(true);
        return field;
    }

    private TextView SectionTitle(int titleId)
    {
        var title = new TextView(this)
        {
            Text = GetString(titleId),
            TextSize = 18
        };
        title.SetPadding(0, 24, 0, 4);
        return title;
    }

    private void AddButton(SectionView section, int labelId, Action action)
    {
        AddButton(section, GetString(labelId), action);
    }

    private void AddButton(SectionView section, string label, Action action)
    {
        var button = new Button(this)
        {
            Text = label
        };
        button.Click += (_, _) =>
        {
            HideSoftKeyboard(button);
            action();
            RevealResult(section);
        };
        section.Add(button);
    }

    private void AddAsyncButton(SectionView section, int labelId, string operation, Func<Task<string>> action)
    {
        var button = new Button(this)
        {
            Text = GetString(labelId)
        };
        button.Click += async (_, _) =>
        {
            HideSoftKeyboard(button);
            try
            {
                var body = await action();
                section.Result.Text = _executionTrace.Message(operation, body);
            }
            catch (Exception error)
            {
                section.Result.Text = _executionTrace.Message(operation, Failure(error));
            }
            RevealResult(section);
        };
        section.Add(button);
    }

    private void HideSoftKeyboard(View source)
    {
        CurrentFocus?.ClearFocus();
        var keyboard = GetSystemService(InputMethodService) as InputMethodManager;
        keyboard?.HideSoftInputFromWindow(source.WindowToken, HideSoftInputFlags.None);
    }

    private static void RevealResult(SectionView section)
    {
        section.Result.Post(() => section.Result.RequestRectangleOnScreen(
            new Rect(0, 0, section.Result.Width, section.Result.Height),
            true));
    }

    private void RunOneWay(
        SectionView section,
        string operation,
        Action action,
        string? successDetails = null)
    {
        string body;
        try
        {
            action();
            body = GetString(Resource.String.request_sent_unverified);
            if (!string.IsNullOrEmpty(successDetails))
                body += "\n" + successDetails;
        }
        catch (Exception error)
        {
            body = Failure(error);
        }
        section.Result.Text = _executionTrace.Message(operation, body);
    }

    private void RunValue(SectionView section, string operation, Func<string> value)
    {
        string body;
        try
        {
            body = "SUCCESS\n" + value();
        }
        catch (Exception error)
        {
            body = Failure(error);
        }
        section.Result.Text = _executionTrace.Message(operation, body);
    }

    private bool AirplaneModeEnabled()
    {
        return Settings.Global.GetInt(ContentResolver, Settings.Global.AirplaneModeOn, 0) == 1;
    }

    private string EnvironmentText()
    {
        var sample = PackageManager?.GetPackageInfo(PackageName ?? string.Empty, PackageInfoFlags.Activities)?.VersionName;
        return
            "model=" + Build.Model +
            "\nandroid=" + Build.VERSION.Release + " (API " + (int)Build.VERSION.SdkInt + ")" +
            "\nsample=" + sample +
            "\nm3sdk=" + SdkVersion() +
            "\nStartUp=" + PackageVersion(StartUpPackage) +
            "\nScanEmul=" + PackageVersion(ScanEmulPackage) +
            "\nKeyTool SL20=" + PackageVersion(KeyToolSl20Package) +
            "\nKeyTool Wake-Up=" + PackageVersion(KeyToolWakeUpPackage) +
            "\nAppCenter=" + PackageVersion(AppCenterPackage);
    }

    private string Failure(Exception error)
    {
        return
            "FAILED" +
            "\ntype=" + error.GetType().FullName +
            "\nmessage=" + (error.Message ?? "No message") +
            "\nmodel=" + Build.Model +
            "\nandroid=" + Build.VERSION.Release + " (API " + (int)Build.VERSION.SdkInt + ")" +
            "\nm3sdk=" + SdkVersion() +
            "\nStartUp=" + PackageVersion(StartUpPackage) +
            "\nScanEmul=" + PackageVersion(ScanEmulPackage) +
            "\nKeyTool SL20=" + PackageVersion(KeyToolSl20Package) +
            "\nKeyTool Wake-Up=" + PackageVersion(KeyToolWakeUpPackage) +
            "\nAppCenter=" + PackageVersion(AppCenterPackage);
    }

    private static string ScannerButtonUiText(ScannerButtonUiResult result)
    {
        var settings = result.Settings;
        return
            "transport=" + result.TransportStatus +
            "\nsuccess=" + result.Success +
            "\nstatus=" + result.Status +
            "\nrawStatus=" + result.RawStatus +
            "\nruntimeApplied=" + result.RuntimeApplied +
            "\nsaved=" + result.IsSaved +
            "\nimagePath=" + (settings == null ? string.Empty : settings.ImagePath) +
            "\nopacity=" + (settings == null ? string.Empty : settings.OpacityPercent.ToString()) +
            "\nsize=" + (settings == null ? string.Empty : settings.Size.ToString());
    }

    private string ProjectMediaText(
        string source,
        string packageName,
        ProjectMediaResult result)
    {
        var content = result.Status.Content();
        return
            source +
            "\nstatus=" + result.Status +
            "\ncode=" + (int)result.Status +
            "\nisSuccess=" + result.IsSuccess +
            "\npackage=" + (string.IsNullOrEmpty(packageName) ? "<empty>" : packageName) +
            "\nerrorMessage=" + (string.IsNullOrEmpty(result.ErrorMessage) ? "<empty>" : result.ErrorMessage) +
            "\ncondition=" + GetString(content.ConditionResource) +
            "\nnextAction=" + GetString(content.ActionResource) +
            "\ndevice=" + Build.Model;
    }

    private static string SdkVersion()
    {
        return typeof(M3Mobile).Assembly.GetCustomAttribute<AssemblyInformationalVersionAttribute>()
                   ?.InformationalVersion
               ?? typeof(M3Mobile).Assembly.GetName().Version?.ToString()
               ?? "UNKNOWN";
    }

    private string PackageVersion(string packageName)
    {
        try
        {
            return PackageManager?.GetPackageInfo(packageName, PackageInfoFlags.Activities)?.VersionName
                   ?? "INSTALLED_VERSION_UNAVAILABLE";
        }
        catch (PackageManager.NameNotFoundException)
        {
            return "NOT_INSTALLED_OR_NOT_VISIBLE";
        }
    }

    private sealed class SectionView
    {
        internal SectionView(LinearLayout layout, TextView result)
        {
            Layout = layout;
            Result = result;
        }

        internal LinearLayout Layout { get; }
        internal TextView Result { get; }

        internal void Add(View view)
        {
            Layout.AddView(view, Layout.ChildCount - 1);
        }
    }
}
