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
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.writeTo
import java.util.Locale

class DeviceSupportMapSourceProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val supportedModelsClassName = SUPPORTED_MODELS_ANNOTATION_NAME
        val unsupportedModelsClassName = UNSUPPORTED_MODELS_ANNOTATION_NAME
        val providerInterfaceName = DEVICE_SUPPORT_MAP_SOURCE_INTERFACE_NAME

        val annotatedFunctions = (
                resolver.getSymbolsWithAnnotation(supportedModelsClassName) +
                        resolver.getSymbolsWithAnnotation(unsupportedModelsClassName)
                ).filterIsInstance<KSFunctionDeclaration>().toSet()

        if (annotatedFunctions.isEmpty())
            return emptyList()

        val functionsByFile = annotatedFunctions.groupBy { it.containingFile!! }

        val allModelNames = resolver.getAllDeviceModelNames()
        
        val generatedProviders = mutableListOf<String>()
        val allSourceFiles = mutableListOf<KSFile>()

        functionsByFile.forEach { (file, functions) ->
            val supportMapForModule = mutableMapOf<String, Set<String>>()

            functions.forEach { func ->
                val key = func.parentDeclaration?.qualifiedName?.asString() +
                        "." + func.simpleName.asString() +
                        func.parameters.map { it.type }.joinToString(prefix = "(", postfix=")")

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
                    ) - "UNKNOWN"

                    else -> return@forEach
                }
                supportMapForModule[key] = finalSupportedModels
            }

            if (supportMapForModule.isEmpty())
                return@forEach

            val providerClassName = file.fileName
                .substringBeforeLast(".")
                .replaceFirstChar { it.titlecase(Locale.getDefault()) } + DEVICE_SUPPORT_MAP_SOURCE_CLASS_NAME
            val providerPackageName = "$BASE_GENERATED_FILE_PACKAGE.device"
            
            generatedProviders.add("$providerPackageName.$providerClassName")
            allSourceFiles.add(file)

            val suppressAnnotation = AnnotationSpec.builder(Suppress::class)
                .addMember("%S", "UNCHECKED_CAST")
                .build()
            val typeV = TypeVariableName("V", ANY)
            val fileSpec = FileSpec.builder(providerPackageName, providerClassName)
                .addType(
                    TypeSpec.classBuilder(providerClassName)
                        .addSuperinterface(ClassName.bestGuess(providerInterfaceName))
                        .addFunction(
                            FunSpec.builder("get")
                                .addModifiers(KModifier.OVERRIDE)
                                .addTypeVariable(typeV)
                                .returns(
                                    Map::class.asClassName()
                                        .parameterizedBy(String::class.asClassName(), typeV)
                                )
                                .addAnnotation(suppressAnnotation)
                                .addCode(buildCodeBlock {
                                    add("return mapOf(\n")
                                    indent()
                                    supportMapForModule.forEach { (key, models) ->
                                        add("%S to setOf(${models.joinToString { "\"$it\"" }}),\n", key)
                                    }
                                    unindent()
                                    add(") as Map<String, V>")
                                })
                                .build()
                        )
                        .build()
                )
                .build()

            try {
                fileSpec.writeTo(codeGenerator, Dependencies(true, file))
            } catch (e: Exception) {
                logger.error("Error writing file for ${file.fileName}: ${e.message}")
            }
        }

        if (generatedProviders.isNotEmpty()) {
            try {
                val serviceFile = codeGenerator.createNewFile(
                    dependencies = Dependencies(true, *allSourceFiles.toTypedArray()),
                    packageName = META_INF_FILE_PACKAGE,
                    fileName = providerInterfaceName,
                    extensionName = ""
                )
                serviceFile.bufferedWriter(Charsets.UTF_8).use { writer ->
                    generatedProviders.forEach { providerClass ->
                        writer.write(providerClass)
                        writer.newLine()
                    }
                }
            } catch (e: Exception) {
                logger.error("Error writing service file: ${e.message}")
            }
        }

        return emptyList()
    }

    private fun getModelsFromAnnotation(annotation: KSAnnotation): Set<String> {
        val modelsArgument = annotation.arguments.firstOrNull { it.name?.asString() == "models" }

        @Suppress("UNCHECKED_CAST")
        val modelDeclarations =
            (modelsArgument?.value as? List<KSType>)?.map { it.declaration } ?: return emptySet()
        return modelDeclarations.map { it.simpleName.asString() }.toSet()
    }

    private fun Resolver.getAllDeviceModelNames(): Set<String> {
        val deviceModelClass =
            getClassDeclarationByName(DEVICE_MODEL_CLASS_NAME) ?: return emptySet()
        return deviceModelClass.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.classKind == ClassKind.ENUM_ENTRY }
            .map { it.simpleName.asString() }
            .toSet()
    }
}
