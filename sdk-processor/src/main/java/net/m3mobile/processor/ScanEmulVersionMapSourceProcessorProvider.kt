package net.m3mobile.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class ScanEmulVersionMapSourceProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AppVersionMapSourceProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            annotationName = REQUIRE_SCANEMUL_ANNOTATION_NAME,
            providerInterfaceName = SCANEMUL_VERSION_MAP_SOURCE_INTERFACE_NAME
        )
    }
}