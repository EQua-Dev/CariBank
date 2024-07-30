package com.schoolprojects.caribank.screens.banker.fees

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.ui.theme.Typography

@Composable
fun BankerFeesScreen(modifier: Modifier = Modifier, navController: NavHostController) {

    Box(contentAlignment = Alignment.Center) {
        Text(text = "Banker Fees Screen", style = Typography.titleMedium)
    }
}