package com.schoolprojects.caribank.screens.banker.dues

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.ui.theme.Typography

@Composable
fun BankerDuesScreen(modifier: Modifier = Modifier,  navController: NavHostController) {

    Box(contentAlignment = Alignment.Center) {
        Text(text = "Banker Dues Screen", style = Typography.titleMedium)
    }

}