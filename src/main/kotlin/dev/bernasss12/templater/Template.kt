package dev.bernasss12.templater

import dev.bernasss12.parsing.CurseforgeDataParser
import dev.bernasss12.parsing.DataParser
import dev.bernasss12.parsing.ModrinthDataParser
import java.io.File

data class Template(
    val name: String,
    val file: File,
    val font: File,
    val dataParser: DataParser,
    val minWidth: Float,
    val height: Float,
    val svgHeight: Float = height,
    val textHeightOffset: Float,
    val pad: Float,
) {

    val padding: Float
        get() = pad * 2

    companion object {


        private val MODRINTH_TEXT_FILE = File("src/main/resources/templates/modrinth/text.svg")
        val MODRINTH_ICON_TEXT = Template(
            name = "modrinth-icon-text",
            file = File("src/main/resources/templates/modrinth/icon_text.svg"),
            font = MODRINTH_TEXT_FILE,
            dataParser = ModrinthDataParser,
            minWidth = 35f,
            textHeightOffset = 18.5f,
            height = 30f,
            svgHeight = 35f,
            pad = 12f,
        )

        private val CURSEFORGE_TEXT_FILE = File("src/main/resources/templates/curseforge/text.svg")
        val CURSEFORGE_ICON_TEXT = Template(
            name = "curseforge-icon-text",
            file = File("src/main/resources/templates/curseforge/icon_text.svg"),
            font = CURSEFORGE_TEXT_FILE,
            dataParser = CurseforgeDataParser,
            minWidth = 35f,
            textHeightOffset = 16f,
            height = 30f,
            pad = 7f,
        )

    }
}