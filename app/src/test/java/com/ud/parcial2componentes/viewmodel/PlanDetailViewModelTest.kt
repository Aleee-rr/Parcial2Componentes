package com.ud.parcial2componentes.viewmodel

import app.cash.turbine.test
import com.ud.parcial2componentes.data.model.Member
import com.ud.parcial2componentes.data.model.Payment
import com.ud.parcial2componentes.data.model.PaymentWithMember
import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.repository.AhorroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class PlanDetailViewModelTest {

    private lateinit var repository: AhorroRepository
    private lateinit var viewModel: PlanDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mock()
        viewModel = PlanDetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `si todo carga bien retorna Success`() = runTest {
        // Datos falsos
        val fakePlan = Plan(
            id = "plan1",
            name = "Plan A",
            motive = "Viaje",
            targetAmount = 1000.0,
            months = 6,
            createdAt = "2025-01-01"
        )

        val fakeMembers = listOf(
            Member(id = "m1", name = "Juan", planId = "plan1", contributionPerMonth = 200.0),
            Member(id = "m2", name = "Ana", planId = "plan1", contributionPerMonth = 300.0)
        )

        val fakePayments = listOf(
            Payment(id = "p1", memberId = "m1", planId = "plan1", amount = 200.0, date = "2025-01-01"),
            Payment(id = "p2", memberId = "m2", planId = "plan1", amount = 300.0, date = "2025-01-02")
        )

        // Mocks
        whenever(repository.getPlanById("plan1")).thenReturn(Result.success(fakePlan))
        whenever(repository.getMembersByPlan("plan1")).thenReturn(Result.success(fakeMembers))
        whenever(repository.getPaymentsByPlan("plan1")).thenReturn(Result.success(fakePayments))

        viewModel.uiState.test {
            viewModel.loadPlanDetails("plan1")
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue(state is PlanDetailUiState.Loading)

            val successState = awaitItem() as PlanDetailUiState.Success

            assertEquals(fakePlan, successState.plan)
            assertEquals(500.0, successState.totalPaid, 0.01)
            assertEquals(
                listOf("Juan", "Ana"),
                successState.paymentsWithMembers.map { it.memberName }
            )
        }
    }

    @Test
    fun `si falla la carga del plan retorna Error`() = runTest {
        whenever(repository.getPlanById("plan1"))
            .thenReturn(Result.failure(Exception("Error cargando plan")))

        whenever(repository.getMembersByPlan("plan1"))
            .thenReturn(Result.success(emptyList()))

        whenever(repository.getPaymentsByPlan("plan1"))
            .thenReturn(Result.success(emptyList()))

        viewModel.uiState.test {
            viewModel.loadPlanDetails("plan1")
            advanceUntilIdle()

            val loading = awaitItem()
            assertTrue(loading is PlanDetailUiState.Loading)

            val error = awaitItem() as PlanDetailUiState.Error
            assertEquals("Error cargando plan", error.message)
        }
    }
}
