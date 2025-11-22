package com.ud.parcial2componentes.data.model

/**
 * DTO para enviar un nuevo pago al backend.
 */
data class PaymentRequest(
    val memberId: String,
    val planId: String,
    val amount: Double
)