package dev.bernasss12.templater

import dev.bernasss12.parsing.CurseforgeDataParser
import dev.bernasss12.parsing.DataParser
import dev.bernasss12.parsing.ModrinthDataParser

data class Template(
    val name: String,
    val res: String,
    val fontRes: String,
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


        val MODRINTH_ICON_TEXT = Template(
            name = "modrinth-icon-text",
            res = "/templates/modrinth/icon_text.svg",
            fontRes = "/templates/modrinth/text.svg",
            dataParser = ModrinthDataParser,
            minWidth = 35f,
            textHeightOffset = 18.5f,
            height = 30f,
            svgHeight = 35f,
            pad = 12f,
        )

        val CURSEFORGE_ICON_TEXT = Template(
            name = "curseforge-icon-text",
            res = "/templates/curseforge/icon_text.svg",
            fontRes = "/templates/curseforge/text.svg",
            dataParser = CurseforgeDataParser,
            minWidth = 35f,
            textHeightOffset = 16f,
            height = 30f,
            pad = 7f,
        )

    }
}
