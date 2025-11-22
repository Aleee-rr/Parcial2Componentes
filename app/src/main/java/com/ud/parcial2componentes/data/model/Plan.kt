package com.ud.parcial2componentes.data.model



data class Plan(
    val _id: String,
    val name: String,
    val motive: String?,
    val targetAmount: Long,
    val months: Int,
    val createdAt: String? = null
)

