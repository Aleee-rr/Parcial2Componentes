package com.ud.parcial2componentes.viewmodel

import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.repository.AhorroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PlansViewModelTest {

    private lateinit var repository: AhorroRepository
    private lateinit var viewModel: PlansViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(AhorroRepository::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 1) init{} dispara loadPlans automáticamente
    @Test
    fun `init activa loadPlans y carga el success`() = runTest {
        val fakePlans = listOf(Plan("1", "Plan A", "Viaje", 100.0, 6, "2025-01-01"))

        whenever(repository.getPlans()).thenReturn(Result.success(fakePlans))

        viewModel = PlansViewModel(repository)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is PlansUiState.Success)
        assertEquals(1, (state as PlansUiState.Success).plans.size)
    }

    // 2) loadPlans - éxito con lista de planes
    @Test
    fun `loadPlans success devuelven plans`() = runTest {
        val samplePlans = listOf(
            Plan("1", "Plan A", "Más plata", 5000.0, 12),
            Plan("2", "Plan B", "Menos plata", 3000.0, 6)
        )

        whenever(repository.getPlans()).thenReturn(Result.success(samplePlans))

        viewModel = PlansViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertTrue(uiState is PlansUiState.Success)
        assertEquals(2, (uiState as PlansUiState.Success).plans.size)
    }

    // 3) loadPlans - lista vacía
    @Test
    fun `La lista vacía de loadPlans muestra el estado Vacío`() = runTest {
        whenever(repository.getPlans()).thenReturn(Result.success(emptyList()))

        viewModel = PlansViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertTrue(uiState is PlansUiState.Empty)
    }

    // 4) loadPlans - error en el repository
    @Test
    fun `El error del repositorio loadPlans muestra el estado de error`() = runTest {
        whenever(repository.getPlans()).thenReturn(Result.failure(Exception("Error test")))

        viewModel = PlansViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertTrue(uiState is PlansUiState.Error)
        assertEquals("Error test", (uiState as PlansUiState.Error).message)
    }
}
