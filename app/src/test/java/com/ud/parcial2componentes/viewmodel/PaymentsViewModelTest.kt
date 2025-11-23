package com.ud.parcial2componentes.viewmodel

import com.ud.parcial2componentes.data.model.Payment
import com.ud.parcial2componentes.data.repository.AhorroRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class PaymentsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mock<AhorroRepository>()
    private val viewModel = PaymentsViewModel(repository)

    /**
     * 1. Validación: monto <= 0 debe devolver Error
     */
    @Test
    fun `monto menor o igual a cero retorna error`() = runTest {
        viewModel.registerPayment("member1", "plan1", 0.0)

        val state = viewModel.registerState.first()
        assertTrue(state is RegisterPaymentState.Error)
        assertEquals("El monto debe ser mayor a 0", (state as RegisterPaymentState.Error).message)
    }

    /**
     * 2. Caso éxito: el repo responde éxito → ViewModel debe emitir Success
     */
    @Test
    fun `si el repositorio responde success el estado debe ser Success`() = runTest {
        val fakePayment = Payment(
            id = "123",
            memberId = "member1",
            planId = "plan1",
            amount = 100.0,
            date = "2025-01-01"
        )

        whenever(repository.registerPayment(any()))
            .thenReturn(Result.success(fakePayment))

        viewModel.registerPayment("member1", "plan1", 100.0)

        val state = viewModel.registerState.first()
        assertTrue(state is RegisterPaymentState.Success)

        verify(repository).registerPayment(any())
    }

    /**
     * 3. Caso error: el repositorio falla → ViewModel debe emitir Error
     */
    @Test
    fun `si el repositorio falla el estado debe ser Error`() = runTest {
        whenever(repository.registerPayment(any()))
            .thenReturn(Result.failure(Exception("Fallo en API")))

        viewModel.registerPayment("member1", "plan1", 50.0)

        val state = viewModel.registerState.first()
        assertTrue(state is RegisterPaymentState.Error)
        assertEquals("Fallo en API", (state as RegisterPaymentState.Error).message)
    }
}
