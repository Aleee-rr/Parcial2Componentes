package com.ud.parcial2componentes.data.repository

import androidx.annotation.VisibleForTesting
import com.google.android.gms.common.api.Api
import com.ud.parcial2componentes.data.model.Plan
import com.ud.parcial2componentes.data.model.Payment
import com.ud.parcial2componentes.data.model.Member
import com.ud.parcial2componentes.data.repository.SavingsRepository
import com.ud.parcial2componentes.data.remote.RetrofitInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class SavingsRepositoryTest {
    @VisibleForTesting
    internal var api = RetrofitInstance.api
    private lateinit var repository: SavingsRepository

    @Before
    fun setup() {
        api = mock()
        repository = SavingsRepository().apply {
            // Reemplazamos el API interno por el mock
            val field = SavingsRepository::class.java.getDeclaredField("api")
            field.isAccessible = true
            field.set(this, api)
        }
    }

    @Test
    fun `getPlans returns list`() = runTest {
        val fakePlans = listOf(
            Plan("1", "Plan A", "vi",1000.0, 12),
            Plan("2", "Plan B", "vi",500.0, 6)
        )

        whenever(api.getPlans()).thenReturn(Response.success(fakePlans))

        val result = repository.getPlans()
        assertTrue(result.isSuccessful)
        assertEquals(2, result.body()?.size)
    }

    @Test
    fun `createPayment success`() = runTest {
        val payment = Payment("1", "1", "1", 100.0, "2025-11-22")
        whenever(api.createPayment(payment)).thenReturn(Response.success(payment))

        val result = repository.createPayment(payment)
        assertTrue(result.isSuccessful)
        assertEquals(100.0, result.body()?.amount)
    }

    @Test
    fun `getMembers returns list`() = runTest {
        val members = listOf(Member("1", "Juan", "1", 100.0))
        whenever(api.getMembersByPlan("1")).thenReturn(Response.success(members))

        val result = repository.getMembers("1")
        assertTrue(result.isSuccessful)
        assertEquals("Juan", result.body()?.first()?.name)
    }
}