package com.schoolprojects.caribank.models

data class PaidDues(
    val paidDuesId: String = "",
    val duesId: String = "",
    val duesAmount: Double = 0.0,
    val studentId: String = "",
    val dateDue: String = "",
    val datePaid: String = "",
    val status: String = "",
)
