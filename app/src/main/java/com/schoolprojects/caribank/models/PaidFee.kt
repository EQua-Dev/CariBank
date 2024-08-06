package com.schoolprojects.caribank.models

data class PaidFee(
    val paidFeeId: String = "",
    val feeId: String = "",
    val studentId: String = "",
    val amountPaid: Double = 0.0,
    val datePaid: String = "",
    val status: String = "",
    val feeDescription: String = "",
    val itemsPaid: List<String> = listOf(),
    val paymentRef: String = "",
    val isVerified: Boolean = false
)
