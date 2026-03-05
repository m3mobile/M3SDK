package net.m3mobile.processor

import androidx.annotation.Keep
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
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

class AppVersionMapSourceProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val annotationName: String,
    private val providerInterfaceName: String,
    private val modelVersionProviderInterfaceName: String
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val annotatedFunctions = resolver.getSymbolsWithAnnotation(annotationName)
            .filterIsInstance<KSFunctionDeclaration>()
            .toSet()

        if (annotatedFunctions.isEmpty())
            return emptyList()

        val functionsByFile = annotatedFunctions.groupBy { it.containingFile!! }
        val generatedProviders = mutableListOf<String>()
        val generatedModelVersionProviders = mutableListOf<String>()
        val allSourceFiles = mutableListOf<KSFile>()
        val modelVersionSourceFiles = mutableListOf<KSFile>()

        functionsByFile.forEach { (file, functions) ->
            val versionMap = mutableMapOf<String, String>()
            val modelVersionMap = mutableMapOf<String, Map<String, String>>()

            functions.forEach { func ->
                val key = func.parentDeclaration?.qualifiedName?.asString() +
                        "." + func.simpleName.asString() +
                        func.parameters.map { it.type }.joinToString(prefix = "(", postfix=")")

                val annotation = func.annotations.firstOrNull {
                    it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationName
                } ?: return@forEach

                val version = annotation.arguments.firstOrNull { it.name?.asString() == VERSION_ANNOTATION_PROPERTY_NAME }?.value as? String

                if (version != null) {
                    versionMap[key] = version
                }

                val overrides = extractModelVersionOverrides(annotation)
                if (overrides.isNotEmpty()) {
                    modelVersionMap[key] = overrides
                }
            }

            if (versionMap.isEmpty()) return@forEach

            val baseClassName = file.fileName
                .substringBeforeLast(".")
                .replaceFirstChar { it.titlecase(Locale.getDefault()) }
            val providerPackageName = "$BASE_GENERATED_FILE_PACKAGE.version"

            // Generate version map source
            val providerClassName = baseClassName + APP_VERSION_MAP_SOURCE_CLASS_NAME
            generatedProviders.add("$providerPackageName.$providerClassName")
            allSourceFiles.add(file)

            generateMapSourceFile(
                providerPackageName, providerClassName, providerInterfaceName, file
            ) {
                add("return mapOf(\n")
                indent()
                versionMap.forEach { entry ->
                    add("%S to %S,\n", entry.key, entry.value)
                }
                unindent()
                add(") as Map<String, V>")
            }

            // Generate model version map source (only if overrides exist)
            if (modelVersionMap.isNotEmpty()) {
                val modelVersionClassName = baseClassName + MODEL_VERSION_MAP_SOURCE_CLASS_NAME
                generatedModelVersionProviders.add("$providerPackageName.$modelVersionClassName")
                modelVersionSourceFiles.add(file)

                generateMapSourceFile(
                    providerPackageName, modelVersionClassName, modelVersionProviderInterfaceName, file
                ) {
                    add("return mapOf(\n")
                    indent()
                    modelVersionMap.forEach { (key, overrides) ->
                        add("%S to mapOf(\n", key)
                        indent()
                        overrides.forEach { (model, version) ->
                            add("%S to %S,\n", model, version)
                        }
                        unindent()
                        add("),\n")
                    }
                    unindent()
                    add(") as Map<String, V>")
                }
            }
        }

        writeServiceFile(providerInterfaceName, generatedProviders, allSourceFiles)
        writeServiceFile(modelVersionProviderInterfaceName, generatedModelVersionProviders, modelVersionSourceFiles)

        return emptyList()
    }

    private fun generateMapSourceFile(
        packageName: String,
        className: String,
        interfaceName: String,
        sourceFile: KSFile,
        codeBuilder: com.squareup.kotlinpoet.CodeBlock.Builder.() -> Unit
    ) {
        val annotations = listOf(
            AnnotationSpec.builder(Suppress::class).addMember("%S", "UNCHECKED_CAST").build(),
            AnnotationSpec.builder(Keep::class).build()
        )

        val typeV = TypeVariableName("V", ANY)
        val fileSpec = FileSpec.builder(packageName, className)
            .addType(
                TypeSpec.classBuilder(className)
                    .addSuperinterface(ClassName.bestGuess(interfaceName))
                    .addAnnotations(annotations)
                    .addFunction(
                        FunSpec.builder("get")
                            .addModifiers(KModifier.OVERRIDE)
                            .addTypeVariable(typeV)
                            .returns(
                                Map::class.asClassName()
                                    .parameterizedBy(String::class.asClassName(), typeV)
                            )
                            .addCode(buildCodeBlock(codeBuilder))
                            .build()
                    )
                    .build()
            )
            .build()

        try {
            fileSpec.writeTo(codeGenerator, Dependencies(true, sourceFile))
        } catch (e: Exception) {
            logger.error("Error writing file for ${sourceFile.fileName}: ${e.message}")
        }
    }

    private fun writeServiceFile(
        interfaceName: String,
        providers: List<String>,
        sourceFiles: List<KSFile>
    ) {
        if (providers.isEmpty()) return
        try {
            val serviceFile = codeGenerator.createNewFile(
                dependencies = Dependencies(true, *sourceFiles.toTypedArray()),
                packageName = META_INF_FILE_PACKAGE,
                fileName = interfaceName,
                extensionName = ""
            )
            serviceFile.bufferedWriter(Charsets.UTF_8).use { writer ->
                providers.forEach { providerClass ->
                    writer.write(providerClass)
                    writer.newLine()
                }
            }
        } catch (e: Exception) {
            logger.error("Error writing service file: ${e.message}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractModelVersionOverrides(annotation: KSAnnotation): Map<String, String> {
        val overridesArg = annotation.arguments
            .firstOrNull { it.name?.asString() == MODEL_VERSION_ANNOTATION_PROPERTY_NAME }
            ?.value as? List<KSAnnotation> ?: return emptyMap()

        val result = mutableMapOf<String, String>()
        for (modelVersionAnnotation in overridesArg) {
            val model = modelVersionAnnotation.arguments
                .firstOrNull { it.name?.asString() == "model" }
                ?.value as? KSType
            val version = modelVersionAnnotation.arguments
                .firstOrNull { it.name?.asString() == VERSION_ANNOTATION_PROPERTY_NAME }
                ?.value as? String

            if (model != null && version != null) {
                result[model.declaration.simpleName.asString()] = version
            }
        }
        return result
    }
}