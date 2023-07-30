package dev.bernasss12.parsing

import dev.bernasss12.fetching.CurseforgeDataFetcher
import io.ktor.util.logging.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object CurseforgeDataParser : DataParser {

    private val fetcher = CurseforgeDataFetcher
    private val logger = KtorSimpleLogger(javaClass.canonicalName)

    private suspend fun getDataObject(id: String): JsonObject {
        return fetcher.requestModInfo(id)["data"]?.jsonObject ?: error("missing \"data\" object")
    }

    override suspend fun getModName(id: String): String {
        return getDataObject(id)["name"]?.jsonPrimitive?.content ?: "Error".also {
            logger.warn("Error while trying to parse 'name' form object...")
        }
    }

    override suspend fun getSupportedGameVersions(id: String): List<String> {
        TODO("traverse latest files, grab versions and then sort them.")
    }

    override suspend fun getDownloadCount(id: String): UInt? {
        return getDataObject(id)["downloadCount"]?.jsonPrimitive?.content?.toUInt()
    }

    override suspend fun getSupportedModLoaders(id: String): List<String> {
        TODO("traverse latest files and grab modloader names from \"gameVersions\".")
    }

    override suspend fun getLicence(id: String): String {
        TODO("not availible on the given data, don't know how to go about it...")
    }

}
