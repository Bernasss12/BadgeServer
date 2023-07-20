package dev.bernasss12.parsing

import dev.bernasss12.fetching.ModrinthDataFetcher
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object ModrinthDataParser : DataParser {

    val fetcher = ModrinthDataFetcher

    override suspend fun getModName(id: String): String {
        return fetcher.requestModInfo(id)["title"]?.jsonPrimitive?.content ?: "Error"
    }

    override suspend fun getSupportedGameVersions(id: String): List<String> {
        val versions = fetcher.requestModInfo(id)["game_versions"]?.jsonArray
        val versionList = versions?.map { it.jsonPrimitive.content }

        return versionList ?: listOf()
    }

    override suspend fun getDownloadCount(id: String): UInt? {
        return fetcher.requestModInfo(id)["downloads"]?.jsonPrimitive?.content?.toUInt()
    }

    override suspend fun getSupportedModLoaders(id: String): List<String> {
        return fetcher.requestModInfo(id)["loaders"]?.jsonArray?.map { it.jsonPrimitive.content } ?: listOf()
    }

    override suspend fun getLicence(id: String): String {
        return fetcher.requestModInfo(id)["license"]
            ?.jsonObject
            ?.get("name")
            ?.jsonPrimitive
            ?.content
            ?: "Error"
    }
}