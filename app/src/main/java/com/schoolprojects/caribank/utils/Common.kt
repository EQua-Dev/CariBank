/*
 * Copyright (c) 2024.
 * Luomy EQua
 * Under Awesomeness Studios
 */

package com.schoolprojects.caribank.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//import org.devstrike.persacg.models.Course
//import org.devstrike.persacg.models.Lecturer
//import org.devstrike.persacg.models.RegisteredCourse
//import org.devstrike.persacg.models.Student
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round

object Common {

    val mAuth = FirebaseAuth.getInstance()
    val fireStoreDB = Firebase.firestore.batch()

    enum class UserTypes(val userType: String) {
        STUDENT("Student"),
        BANKER("Banker"),
    }

    enum class Levels(val level: String){
        ONE("100 Level"),
        TWO("200 Level"),
        THREE("300 Level"),
        FOUR("400 Level"),
    }



    private const val STUDENTS_REF = "CariBank Students"
    private const val ACCOUNTS_REF = "CariBank Accounts"
    private const val LECTURERS_REF = "Correps Lecturers"
    private const val ACCOUNTS_HISTORY_REF = "CariBank Accounts History"
    private const val COURSES_REF = "Courses"
    private const val LOANS_REF = "Caribank Loans"
    private const val SAVINGS_REF = "Caribank Savings"
    private const val PAID_FEES_REF = "Caribank Paid Fees"
    //const val HEALTH_RECORD_REF = "Patrealysis Health Record"

    val studentsCollectionRef = Firebase.firestore.collection(STUDENTS_REF)
    val accountsCollectionRef = Firebase.firestore.collection(ACCOUNTS_REF)
    val accountsHistoryCollectionRef = Firebase.firestore.collection(ACCOUNTS_HISTORY_REF)
    val loansCollectionRef = Firebase.firestore.collection(LOANS_REF)
    val savingsCollectionRef = Firebase.firestore.collection(SAVINGS_REF)
    val paidFeesCollectionRef = Firebase.firestore.collection(PAID_FEES_REF)
    val coursesCollectionRef = Firebase.firestore.collection(COURSES_REF)
    //val healthRecordCollectionRef = Firebase.firestore.collection(HEALTH_RECORD_REF)

    fun logout() {
        mAuth.signOut()
    }

    enum class TransactionType(val transactionType: String){
        CREDIT("credit"),
        DEBIT("debit")
    }

}
