package dev.bernasss12.util

import dev.bernasss12.parsing.DataParser
import dev.bernasss12.templater.Template
import dev.bernasss12.templater.Templater
import io.ktor.util.logging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.path.exists
import kotlin.io.path.notExists
import kotlin.io.path.readText

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
        val templateResourceText = javaClass.getResourceAsStream(template.res)?.use {
            it.reader().readText()
        } ?: error("Could not open resource ${template.res}")
        val templater = Templater(templateResourceText)

        val text = when (DataTypes.valueOf(dataTypeString)) {
            DataTypes.DOWNLOADS -> {
                val downloadCount: UInt = dataFetcher.getDownloadCount(ident) ?: 0u.also {
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

        val exportPath = Common.tempFile("render-${template.name}", text, "svg")
        return withContext(Dispatchers.IO) {
            if (exportPath.exists()) {
                return@withContext exportPath.readText()
            }
            Common.runInkscape(
                logger = logger,
                input = templater.replaceWithSingleText(text, width, template),
                inkscapeOptions = "--export-overwrite --export-text-to-path --export-type=svg --export-plain-svg --export-filename=$exportPath"
            )
            if (exportPath.notExists()) {
                error("Inkscape did not render the file $exportPath!")
            }
            return@withContext exportPath.readText()
        }
    }
}
