package dev.bernasss12.fetching

import kotlinx.serialization.json.JsonObject

interface DataFetcher {
    suspend fun requestModInfo(modid: String): JsonObject
}