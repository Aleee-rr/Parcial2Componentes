package com.ud.parcial2componentes.data.model

/**
 * DTO para crear un nuevo miembro.
 */
data class CreateMemberRequest(
    val name: String,
    val planId: String,
    val contributionPerMonth: Double
)