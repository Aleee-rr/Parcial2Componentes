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
 * ViewModel para el detalle de un plan específico.
 * Maneja la información del plan, sus miembros y pagos asociados.
 */
class PlanDetailViewModel(
    private val repository: AhorroRepository = AhorroRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlanDetailUiState>(PlanDetailUiState.Loading)
    val uiState: StateFlow<PlanDetailUiState> = _uiState.asStateFlow()

    fun loadPlanDetails(planId: String) {
        viewModelScope.launch {
            _uiState.value = PlanDetailUiState.Loading

            // Cargar plan, miembros y pagos en paralelo
            val planResult = repository.getPlanById(planId)
            val membersResult = repository.getMembersByPlan(planId)
            val paymentsResult = repository.getPaymentsByPlan(planId)

            if (planResult.isSuccess) {
                val plan = planResult.getOrNull()!!
                val members = membersResult.getOrNull() ?: emptyList()
                val payments = paymentsResult.getOrNull() ?: emptyList()

                // Crear un mapa de memberId -> nombre para búsqueda rápida
                val memberMap = members.associateBy { it.id }

                // Combinar pagos con nombres de miembros
                val paymentsWithMembers = payments.map { payment ->
                    val memberName = memberMap[payment.memberId]?.name ?: "Desconocido"
                    PaymentWithMember(payment, memberName)
                }

                val totalPaid = payments.sumOf { it.amount }
                val progress = plan.calculateProgress(totalPaid)

                _uiState.value = PlanDetailUiState.Success(
                    plan = plan,
                    members = members,
                    payments = payments,
                    paymentsWithMembers = paymentsWithMembers,
                    totalPaid = totalPaid,
                    progress = progress
                )
            } else {
                _uiState.value = PlanDetailUiState.Error(
                    planResult.exceptionOrNull()?.message ?: "Error cargando plan"
                )
            }
        }
    }
}

sealed class PlanDetailUiState {
    object Loading : PlanDetailUiState()
    data class Success(
        val plan: Plan,
        val members: List<Member>,
        val payments: List<Payment>,
        val paymentsWithMembers: List<PaymentWithMember>,  //NUEVA PROPIEDAD
        val totalPaid: Double,
        val progress: Int
    ) : PlanDetailUiState()
    data class Error(val message: String) : PlanDetailUiState()
}