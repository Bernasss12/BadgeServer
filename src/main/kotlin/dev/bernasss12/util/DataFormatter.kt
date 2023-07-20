package dev.bernasss12.util

import dev.bernasss12.templater.Template
import dev.bernasss12.templater.Templater
import io.ktor.util.logging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.imageio.ImageIO

object DataFormatter {

    private val suffixes = listOf('k', 'M', 'G', 'T')
    private val logger = KtorSimpleLogger("DataFormatter")

    fun formatNumberWithSeparators(number: UInt, separator: String): String =
        number.toString().reversed().chunked(3).reversed().joinToString(separator)

    fun formatNumberShorten(number: UInt, decimal: Boolean): String =
        number.toString().reversed().chunked(3).reversed().let { splitNumber ->
            val result = StringBuilder(splitNumber.first())
            if (decimal && splitNumber.size >= 2) result.append(".${splitNumber[1]}")
            if (splitNumber.size >= 2) result.append(suffixes[splitNumber.size - 2])
            return result.toString()
        }

    suspend fun getComputedLength(model: Template, text: String): Int {
        val tempFolder = File("temp")

        if (!tempFolder.exists()) {
            tempFolder.mkdir()
        }

        val identifier = text.replace("[^a-zA-Z0-9\\-]".toRegex(), "_").replace("_+".toRegex(), "_")
        val tempFileName = "${tempFolder.name}/temp-${model.name}-${identifier}"
        val tempFilePng = File("$tempFileName.png")
        val tempFileSvg = File("$tempFileName.svg")

        val textWidth = withContext(Dispatchers.IO) {
            if (!tempFilePng.exists()) {
                val fontResourceText = javaClass.getResourceAsStream(model.fontRes)?.use {
                    it.reader().readText()
                } ?: error("Could not open font resource ${model.fontRes}")
                val textSvg = Templater(fontResourceText).replaceText(text)
                tempFileSvg.writeText(textSvg)
                val inkscapeProcess = Runtime.getRuntime().exec("inkscape --export-area-drawing --export-type=png ${tempFileSvg.path}")
                inkscapeProcess.waitFor()
                logger.debug("Inkscape standard output: " + inkscapeProcess.inputReader().readText())
                logger.debug("Inkscape error output: " + inkscapeProcess.errorReader().readText())
//                if (inkscapeProcess.exitValue() != 0) println("Problem converting image.")
            }
            if (tempFilePng.exists()) {
                ImageIO.read(tempFilePng)
            } else {
                throw Error("Inkscape didn't create the png.")
            }
        }.width

        return textWidth
    }

    fun formatVersions(gameVersions: List<String>, maxVersions: Int): String {
        return gameVersions.takeLast(maxVersions).reversed().joinToString(" | ") // TODO sort chronologically if not originally sorted.
    }

    fun formatLoaders(supportedModLoaders: List<String>): String {
        return supportedModLoaders.joinToString(" | ")
    }

}
