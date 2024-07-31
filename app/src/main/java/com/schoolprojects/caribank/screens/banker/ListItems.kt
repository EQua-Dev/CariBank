package com.schoolprojects.caribank.screens.banker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.schoolprojects.caribank.models.Loan

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
