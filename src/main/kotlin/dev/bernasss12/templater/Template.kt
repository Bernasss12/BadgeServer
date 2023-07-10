package dev.bernasss12.templater

import dev.bernasss12.fetching.CurseforgeDataFetcher
import dev.bernasss12.fetching.DataFetcher
import dev.bernasss12.fetching.ModrinthDataFetcher
import java.io.File

data class Template(
    val name: String,
    val file: File,
    val font: File,
    val dataFetcher: DataFetcher,
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
        public val MODRINTH_ICON_TEXT = Template(
            name = "modrinth-icon-text",
            file = File("src/main/resources/templates/modrinth/icon_text.svg"),
            font = MODRINTH_TEXT_FILE,
            dataFetcher = ModrinthDataFetcher,
            minWidth = 35f,
            textHeightOffset = 18.5f,
            height = 30f,
            svgHeight = 35f,
            pad = 12f,
        )

        private val CURSEFORGE_TEXT_FILE = File("src/main/resources/templates/curseforge/text.svg")
        public val CURSEFORGE_ICON_TEXT = Template(
            name = "curseforge-icon-text",
            file = File("src/main/resources/templates/curseforge/icon_text.svg"),
            font = CURSEFORGE_TEXT_FILE,
            dataFetcher = CurseforgeDataFetcher,
            minWidth = 35f,
            textHeightOffset = 16f,
            height = 30f,
            pad = 7f,
        )

    }
}