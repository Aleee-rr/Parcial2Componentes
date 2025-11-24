package com.ud.parcial2componentes.data.repository

import android.util.Log
import com.ud.parcial2componentes.data.model.CreateMemberRequest
import com.ud.parcial2componentes.data.model.CreatePlanRequest
import com.ud.parcial2componentes.data.model.Member
import com.ud.parcial2componentes.data.model.Payment
import com.ud.parcial2componentes.data.model.PaymentRequest
import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository pattern: Intermediario entre el ViewModel y la fuente de datos (API).
 * Maneja toda la lógica de acceso a datos y proporciona una interfaz limpia.
 *
 * Ventajas:
 * - Desacopla el ViewModel de los detalles de implementación de red
 * - Facilita testing (se puede mockear fácilmente)
 * - Centraliza el manejo de errores
 */
class AhorroRepository {

    private val api = RetrofitClient.apiService

    // ========== PLANES ==========

    suspend fun getPlans(): Result<List<Plan>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getPlans()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPlanById(planId: String): Result<Plan> = withContext(Dispatchers.IO) {
        try {
            val response = api.getPlanById(planId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Plan no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== MIEMBROS ==========

    suspend fun getMembersByPlan(planId: String): Result<List<Member>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMembersByPlan(planId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error obteniendo miembros"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== PAGOS ==========

    suspend fun registerPayment(payment: PaymentRequest): Result<Payment> = withContext(Dispatchers.IO) {
        try {
            val response = api.registerPayment(payment)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error registrando pago"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPaymentsByPlan(planId: String): Result<List<Payment>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getPaymentsByPlan(planId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se encontraron pagos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun createPlan(
        name: String,
        motive: String,
        targetAmount: Double,
        months: Int
    ): Result<Plan> = withContext(Dispatchers.IO) {
        try {
            val request = CreatePlanRequest(
                name = name,
                motive = motive,
                targetAmount = targetAmount,
                months = months
            )

            val response = api.createPlan(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error creando plan: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createMember(
        name: String,
        planId: String,
        contributionPerMonth: Double
    ): Result<Member> = withContext(Dispatchers.IO) {
        try {
            val request = CreateMemberRequest(
                name = name,
                planId = planId,
                contributionPerMonth = contributionPerMonth
            )

            val response = api.createMember(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error creando miembro: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}