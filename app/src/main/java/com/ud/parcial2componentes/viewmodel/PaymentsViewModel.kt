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
 * ViewModel para el registro de pagos.
 * Maneja la lógica de envío y validación de pagos.
 */
class PaymentsViewModel(
    private val repository: AhorroRepository = AhorroRepository()
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterPaymentState>(RegisterPaymentState.Idle)
    val registerState: StateFlow<RegisterPaymentState> = _registerState.asStateFlow()

    fun registerPayment(memberId: String, planId: String, amount: Double) {
        viewModelScope.launch {
            _registerState.value = RegisterPaymentState.Loading

            // Validación básica
            if (amount <= 0) {
                _registerState.value = RegisterPaymentState.Error("El monto debe ser mayor a 0")
                return@launch
            }

            val payment = PaymentRequest(
                memberId = memberId,
                planId = planId,
                amount = amount
            )

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

    fun resetState() {
        _registerState.value = RegisterPaymentState.Idle
    }
}

sealed class RegisterPaymentState {
    object Idle : RegisterPaymentState()
    object Loading : RegisterPaymentState()
    object Success : RegisterPaymentState()
    data class Error(val message: String) : RegisterPaymentState()
}