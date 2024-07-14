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
import com.schoolprojects.caribank.screens.student.paydues.StudentPayDues
import com.schoolprojects.caribank.screens.student.payfees.StudentPayFees
import com.schoolprojects.caribank.screens.student.savings.StudentSavings
import com.schoolprojects.caribank.screens.student.takeloan.StudentTakeLoan
import com.schoolprojects.caribank.screens.student.transactions.StudentTransactions
import com.schoolprojects.caribank.screens.student.SemesterScreen

@Composable
fun StudentBottomNavigationGraph(navController: NavHostController) {
//    NavHost(navController = navController, startDestination = Screen.SupervisorHouses.route) {
//        composable(
//            route = Screen.SupervisorHouses.route
//        ) {
//            SupervisorHouses(navController = navController)
//        }
//        composable(
//            route = Screen.SupervisorStaff.route
//        ) {
//            SupervisorStaff(navController = navController)
//        }
//        composable(
//            route = Screen.SupervisorShifts.route
//        ) {
//            SupervisorShifts(navController = navController)
//        }
//        composable(
//            route = Screen.SupervisorProfile.route
//        ) {
//            SupervisorProfile(navController = navController)
//        }
//    }
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
            Screen.FeesSemesterScreen.route,
            arguments = listOf(
                navArgument(name = "level") { type = NavType.StringType },
                navArgument(name = "semester") { type = NavType.StringType }
            ),
        ) {
            val level = it.arguments?.getString("level")
            val semester = it.arguments?.getString("semester")
            SemesterScreen(
                level = level!!, semester = semester!!,
                onBack = { navController.popBackStack() },
                onPay = {}
                //onBackRequested = onBackRequested,

            )
        }
    }

}