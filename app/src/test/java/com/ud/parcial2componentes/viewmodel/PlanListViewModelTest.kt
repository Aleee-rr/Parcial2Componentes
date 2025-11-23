package com.ud.parcial2componentes.viewmodel

import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.repository.SavingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class PlanListViewModelTest {

    private lateinit var viewModel: PlanListViewModel
    private lateinit var repository: SavingsRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(SavingsRepository::class.java)
        viewModel = PlanListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 1) Carga correcta de planes
    @Test
    fun `loadPlans - success response updates plans list`() = runTest {
        val samplePlans = listOf(
            Plan(id = "1", name = "Plan A", motive = "Viaje", targetAmount = 1000.0, months = 6),
            Plan(id = "2", name = "Plan B", motive = "Estudio", targetAmount = 500.0, months = 3)
        )

        // Mock repository success
        `when`(repository.getPlans()).thenReturn(Response.success(samplePlans))

        viewModel.loadPlans()

        // Avanzar corrutinas
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(samplePlans, viewModel.plans.value)
        assertFalse(viewModel.loading.value)
        assertNull(viewModel.error.value)
    }

    // 2) Error HTTP desde el servidor
    @Test
    fun `loadPlans - http error updates error state`() = runTest {
        val errorResponse = Response.error<List<Plan>>(
            404,
            ResponseBody.create("application/json".toMediaTypeOrNull(), "Not found")
        )

        `when`(repository.getPlans()).thenReturn(errorResponse)

        viewModel.loadPlans()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Error HTTP 404", viewModel.error.value)
        assertTrue(viewModel.plans.value.isEmpty())
        assertFalse(viewModel.loading.value)
    }

    // 3) Excepción al conectarse (sin internet, timeout, etc.)
    @Test
    fun `loadPlans - exception shows connection error`() = runTest {
        `when`(repository.getPlans()).thenThrow(RuntimeException("Timeout"))

        viewModel.loadPlans()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("No se pudo conectar: Timeout", viewModel.error.value)
        assertTrue(viewModel.plans.value.isEmpty())
        assertFalse(viewModel.loading.value)
    }
}