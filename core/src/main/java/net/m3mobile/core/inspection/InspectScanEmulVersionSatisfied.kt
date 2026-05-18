package net.m3mobile.core.inspection

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.source.ScanEmulModelVersionMapSource
import net.m3mobile.core.source.ScanEmulVersionMapSource
import java.util.ServiceLoader

private const val SCANEMUL_APP_NAME = "ScanEmul"
private const val SCANEMUL_APP_PACKAGE = "net.m3mobile.app.scanemul"

@InternalM3Api
public class InspectScanEmulVersionSatisfied
    : InspectAppVersionSatisfied<ScanEmulVersionMapSource, ScanEmulModelVersionMapSource>() {
    override val serviceLoader: ServiceLoader<ScanEmulVersionMapSource> =
        ServiceLoader.load(ScanEmulVersionMapSource::class.java)
    override val modelVersionServiceLoader: ServiceLoader<ScanEmulModelVersionMapSource> =
        ServiceLoader.load(ScanEmulModelVersionMapSource::class.java)
    override val appName: String = SCANEMUL_APP_NAME
    override val appPackage: String = SCANEMUL_APP_PACKAGE
}
