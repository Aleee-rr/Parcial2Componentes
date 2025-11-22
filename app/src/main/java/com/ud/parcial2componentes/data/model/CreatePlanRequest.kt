package com.ud.parcial2componentes.data.model

/**
 * DTO para crear un nuevo plan (sin ID ni createdAt).
 */
data class CreatePlanRequest(
    val name: String,
    val motive: String,
    val targetAmount: Double,
    val months: Int
)