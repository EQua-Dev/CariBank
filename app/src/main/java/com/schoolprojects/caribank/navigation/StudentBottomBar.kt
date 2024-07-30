package com.schoolprojects.caribank.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun StudentBottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Transactions,
        BottomBarScreen.PayFees,
        BottomBarScreen.TakeLoan,
        BottomBarScreen.PayDues,
        BottomBarScreen.Savings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            StudentAddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )

        }
    }

}


@Composable
fun RowScope.StudentAddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        label = {
            Text(text = screen.title)
        },
        onClick = { navController.navigate(screen.route) },
        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) })
}


@Composable
fun BankerBottomBar(navController: NavHostController) {
    val screens = listOf(
        BankerBottomBarScreen.Accounts,
        BankerBottomBarScreen.Fees,
        BankerBottomBarScreen.Loans,
        BankerBottomBarScreen.Dues,
        BankerBottomBarScreen.Savings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            BankerAddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )

        }
    }

}


@Composable
fun RowScope.BankerAddItem(
    screen: BankerBottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        label = {
            Text(text = screen.title)
        },
        onClick = { navController.navigate(screen.route) },
        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) })
}