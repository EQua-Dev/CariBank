package com.schoolprojects.caribank.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(val route: String, val icon: ImageVector, val title: String) {
    object Transactions : NavItem("student_transactions", Icons.Default.History, "Transactions")
    object PayFees : NavItem("student_pay_fees", Icons.Default.Payments, "Pay Fees")
    object TakeLoan : NavItem("student_take_loan", Icons.Default.VolunteerActivism, "Take Loan")
    object PayDues : NavItem("student_pay_dues", Icons.Default.Gavel, "Pay Dues")
    object Savings : NavItem("student_savings", Icons.Default.Savings, "Savings")
}