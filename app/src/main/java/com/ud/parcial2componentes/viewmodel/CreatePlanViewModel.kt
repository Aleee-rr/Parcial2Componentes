package com.ud.parcial2componentes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ud.parcial2componentes.data.repository.AhorroRepository
import com.ud.parcial2componentes.ui.screens.MemberInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para crear un nuevo plan.
 * Maneja la lógica de validación y creación del plan con sus miembros.
 */
class CreatePlanViewModel(
    private val repository: AhorroRepository = AhorroRepository()
) : ViewModel() {

    private val _createState = MutableStateFlow<CreatePlanState>(CreatePlanState.Idle)
    val createState: StateFlow<CreatePlanState> = _createState.asStateFlow()

    fun createPlan(
        name: String,
        motive: String,
        targetAmount: Double,
        months: Int,
        members: List<MemberInput>
    ) {
        viewModelScope.launch {
            _createState.value = CreatePlanState.Loading

            // Validaciones
            if (name.isBlank()) {
                _createState.value = CreatePlanState.Error("El nombre no puede estar vacío")
                return@launch
            }
            if (targetAmount <= 0) {
                _createState.value = CreatePlanState.Error("La meta debe ser mayor a 0")
                return@launch
            }
            if (months <= 0) {
                _createState.value = CreatePlanState.Error("Los meses deben ser mayor a 0")
                return@launch
            }
            if (members.isEmpty()) {
                _createState.value = CreatePlanState.Error("Debe agregar al menos un miembro")
                return@launch
            }

            // Crear plan
            repository.createPlan(name, motive, targetAmount, months)
                .onSuccess { plan ->
                    // Crear miembros para ese plan
                    var allMembersCreated = true
                    members.forEach { memberInput ->
                        val result = repository.createMember(
                            name = memberInput.name,
                            planId = plan.id,
                            contributionPerMonth = memberInput.contributionPerMonth
                        )
                        if (result.isFailure) {
                            allMembersCreated = false
                        }
                    }

                    if (allMembersCreated) {
                        _createState.value = CreatePlanState.Success
                    } else {
                        _createState.value = CreatePlanState.Error("Error creando algunos miembros")
                    }
                }
                .onFailure { error ->
                    _createState.value = CreatePlanState.Error(
                        error.message ?: "Error creando el plan"
                    )
                }
        }
    }

    fun resetState() {
        _createState.value = CreatePlanState.Idle
    }
}

sealed class CreatePlanState {
    object Idle : CreatePlanState()
    object Loading : CreatePlanState()
    object Success : CreatePlanState()
    data class Error(val message: String) : CreatePlanState()
}