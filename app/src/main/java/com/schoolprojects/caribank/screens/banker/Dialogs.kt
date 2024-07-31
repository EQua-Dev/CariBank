package com.schoolprojects.caribank.screens.banker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.schoolprojects.caribank.models.Loan
import com.schoolprojects.caribank.viewmodels.BankerHomeViewModel

@Composable
fun ShowLoanApprovalDialog(loan: Loan, bankerHomeViewModel: BankerHomeViewModel) {
    val student by bankerHomeViewModel.getAccountOwner(loan.studentId)
        .collectAsState(initial = null)
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Loan Approval") },
            text = {
                Column {
                    Text(text = "Loan Amount: ₦${String.format("%,.2f", loan.loanAmount)}")
                    Text(text = "Loan Term: ${loan.loanTerm} days")
                    Text(text = "Description: ${loan.loanDescription}")
                    Spacer(modifier = Modifier.height(16.dp))
                    student?.let {
                        val name = "${it.studentFirstName} ${it.studentLastName}"
                        Text(text = "Account Owner: $name")
                        Text(text = "Owner Reg No: ${it.studentRegNo}")
                        Text(text = "Owner Level: ${it.studentCurrentLevel}")
                        Text(text = "Owner Department: ${it.studentDepartment}")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    bankerHomeViewModel.approveLoan(loan)
                    openDialog.value = false
                }) {
                    Text("Approve")
                }
            },
            dismissButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}