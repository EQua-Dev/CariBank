package com.schoolprojects.caribank.screens.banker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.schoolprojects.caribank.navigation.BankerBottomBar
import com.schoolprojects.caribank.navigation.BankerBottomNavigationGraph
import com.schoolprojects.caribank.navigation.Screen
import com.schoolprojects.caribank.navigation.StudentBottomBar
import com.schoolprojects.caribank.navigation.StudentBottomNavigationGraph
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.utils.Common
import com.schoolprojects.caribank.viewmodels.BankerHomeViewModel

@Composable
fun BankerHomeScreen(
    modifier: Modifier = Modifier,

    baseNavHostController: NavHostController,
    onNavigationRequested: (String, Boolean) -> Unit,
    bankerHomeViewModel: BankerHomeViewModel = hiltViewModel()
) {

    val navController = rememberNavController()


    val studentData by remember {
        bankerHomeViewModel.studentInfo
    }.collectAsState()
    val errorMessage = remember { mutableStateOf("") }
    val showLoading by remember { mutableStateOf(bankerHomeViewModel.showLoading) }
    val openDialog by remember { mutableStateOf(bankerHomeViewModel.openDialog) }



    LaunchedEffect(key1 = null) {
        /* getStudentInfo(
            mAuth.uid!!,
            onLoading = {
                studentHomeViewModel.updateLoadingStatus(it)
            },
            onStudentDataFetched = { student ->
                studentHomeViewModel.updateStudentInfo(student)
            },
            onStudentNotFetched = { error ->
                errorMessage.value = error
            })*/
    }

    Scaffold(
        bottomBar = {
            BankerBottomBar(navController = navController)
        }, topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hello, Admin",
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(4.dp),
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            bankerHomeViewModel.updateDialogStatus()
                        })
                }
            }

        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                innerPadding
            )
        ) {
            BankerBottomNavigationGraph(navController = navController)
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            title = {
                Text(text = "Logout", style = Typography.titleLarge)
            },
            text = {
                Text(text = "Do you want to logout?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        Common.mAuth.signOut()
                        baseNavHostController.navigate(Screen.Login.route)
                        bankerHomeViewModel.updateDialogStatus()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        bankerHomeViewModel.updateDialogStatus()
                    }
                ) {
                    Text("No")
                }
            },

            )
    }

    if (showLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))

        }
    }


}