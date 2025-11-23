package com.ud.parcial2componentes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ud.parcial2componentes.data.model.PaymentRequest
import com.ud.parcial2componentes.data.repository.AhorroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsable del registro de pagos de miembros en planes de ahorro.
 *
 * - Valida la información del pago antes de enviarla al repositorio.
 * - Gestiona el estado de la operación mediante un flujo de estado [registerState].
 * - Expone métodos para registrar pagos y reiniciar el estado.
 */
class PaymentsViewModel(
    private val repository: AhorroRepository = AhorroRepository()
) : ViewModel() {

    // Estado interno mutable
    private val _registerState = MutableStateFlow<RegisterPaymentState>(RegisterPaymentState.Idle)

    /**
     * Estado público que representa el estado actual del registro de pagos.
     * Puede ser Idle, Loading, Success o Error.
     */
    val registerState: StateFlow<RegisterPaymentState> = _registerState.asStateFlow()

    /**
     * Registra un pago para un miembro en un plan específico.
     *
     * @param memberId ID del miembro que realiza el pago
     * @param planId ID del plan al que se asocia el pago
     * @param amount Monto del pago, debe ser mayor a 0
     */
    fun registerPayment(memberId: String, planId: String, amount: Double) {
        viewModelScope.launch {
            _registerState.value = RegisterPaymentState.Loading

            // ===== Validación del monto =====
            if (amount <= 0) {
                _registerState.value = RegisterPaymentState.Error("El monto debe ser mayor a 0")
                return@launch
            }

            // Crear objeto PaymentRequest
            val payment = PaymentRequest(
                memberId = memberId,
                planId = planId,
                amount = amount
            )

            // Enviar al repositorio y actualizar estado según resultado
            repository.registerPayment(payment)
                .onSuccess {
                    _registerState.value = RegisterPaymentState.Success
                }
                .onFailure { error ->
                    _registerState.value = RegisterPaymentState.Error(
                        error.message ?: "Error registrando pago"
                    )
                }
        }
    }

    /**
     * Reinicia el estado de registro a [RegisterPaymentState.Idle].
     * Útil para limpiar mensajes de error o éxito después de mostrarlos en la UI.
     */
    fun resetState() {
        _registerState.value = RegisterPaymentState.Idle
    }
}

/**
 * Estados posibles durante el registro de un pago.
 */
sealed class RegisterPaymentState {
    /** Estado inicial, sin acciones. */
    object Idle : RegisterPaymentState()

    /** Operación en progreso. */
    object Loading : RegisterPaymentState()

    /** Pago registrado exitosamente. */
    object Success : RegisterPaymentState()

    /** Ocurrió un error durante el registro del pago. */
    data class Error(val message: String) : RegisterPaymentState()
}
