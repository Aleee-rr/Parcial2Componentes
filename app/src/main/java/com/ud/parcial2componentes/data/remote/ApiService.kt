package com.ud.parcial2componentes.data.remote

import com.ud.parcial2componentes.data.model.CreateMemberRequest
import com.ud.parcial2componentes.data.model.CreatePlanRequest
import com.ud.parcial2componentes.data.model.Member
import com.ud.parcial2componentes.data.model.Payment
import com.ud.parcial2componentes.data.model.PaymentRequest
import com.ud.parcial2componentes.data.model.Plan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz que define todos los endpoints REST del backend.
 * Retrofit genera automáticamente la implementación.
 */
interface ApiService {

    // ========== PLANES ==========

    @GET("plans")
    suspend fun getPlans(): Response<List<Plan>>

    @GET("plans/{id}")
    suspend fun getPlanById(@Path("id") planId: String): Response<Plan>

    @POST("plans")
    suspend fun createPlan(@Body plan: CreatePlanRequest): Response<Plan>  // ← Usa el DTO

    // ========== MIEMBROS ==========

    @GET("members/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): Response<List<Member>>

    @POST("members")
    suspend fun createMember(@Body member: CreateMemberRequest): Response<Member>  // ← Usa el DTO

    // ========== PAGOS ==========

    @POST("payments")
    suspend fun registerPayment(@Body payment: PaymentRequest): Response<Payment>

    @GET("payments/plan/{planId}")
    suspend fun getPaymentsByPlan(@Path("planId") planId: String): Response<List<Payment>>
}