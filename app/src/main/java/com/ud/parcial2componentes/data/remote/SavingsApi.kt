package com.ud.parcial2componentes.data.remote

import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.model.Payment
import com.ud.parcial2componentes.data.model.Member
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz que define los endpoints REST para el sistema de ahorro familiar.
 *
 * Retrofit generará automáticamente la implementación.
 */
interface SavingsApi {

    // ==================== PLANES ====================

    /**
     * Obtiene la lista de todos los planes de ahorro.
     * @return Response con la lista de [Plan].
     */
    @GET("api/plans")
    suspend fun getPlans(): Response<List<Plan>>

    /**
     * Obtiene un plan por su ID.
     * @param id ID del plan.
     * @return Response con el [Plan] correspondiente.
     */
    @GET("api/plans/{id}")
    suspend fun getPlan(@Path("id") id: String): Response<Plan>

    // ==================== PAGOS ====================

    /**
     * Obtiene los pagos asociados a un plan.
     * @param planId ID del plan.
     * @return Response con la lista de [Payment] correspondientes.
     */
    @GET("api/payments/plan/{planId}")
    suspend fun getPaymentsByPlan(@Path("planId") planId: String): Response<List<Payment>>

    /**
     * Crea un nuevo pago en el sistema.
     * @param payment Objeto [Payment] con la información del pago.
     * @return Response con el [Payment] creado.
     */
    @POST("api/payments")
    suspend fun createPayment(@Body payment: Payment): Response<Payment>

    // ==================== MIEMBROS ====================

    /**
     * Obtiene los miembros asociados a un plan específico.
     * @param planId ID del plan.
     * @return Response con la lista de [Member].
     */
    @GET("api/members/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): Response<List<Member>>

    /**
     * Crea un nuevo miembro en un plan.
     * @param member Objeto [Member] con la información del miembro.
     * @return Response con el [Member] creado.
     */
    @POST("api/members")
    suspend fun createMember(@Body member: Member): Response<Member>
}
