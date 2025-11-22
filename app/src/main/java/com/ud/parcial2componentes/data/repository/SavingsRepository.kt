package com.ud.parcial2componentes.data.repository

import com.ud.parcial2componentes.data.model.Payment
import com.ud.parcial2componentes.data.remote.RetrofitInstance
import retrofit2.Response

class SavingsRepository {
    private val api = RetrofitInstance.api

    suspend fun getPlans(): Response<List<com.ud.parcial2componentes.data.model.Plan>> =
        api.getPlans()

    suspend fun getPlan(id: String) = api.getPlan(id)

    suspend fun getPayments(planId: String) = api.getPaymentsByPlan(planId)

    suspend fun createPayment(payment: Payment) = api.createPayment(payment)

    suspend fun getMembers(planId: String) = api.getMembersByPlan(planId)

    suspend fun createMember(member: com.ud.parcial2componentes.data.model.Member) =
        api.createMember(member)
}
