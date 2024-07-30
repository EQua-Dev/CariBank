package com.schoolprojects.caribank.screens.student.payfees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.schoolprojects.caribank.models.Fee
import com.schoolprojects.caribank.ui.theme.Typography

@Composable
fun FeesPaymentScreen(level: String, semester: String, onBack: () -> Unit, onPay: () -> Unit) {


    val feeWithMultipleItems = Fee(
        feeId = "1",
        feeName = "Comprehensive Fee",
        feeTotalAmount = 113000.0,
        feeDescription = "Comprehensive fee covering various services",
        feeLevel = "100 Level",
        feeSemester = "1st Semester",
        feeItems = mapOf(
            "Tuition" to 50000.0,
            "Hostel" to 20000.0,
            "Library" to 5000.0,
            "Laboratory" to 10000.0,
            "Examination" to 15000.0,
            "Sports" to 3000.0,
            "Development" to 8000.0,
            "Graduation" to 25000.0
        ),
        feeStatus = "Pending"
    )
    val amountPaid = remember { mutableStateOf(0.0) }
    val amountRemaining = feeWithMultipleItems.feeTotalAmount - amountPaid.value

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Column {
                Text(text = level, style = Typography.bodyLarge)
                Text(text = semester, style = Typography.bodyLarge)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Total: ${feeWithMultipleItems.feeTotalAmount}", style = Typography.bodyLarge)
                Text(text = "Paid: $${amountPaid.value}", style = Typography.bodyLarge)
                Text(text = "Remaining: $${amountRemaining}", style = Typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(feeWithMultipleItems.feeItems.toList()) { (item, amount) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item, style = Typography.bodyMedium)
                    Text(text = "$$amount", style = Typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onPay() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Pay")
        }
    }
}