package com.schoolprojects.caribank.models

data class AccountHistory(
    val historyId: String = "",
    val date: String = "",
    val accountId: String = "",
    val transactionType: String = "",
    val transactionAmount: Double = 0.0,
    val balance: Double = 0.0,
    val description: String = "",
)
