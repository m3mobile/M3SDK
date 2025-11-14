package net.m3mobile.processor

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.writeTo
import java.util.Locale

class DeviceSupportProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val supportedModelsClassName = "net.m3mobile.core.SupportedModels"
        val unsupportedModelsClassName = "net.m3mobile.core.UnsupportedModels"
        val providerInterfaceName = "net.m3mobile.core.device.DeviceSupportProvider"

        val annotatedFunctions = (
                resolver.getSymbolsWithAnnotation(supportedModelsClassName) +
                        resolver.getSymbolsWithAnnotation(unsupportedModelsClassName)
                ).filterIsInstance<KSFunctionDeclaration>().toSet()

        if (annotatedFunctions.isEmpty())
            return emptyList()


        val functionsByFile = annotatedFunctions.groupBy { it.containingFile!! }

        val allModelNames = resolver.getAllDeviceModelNames()
        functionsByFile.forEach { (file, functions) ->
            val supportMapForModule = mutableMapOf<String, Set<String>>()

            functions.forEach { func ->
                val key = func.parentDeclaration?.qualifiedName?.asString() + "." + func.simpleName.asString()
                val supportedAnnotation = func.annotations.firstOrNull {
                    it.annotationType.resolve().declaration.qualifiedName?.asString() == supportedModelsClassName
                }
                val unsupportedAnnotation = func.annotations.firstOrNull {
                    it.annotationType.resolve().declaration.qualifiedName?.asString() == unsupportedModelsClassName
                }

                val finalSupportedModels = when {
                    supportedAnnotation != null -> getModelsFromAnnotation(supportedAnnotation)
                    unsupportedAnnotation != null -> allModelNames - getModelsFromAnnotation(unsupportedAnnotation)
                    else -> return@forEach
                }
                supportMapForModule[key] = finalSupportedModels
            }

            if (supportMapForModule.isEmpty())
                return@forEach

            val providerClassName = file.fileName
                .substringBeforeLast(".")
                .replaceFirstChar { it.titlecase(Locale.getDefault()) } + "DeviceSupportProvider"
            val providerPackageName = "net.m3mobile.sdk.generated"

            val fileSpec = FileSpec.builder(providerPackageName, providerClassName)
                .addType(TypeSpec.classBuilder(providerClassName)
                    .addSuperinterface(ClassName.bestGuess(providerInterfaceName))
                    .addFunction(FunSpec.builder("getSupportMap")
                        .addModifiers(KModifier.OVERRIDE)
                        .returns(Map::class.asClassName().parameterizedBy(String::class.asClassName(), Set::class.asClassName().parameterizedBy(String::class.asClassName())))
                        .addCode(buildCodeBlock {
                            add("return mapOf(\n")
                            indent()
                            supportMapForModule.forEach { (key, models) ->
                                val formatPlaceholders = models.joinToString { "%S" }
                                val args = arrayOf(key) + models.toTypedArray()
                                add("%S to setOf($formatPlaceholders),\n", *args)
                            }
                            unindent()
                            add(")")
                            })
                        .build())
                    .build())
                .build()

            try {
                fileSpec.writeTo(codeGenerator, Dependencies(true, file))
            } catch(e: Exception) {
                logger.error("Error writing file for ${file.fileName}: ${e.message}")
            }

            try {
                val serviceFile = codeGenerator.createNewFile(
                    dependencies = Dependencies(true, file),
                    packageName = "META-INF.services",
                    fileName = providerInterfaceName,
                    extensionName = ""
                )
                serviceFile.bufferedWriter(Charsets.UTF_8).use {
                    it.write("$providerPackageName.$providerClassName")
                }
            } catch (e: Exception) {
                logger.error("Error writing service file for ${file.fileName}: ${e.message}")
            }
        }

        return emptyList()
    }

    private fun getModelsFromAnnotation(annotation: KSAnnotation): Set<String> {
        val modelsArgument = annotation.arguments.firstOrNull { it.name?.asString() == "models" }

        @Suppress("UNCHECKED_CAST")
        val modelDeclarations = (modelsArgument?.value as? List<KSType>)?.map { it.declaration } ?: return emptySet()
        return modelDeclarations.map { it.simpleName.asString() }.toSet()
    }

    private fun Resolver.getAllDeviceModelNames(): Set<String> {
        val deviceModelClass = getClassDeclarationByName("net.m3mobile.core.device.DeviceModel") ?: return emptySet()
        return deviceModelClass.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.classKind == ClassKind.ENUM_ENTRY }
            .map { it.simpleName.asString() }
            .toSet()
    }
}