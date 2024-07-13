package com.schoolprojects.caribank.models

data class Student(
    val studentId: String = "",
    val studentFirstName: String = "",
    val studentLastName: String = "",
    val studentRegNo: String = "",
    val studentEmail: String = "",
    val studentGender: String = "",
    val studentDepartment: String = "",
    val studentCurrentLevel: String = "",
    val loanCreditScore: String = "0.0",

)
