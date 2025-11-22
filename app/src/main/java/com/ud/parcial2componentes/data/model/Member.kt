package com.ud.parcial2componentes.data.model

import com.google.gson.annotations.SerializedName

/**
 * Representa un miembro asociado a un plan de ahorro.
 */
data class Member(
    @SerializedName("_id")
    val id: String = "",  // ← Ahora es opcional

    val name: String,
    val planId: String,
    val contributionPerMonth: Double,

    val joinedAt: String = ""  // ← Ahora es opcional
)