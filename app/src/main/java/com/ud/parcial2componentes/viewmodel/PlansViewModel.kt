package com.ud.parcial2componentes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.repository.AhorroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de lista de planes.
 *
 * Responsabilidades:
 * - Obtener planes desde el repository
 * - Exponer el estado de la UI mediante StateFlow
 * - Manejar estados de carga y errores
 *
 * NO contiene lógica de UI, solo lógica de negocio y manejo de estado.
 */
class PlansViewModel(
    private val repository: AhorroRepository = AhorroRepository()
) : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow<PlansUiState>(PlansUiState.Loading)

    // Estado público inmutable para la UI
    val uiState: StateFlow<PlansUiState> = _uiState.asStateFlow()

    init {
        loadPlans()
    }

    fun loadPlans() {
        viewModelScope.launch {
            _uiState.value = PlansUiState.Loading

            repository.getPlans()
                .onSuccess { plans ->
                    _uiState.value = if (plans.isEmpty()) {
                        PlansUiState.Empty
                    } else {
                        PlansUiState.Success(plans)
                    }
                }
                .onFailure { error ->
                    _uiState.value = PlansUiState.Error(
                        error.message ?: "Error desconocido"
                    )
                }
        }
    }
}

/**
 * Estados posibles de la UI de planes.
 * Sealed class para garantizar que se manejen todos los casos.
 */
sealed class PlansUiState {
    object Loading : PlansUiState()
    object Empty : PlansUiState()
    data class Success(val plans: List<Plan>) : PlansUiState()
    data class Error(val message: String) : PlansUiState()
}