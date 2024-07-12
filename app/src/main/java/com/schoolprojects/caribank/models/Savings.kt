package com.schoolprojects.caribank.models

data class Savings(
    val savingsId: String = "",
    val accountId: String = "",
    val dateCreated: String = "",
    val savingsAmount: Double = 0.0,
    val interestRate: Double = 0.0,
    val dueDate: String = "",
    val savingsTitle: String = "",
    val savingsDescription: String = "",
    val savingsStatus: String = "",
)
