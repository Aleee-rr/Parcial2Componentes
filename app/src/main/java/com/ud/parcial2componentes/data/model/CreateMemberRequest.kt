package com.ud.parcial2componentes.data.model

/**
 * Data Transfer Object (DTO) utilizado para crear un nuevo miembro en el sistema.
 *
 * Este objeto encapsula la información necesaria para registrar un miembro,
 * incluyendo su nombre, el plan seleccionado y la contribución mensual.
 *
 * @property name Nombre completo del miembro.
 * @property planId Identificador único del plan al que se suscribe el miembro.
 * @property contributionPerMonth Monto de la contribución mensual del miembro en la moneda local.
 */
data class CreateMemberRequest(
    val name: String,
    val planId: String,
    val contributionPerMonth: Double
)
