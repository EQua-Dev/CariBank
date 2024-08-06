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

)

data class FeeWithStudent(
    val paidFee: PaidFee,
    val student: Student
)

data class DueWithStudent(
    val paidDue: PaidDues,
    val student: Student
)
