package com.ud.parcial2componentes.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo que representa un plan de ahorro familiar.
 * Se mapea directamente con la respuesta del backend.
 */
data class Plan(
    @SerializedName("_id")
    val id: String = "",  // Ahora es opcional

    val name: String,
    val motive: String,
    val targetAmount: Double,
    val months: Int,

    val createdAt: String = ""  // ← Ahora es opcional
) {
    /**
     * Calcula el progreso del plan basado en los pagos totales recibidos.
     * @param totalPaid Total acumulado de pagos
     * @return Porcentaje de 0 a 100
     */
    fun calculateProgress(totalPaid: Double): Int {
        if (targetAmount <= 0) return 0
        return ((totalPaid / targetAmount) * 100).toInt().coerceIn(0, 100)
    }

    /**
     * Calcula cuánto falta para completar la meta.
     */
    fun remainingAmount(totalPaid: Double): Double {
        return (targetAmount - totalPaid).coerceAtLeast(0.0)
    }
}