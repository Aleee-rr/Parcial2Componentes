package com.ud.parcial2componentes.data.model

/**
 * Representa un pago junto con el nombre del miembro que lo realizó.
 *
 * Esta clase se utiliza para mostrar información combinada de un pago y
 * el miembro asociado, facilitando la visualización en interfaces de usuario
 * o reportes sin necesidad de hacer múltiples consultas.
 *
 * @property payment Objeto [Payment] que contiene los detalles del pago.
 * @property memberName Nombre completo del miembro que realizó el pago.
 */
data class PaymentWithMember(
    val payment: Payment,
    val memberName: String
)
