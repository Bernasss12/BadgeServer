package dev.bernasss12.fetching

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.plugins.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

object ModrinthDataFetcher : DataFetcher {

    private const val modrinthUrl = "https://api.modrinth.com/v2/project/"

    private suspend fun getInfo(id: String): JsonObject {
        HttpClient(Java).use { client ->
            val response = client.get("$modrinthUrl$id") {
                headers {
                    append(HttpHeaders.Accept, ContentType.Application.Json)
                }
            }
            if (response.status == HttpStatusCode.OK) {
                return Json.decodeFromString(response.body() as String)
            } else {
                throw NotFoundException("Resource not found. ID or SLUG might be wrong.")
            }
        }
    }

    override suspend fun getModName(id: String): String {
        return getInfo(id)["title"]?.jsonPrimitive?.content?: "Error"
    }

    override suspend fun getSupportedGameVersions(id: String): List<String> {
        TODO()
    }

    override suspend fun getDownloadCount(id: String): UInt {
        return getInfo(id)["downloads"]?.jsonPrimitive?.content?.toUInt()?: throw UnsupportedOperationException()
    }

    override suspend fun getSupportedModLoaders(id: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getLicence(id: String): String {
        TODO("Not yet implemented")
    }

}