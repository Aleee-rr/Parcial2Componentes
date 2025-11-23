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
 * ViewModel para la pantalla de lista de planes de ahorro.
 *
 * Responsabilidades:
 * 1. Obtener la lista de planes desde el [AhorroRepository].
 * 2. Exponer el estado de la UI mediante [StateFlow] de tipo [PlansUiState].
 * 3. Manejar los estados de carga, éxito, vacío y error.
 *
 * Nota:
 * - No contiene lógica de UI, solo lógica de negocio y manejo de estado.
 * - El estado se inicializa con [PlansUiState.Loading] y se actualiza automáticamente al cargar los datos.
 */
class PlansViewModel(
    private val repository: AhorroRepository = AhorroRepository()
) : ViewModel() {

    /** Estado interno mutable que mantiene la UI */
    private val _uiState = MutableStateFlow<PlansUiState>(PlansUiState.Loading)

    /** Estado público inmutable que se expone a la UI */
    val uiState: StateFlow<PlansUiState> = _uiState.asStateFlow()

    /** Inicializa la carga de planes al crear el ViewModel */
    init {
        loadPlans()
    }

    /**
     * Carga la lista de planes desde el repositorio.
     *
     * Flujo:
     * 1. Establece [_uiState] a Loading.
     * 2. Intenta obtener la lista de planes.
     * 3. Si la operación es exitosa:
     *    - Si la lista está vacía, se emite [PlansUiState.Empty].
     *    - Si hay planes, se emite [PlansUiState.Success] con los datos.
     * 4. Si hay un error, se emite [PlansUiState.Error] con el mensaje correspondiente.
     */
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
 * Representa los posibles estados de la UI para la lista de planes.
 * Uso de `sealed class` permite que la UI maneje exhaustivamente todos los casos.
 */
sealed class PlansUiState {
    /** La UI está cargando los datos */
    object Loading : PlansUiState()

    /** La lista de planes está vacía */
    object Empty : PlansUiState()

    /** La lista de planes se cargó correctamente */
    data class Success(val plans: List<Plan>) : PlansUiState()

    /** Ocurrió un error al cargar los datos */
    data class Error(val message: String) : PlansUiState()
}
