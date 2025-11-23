package com.ud.parcial2componentes.data.model

/**
 * Data Transfer Object (DTO) utilizado para crear un nuevo plan en el sistema.
 *
 * Este objeto encapsula la información necesaria para registrar un plan,
 * sin incluir su ID ni la fecha de creación, ya que estos se generan automáticamente.
 *
 * @property name Nombre del plan.
 * @property motive Motivo o descripción del plan.
 * @property targetAmount Monto objetivo que se busca alcanzar con el plan.
 * @property months Duración del plan en meses.
 */
data class CreatePlanRequest(
    val name: String,
    val motive: String,
    val targetAmount: Double,
    val months: Int
)
