package dev.bernasss12.util

import org.slf4j.Logger
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.readText

object Common {
    private val unsafeRegex = "[^a-zA-Z0-9\\-]".toRegex()
    private val multiUnderscoresRegex = "_+".toRegex()

    /**
     * Tries to get the user agent from the badge-server-ua file.
     */
    fun getUserAgent(logger: Logger): String {
        return getPrivateString("badge-server-ua", logger)
            ?: "Bernasss12/BadgeServer"
    }

    /**
     * Gets the curseforge api token from curseforge-api-token.
     */
    fun getCurseforgeSecret(logger: Logger): String? {
        return getPrivateString("curseforge-api-token", logger).also {
            println(it)
            println(it?.chars())
        }
    }

    /**
     * Tries to get the user-agent to be sent in the GET requests from the following places, in order:
     * ./                       Current directory
     * ~/.config                User config directory (linux only)
     * $BADGE_SERVER_HOME/      User defined environment variable
     */
    private fun getPrivateString(filename: String, logger: Logger): String? {
        // Try current directory
        Paths.get("", filename).toAbsolutePath().let { currentPath ->
            if (currentPath.exists()) {
                return currentPath.readText().trim()
            }
        }

        // Try config folder
        val homePath = System.getProperty("user.home")
        Paths.get(homePath, ".config", filename).toAbsolutePath().let { configPath ->
            if (configPath.exists()) {
                return configPath.readText().trim()
            }
        }

        // Try environment variable folder
        val envPath = System.getenv("BADGE_SERVER_HOME") ?: ""
        if (envPath.isNotBlank()) {
            Paths.get(envPath, filename).let { environmentPath ->
                if (environmentPath.exists()) {
                    return environmentPath.readText().trim()
                }
            }
        }

        logger.warn("Did not find secret anywhere! $filename")
        return null
    }

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
            logger.debug("Inkscape standard output: $it")
        }
        inkscapeProcess.errorReader().readText().takeIf { it.isNotBlank() }.also {
            logger.debug("Inkscape error output: $it")
        }
    }

}