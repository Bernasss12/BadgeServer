package dev.bernasss12.fetching

import dev.bernasss12.util.Common
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.util.logging.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.time.Duration.Companion.minutes

object CurseforgeDataFetcher : DataFetcher {

    private const val CURSEFORGE_URL = "https://api.curseforge.com/v1/mods/"
    private val logger = KtorSimpleLogger(javaClass.canonicalName)
    private val mutex = Mutex()
    private var _key = Common.getCurseforgeSecret(logger)
    private val apiKey: String?
        get() {
            if (_key == null) {
                // Tries to get the token everytime if it was not previously gotten.
                _key = Common.getCurseforgeSecret(logger)
            }
            return _key
        }
    private val httpClient = HttpClient(Java) {
        install(UserAgent) {
            agent = Common.getUserAgent(logger)
        }
    }

    private val cache: MutableMap<UInt, Pair<Long, JsonObject>> = hashMapOf()
    private val cacheTimeout = 5.minutes

    override suspend fun requestModInfo(modid: String): JsonObject {
        val key = apiKey
        if (key == null) {
            logger.error("No curseforge api key found, this won't do.")
            error("No curseforge api key found, this won't do.")
        } else {
            mutex.withLock {
                val id: UInt = modid.toUIntOrNull() ?: throw BadRequestException("modid parameter does not match regular expression for mod ids")

                val cacheEntry = cache[id]
                if (cacheEntry != null && System.currentTimeMillis() <= cacheEntry.first + cacheTimeout.inWholeMilliseconds) {
                    return cacheEntry.second
                }

                val requestUrl = "$CURSEFORGE_URL$id"
                val response = httpClient.get(requestUrl) {
                    headers {
                        append(HttpHeaders.Accept, ContentType.Application.Json)
                        append("x-api-key", key)
                    }
                }
                if (response.status == HttpStatusCode.OK) {
                    val currentTime = System.currentTimeMillis()
                    return Json.decodeFromString<JsonObject>(response.body() as String).also { jsonObject ->
                        cache[id] = currentTime to jsonObject
                    }
                } else {
                    val body: String = try {
                        response.body()
                    } catch (err: RuntimeException) {
                        "unable to get response body: ${err.javaClass.name}"
                    }
                    logger.error("Requesting $requestUrl returned bad status ${response.status}:\n$body")
                    throw RuntimeException("Curseforge API request did not succeed")
                }
            }
        }
    }
}