package com.schoolprojects.caribank.models

data class PaidDues(
    val paidDuesId: String = "",
    val duesId: String = "",
    val amountPaid: Double = 0.0,
    val dueDescription: String = "",
    val studentId: String = "",
    val dateDue: String = "",
    val datePaid: String = "",
    val status: String = "",
    val itemsPaid: List<String> = listOf(),
    val paymentRef: String = "",
    val isVerified: Boolean = false
    )

