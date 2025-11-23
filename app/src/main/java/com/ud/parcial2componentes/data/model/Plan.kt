package com.ud.parcial2componentes.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo que representa un plan de ahorro familiar.
 *
 * Esta clase se mapea directamente con la respuesta del backend y contiene
 * información sobre la meta de ahorro, motivo, duración y fecha de creación.
 *
 * @property id Identificador único del plan. Se corresponde con el campo "_id" en JSON. Opcional.
 * @property name Nombre del plan de ahorro.
 * @property motive Motivo o finalidad del plan de ahorro.
 * @property targetAmount Monto objetivo que se desea alcanzar con el plan.
 * @property months Duración del plan en meses.
 * @property createdAt Fecha de creación del plan en formato ISO 8601. Opcional.
 */
data class Plan(
    @SerializedName("_id")
    val id: String = "",

    val name: String,
    val motive: String,
    val targetAmount: Double,
    val months: Int,

    val createdAt: String = ""
) {
    /**
     * Calcula el progreso del plan basado en los pagos totales recibidos.
     *
     * @param totalPaid Total acumulado de pagos realizados al plan.
     * @return Porcentaje de progreso del 0 al 100.
     */
    fun calculateProgress(totalPaid: Double): Int {
        if (targetAmount <= 0) return 0
        return ((totalPaid / targetAmount) * 100).toInt().coerceIn(0, 100)
    }

    /**
     * Calcula cuánto falta para completar la meta del plan.
     *
     * @param totalPaid Total acumulado de pagos realizados al plan.
     * @return Monto restante para alcanzar la meta, mínimo 0.
     */
    fun remainingAmount(totalPaid: Double): Double {
        return (targetAmount - totalPaid).coerceAtLeast(0.0)
    }
}
