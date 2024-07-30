package com.schoolprojects.caribank.screens.student.payfees

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.navigation.Screen
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.viewmodels.StudentHomeViewModel

@Composable
fun StudentPayFees(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    studentHomeViewModel: StudentHomeViewModel = hiltViewModel()
) {
    val studentData by remember { studentHomeViewModel.studentInfo }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Department: ${studentData.studentDepartment}",
            style = Typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        val levels = listOf("100 Level", "200 Level", "300 Level", "400 Level")

        levels.forEach { level ->
            ExpandableCard(level) { expanded ->
                if (expanded) {
                    Column {
                        Button(
                            onClick = {
//                                navController.navigate(Screen.Login)
                                navigateToPayFees(navController, level, "1st Semester")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "1st Semester")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                //navigateToPayFees(navController, level, "2nd Semester")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "2nd Semester")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ExpandableCard(title: String, content: @Composable (Boolean) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable { expanded = !expanded }
            )
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                content(expanded)
            }
        }
    }
}

fun navigateToPayFees(navController: NavHostController, level: String, semester: String) {
    // Capture the selected level and semester and navigate to the new screen
    val route = "pay_fees_detail/$level/$semester"
    navController.navigate(
        Screen.FeesSemesterScreen.route.replace("{level}", level)
            .replace("{semester}", semester)
    )
    //navController.navigate(route)
}