package com.schoolprojects.caribank.utils

import kotlin.random.Random

object HelpMe {
    fun generateAccountNumber(): String {
        var seed: Long = System.currentTimeMillis()
        seed++
        val randomNumber = Random(seed).nextInt(1000000000, 9999999999.toInt())
        return randomNumber.toString()

    }

    fun isCreditScoreEligible(loanAmount: Double, creditScore: Double): Boolean {
        val maxLoanBasedOnCreditScore = creditScore * 100000 // Example calculation
        return loanAmount <= maxLoanBasedOnCreditScore
    }

    // Helper function to generate a payment reference
    fun generatePaymentRef(): String {
        var seed: Long = System.currentTimeMillis()
        seed++
        val randomNumber = Random(seed).nextInt(10000, 99999.toInt())
        return "REF$randomNumber"
    }

}