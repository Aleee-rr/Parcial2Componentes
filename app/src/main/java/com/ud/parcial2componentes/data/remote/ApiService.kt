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
 * Interfaz que define los endpoints REST del backend.
 *
 * Retrofit genera automáticamente la implementación de esta interfaz.
 * Contiene operaciones para gestionar planes, miembros y pagos de ahorro.
 */
interface ApiService {

    // ================= PLANES =================

    /**
     * Obtiene todos los planes de ahorro.
     *
     * @return [Response] que contiene la lista de [Plan].
     */
    @GET("plans")
    suspend fun getPlans(): Response<List<Plan>>

    /**
     * Obtiene un plan por su identificador.
     *
     * @param planId ID del plan a buscar.
     * @return [Response] que contiene el [Plan] solicitado.
     */
    @GET("plans/{id}")
    suspend fun getPlanById(@Path("id") planId: String): Response<Plan>

    /**
     * Crea un nuevo plan de ahorro.
     *
     * @param plan DTO [CreatePlanRequest] con los datos del plan.
     * @return [Response] que contiene el [Plan] creado.
     */
    @POST("plans")
    suspend fun createPlan(@Body plan: CreatePlanRequest): Response<Plan>

    // ================= MIEMBROS =================

    /**
     * Obtiene los miembros asociados a un plan específico.
     *
     * @param planId ID del plan.
     * @return [Response] con la lista de [Member] asociados al plan.
     */
    @GET("members/plan/{planId}")
    suspend fun getMembersByPlan(@Path("planId") planId: String): Response<List<Member>>

    /**
     * Crea un nuevo miembro asociado a un plan.
     *
     * @param member DTO [CreateMemberRequest] con los datos del miembro.
     * @return [Response] que contiene el [Member] creado.
     */
    @POST("members")
    suspend fun createMember(@Body member: CreateMemberRequest): Response<Member>

    // ================= PAGOS =================

    /**
     * Registra un nuevo pago realizado por un miembro.
     *
     * @param payment DTO [PaymentRequest] con los datos del pago.
     * @return [Response] que contiene el [Payment] registrado.
     */
    @POST("payments")
    suspend fun registerPayment(@Body payment: PaymentRequest): Response<Payment>

    /**
     * Obtiene todos los pagos realizados a un plan específico.
     *
     * @param planId ID del plan.
     * @return [Response] con la lista de [Payment] asociados al plan.
     */
    @GET("payments/plan/{planId}")
    suspend fun getPaymentsByPlan(@Path("planId") planId: String): Response<List<Payment>>
}
