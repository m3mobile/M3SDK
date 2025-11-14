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
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.writeTo

class DeviceSupportProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val supportedModelsClassName = "net.m3mobile.core.SupportedModels"
        val unsupportedModelsClassName = "net.m3mobile.core.UnsupportedModels"

        val annotatedFunctions = (
                resolver.getSymbolsWithAnnotation(supportedModelsClassName) +
                        resolver.getSymbolsWithAnnotation(unsupportedModelsClassName)
                ).filterIsInstance<KSFunctionDeclaration>().toSet()

        if (annotatedFunctions.isEmpty())
            return emptyList()

        val supportMap = mutableMapOf<String, Set<String>>()
        val allModelNames = getAllDeviceModelNames(resolver)

        annotatedFunctions.forEach { func ->
            val key =
                func.parentDeclaration?.qualifiedName?.asString() + "." + func.simpleName.asString()

            val supportedAnnotation = func.annotations.firstOrNull {
                it.annotationType.resolve().declaration.qualifiedName?.asString() == supportedModelsClassName
            }
            val unsupportedAnnotation = func.annotations.firstOrNull {
                it.annotationType.resolve().declaration.qualifiedName?.asString() == unsupportedModelsClassName
            }

            val finalSupportedModels = when {
                supportedAnnotation != null -> getModelsFromAnnotation(supportedAnnotation)
                unsupportedAnnotation != null -> allModelNames - getModelsFromAnnotation(
                    unsupportedAnnotation
                )

                else -> return@forEach
            }
            supportMap[key] = finalSupportedModels
        }

        if (supportMap.isNotEmpty()) {
            val fileSpec =
                FileSpec.builder("net.m3mobile.core.device", "GeneratedDeviceSupportInfo")
                    .addType(
                        TypeSpec.objectBuilder("GeneratedDeviceSupportInfo")
                            .addProperty(
                                PropertySpec.builder(
                                    "supportMap",
                                    Map::class.asClassName().parameterizedBy(
                                        String::class.asClassName(),
                                        Set::class.asClassName()
                                            .parameterizedBy(String::class.asClassName())
                                    )
                                )
                                    .initializer(CodeBlock.builder().apply {
                                        add("mapOf(\n")
                                        indent()
                                        supportMap.forEach { (key, models) ->
                                            add(
                                                "%S to setOf(${models.joinToString { "%S" }}),\n",
                                                key
                                            )
                                        }
                                        unindent()
                                        add(")")
                                    }.build())
                                    .build()
                            )
                            .build()
                    )
                    .build()

            fileSpec.writeTo(
                codeGenerator,
                Dependencies(
                    true,
                    *annotatedFunctions.mapNotNull { it.containingFile }.toTypedArray()
                )
            )
        }

        return emptyList()
    }

    private fun getModelsFromAnnotation(annotation: KSAnnotation): Set<String> {
        val modelsArgument = annotation.arguments.firstOrNull { it.name?.asString() == "models" }

        val modelDeclarations = (modelsArgument?.value as? List<KSType>)?.map { it.declaration } ?: return emptySet()
        return modelDeclarations.map { it.simpleName.asString() }.toSet()
    }

    private fun getAllDeviceModelNames(resolver: Resolver): Set<String> {
        val deviceModelClass = resolver.getClassDeclarationByName("net.m3mobile.core.device.DeviceModel") ?: return emptySet()
        return deviceModelClass.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.classKind == ClassKind.ENUM_ENTRY }
            .map { it.simpleName.asString() }
            .toSet()
    }
}