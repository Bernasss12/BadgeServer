package dev.bernasss12.templater

import java.io.File

data class Template(val file: File, val text: File) {
    companion object Templates {


        private val MODRINTH_TEXT = File("src/main/resources/templates/modrinth/text.svg")
        private val MODRINTH_ICON_TEXT = Template(File("src/main/resources/templates/modrinth/icon_text.svg"), MODRINTH_TEXT)

        private val CURSEFORGE_TEXT = File("src/main/resources/templates/curseforge/text.svg")
        private val CURSEFORGE_ICON_TEXT = Template(File("src/main/resources/templates/curseforge/icon_text.svg"), CURSEFORGE_TEXT)

    }
}