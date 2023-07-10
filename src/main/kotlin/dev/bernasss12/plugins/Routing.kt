package dev.bernasss12.plugins

import dev.bernasss12.fetching.ModrinthDataFetcher
import dev.bernasss12.templater.Template
import dev.bernasss12.templater.Templater
import dev.bernasss12.util.DataFormatter
import dev.bernasss12.util.SvgGenerator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {

    routing {
        get("/badges/{badge-source}/{mod-id-slug}/{info-type}") {
            val text: String = when (call.parameters["info-type"]) {
                "downloads" -> DataFormatter.formatNumberWithSeparators(ModrinthDataFetcher.getDownloadCount(call.parameters["mod-id-slug"]!!), ",")
                "down" -> DataFormatter.formatNumberShorten(ModrinthDataFetcher.getDownloadCount(call.parameters["mod-id-slug"]!!), false)
                "name" -> ModrinthDataFetcher.getModName(call.parameters["mod-id-slug"]!!)
                else -> throw Error("Not valid type.")
            }

            val xml = when (call.parameters["badge-source"]) {
                "curseforge" -> curseforge(text)
                "modrinth" -> modrinth(text)
                else -> throw Error("Not valid source")
            }

            call.respondText(xml, ContentType.Image.SVG, HttpStatusCode.OK)
        }
        get(Regex("/modrinth/(?<modid>[\\w!@\$()`.+,\"\\-']{3,64})(/downloads(/(?<lang>[a-z]{2}))?)?")) {
            val text = DataFormatter.formatNumberShorten(ModrinthDataFetcher.getDownloadCount(call.parameters["modid"]!!), false)
            val xml = modrinth(text)
            call.respondText(xml, ContentType.Image.SVG, HttpStatusCode.OK)
        }
        get(Regex("/curseforge/(?<modid>[\\w!@\$()`.+,\"\\-']{3,64})(/downloads(/(?<lang>[a-z]{2}))?)?")) {
            val text = DataFormatter.formatNumberShorten(ModrinthDataFetcher.getDownloadCount(call.parameters["modid"]!!), false)
            val xml = curseforge(text)
            call.respondText(xml, ContentType.Image.SVG, HttpStatusCode.OK)
        }
        get("/modrinth/{mod-id-slug}/name") {
            call.respondText(
                text = SvgGenerator.generate(
                    Template.MODRINTH_ICON_TEXT,
                    call.parameters["modid"]!!,
                    ModrinthDataFetcher,
                    SvgGenerator.DataTypes.NAME.toString()
                ),
                contentType = ContentType.Image.SVG,
                status = HttpStatusCode.OK
            )
        }
    }
}
