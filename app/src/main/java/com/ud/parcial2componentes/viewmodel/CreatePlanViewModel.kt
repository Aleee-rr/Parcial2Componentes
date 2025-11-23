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
 * ViewModel responsable de la creación de un nuevo plan de ahorro.
 *
 * - Maneja la lógica de validación de datos del plan.
 * - Interactúa con el [AhorroRepository] para crear el plan y sus miembros.
 * - Expone un flujo de estado [createState] que indica el estado de la operación.
 */
class CreatePlanViewModel(
    private val repository: AhorroRepository = AhorroRepository()
) : ViewModel() {

    // Estado interno mutable
    private val _createState = MutableStateFlow<CreatePlanState>(CreatePlanState.Idle)

    /**
     * Estado público de creación de plan.
     * Puede ser Idle, Loading, Success o Error.
     */
    val createState: StateFlow<CreatePlanState> = _createState.asStateFlow()

    /**
     * Crea un nuevo plan con los datos proporcionados y los miembros asociados.
     *
     * @param name Nombre del plan
     * @param motive Motivo del plan
     * @param targetAmount Monto objetivo a alcanzar
     * @param months Duración del plan en meses
     * @param members Lista de miembros a asociar al plan
     */
    fun createPlan(
        name: String,
        motive: String,
        targetAmount: Double,
        months: Int,
        members: List<MemberInput>
    ) {
        viewModelScope.launch {
            _createState.value = CreatePlanState.Loading

            // ===== Validaciones de datos =====
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

            // ===== Crear plan en el repositorio =====
            repository.createPlan(name, motive, targetAmount, months)
                .onSuccess { plan ->
                    // Crear miembros asociados
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

                    // Actualizar estado según resultados
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

    /**
     * Reinicia el estado del flujo a [CreatePlanState.Idle].
     * Útil después de mostrar mensajes de éxito o error.
     */
    fun resetState() {
        _createState.value = CreatePlanState.Idle
    }
}

/**
 * Estados posibles de la operación de creación de un plan.
 */
sealed class CreatePlanState {
    /** Estado inicial, sin acciones. */
    object Idle : CreatePlanState()

    /** Operación en progreso. */
    object Loading : CreatePlanState()

    /** Operación completada con éxito. */
    object Success : CreatePlanState()

    /** Ocurrió un error, con mensaje descriptivo. */
    data class Error(val message: String) : CreatePlanState()
}
