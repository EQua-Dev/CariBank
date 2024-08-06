package com.schoolprojects.caribank.screens.banker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schoolprojects.caribank.models.DueWithStudent
import com.schoolprojects.caribank.models.FeeWithStudent
import com.schoolprojects.caribank.models.Loan
import com.schoolprojects.caribank.models.Savings
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.viewmodels.BankerHomeViewModel

@Composable
fun LoanItem(loan: Loan, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Loan Amount: ₦${String.format("%,.2f", loan.loanAmount)}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Loan Term: ${loan.loanTerm} days", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Status: ${loan.loanStatus.capitalize()}", style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.Default.Info, contentDescription = "Loan Details", tint = Color.Gray)
        }
    }
}

@Composable
fun FeesList(
    fees: List<FeeWithStudent>,
    onVerifyFee: (FeeWithStudent) -> Unit
) {
        // Group fees by their description (level or other criteria)
        val groupedFees = fees.groupBy { it.paidFee.feeDescription }

        LazyColumn {
            // Iterate over map entries
            groupedFees.forEach { (level, feesInLevel) ->
                // Header for each group
                item {
                    Text(
                        text = level,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                // Iterate over the list of fees for each group
                items(feesInLevel) { feeWithStudent ->
                    FeeItem(
                        feeWithStudent = feeWithStudent,
                        onVerify = { onVerifyFee(feeWithStudent) }
                    )
                }
            }
        }
}

@Composable
fun FeeItem(
    feeWithStudent: FeeWithStudent,
    onVerify: () -> Unit
) {
    val (fee, student) = feeWithStudent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Student Name: ${student.studentFirstName} ${student.studentLastName}", fontSize = 18.sp)
            Text(text = "Student Reg No: ${student.studentRegNo}")
            Text(text = "Department: ${student.studentDepartment}")
            Text(text = "Current Level: ${student.studentCurrentLevel}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Payment Ref: ${fee.paymentRef}")
            Text(text = "Amount: ${fee.amountPaid}")
            Text(text = "Date Paid: ${fee.datePaid}")
            Text(text = "Verification Status: ${if (fee.isVerified) "Verified" else "Not Verified"}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onVerify,
                enabled = !fee.isVerified
            ) {
                Text("Verify Payment")
            }
        }
    }
}


@Composable
fun DuesList(
    dues: List<DueWithStudent>,
    onVerifyDue: (DueWithStudent) -> Unit
) {
    // Group fees by their description (level or other criteria)
    val groupedDues = dues.groupBy { it.paidDue.dueDescription }

    LazyColumn {
        // Iterate over map entries
        groupedDues.forEach { (level, duesInLevel) ->
            // Header for each group
            item {
                Text(
                    text = level,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
            // Iterate over the list of fees for each group
            items(duesInLevel) { dueWithStudent ->
                DueItem(
                    dueWithStudent = dueWithStudent,
                    onVerify = { onVerifyDue(dueWithStudent) }
                )
            }
        }
    }
}

@Composable
fun DueItem(
    dueWithStudent: DueWithStudent,
    onVerify: () -> Unit
) {
    val (due, student) = dueWithStudent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Student Name: ${student.studentFirstName} ${student.studentLastName}", fontSize = 18.sp)
            Text(text = "Student Reg No: ${student.studentRegNo}")
            Text(text = "Department: ${student.studentDepartment}")
            Text(text = "Current Level: ${student.studentCurrentLevel}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Payment Ref: ${due.paymentRef}")
            Text(text = "Amount: ${due.amountPaid}")
            Text(text = "Date Paid: ${due.datePaid}")
            Text(text = "Verification Status: ${if (due.isVerified) "Verified" else "Not Verified"}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onVerify,
                enabled = !due.isVerified
            ) {
                Text("Verify Payment")
            }
        }
    }
}


@Composable
fun SavingItem(saving: Savings, viewModel: BankerHomeViewModel) {
    val withdrawAmount = viewModel.calculateWithdrawAmount(saving)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = saving.savingsTitle,
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Savings Amount: ${saving.savingsAmount}",
                    style = Typography.bodyMedium
                )
                Text(
                    text = "Due Date: ${saving.dueDate}",
                    style = Typography.bodyMedium
                )
                Text(
                    text = "Withdraw Amount: ₦$withdrawAmount",
                    style = Typography.bodyMedium
                )
            }
        }
    }
}

