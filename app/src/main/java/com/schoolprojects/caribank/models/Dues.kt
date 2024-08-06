package com.schoolprojects.caribank.models

data class Due(
    val dueId: String = "",
    val dueName: String = "",
    val dueTotalAmount: Double = 0.0,
    val dueDescription: String = "",
    val dueLevel: String = "",
    val dueSemester: String = "",
    val dueItems: Map<String, Double> = mapOf(),
    val dueStatus: String = "",
)

val schoolDues = listOf(
    // 100 Level
    Due(
        dueId = "1",
        dueName = "Departmental Dues",
        dueTotalAmount = 15000.0,
        dueDescription = "Departmental dues for 100 Level students",
        dueLevel = "100 Level",
        dueSemester = "1st Semester",
        dueItems = mapOf(
            "Department Fee" to 5000.0,
            "Laboratory Fee" to 3000.0,
            "Library Fee" to 2000.0,
            "Student Union Fee" to 3000.0,
            "Club Dues" to 2000.0
        ),
        dueStatus = "Pending"
    ),
    Due(
        dueId = "2",
        dueName = "Departmental Dues",
        dueTotalAmount = 15000.0,
        dueDescription = "Departmental dues for 100 Level students",
        dueLevel = "100 Level",
        dueSemester = "2nd Semester",
        dueItems = mapOf(
            "Department Fee" to 5000.0,
            "Laboratory Fee" to 3000.0,
            "Library Fee" to 2000.0,
            "Student Union Fee" to 3000.0,
            "Club Dues" to 2000.0
        ),
        dueStatus = "Pending"
    ),
    // 200 Level
    Due(
        dueId = "3",
        dueName = "Departmental Dues",
        dueTotalAmount = 16000.0,
        dueDescription = "Departmental dues for 200 Level students",
        dueLevel = "200 Level",
        dueSemester = "1st Semester",
        dueItems = mapOf(
            "Department Fee" to 6000.0,
            "Laboratory Fee" to 3500.0,
            "Library Fee" to 2500.0,
            "Student Union Fee" to 3000.0,
            "Club Dues" to 1000.0
        ),
        dueStatus = "Pending"
    ),
    Due(
        dueId = "4",
        dueName = "Departmental Dues",
        dueTotalAmount = 16000.0,
        dueDescription = "Departmental dues for 200 Level students",
        dueLevel = "200 Level",
        dueSemester = "2nd Semester",
        dueItems = mapOf(
            "Department Fee" to 6000.0,
            "Laboratory Fee" to 3500.0,
            "Library Fee" to 2500.0,
            "Student Union Fee" to 3000.0,
            "Club Dues" to 1000.0
        ),
        dueStatus = "Pending"
    ),
    // 300 Level
    Due(
        dueId = "5",
        dueName = "Departmental Dues",
        dueTotalAmount = 17000.0,
        dueDescription = "Departmental dues for 300 Level students",
        dueLevel = "300 Level",
        dueSemester = "1st Semester",
        dueItems = mapOf(
            "Department Fee" to 7000.0,
            "Laboratory Fee" to 4000.0,
            "Library Fee" to 3000.0,
            "Student Union Fee" to 2000.0,
            "Club Dues" to 1000.0
        ),
        dueStatus = "Pending"
    ),
    Due(
        dueId = "6",
        dueName = "Departmental Dues",
        dueTotalAmount = 17000.0,
        dueDescription = "Departmental dues for 300 Level students",
        dueLevel = "300 Level",
        dueSemester = "2nd Semester",
        dueItems = mapOf(
            "Department Fee" to 7000.0,
            "Laboratory Fee" to 4000.0,
            "Library Fee" to 3000.0,
            "Student Union Fee" to 2000.0,
            "Club Dues" to 1000.0
        ),
        dueStatus = "Pending"
    ),
    // 400 Level
    Due(
        dueId = "7",
        dueName = "Departmental Dues",
        dueTotalAmount = 18000.0,
        dueDescription = "Departmental dues for 400 Level students",
        dueLevel = "400 Level",
        dueSemester = "1st Semester",
        dueItems = mapOf(
            "Department Fee" to 8000.0,
            "Laboratory Fee" to 4500.0,
            "Library Fee" to 3000.0,
            "Student Union Fee" to 1500.0,
            "Club Dues" to 1000.0
        ),
        dueStatus = "Pending"
    ),
    Due(
        dueId = "8",
        dueName = "Departmental Dues",
        dueTotalAmount = 18000.0,
        dueDescription = "Departmental dues for 400 Level students",
        dueLevel = "400 Level",
        dueSemester = "2nd Semester",
        dueItems = mapOf(
            "Department Fee" to 8000.0,
            "Laboratory Fee" to 4500.0,
            "Library Fee" to 3000.0,
            "Student Union Fee" to 1500.0,
            "Club Dues" to 1000.0
        ),
        dueStatus = "Pending"
    )
)

