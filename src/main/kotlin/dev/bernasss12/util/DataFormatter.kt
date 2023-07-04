package dev.bernasss12.util

import dev.bernasss12.templater.Templater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import javax.imageio.ImageIO

object DataFormatter {

    val suffixes = listOf('k', 'M', 'G', 'T')

    public fun formatNumberWithSeparators(number: UInt, separator: String) : String =
        number.toString().reversed().chunked(3).reversed().joinToString(separator)

    public fun formatNumberShorten(number: UInt, decimal: Boolean) : String =
        number.toString().reversed().chunked(3).reversed().let { splitNumber ->
            val result = StringBuilder(splitNumber.first())
            if(decimal && splitNumber.size >= 2) result.append(".${splitNumber[1]}")
            if(splitNumber.size >= 2) result.append(suffixes[splitNumber.size - 2])
            return result.toString()
        }

    public suspend fun getComputedLength(model: String, name: String): Int {
        val textWidth = withContext(Dispatchers.IO) {
            val textSvg = Templater(File("src/main/resources/templates/$model/text.svg")).replacePlaceholders("text" to name)
            val outputSvg = File("temp-text-curse.svg")
            outputSvg.writeText(textSvg)
            Runtime.getRuntime().exec("inkscape --export-area-drawing --export-type=png temp-text-curse.svg").waitFor()
            val inputPng = File("temp-text-curse.png")
            ImageIO.read(inputPng).width
        }
        return textWidth
    }

}