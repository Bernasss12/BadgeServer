package dev.bernasss12.plugins

import dev.bernasss12.fetching.CurseforgeDataFetcher
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
        get(Regex("/modrinth/(?<modid>[\\w!@\$()`.+,\"\\-']{3,64})(/downloads(/(?<lang>[a-z]{2}))?)?")) {
            call.respondText(
                text = SvgGenerator.generate(
                    Template.MODRINTH_ICON_TEXT,
                    call.parameters["modid"]!!,
                    ModrinthDataFetcher,
                    SvgGenerator.DataTypes.DOWNLOADS.toString()
                ),
                contentType = ContentType.Image.SVG,
                status = HttpStatusCode.OK
            )
        }
        get(Regex("/curseforge/(?<modid>[\\w!@\$()`.+,\"\\-']{3,64})(/downloads(/(?<lang>[a-z]{2}))?)?")) {
            call.respondText(
                text = SvgGenerator.generate(
                    Template.CURSEFORGE_ICON_TEXT,
                    call.parameters["modid"]!!,
                    CurseforgeDataFetcher,
                    SvgGenerator.DataTypes.DOWNLOADS.toString()
                ),
                contentType = ContentType.Image.SVG,
                status = HttpStatusCode.OK
            )
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
