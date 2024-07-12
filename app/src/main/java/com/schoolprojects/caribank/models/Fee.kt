package com.schoolprojects.caribank.models

data class Fee(
    val feeId: String = "",
    val feeName: String = "",
    val feeTotalAmount: Double = 0.0,
    val feeDescription: String = "",
    val feeLevel: String = "",
    val feeSemester: String = "",
    val feeItems: Map<String, Double> = mapOf(),
    val feeStatus: String = "",
)
