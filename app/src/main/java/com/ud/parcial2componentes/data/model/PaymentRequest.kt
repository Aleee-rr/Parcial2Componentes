package com.ud.parcial2componentes.data.model

/**
 * Data Transfer Object (DTO) utilizado para enviar un nuevo pago al backend.
 *
 * Esta clase encapsula la información mínima necesaria para registrar un pago
 * realizado por un miembro de un plan de ahorro.
 *
 * @property memberId Identificador del miembro que realiza el pago.
 * @property planId Identificador del plan de ahorro asociado al pago.
 * @property amount Monto del pago que se desea registrar.
 */
data class PaymentRequest(
    val memberId: String,
    val planId: String,
    val amount: Double
)
