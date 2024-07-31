package com.schoolprojects.caribank.screens.student.transactions

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ApplyLoanDialog(
    onDismiss: () -> Unit,
    onApply: (String, String, Calendar) -> Unit,
    loanAmount: String,
    onLoanAmountChange: (String) -> Unit,
    loanReason: String,
    onLoanReasonChange: (String) -> Unit,
    paybackDate: String,
    onPaybackDateChange: (String) -> Unit,
    selectedDate: Calendar,
    onDateSelected: (Calendar, String) -> Unit
) {
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            val calendar = Calendar.getInstance().apply {
                set(year, month, day)
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            onDateSelected(calendar, dateFormat.format(calendar.time))
        },
        selectedDate.get(Calendar.YEAR),
        selectedDate.get(Calendar.MONTH),
        selectedDate.get(Calendar.DAY_OF_MONTH)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onApply(loanAmount, loanReason, selectedDate) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(text = "Loan Application") },
        text = {
            Column {
                OutlinedTextField(
                    value = loanAmount,
                    onValueChange = onLoanAmountChange,
                    label = { Text("Loan Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = loanReason,
                    onValueChange = onLoanReasonChange,
                    label = { Text("Reason for Loan") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = paybackDate,
                    onValueChange = onPaybackDateChange,
                    label = { Text("Payback Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Select Date"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
