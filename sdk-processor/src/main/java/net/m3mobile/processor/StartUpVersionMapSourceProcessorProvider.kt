package net.m3mobile.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class StartUpVersionMapSourceProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AppVersionMapSourceProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            annotationName = "net.m3mobile.core.RequiresStartUp",
            providerInterfaceName = "net.m3mobile.core.source.StartUpVersionMapSource"
        )
    }
}