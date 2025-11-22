package com.ud.parcial2componentes.data.model

data class Payment(
    val _id: String? = null,
    val planId: String,
    val memberId: String? = null,
    val amount: Long,
    val date: String
)
