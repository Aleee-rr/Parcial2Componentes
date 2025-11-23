package com.ud.parcial2componentes.data.model

import com.google.gson.annotations.SerializedName

/**
 * Representa un miembro asociado a un plan de ahorro.
 *
 * Esta clase se utiliza para mapear la información de un miembro desde la API o
 * para almacenar localmente sus datos. Incluye información básica del miembro,
 * el plan al que pertenece, su contribución mensual y fechas relevantes.
 *
 * @property id Identificador único del miembro. Se corresponde con el campo "_id" en JSON.
 *                 Es opcional y se asigna automáticamente si no se proporciona.
 * @property name Nombre completo del miembro.
 * @property planId Identificador del plan de ahorro al que pertenece el miembro.
 * @property contributionPerMonth Monto que el miembro contribuye mensualmente.
 * @property joinedAt Fecha en la que el miembro se unió al plan (en formato ISO 8601). Opcional.
 */
data class Member(
    @SerializedName("_id")
    val id: String = "",

    val name: String,
    val planId: String,
    val contributionPerMonth: Double,

    val joinedAt: String = ""
)
