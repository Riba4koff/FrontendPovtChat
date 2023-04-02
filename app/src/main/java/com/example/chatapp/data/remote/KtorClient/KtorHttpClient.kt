package com.example.chatapp.data.remote.KtorClient

import android.app.Application
import android.util.Log
import com.example.chatapp.data.preferencesDataStore.SessionManager
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KtorHttpClient() : KoinComponent {
    private val json = kotlinx.serialization.json.Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    val sessionManager by inject<SessionManager>()

    companion object {
        private const val CONNECT_TIME_OUT = 2500L
        private const val WEBSOCKET_TIME_OUT = 5_000L
        private const val TAG_KTOR_LOGGER = "KTOR_LOGGER: "
        private const val TAG_HTTP_STATUS_LOGGER = "HTTP_STATUS: "
    }

    val client: HttpClient get() = HttpClient(OkHttp) {
        expectSuccess = false

        engine {
            threadsCount = 8
            pipelining = true
        }

       
        install(Auth) {
            bearer {
                loadTokens { BearerTokens(token, "") }
            }
        }
        install(WebSockets)
        install(Logging) {
            level = LogLevel.NONE
            logger = object : Logger {
                override fun log(message: String) {
                    Log.i(TAG_KTOR_LOGGER, message)
                }
            }
        }
        install(ResponseObserver) {
            onResponse { response ->
                Log.i(TAG_HTTP_STATUS_LOGGER, "${response.status.value}")
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
        install(HttpTimeout) {
            connectTimeoutMillis = CONNECT_TIME_OUT
            socketTimeoutMillis = WEBSOCKET_TIME_OUT
        }
        defaultRequest {
            if (method != HttpMethod.Get) contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }
}
