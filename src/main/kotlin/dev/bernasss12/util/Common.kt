package dev.bernasss12.util

import org.slf4j.Logger
import java.io.File
import java.nio.file.Path

object Common {
    private val unsafeRegex = "[^a-zA-Z0-9\\-]".toRegex()
    private val multiUnderscoresRegex = "_+".toRegex()

    fun getTempFolder(): File = File("temp").also { it.mkdir() }
    fun makeSafeIdentifier(text: String): String =
        text.replace(unsafeRegex, "_").replace(multiUnderscoresRegex, "_")
    fun tempFile(prefix: String, text: String, extension: String = ""): Path =
        getTempFolder().toPath().resolve("${prefix}-${makeSafeIdentifier(text)}" + if (extension.isBlank()) "" else ".$extension")

    fun runInkscape(logger: Logger, input: String, inkscapeOptions: String) {
        val inkscapeProcess = Runtime.getRuntime().exec("inkscape --pipe $inkscapeOptions")
        inkscapeProcess.outputStream.use {
            it.write(input.toByteArray())
        }
        inkscapeProcess.waitFor()
        inkscapeProcess.inputReader().readText().takeIf { it.isNotBlank() }.also {
            logger.debug("Inkscape standard output: $it" )
        }
        inkscapeProcess.errorReader().readText().takeIf { it.isNotBlank() }.also {
            logger.debug("Inkscape error output: $it")
        }
    }

}