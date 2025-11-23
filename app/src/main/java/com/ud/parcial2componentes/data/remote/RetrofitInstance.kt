package com.ud.parcial2componentes.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que provee la instancia de Retrofit configurada para la API de ahorro.
 *
 * Características:
 * - Base URL: [BASE_URL]
 * - Logging de solicitudes y respuestas HTTP para depuración.
 * - Cliente OkHttp con interceptor de logging.
 *
 * Uso:
 * ```
 * val api = RetrofitInstance.api
 * val plans = api.getPlans()
 * ```
 */
object RetrofitInstance {

    /** URL base del backend. */
    private const val BASE_URL = "http://10.0.2.2:3000/"
    // Nota: 10.0.2.2 se usa en emuladores Android.
    // Para un dispositivo físico, usar la IP de la computadora (ej: http://192.168.1.10:3000/)

    /** Interceptor para mostrar logs de requests y responses HTTP. */
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /** Cliente HTTP configurado con interceptor de logging. */
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    /** Instancia perezosa de Retrofit que expone la API. */
    val api: SavingsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SavingsApi::class.java)
    }
}

