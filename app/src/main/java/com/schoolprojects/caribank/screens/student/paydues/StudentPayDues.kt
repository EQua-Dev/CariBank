package com.schoolprojects.caribank.screens.student.paydues

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.navigation.Screen
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.utils.getDate
import com.schoolprojects.caribank.viewmodels.StudentHomeViewModel

@Composable
fun StudentPayDues(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    studentHomeViewModel: StudentHomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val studentData by remember { studentHomeViewModel.studentInfo }.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var paymentRef by remember { mutableStateOf("") }
    val openSearchDialog = remember { studentHomeViewModel.openSearchDialog }
    val matchingDue by remember { studentHomeViewModel.matchingDue }.collectAsState()
    val schoolDue by remember { studentHomeViewModel.schoolDue }.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Department: ${studentData?.studentDepartment}",
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
                                navigateToPayFees(navController, level, "2nd Semester")
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
        // Floating Action Button for searching payment reference
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
                Icon(Icons.Default.Search, contentDescription = "Search Payment Ref")
                Text(text = "Search Fees")
            }

        }

        // Dialog for entering payment reference
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Enter Payment Reference") },
                text = {
                    TextField(
                        value = paymentRef,
                        onValueChange = { paymentRef = it },
                        label = { Text("Payment Reference") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            studentHomeViewModel.searchDueByPaymentRef(paymentRef, onError = {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            })
                            showDialog = false
                        }
                    ) {
                        Text("Search")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Dialog for displaying matching fee details
        matchingDue?.let { paidDue ->
            AlertDialog(
                onDismissRequest = { studentHomeViewModel.clearMatchingFee() },
                title = { Text("Due Details") },
                text = {
                    Column {
                        Row {
                            Text("Due Status: ", fontWeight = FontWeight.Bold)
                            Text(
                                if (paidDue.amountPaid != schoolDue?.dueTotalAmount) {
                                    "part payment"
                                } else {
                                    "complete payment"
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text("Payment Ref: ", fontWeight = FontWeight.Bold)
                            Text(paidDue.paymentRef)
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        Row {
                            Text("Amount Paid: ", fontWeight = FontWeight.Bold)
                            Text("₦${paidDue.amountPaid}")
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        Row {
                            Text("Amount Remaining: ", fontWeight = FontWeight.Bold)
                            Text("₦${schoolDue?.dueTotalAmount!!.minus(paidDue.amountPaid)}")
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        Row {
                            Text("Date Paid: ", fontWeight = FontWeight.Bold)
                            Text(getDate(paidDue.datePaid.toLong(), "EEE, dd MMM yyyy"))
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        Row {
                            Text("Due Description: ", fontWeight = FontWeight.Bold)
                            Text(paidDue.dueDescription)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row {
                            Text("Items Paid: ", fontWeight = FontWeight.Bold)
                            Text(paidDue.itemsPaid.joinToString(", "))
                        }

                    }
                },
                confirmButton = {
                    Button(
                        onClick = { studentHomeViewModel.clearMatchingDue() }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun ExpandableCard(title: String, content: @Composable (Boolean) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
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
        Screen.DuesSemesterScreen.route.replace("{level}", level)
            .replace("{semester}", semester)
    )
    //navController.navigate(route)
}