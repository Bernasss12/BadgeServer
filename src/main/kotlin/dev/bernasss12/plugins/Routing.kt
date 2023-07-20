package dev.bernasss12.plugins

import dev.bernasss12.fetching.ModrinthDataFetcher
import dev.bernasss12.parsing.CurseforgeDataParser
import dev.bernasss12.parsing.ModrinthDataParser
import dev.bernasss12.templater.Template
import dev.bernasss12.util.SvgGenerator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    suspend fun getModID(call: ApplicationCall): String? {
        return call.parameters["modid"].also {
            if (it == null) {
                call.respond(HttpStatusCode.BadRequest, "modid not passed")
            }
        }
    }

    routing {
        get(Regex("/modrinth/(?<modid>${ModrinthDataFetcher.MOD_ID_REGEX})(/downloads(/(?<lang>[a-z]{2}))?)?")) {
            val modId = getModID(call) ?: return@get
            call.respondText(
                text = SvgGenerator.generate(
                    Template.MODRINTH_ICON_TEXT,
                    modId,
                    ModrinthDataParser,
                    SvgGenerator.DataTypes.DOWNLOADS.toString()
                ),
                contentType = ContentType.Image.SVG,
                status = HttpStatusCode.OK
            )
        }
        get(Regex("/curseforge/(?<modid>[0-9]+)(/downloads(/(?<lang>[a-z]{2}))?)?")) {
            val modId = getModID(call) ?: return@get
            call.respondText(
                text = SvgGenerator.generate(
                    Template.CURSEFORGE_ICON_TEXT,
                    modId,
                    CurseforgeDataParser,
                    SvgGenerator.DataTypes.DOWNLOADS.toString()
                ),
                contentType = ContentType.Image.SVG,
                status = HttpStatusCode.OK
            )
        }
        get("/modrinth/{modid}/name") {
            val modId = getModID(call) ?: return@get
            call.respondText(
                text = SvgGenerator.generate(
                    Template.MODRINTH_ICON_TEXT,
                    modId,
                    ModrinthDataParser,
                    SvgGenerator.DataTypes.NAME.toString()
                ),
                contentType = ContentType.Image.SVG,
                status = HttpStatusCode.OK
            )
        }
        get("/modrinth/{modid}/versions") {
            val modId = getModID(call) ?: return@get
            call.respondText(
                text = SvgGenerator.generate(
                    Template.MODRINTH_ICON_TEXT,
                    modId,
                    ModrinthDataParser,
                    SvgGenerator.DataTypes.VERSIONS.toString()
                ),
                contentType = ContentType.Image.SVG,
                status = HttpStatusCode.OK
            )
        }
        get("/modrinth/{modid}/loaders") {
            val modId = getModID(call) ?: return@get
            call.respondText(
                text = SvgGenerator.generate(
                    Template.MODRINTH_ICON_TEXT,
                    modId,
                    ModrinthDataParser,
                    SvgGenerator.DataTypes.LOADER.toString()
                ),
                contentType = ContentType.Image.SVG,
                status = HttpStatusCode.OK
            )
        }
        get("/modrinth/{modid}/licence") {
            val modId = getModID(call) ?: return@get
            call.respondText(
                text = SvgGenerator.generate(
                    Template.MODRINTH_ICON_TEXT,
                    modId,
                    ModrinthDataParser,
                    SvgGenerator.DataTypes.LICENSE.toString()
                ),
                contentType = ContentType.Image.SVG,
                status = HttpStatusCode.OK
            )
        }
    }
}
