package net.m3mobile.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
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

class StartUpSupportProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotationName = "net.m3mobile.core.RequiresStartUp"
        val providerInterfaceName = "net.m3mobile.core.startup.StartUpSupportProvider"

        val annotatedFunctions = resolver.getSymbolsWithAnnotation(annotationName)
            .filterIsInstance<KSFunctionDeclaration>()
            .toSet()

        if (annotatedFunctions.isEmpty())
            return emptyList()

        val functionsByFile = annotatedFunctions.groupBy { it.containingFile!! }
        val generatedProviders = mutableListOf<String>()
        val allSourceFiles = mutableListOf<KSFile>()

        functionsByFile.forEach { (file, functions) ->
            val versionMap = mutableMapOf<String, String>()

            functions.forEach { func ->
                val key = func.parentDeclaration?.qualifiedName?.asString() +
                        "." + func.simpleName.asString() +
                        func.parameters.map { it.type }.joinToString(prefix = "(", postfix=")")
                
                val annotation = func.annotations.firstOrNull {
                    it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationName
                } ?: return@forEach

                val version = annotation.arguments.firstOrNull { it.name?.asString() == "version" }?.value as? String
                
                if (version != null) {
                    versionMap[key] = version
                }
            }

            if (versionMap.isEmpty()) return@forEach

            val providerClassName = file.fileName
                .substringBeforeLast(".")
                .replaceFirstChar { it.titlecase(Locale.getDefault()) } + "StartUpSupportProvider"
            val providerPackageName = "net.m3mobile.sdk.generated"

            generatedProviders.add("$providerPackageName.$providerClassName")
            allSourceFiles.add(file)

            val fileSpec = FileSpec.builder(providerPackageName, providerClassName)
                .addType(
                    TypeSpec.classBuilder(providerClassName)
                        .addSuperinterface(ClassName.bestGuess(providerInterfaceName))
                        .addFunction(
                            FunSpec.builder("getStartUpVersionMap")
                                .addModifiers(KModifier.OVERRIDE)
                                .returns(
                                    Map::class.asClassName().parameterizedBy(
                                        String::class.asClassName(),
                                        String::class.asClassName()
                                    )
                                )
                                .addCode(buildCodeBlock {
                                    add("return mapOf(\n")
                                    indent()
                                    versionMap.forEach { entry ->
                                        add("%S to %S,\n", entry.key, entry.value)
                                    }
                                    unindent()
                                    add(")")
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
                    packageName = "META-INF.services",
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
}