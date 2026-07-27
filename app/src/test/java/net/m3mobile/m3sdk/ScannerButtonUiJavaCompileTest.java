package net.m3mobile.m3sdk;

import net.m3mobile.feature.scanemul.params.ScannerButtonUiOptions;
import net.m3mobile.feature.scanemul.params.ScannerButtonUiResult;
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSettings;
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSize;
import net.m3mobile.feature.scanemul.api.ScanEmulScannerSettingApi;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScannerButtonUiJavaCompileTest {

    @Test
    public void scannerButtonUiTypesAreUsableFromJava() {
        ScannerButtonUiOptions options = new ScannerButtonUiOptions(
                "",
                100,
                ScannerButtonUiSize.MEDIUM
        );
        ScannerButtonUiSettings settings = new ScannerButtonUiSettings(
                "",
                100,
                ScannerButtonUiSize.MEDIUM
        );

        assertEquals("", options.imagePath());
        assertEquals(Integer.valueOf(100), options.opacityPercent());
        assertEquals(ScannerButtonUiSize.MEDIUM, settings.size());
    }

    private void compileOnlyReferences(ScanEmulScannerSettingApi sdk) {
        ScannerButtonUiOptions options = new ScannerButtonUiOptions(
                "/sdcard/Download/button.png",
                80,
                ScannerButtonUiSize.LARGE
        );
        sdk.setScannerButtonUi(options, (ScannerButtonUiResult result, Exception error) -> {
        });
        sdk.setScannerButtonUi(options, "set-request-id", (result, error) -> {
        });
        sdk.getScannerButtonUi((result, error) -> {
        });
        sdk.getScannerButtonUi("get-request-id", (result, error) -> {
        });
        sdk.setAndVerifyScannerButtonUi(options, (result, error) -> {
        });
    }
}
