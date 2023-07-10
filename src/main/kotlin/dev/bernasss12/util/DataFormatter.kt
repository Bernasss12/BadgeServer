package dev.bernasss12.util

import dev.bernasss12.templater.Template
import dev.bernasss12.templater.Templater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.imageio.ImageIO

object DataFormatter {

    private val suffixes = listOf('k', 'M', 'G', 'T')

    public fun formatNumberWithSeparators(number: UInt, separator: String): String =
        number.toString().reversed().chunked(3).reversed().joinToString(separator)

    public fun formatNumberShorten(number: UInt, decimal: Boolean): String =
        number.toString().reversed().chunked(3).reversed().let { splitNumber ->
            val result = StringBuilder(splitNumber.first())
            if (decimal && splitNumber.size >= 2) result.append(".${splitNumber[1]}")
            if (splitNumber.size >= 2) result.append(suffixes[splitNumber.size - 2])
            return result.toString()
        }

    public suspend fun getComputedLength(model: Template, text: String): Int {
        val tempFolder = File("temp")

        if (!tempFolder.exists()) {
            tempFolder.mkdir()
        }

        val tempFileName = "${tempFolder.name}/temp-${model.name}-${text.replace(" ", "_")}"
        val tempFilePng = File("$tempFileName.png")
        val tempFileSvg = File("$tempFileName.svg")

        val textWidth = withContext(Dispatchers.IO) {
            if (!tempFilePng.exists()) {
                val textSvg = Templater(model.font).replacePlaceholders("text" to text)
                tempFileSvg.writeText(textSvg)
                Runtime.getRuntime().exec("inkscape --export-area-drawing --export-type=png ${tempFileSvg.name}").waitFor()
            }
            ImageIO.read(tempFilePng)
        }.width

        return textWidth
    }

    fun formatVersions(gameVersions: List<String>, maxVersions: Int): String {
        return gameVersions.take(maxVersions).joinToString(" | ") // TODO sort chronologically if not originally sorted.
    }

    fun formatLoaders(supportedModLoaders: List<String>): String {
        return supportedModLoaders.joinToString(" | ")
    }

}