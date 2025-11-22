package com.ud.parcial2componentes.data.remote

import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.model.Payment
import com.ud.parcial2componentes.data.model.Member
import retrofit2.Response
import retrofit2.http.*

interface SavingsApi {
    @GET("api/plans")
    suspend fun getPlans(): Response<List<Plan>>

    @GET("api/plans/{id}")
    suspend fun getPlan(@Path("id") id: String): Response<Plan>

    @GET("api/payments/plan/{planId}")
    suspend fun getPaymentsByPlan(@Path("planId") planId: String): Response<List<Payment>>

    @POST("api/payments")
    suspend fun createPayment(@Body payment: Payment): Response<Payment>

    @GET("api/members/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): Response<List<Member>>

    @POST("api/members")
    suspend fun createMember(@Body member: Member): Response<Member>
}
