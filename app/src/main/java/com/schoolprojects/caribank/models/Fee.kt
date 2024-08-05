package com.schoolprojects.caribank.models

data class Fee(
    val feeId: String = "",
    val feeName: String = "",
    val feeTotalAmount: Double = 0.0,
    val feeDescription: String = "",
    val feeLevel: String = "",
    val feeSemester: String = "",
    val feeItems: Map<String, Double> = mapOf(),
    val feeStatus: String = ""
)


// List of fees for all levels and semesters
val schoolFees = listOf(
    // 100 Level
    Fee(
        feeId = "1",
        feeName = "Comprehensive Fee",
        feeTotalAmount = 113000.0,
        feeDescription = "Comprehensive fee covering various services",
        feeLevel = "100 Level",
        feeSemester = "1st Semester",
        feeItems = mapOf(
            "Tuition" to 50000.0,
            "Hostel" to 20000.0,
            "Library" to 5000.0,
            "Laboratory" to 10000.0,
            "Orientation" to 5000.0,
            "Development" to 8000.0,
            "Sports" to 3000.0,
            "Miscellaneous" to 7000.0
        ),
        feeStatus = "Pending"
    ),
    Fee(
        feeId = "2",
        feeName = "Comprehensive Fee",
        feeTotalAmount = 110000.0,
        feeDescription = "Comprehensive fee covering various services",
        feeLevel = "100 Level",
        feeSemester = "2nd Semester",
        feeItems = mapOf(
            "Tuition" to 48000.0,
            "Hostel" to 19000.0,
            "Library" to 4000.0,
            "Laboratory" to 9000.0,
            "Examination" to 12000.0,
            "Sports" to 3000.0,
            "Miscellaneous" to 7000.0
        ),
        feeStatus = "Pending"
    ),
    // 200 Level
    Fee(
        feeId = "3",
        feeName = "Comprehensive Fee",
        feeTotalAmount = 120000.0,
        feeDescription = "Comprehensive fee covering various services",
        feeLevel = "200 Level",
        feeSemester = "1st Semester",
        feeItems = mapOf(
            "Tuition" to 55000.0,
            "Hostel" to 21000.0,
            "Library" to 6000.0,
            "Laboratory" to 11000.0,
            "Examination" to 13000.0,
            "Development" to 9000.0,
            "Sports" to 4000.0,
            "Miscellaneous" to 8000.0
        ),
        feeStatus = "Pending"
    ),
    Fee(
        feeId = "4",
        feeName = "Comprehensive Fee",
        feeTotalAmount = 125000.0,
        feeDescription = "Comprehensive fee covering various services",
        feeLevel = "200 Level",
        feeSemester = "2nd Semester",
        feeItems = mapOf(
            "Tuition" to 57000.0,
            "Hostel" to 22000.0,
            "Library" to 6500.0,
            "Laboratory" to 11500.0,
            "Examination" to 13500.0,
            "Development" to 9500.0,
            "Sports" to 4500.0,
            "Miscellaneous" to 8000.0
        ),
        feeStatus = "Pending"
    ),
    // 300 Level
    Fee(
        feeId = "5",
        feeName = "Comprehensive Fee",
        feeTotalAmount = 130000.0,
        feeDescription = "Comprehensive fee covering various services",
        feeLevel = "300 Level",
        feeSemester = "1st Semester",
        feeItems = mapOf(
            "Tuition" to 60000.0,
            "Hostel" to 23000.0,
            "Library" to 7000.0,
            "Laboratory" to 12000.0,
            "Examination" to 14000.0,
            "Development" to 10000.0,
            "Sports" to 5000.0,
            "Miscellaneous" to 9000.0
        ),
        feeStatus = "Pending"
    ),
    Fee(
        feeId = "6",
        feeName = "Comprehensive Fee",
        feeTotalAmount = 135000.0,
        feeDescription = "Comprehensive fee covering various services",
        feeLevel = "300 Level",
        feeSemester = "2nd Semester",
        feeItems = mapOf(
            "Tuition" to 62000.0,
            "Hostel" to 24000.0,
            "Library" to 7500.0,
            "Laboratory" to 12500.0,
            "Examination" to 14500.0,
            "Development" to 10500.0,
            "Sports" to 5500.0,
            "Miscellaneous" to 9000.0
        ),
        feeStatus = "Pending"
    ),
    // 400 Level
    Fee(
        feeId = "7",
        feeName = "Comprehensive Fee",
        feeTotalAmount = 140000.0,
        feeDescription = "Comprehensive fee covering various services",
        feeLevel = "400 Level",
        feeSemester = "1st Semester",
        feeItems = mapOf(
            "Tuition" to 65000.0,
            "Hostel" to 25000.0,
            "Library" to 8000.0,
            "Laboratory" to 13000.0,
            "Examination" to 15000.0,
            "Development" to 11000.0,
            "Sports" to 6000.0,
            "Miscellaneous" to 10000.0
        ),
        feeStatus = "Pending"
    ),
    Fee(
        feeId = "8",
        feeName = "Comprehensive Fee",
        feeTotalAmount = 145000.0,
        feeDescription = "Comprehensive fee covering various services",
        feeLevel = "400 Level",
        feeSemester = "2nd Semester",
        feeItems = mapOf(
            "Tuition" to 67000.0,
            "Hostel" to 26000.0,
            "Library" to 8500.0,
            "Laboratory" to 13500.0,
            "Examination" to 15500.0,
            "Development" to 11500.0,
            "Sports" to 6500.0,
            "Miscellaneous" to 10000.0
        ),
        feeStatus = "Pending"
    )
)

