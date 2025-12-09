package net.m3mobile.core.inspection

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.source.StartUpVersionMapSource
import java.util.ServiceLoader

private const val STARTUP_APP_NAME = "StartUp"
private const val STARTUP_APP_PACKAGE = "com.m3.startup"

@InternalM3Api
class InspectStartUpVersionSatisfied : InspectAppVersionSatisfied<StartUpVersionMapSource>() {
    override val serviceLoader = ServiceLoader.load(StartUpVersionMapSource::class.java)
    override val appName = STARTUP_APP_NAME
    override val appPackage = STARTUP_APP_PACKAGE
}
