package com.ud.parcial2componentes.data.repository

import com.ud.parcial2componentes.data.model.*
import com.ud.parcial2componentes.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.*
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
class AhorroRepositoryTest {

    private lateinit var api: ApiService
    private lateinit var repository: AhorroRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        api = mock(ApiService::class.java)
        repository = AhorroRepository()

        // Sobreescribir el apiService del repository usando reflexión (porque es val)
        val apiField = AhorroRepository::class.java.getDeclaredField("api")
        apiField.isAccessible = true
        apiField.set(repository, api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Helper para crear error HTTP
    private fun errorResponse(): ResponseBody =
        ResponseBody.create("application/json".toMediaTypeOrNull(), "error")

    // getPlans()
    @Test
    fun `getPlans returns success`() = runTest {
        val fakeList = listOf(Plan("1", "Plan A", "Viaje", 100.0, 6, "2025-01-01"))
        whenever(api.getPlans()).thenReturn(Response.success(fakeList))

        val result = repository.getPlans()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()!!.size)
    }

    @Test
    fun `getPlans returns failure when HTTP error`() = runTest {
        whenever(api.getPlans()).thenReturn(
            Response.error(500, errorResponse())
        )

        val result = repository.getPlans()

        assertTrue(result.isFailure)
    }

    // --------------------------------------------------------------------
    // getPlanById()
    // --------------------------------------------------------------------
    @Test
    fun `getPlanById returns success`() = runTest {
        val fakePlan = Plan("1", "Plan A", "Viaje", 100.0, 6, "2025-01-01")
        whenever(api.getPlanById("1")).thenReturn(Response.success(fakePlan))

        val result = repository.getPlanById("1")

        assertTrue(result.isSuccess)
        assertEquals(fakePlan.name, result.getOrNull()!!.name)
    }

    @Test
    fun `getPlanById returns failure`() = runTest {
        whenever(api.getPlanById("1")).thenReturn(
            Response.error(404, errorResponse())
        )

        val result = repository.getPlanById("1")

        assertTrue(result.isFailure)
    }

    // getMembersByPlan()
    @Test
    fun `getMembersByPlan returns success`() = runTest {
        val members = listOf(Member("1", "Juan", "1", 200.0))
        whenever(api.getMembersByPlan("1")).thenReturn(Response.success(members))

        val result = repository.getMembersByPlan("1")

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()!!.size)
    }

    @Test
    fun `getMembersByPlan returns failure`() = runTest {
        whenever(api.getMembersByPlan("1"))
            .thenReturn(Response.error(400, errorResponse()))

        val result = repository.getMembersByPlan("1")

        assertTrue(result.isFailure)
    }

    // --------------------------------------------------------------------
    // registerPayment()
    // --------------------------------------------------------------------
    @Test
    fun `registerPayment success`() = runTest {
        val fakePayment = Payment("1", "1", "1", 100.0, "2025-11-22")
        val request = PaymentRequest("1", "1", 200.0)

        whenever(api.registerPayment(request))
            .thenReturn(Response.success(fakePayment))

        val result = repository.registerPayment(request)

        assertTrue(result.isSuccess)
        assertEquals(100.0, result.getOrNull()!!.amount, 0.01)
    }

    @Test
    fun `registerPayment failure`() = runTest {
        val request = PaymentRequest("1", "2", 200.0)

        whenever(api.registerPayment(request))
            .thenReturn(Response.error(400, errorResponse()))

        val result = repository.registerPayment(request)

        assertTrue(result.isFailure)
    }

    // getPaymentsByPlan()
    @Test
    fun `getPaymentsByPlan success`() = runTest {
        val fakeList = listOf(Payment("1", "1", "1",50.0, "2025-11-22"))

        whenever(api.getPaymentsByPlan("1"))
            .thenReturn(Response.success(fakeList))

        val result = repository.getPaymentsByPlan("1")

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()!!.size)
    }

    @Test
    fun `getPaymentsByPlan failure`() = runTest {
        whenever(api.getPaymentsByPlan("1"))
            .thenReturn(Response.error(404, errorResponse()))

        val result = repository.getPaymentsByPlan("1")

        assertTrue(result.isFailure)
    }

    // createPlan()
    @Test
    fun `createPlan success`() = runTest {
        val fakePlan = Plan("1", "Plan Nuevo", "viaje",500.0, 6)

        // FIX: especificar tipo genérico
        whenever(api.createPlan(any<CreatePlanRequest>()))
            .thenReturn(Response.success(fakePlan))

        val result = repository.createPlan("Plan Nuevo", "Ahorro", 500.0, 6)

        assertTrue(result.isSuccess)
        assertEquals("Plan Nuevo", result.getOrNull()!!.name)
    }


    @Test
    fun `createPlan failure`() = runTest {
        whenever(api.createPlan(any()))
            .thenReturn(Response.error(500, errorResponse()))

        val result = repository.createPlan("Plan", "motivo", 100.0, 5)

        assertTrue(result.isFailure)
    }

    // createMember()
    @Test
    fun `createMember success`() = runTest {
        val fakeMember = Member("1", "Juan", "1", 100.0)

        whenever(api.createMember(any()))
            .thenReturn(Response.success(fakeMember))

        val result = repository.createMember("Juan", "1", 100.0)

        assertTrue(result.isSuccess)
        assertEquals("Juan", result.getOrNull()!!.name)
    }

    @Test
    fun `createMember failure`() = runTest {
        whenever(api.createMember(any()))
            .thenReturn(Response.error(400, errorResponse()))

        val result = repository.createMember("Juan", "1", 100.0)

        assertTrue(result.isFailure)
    }
}