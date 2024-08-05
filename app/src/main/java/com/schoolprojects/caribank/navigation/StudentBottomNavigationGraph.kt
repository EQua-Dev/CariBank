/*
 * Copyright (c) 2023.
 * Richard Uzor
 * Under the authority of Devstrike Digital Limited
 */

package com.schoolprojects.caribank.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.schoolprojects.caribank.screens.banker.accounts.BankerAccountsScreen
import com.schoolprojects.caribank.screens.banker.dues.BankerDuesScreen
import com.schoolprojects.caribank.screens.banker.fees.BankerFeesScreen
import com.schoolprojects.caribank.screens.banker.loans.BankerLoansScreen
import com.schoolprojects.caribank.screens.banker.savings.BankerSavingsScreen
import com.schoolprojects.caribank.screens.student.paydues.DuesPaymentScreen
import com.schoolprojects.caribank.screens.student.paydues.StudentPayDues
import com.schoolprojects.caribank.screens.student.payfees.StudentPayFees
import com.schoolprojects.caribank.screens.student.savings.StudentSavings
import com.schoolprojects.caribank.screens.student.takeloan.StudentTakeLoan
import com.schoolprojects.caribank.screens.student.transactions.StudentTransactions
import com.schoolprojects.caribank.screens.student.payfees.FeesPaymentScreen

@Composable
fun StudentBottomNavigationGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = BottomBarScreen.Transactions.route) {
        composable(
            route = BottomBarScreen.Transactions.route
        ) {
            StudentTransactions(navController = navController)
        }
        composable(
            route = BottomBarScreen.PayFees.route
        ) {
            StudentPayFees(navController = navController)
        }
        composable(
            route = BottomBarScreen.TakeLoan.route
        ) {
            StudentTakeLoan(navController = navController)
        }
        composable(
            route = BottomBarScreen.PayDues.route
        ) {
            StudentPayDues(navController = navController)
        }
        composable(
            route = BottomBarScreen.Savings.route
        ) {
            StudentSavings(navController = navController)
        }
        composable(
            Screen.FeesSemester.route,
            arguments = listOf(
                navArgument(name = "level") { type = NavType.StringType },
                navArgument(name = "semester") { type = NavType.StringType }
            ),
        ) {
            val level = it.arguments?.getString("level")
            val semester = it.arguments?.getString("semester")
            FeesPaymentScreen(
                level = level!!, semester = semester!!,
                onBack = { navController.popBackStack() },
                //onBackRequested = onBackRequested,

            )
        }
        composable(
            Screen.DuesSemesterScreen.route,
            arguments = listOf(
                navArgument(name = "level") { type = NavType.StringType },
                navArgument(name = "semester") { type = NavType.StringType }
            ),
        ) {
            val level = it.arguments?.getString("level")
            val semester = it.arguments?.getString("semester")
            DuesPaymentScreen(
                level = level!!, semester = semester!!,
                onBack = { navController.popBackStack() },
                onPay = {}
                //onBackRequested = onBackRequested,

            )
        }
    }

}

@Composable
fun BankerBottomNavigationGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = BankerBottomBarScreen.Accounts.route) {
        composable(
            route = BankerBottomBarScreen.Accounts.route
        ) {
            BankerAccountsScreen(navController = navController)
        }
        composable(
            route = BankerBottomBarScreen.Fees.route
        ) {
            BankerFeesScreen(navController = navController)
        }
        composable(
            route = BankerBottomBarScreen.Loans.route
        ) {
            BankerLoansScreen(navController = navController)
        }
        composable(
            route = BankerBottomBarScreen.Dues.route
        ) {
            BankerDuesScreen(navController = navController)
        }
        composable(
            route = BankerBottomBarScreen.Savings.route
        ) {
            BankerSavingsScreen(navController = navController)
        }
        composable(
            Screen.FeesSemester.route,
            arguments = listOf(
                navArgument(name = "level") { type = NavType.StringType },
                navArgument(name = "semester") { type = NavType.StringType }
            ),
        ) {
            val level = it.arguments?.getString("level")
            val semester = it.arguments?.getString("semester")
            FeesPaymentScreen(
                level = level!!, semester = semester!!,
                onBack = { navController.popBackStack() },
                //onBackRequested = onBackRequested,

            )
        }
        composable(
            Screen.DuesSemesterScreen.route,
            arguments = listOf(
                navArgument(name = "level") { type = NavType.StringType },
                navArgument(name = "semester") { type = NavType.StringType }
            ),
        ) {
            val level = it.arguments?.getString("level")
            val semester = it.arguments?.getString("semester")
            DuesPaymentScreen(
                level = level!!, semester = semester!!,
                onBack = { navController.popBackStack() },
                onPay = {}
                //onBackRequested = onBackRequested,

            )
        }
    }

}