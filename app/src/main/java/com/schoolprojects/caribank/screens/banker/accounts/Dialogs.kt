package com.schoolprojects.caribank.screens.banker.accounts

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.schoolprojects.caribank.models.Account
import com.schoolprojects.caribank.models.Student
import com.schoolprojects.caribank.utils.getDate
import com.schoolprojects.caribank.viewmodels.BankerHomeViewModel

@Composable
fun AccountDetailsDialog(account: Account, accountOwner: Student?, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Account Details") },
        text = {
            Column {
                Text(text = "Account Number: ${account.accountNumber}")
                Text(text = "Account Balance: ₦${account.accountBalance}")
                Text(
                    text = "Date Created: ${
                        getDate(
                            account.dateCreated.toLong(),
                            "EEE dd MMM yyyy"
                        )
                    }"
                )
                Text(text = "Account Status: ${account.accountStatus}")

                Spacer(modifier = Modifier.height(16.dp))

                accountOwner?.let {
                    Text(text = "Owner Name: ${it.studentFirstName} ${it.studentLastName}")
                    Text(text = "Registration No: ${it.studentRegNo}")
                    Text(text = "Email: ${it.studentEmail}")
                    Text(text = "Gender: ${it.studentGender}")
                    Text(text = "Department: ${it.studentDepartment}")
                    Text(text = "Current Level: ${it.studentCurrentLevel}")
                    Text(text = "Credit Score: ${it.loanCreditScore}")
                } ?: Text("Owner details are not available.")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Close")
            }
        }
    )
}


@Composable
fun TopUpDialog(account: Account, viewModel: BankerHomeViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val topUpAmount = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Top Up Account") },
        text = {
            Column {
                TextField(
                    value = topUpAmount.value,
                    onValueChange = { topUpAmount.value = it },
                    label = { Text("Amount to Top Up") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = topUpAmount.value.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        viewModel.topUpAccount(account.accountId, amount)
                        Toast.makeText(
                            context,
                            "Account topped up successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Please enter a valid amount.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            ) {
                Text("Top Up")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


