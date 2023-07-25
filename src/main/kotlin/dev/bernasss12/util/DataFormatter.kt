package dev.bernasss12.util

import dev.bernasss12.templater.Template
import dev.bernasss12.templater.Templater
import io.ktor.util.logging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.imageio.ImageIO
import kotlin.io.path.exists

object DataFormatter {

    private val suffixes = listOf('k', 'M', 'G', 'T')
    private val logger = KtorSimpleLogger(javaClass.canonicalName)

    fun formatNumberWithSeparators(number: UInt, separator: String): String =
        number.toString().reversed().chunked(3).reversed().joinToString(separator)

    fun formatNumberShorten(number: UInt, decimal: Boolean): String =
        number.toString().reversed().chunked(3).reversed().let { splitNumber ->
            val result = StringBuilder(splitNumber.first().reversed())
            if (decimal && splitNumber.size >= 2) result.append(".${splitNumber[1].reversed()}")
            if (splitNumber.size >= 2) result.append(suffixes[splitNumber.size - 2])
            return result.toString()
        }

    suspend fun getComputedLength(model: Template, text: String): Int {
        val tempFilePng = Common.tempFile(model.name, text, "png")

        val textWidth = withContext(Dispatchers.IO) {
            if (!tempFilePng.exists()) {
                val fontResourceText = javaClass.getResourceAsStream(model.fontRes)?.use {
                    it.reader().readText()
                } ?: error("Could not open font resource ${model.fontRes}")
                val textSvg = Templater(fontResourceText).replaceText(text)
                Common.runInkscape(
                    logger = logger,
                    input = textSvg,
                    inkscapeOptions = "--export-overwrite --export-area-drawing --export-type=png --export-filename=$tempFilePng"
                )
            }
            if (tempFilePng.exists()) {
                ImageIO.read(tempFilePng.toFile())
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
