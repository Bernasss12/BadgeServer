package dev.bernasss12.plugins

import dev.bernasss12.fetching.ModrinthDataFetcher
import dev.bernasss12.templater.Templater
import dev.bernasss12.util.DataFormatter
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

        }
    }
}

private suspend fun curseforge(name: String): String {

    val model = "curseforge"

    val textWidth = DataFormatter.getComputedLength(model, name)

    val pad = 7
    val padding = pad * 2

    val xml = Templater(File("src/main/resources/templates/curseforge/icon_text.svg"))
        .replacePlaceholders(
            mapOf(
                "text" to name,
                "text-y" to 16,
                "text-x" to (textWidth + padding) / 2.0 + 35,
                "text-rect-width" to textWidth + padding,
                "svg-width" to textWidth + padding + 35,
                "svg-height" to 30,
            )
        )
    return xml
}

private suspend fun modrinth(name: String): String {
    val model = "modrinth"

    val textWidth = DataFormatter.getComputedLength(model, name)

    val pad = 12
    val padding = pad * 2

    return Templater(File("src/main/resources/templates/modrinth/icon_text.svg"))
        .replacePlaceholders(
            mapOf<String, Any>(
                "text" to name,
                "text-x" to (textWidth + padding) / 2.0 + 35,
                "text-y" to 18.5,
                "text-rect-width" to textWidth + padding + 36,
                "text-rect-height" to 30,
                "svg-width" to textWidth + padding + 35,
                "svg-height" to 35,
            )
        )
}
