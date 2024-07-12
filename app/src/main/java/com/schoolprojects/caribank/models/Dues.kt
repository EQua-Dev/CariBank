package com.schoolprojects.caribank.models

data class Dues(
    val duesId: String = "",
    val duesName: String = "",
    val duesTotalAmount: Double = 0.0,
    val duesDescription: String = "",
    val duesLevel: String = "",
    val duesSemester: String = "",
    val duesTitle: String = "",
    val duesStatus: String = "",
)
