package com.schoolprojects.caribank.screens.student.savings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.schoolprojects.caribank.models.Savings
import java.text.DecimalFormat


@Composable
fun SavingsItem(savings: Savings) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = savings.savingsTitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Date Created: ${savings.dateCreated}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Amount: ₦${DecimalFormat("#,###.00").format(savings.savingsAmount)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Interest Rate: ${savings.interestRate}%",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Due Date: ${savings.dueDate}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = "Status: ${savings.savingsStatus}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = when (savings.savingsStatus) {
                    "Completed" -> Color(0xFF388E3C)
                    "Active" -> MaterialTheme.colorScheme.primary
                    else -> Color.Gray
                },
                modifier = Modifier.padding(4.dp)
            )
        }
    }

}