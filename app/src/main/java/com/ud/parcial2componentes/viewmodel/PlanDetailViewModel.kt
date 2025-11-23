package com.ud.parcial2componentes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ud.parcial2componentes.data.model.Member
import com.ud.parcial2componentes.data.model.Payment
import com.ud.parcial2componentes.data.model.PaymentWithMember
import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.repository.AhorroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsable de manejar el detalle de un plan de ahorro.
 *
 * - Carga la información del plan, sus miembros y los pagos asociados.
 * - Calcula el progreso del plan basado en los pagos realizados.
 * - Combina los pagos con los nombres de los miembros para una visualización más clara.
 */
class PlanDetailViewModel(
    private val repository: AhorroRepository = AhorroRepository()
) : ViewModel() {

    // Estado interno mutable que mantiene la UI actual
    private val _uiState = MutableStateFlow<PlanDetailUiState>(PlanDetailUiState.Loading)

    /**
     * Estado público que representa la información actual de la UI:
     * puede ser Loading, Success con datos o Error.
     */
    val uiState: StateFlow<PlanDetailUiState> = _uiState.asStateFlow()

    /**
     * Carga los detalles completos de un plan específico.
     *
     * - planId: ID del plan que se desea consultar.
     *
     * Este método carga en paralelo:
     * 1. Información del plan.
     * 2. Lista de miembros asociados.
     * 3. Lista de pagos realizados.
     *
     * Luego combina los pagos con los nombres de los miembros
     * y calcula el progreso total del plan.
     */
    fun loadPlanDetails(planId: String) {
        viewModelScope.launch {
            _uiState.value = PlanDetailUiState.Loading

            // Llamadas al repositorio
            val planResult = repository.getPlanById(planId)
            val membersResult = repository.getMembersByPlan(planId)
            val paymentsResult = repository.getPaymentsByPlan(planId)

            if (planResult.isSuccess) {
                val plan = planResult.getOrNull()!!
                val members = membersResult.getOrNull() ?: emptyList()
                val payments = paymentsResult.getOrNull() ?: emptyList()

                // Mapear memberId -> Member para búsqueda rápida
                val memberMap = members.associateBy { it.id }

                // Combinar pagos con nombres de miembros
                val paymentsWithMembers = payments.map { payment ->
                    val memberName = memberMap[payment.memberId]?.name ?: "Desconocido"
                    PaymentWithMember(payment, memberName)
                }

                val totalPaid = payments.sumOf { it.amount }
                val progress = plan.calculateProgress(totalPaid)

                // Actualizar estado con datos completos
                _uiState.value = PlanDetailUiState.Success(
                    plan = plan,
                    members = members,
                    payments = payments,
                    paymentsWithMembers = paymentsWithMembers,
                    totalPaid = totalPaid,
                    progress = progress
                )
            } else {
                // Manejo de error si falla la carga del plan
                _uiState.value = PlanDetailUiState.Error(
                    planResult.exceptionOrNull()?.message ?: "Error cargando plan"
                )
            }
        }
    }
}

/**
 * Estados posibles para la pantalla de detalle de un plan.
 */
sealed class PlanDetailUiState {
    /** Estado de carga mientras se obtienen los datos */
    object Loading : PlanDetailUiState()

    /**
     * Estado exitoso con datos completos.
     *
     * @property plan Información general del plan.
     * @property members Lista de miembros asociados al plan.
     * @property payments Lista de pagos realizados.
     * @property paymentsWithMembers Lista de pagos combinados con nombres de miembros.
     * @property totalPaid Total acumulado de pagos.
     * @property progress Porcentaje de progreso del plan (0 a 100).
     */
    data class Success(
        val plan: Plan,
        val members: List<Member>,
        val payments: List<Payment>,
        val paymentsWithMembers: List<PaymentWithMember>,
        val totalPaid: Double,
        val progress: Int
    ) : PlanDetailUiState()

    /**
     * Estado de error al cargar los datos del plan.
     *
     * @property message Mensaje descriptivo del error.
     */
    data class Error(val message: String) : PlanDetailUiState()
}
