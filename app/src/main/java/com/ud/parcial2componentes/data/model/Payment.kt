package com.ud.parcial2componentes.data.model

import com.google.gson.annotations.SerializedName

/**
 * Representa un pago registrado en el sistema.
 */
data class Payment(
    @SerializedName("_id")
    val id: String,

    val memberId: String,
    val planId: String,
    val amount: Double,
    val date: String
)