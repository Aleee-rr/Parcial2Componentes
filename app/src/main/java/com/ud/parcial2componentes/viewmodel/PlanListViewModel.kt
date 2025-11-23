package com.ud.parcial2componentes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.repository.SavingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que maneja la lista de planes de ahorro.
 *
 * - Carga los planes desde el repositorio.
 * - Expone estados de carga, error y la lista de planes a la UI.
 */
class PlanListViewModel(
    private val repo: SavingsRepository = SavingsRepository()
) : ViewModel() {

    // Lista de planes cargados
    private val _plans = MutableStateFlow<List<Plan>>(emptyList())
    val plans: StateFlow<List<Plan>> get() = _plans

    // Estado de carga mientras se obtienen los planes
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    // Mensaje de error en caso de falla
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    /**
     * Carga los planes desde el repositorio.
     *
     * - Actualiza `_loading` a true mientras se realiza la petición.
     * - Si la respuesta es exitosa, actualiza `_plans`.
     * - Si ocurre un error HTTP o de conexión, actualiza `_error`.
     * - Finalmente, `_loading` vuelve a false.
     */
    fun loadPlans() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val resp = repo.getPlans()
                if (resp.isSuccessful) {
                    _plans.value = resp.body() ?: emptyList()
                } else {
                    _error.value = "Error HTTP ${resp.code()}"
                    println("Error HTTP: ${resp.code()} - ${resp.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _error.value = "No se pudo conectar: ${e.localizedMessage}"
                println("Exception: ${e.localizedMessage}")
            } finally {
                _loading.value = false
            }
        }
    }
}
