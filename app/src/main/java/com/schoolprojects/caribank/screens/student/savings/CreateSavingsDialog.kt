package com.schoolprojects.caribank.screens.student.savings

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.schoolprojects.caribank.models.Account
import com.schoolprojects.caribank.models.Savings
import com.schoolprojects.caribank.utils.calculateInterest
import com.schoolprojects.caribank.utils.calculateInterestRate
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID


@Composable
fun CreateSavingsDialog(
    showDialog: MutableState<Boolean>,
    accountDetails: Account?,
    onCreateSavings: (Savings) -> Unit
) {
    val savingsTitle = remember { mutableStateOf("") }
    val savingsAmount = remember { mutableStateOf("") }
    val withdrawDate = remember { mutableStateOf("") }
    val calculatedAmount = remember { mutableStateOf(0.0) }

    // Date picker for withdrawal date
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            withdrawDate.value = "$year-${month + 1}-$dayOfMonth"
            calculateInterest(
                savingsAmount.value.toDoubleOrNull() ?: 0.0,
                withdrawDate.value
            )?.let { calculatedAmount.value = it }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        val newSavings = Savings(
                            savingsId = UUID.randomUUID().toString(),
                            accountId = accountDetails?.accountId!!,
                            dateCreated = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(
                                Date()
                            ),
                            savingsAmount = savingsAmount.value.toDoubleOrNull() ?: 0.0,
                            interestRate = calculateInterestRate(
                                savingsAmount.value.toDoubleOrNull() ?: 0.0,
                                withdrawDate.value
                            ),
                            dueDate = withdrawDate.value,
                            savingsTitle = savingsTitle.value,
                            savingsDescription = "Custom savings created for ${savingsTitle.value}",
                            savingsStatus = "Active"
                        )
                        onCreateSavings(newSavings)
                        showDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = "Create New Savings") },
            text = {
                Column {
                    Text(
                        text = "Withdraw Amount: ₦${
                            DecimalFormat("#,###.00").format(
                                calculatedAmount.value
                            )
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = savingsTitle.value,
                        onValueChange = { savingsTitle.value = it },
                        label = { Text("Savings Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = savingsAmount.value,
                        onValueChange = { newAmount ->
                            savingsAmount.value = newAmount
                            calculateInterest(
                                newAmount.toDoubleOrNull() ?: 0.0,
                                withdrawDate.value
                            )?.let { calculatedAmount.value = it }
                        },
                        label = { Text("Savings Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = withdrawDate.value,
                        onValueChange = { withdrawDate.value = it },
                        label = { Text("Withdraw Date") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                                datePickerDialog.show()
                            }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                            }
                        }
                    )
                }
            }
        )
    }
}

