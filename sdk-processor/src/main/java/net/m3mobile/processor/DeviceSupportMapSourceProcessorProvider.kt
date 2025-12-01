package net.m3mobile.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class DeviceSupportMapSourceProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DeviceSupportMapSourceProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}