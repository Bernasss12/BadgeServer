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
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object ModrinthDataFetcher : DataFetcher {

    const val MOD_ID_REGEX = "[\\w!@\$()`.+,\"\\-']{3,64}"

    private const val modrinthUrl = "https://api.modrinth.com/v2/project/"
    private val modIdRegex = MOD_ID_REGEX.toRegex()
    private val logger = KtorSimpleLogger(javaClass.canonicalName)
    private val mutex = Mutex()
    private val httpClient = HttpClient(Java) {
        install(UserAgent) {
            agent = Common.getUserAgent(logger)
        }
    }

    private val cache: MutableMap<String, Pair<Long, JsonObject>> = hashMapOf()
    private val cacheTimeout = 5.minutes

    private var remainingRequests: Int = 0
    private var resetAt: Long = 0

    override suspend fun requestModInfo(modid: String): JsonObject {
        mutex.withLock {
            val cacheEntry = cache[modid]
            if (cacheEntry != null && System.currentTimeMillis() <= cacheEntry.first + cacheTimeout.inWholeMilliseconds) {
                return cacheEntry.second
            }
            if (!modid.matches(modIdRegex)) {
                throw BadRequestException("modid parameter does not match regular expression for mod ids")
            }
            if (remainingRequests == 0) {
                delay((resetAt - System.currentTimeMillis()).milliseconds)
            }
            val requestUrl = "$modrinthUrl$modid"
            val response = httpClient.get(requestUrl) {
                headers {
                    append(HttpHeaders.Accept, ContentType.Application.Json)
                }
            }
            if (response.status == HttpStatusCode.OK) {
                remainingRequests = response.headers["X-RateLimit-Remaining"]?.toInt() ?: 100
                val currentTime = System.currentTimeMillis()
                resetAt = (response.headers["X-RateLimit-Reset"]?.toInt()?.plus(1) ?: 60).seconds.inWholeMilliseconds + currentTime
                return Json.decodeFromString<JsonObject>(response.body() as String).also { jsonObject ->
                    cache[modid] = currentTime to jsonObject
                }
            } else {
                val body: String = try {
                    response.body()
                } catch (err: RuntimeException) {
                    "unable to get response body: ${err.javaClass.name}"
                }
                logger.error("Requesting $requestUrl returned bad status ${response.status}:\n$body")
                throw RuntimeException("modrinth API request did not succeed")
            }
        }
    }
}