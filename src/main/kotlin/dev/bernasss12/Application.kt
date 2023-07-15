package dev.bernasss12

import dev.bernasss12.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.ratelimit.*
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(RateLimit) {
        global {
            rateLimiter(limit = 20, refillPeriod = 60.seconds)
        }
    }
    configureRouting()
}
