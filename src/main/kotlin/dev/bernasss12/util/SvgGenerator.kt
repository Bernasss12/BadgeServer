package dev.bernasss12.util

import dev.bernasss12.parsing.DataParser
import dev.bernasss12.templater.Template
import dev.bernasss12.templater.Templater
import io.ktor.util.logging.*

object SvgGenerator {

    private val logger = KtorSimpleLogger(javaClass.canonicalName)

    enum class DataTypes {
        DOWNLOADS, // Shows mod downloads
        NAME, // Shows mod name
        VERSIONS, // Shows game versions
        LOADER, // Shows mod loaders
        LICENSE; // Shows mod licence
    }

    suspend fun generate(template: Template, ident: String, dataFetcher: DataParser, dataTypeString: String): String {
        val templater = Templater(template.file)

        val text = when (DataTypes.valueOf(dataTypeString)) {
            DataTypes.DOWNLOADS -> {
                val downloadCount: UInt = dataFetcher.getDownloadCount(ident) ?: 0u.also{
                    logger.error("")
                    throw RuntimeException("could not fetch download count")
                }
                DataFormatter.formatNumberShorten(downloadCount, false)
            }
            DataTypes.NAME -> dataFetcher.getModName(ident)
            DataTypes.VERSIONS -> DataFormatter.formatVersions(dataFetcher.getSupportedGameVersions(ident), 3)
            DataTypes.LOADER -> DataFormatter.formatLoaders(dataFetcher.getSupportedModLoaders(ident))
            DataTypes.LICENSE -> dataFetcher.getLicence(ident)
        }

        val width = DataFormatter.getComputedLength(template, text)

        return templater.replaceWithSingleText(text, width, template)
    }
}