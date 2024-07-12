package com.schoolprojects.caribank.models

data class Account(
    val accountId: String = "",
    val accountNumber: String = "",
    val accountBalance: Double = 0.0,
    val dateCreated: String = "",
    val accountOwner: String = "",
)
