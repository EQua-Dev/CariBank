package com.schoolprojects.caribank.models

data class Loan(
    val loanId: String = "",
    val loanAmount: Double = 0.0,
    val loanTerm: Int = 0,
    val loanStatus: String = "",
    val studentId: String = "",
    val loanDescription: String = "",
    val dateCreated: String = "",
    val datePaidBack: String = "",
)
