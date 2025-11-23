package com.ud.parcial2componentes.viewmodel

import com.ud.parcial2componentes.data.model.Member
import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.repository.AhorroRepository
import com.ud.parcial2componentes.ui.screens.MemberInput
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePlanViewModelTest {
    private lateinit var repository: AhorroRepository
    private lateinit var viewModel: CreatePlanViewModel
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Before
    fun setup() {
        repository = mock()
        viewModel = CreatePlanViewModel(repository)
    }

    // Validaciones para crear un plan

    @Test
    fun `nombre vacío retorna error`() = runTest {
        viewModel.createPlan(
            name = "",
            motive = "Viaje",
            targetAmount = 100.0,
            months = 5,
            members = listOf(MemberInput("Dana", 20.0))
        )

        assertTrue(viewModel.createState.value is CreatePlanState.Error)
    }

    @Test
    fun `targetAmount menor o igual a cero retorna error`() = runTest {
        viewModel.createPlan(
            name = "Plan 1",
            motive = "Viaje",
            targetAmount = 0.0,
            months = 5,
            members = listOf(MemberInput("Dana", 20.0))
        )

        val state = viewModel.createState.value
        assert(state is CreatePlanState.Error)
        assertEquals("La meta debe ser mayor a 0", (state as CreatePlanState.Error).message)
    }

    @Test
    fun `meses invalidos retornan error`() = runTest {
        viewModel.createPlan(
            name = "Plan 1",
            motive = "Viaje",
            targetAmount = 100.0,
            months = 0,
            members = listOf(MemberInput("Dana", 20.0))
        )

        assertTrue(viewModel.createState.value is CreatePlanState.Error)
    }

    @Test
    fun `sin miembros retorna error`() = runTest {
        viewModel.createPlan(
            name = "Plan 1",
            motive = "Viaje",
            targetAmount = 100.0,
            months = 5,
            members = emptyList()
        )

        val state = viewModel.createState.value
        assert(state is CreatePlanState.Error)
        assertEquals("Debe agregar al menos un miembro", (state as CreatePlanState.Error).message)
    }

    //Mokear API

    @Test
    fun `crear plan exitoso y todos los miembros creados retorna Success`() = runTest {
        // Mock crear plan
        val fakePlan = Plan(id = "1", name = "Plan 1", motive = "Viaje", targetAmount = 100.0, months = 3)
        whenever(repository.createPlan(any(), any(), any(), any()))
            .thenReturn(Result.success(fakePlan))

        // Mock crear miembro exitoso
        val fakeMember = Member(
            id = "1",
            name = "Dana",
            planId = "1",
            contributionPerMonth = 20.0
        )

        whenever(repository.createMember(any(), any(), any()))
            .thenReturn(Result.success(fakeMember))


        viewModel.createPlan(
            name = "Plan 1",
            motive = "",
            targetAmount = 100.0,
            months = 3,
            members = listOf(MemberInput("Dana", 20.0))
        )

        assert(viewModel.createState.value is CreatePlanState.Success)
    }

    @Test
    fun `crear plan exitoso pero un miembro falla retorna Error parcial`() = runTest {
        val fakePlan = Plan(id = "1", name = "Plan 1", motive = "Viaje", targetAmount = 100.0, months = 3)

        whenever(repository.createPlan(any(), any(), any(), any()))
            .thenReturn(Result.success(fakePlan))

        // 1 éxito, 1 fallo
        whenever(repository.createMember(any(), any(), any()))
            .thenReturn(
                Result.failure(Exception("Error creando miembro"))
            )

        viewModel.createPlan(
            name = "Plan 1",
            motive = "",
            targetAmount = 100.0,
            months = 3,
            members = listOf(MemberInput("Dana", 20.0))
        )

        val state = viewModel.createState.value
        assert(state is CreatePlanState.Error)
        assertEquals("Error creando algunos miembros", (state as CreatePlanState.Error).message)
    }

    @Test
    fun `error al crear plan retorna Error`() = runTest {
        whenever(repository.createPlan(any(), any(), any(), any()))
            .thenReturn(Result.failure(Exception("Plan inválido")))

        viewModel.createPlan(
            name = "Plan 1",
            motive = "",
            targetAmount = 100.0,
            months = 3,
            members = listOf(MemberInput("Dana", 20.0))
        )

        val state = viewModel.createState.value
        assert(state is CreatePlanState.Error)
        assertEquals("Plan inválido", (state as CreatePlanState.Error).message)
    }
}
