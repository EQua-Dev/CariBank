package com.schoolprojects.caribank.screens.student.takeloan

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.models.Loan
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.utils.Common.mAuth
import com.schoolprojects.caribank.utils.HelpMe.isCreditScoreEligible
import com.schoolprojects.caribank.utils.calculateDaysDifference
import com.schoolprojects.caribank.utils.getDate
import com.schoolprojects.caribank.viewmodels.StudentHomeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun StudentTakeLoan(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    studentHomeViewModel: StudentHomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val studentData by remember { studentHomeViewModel.studentInfo }.collectAsState()
    val accountData by remember { studentHomeViewModel.accountInfo }.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var loanAmount by remember { mutableStateOf("") }
    var loanReason by remember { mutableStateOf("") }
    var paybackDate by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var showCreditInfoDialog by remember { mutableStateOf(false) }

    val loanHistory by studentHomeViewModel.loanHistory.collectAsState(initial = emptyList())


    if (showCreditInfoDialog) {
        AlertDialog(
            onDismissRequest = { showCreditInfoDialog = false },
            confirmButton = {
                TextButton(onClick = { showCreditInfoDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text(text = "Credit Score Information") },
            text = {
                Text(
                    text = """
                    Your credit score is determined by factors such as payment history, outstanding debts, and the number of open accounts. A higher credit score indicates better creditworthiness and may improve your chances of loan approval.
                """.trimIndent()
                )
            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Account Balance Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Account Balance: ₦${
                            String.format(
                                "%.2f",
                                accountData?.accountBalance
                            )
                        }",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(onClick = { showDialog = true }) {
                        Text(text = "Apply")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Credit Score: ${accountData?.loanCreditScore}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = { showCreditInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Loan History Heading
        Text(
            text = "Loan History",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Loan History List
        if (loanHistory.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No loans records",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn {
                items(loanHistory) { loan ->
                    LoanHistoryItem(loan = loan)
                }
            }
        }

    }
    if (showDialog) {
        ApplyLoanDialog(
            onDismiss = { showDialog = false },
            onApply = { amount, reason, date ->
                val today = Calendar.getInstance()
                val daysDifference = calculateDaysDifference(today, selectedDate)

                if (isCreditScoreEligible(
                        amount.toDouble(),
                        accountData?.loanCreditScore?.toDouble() ?: 0.0
                    )
                ) {

                    val loan = Loan(
                        loanId = UUID.randomUUID().toString(),
                        loanAmount = amount.toDouble(),
                        loanTerm = daysDifference, // Assuming a 12-month term for now
                        loanStatus = "pending",
                        studentId = mAuth.uid.toString(), // Replace with actual student ID
                        loanDescription = reason,
                        dateCreated = System.currentTimeMillis().toString(),
                        dateApproved = "",
                        datePaidBack = "",
                        payBackDate = selectedDate.timeInMillis.toString()
                    )
                    studentHomeViewModel.applyForLoan(loan)

                    //onApplyLoan(loan)
                    showDialog = false
                } else {
                    Toast.makeText(
                        context,
                        "Insufficient credit score for this loan amount",
                        Toast.LENGTH_LONG
                    ).show()

                }
            },
            loanAmount = loanAmount,
            onLoanAmountChange = { loanAmount = it },
            loanReason = loanReason,
            onLoanReasonChange = { loanReason = it },
            paybackDate = paybackDate,
            onPaybackDateChange = { paybackDate = it },
            selectedDate = selectedDate,
            onDateSelected = { date, formattedDate ->
                selectedDate = date
                paybackDate = formattedDate
            }
        )
    }
}

@Composable
fun LoanHistoryItem(loan: Loan) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFCBD0D3))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Date Applied: ${getDate(loan.dateCreated.toLong(), "EEE, dd MMM yyyy")}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Date Approved: ${
                    if (loan.dateApproved.isNotEmpty()) {
                        getDate(
                            loan.dateApproved.toLong(),
                            "EEE, dd MMM yyyy"
                        )
                    } else {
                        "Pending"
                    }
                }",
                style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Status: ${loan.loanStatus}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Amount: ₦${String.format("%.2f", loan.loanAmount)}",
                style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun ApplyLoanDialog(
    onDismiss: () -> Unit,
    onApply: (String, String, String) -> Unit,
    loanAmount: String,
    onLoanAmountChange: (String) -> Unit,
    loanReason: String,
    onLoanReasonChange: (String) -> Unit,
    paybackDate: String,
    onPaybackDateChange: (String) -> Unit,
    selectedDate: Calendar,
    onDateSelected: (Calendar, String) -> Unit
) {

    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onApply(loanAmount, loanReason, paybackDate) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(text = "Apply for Loan") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = loanAmount,
                    onValueChange = onLoanAmountChange,
                    label = { Text("Loan Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = loanReason,
                    onValueChange = onLoanReasonChange,
                    label = { Text("Reason for Loan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = paybackDate,
                    onValueChange = onPaybackDateChange,
                    label = { Text("Payback Date") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                Button(
                    onClick = {
                        showDatePicker(context, selectedDate) { date, formattedDate ->
                            onDateSelected(date, formattedDate)
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Select Date")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun showDatePicker(
    context: Context,
    initialDate: Calendar,
    onDateSelected: (Calendar, String) -> Unit
) {
    val year = initialDate.get(Calendar.YEAR)
    val month = initialDate.get(Calendar.MONTH)
    val day = initialDate.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val formattedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
            onDateSelected(selectedDate, formattedDate)

        }, year, month, day

    )
    datePickerDialog.datePicker.minDate = initialDate.timeInMillis


    datePickerDialog.show()
}
