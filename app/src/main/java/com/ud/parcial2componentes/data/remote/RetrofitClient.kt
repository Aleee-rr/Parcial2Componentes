package com.ud.parcial2componentes.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton que provee la instancia de Retrofit configurada para la API.
 *
 * Características:
 * - Base URL: [BASE_URL]
 * - Logging de requests y responses para debug.
 * - Timeouts configurados en 30 segundos para conexión, lectura y escritura.
 *
 * Uso:
 * ```
 * val api = RetrofitClient.apiService
 * val plans = api.getPlans()
 * ```
 */
object RetrofitClient {

    /** Base URL del backend. */
    private const val BASE_URL = "http://10.0.2.2:3000/api/"
    // Nota: 10.0.2.2 es para el emulador de Android.
    // Para dispositivo físico, usa la IP de la computadora (ej: http://192.168.1.10:3000/api/)

    /** Interceptor para logging de solicitudes/respuestas HTTP. */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /** Cliente HTTP con interceptors y timeouts configurados. */
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /** Instancia de Retrofit configurada con Gson y el cliente HTTP. */
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /** Servicio API generado por Retrofit. */
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
