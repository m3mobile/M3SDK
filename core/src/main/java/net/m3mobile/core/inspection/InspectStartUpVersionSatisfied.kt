package net.m3mobile.core.inspection

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.source.StartUpVersionMapSource
import java.util.ServiceLoader

@InternalM3Api
class InspectStartUpVersionSatisfied : InspectAppVersionSatisfied<StartUpVersionMapSource>() {
    override val serviceLoader = ServiceLoader.load(StartUpVersionMapSource::class.java)
    override val appName = "StartUp"
    override val appPackage = "com.m3.startup"
}
