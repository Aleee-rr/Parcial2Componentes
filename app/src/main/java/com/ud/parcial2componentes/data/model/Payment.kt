package com.ud.parcial2componentes.data.model

import com.google.gson.annotations.SerializedName

/**
 * Representa un pago registrado en el sistema.
 *
 * Esta clase se utiliza para mapear los pagos realizados por los miembros
 * de los planes de ahorro, incluyendo información del miembro, el plan,
 * el monto pagado y la fecha del pago.
 *
 * @property id Identificador único del pago. Se corresponde con el campo "_id" en JSON.
 * @property memberId Identificador del miembro que realizó el pago.
 * @property planId Identificador del plan de ahorro al que pertenece el pago.
 * @property amount Monto del pago realizado.
 * @property date Fecha en la que se registró el pago (formato ISO 8601 recomendado).
 */
data class Payment(
    @SerializedName("_id")
    val id: String,

    val memberId: String,
    val planId: String,
    val amount: Double,
    val date: String
)
