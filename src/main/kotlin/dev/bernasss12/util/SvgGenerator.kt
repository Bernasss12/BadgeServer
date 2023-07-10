package dev.bernasss12.util

import dev.bernasss12.fetching.DataFetcher
import dev.bernasss12.templater.Template
import dev.bernasss12.templater.Templater

object SvgGenerator {

    public enum class DataTypes {
        DOWNLOADS, // Shows mod downloads
        NAME, // Shows mod name
        VERSIONS, // Shows game versions
        LOADER, // Shows mod loaders
        LICENSE; // Shows mod licence
    }

    suspend fun generate(template: Template, ident: String, dataFetcher: DataFetcher, dataTypeString: String): String {
        val templater = Templater(template.file)

        val text = when (DataTypes.valueOf(dataTypeString)) {
            DataTypes.DOWNLOADS -> DataFormatter.formatNumberShorten(dataFetcher.getDownloadCount(ident), false)
            DataTypes.NAME -> dataFetcher.getModName(ident)
            DataTypes.VERSIONS -> DataFormatter.formatVersions(dataFetcher.getSupportedGameVersions(ident), 3)
            DataTypes.LOADER -> DataFormatter.formatLoaders(dataFetcher.getSupportedModLoaders(ident))
            DataTypes.LICENSE -> dataFetcher.getLicence(ident)
        }

        val width = DataFormatter.getComputedLength(template, text)

        return templater.replaceWithSingleText(text, width, template)
    }
}