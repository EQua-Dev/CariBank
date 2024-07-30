package com.schoolprojects.caribank.screens.student.paydues

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schoolprojects.caribank.models.Dues
import com.schoolprojects.caribank.models.Fee
import com.schoolprojects.caribank.ui.theme.Typography
import java.text.DecimalFormat

@Composable
fun DuesPaymentScreen(level: String, semester: String, onBack: () -> Unit, onPay: () -> Unit) {

    val dummyDuesList = listOf(
        Dues(
            duesId = "1",
            duesName = "Library Fee",
            duesTotalAmount = 5000.0,
            duesDescription = "Fee for library services",
            duesLevel = "100 Level",
            duesSemester = "1st Semester",
            duesTitle = "Library",
            duesStatus = "Pending",
        ),
        Dues(
            duesId = "2",
            duesName = "Sports Fee",
            duesTotalAmount = 3000.0,
            duesDescription = "Fee for sports facilities",
            duesLevel = "100 Level",
            duesSemester = "1st Semester",
            duesTitle = "Sports",
            duesStatus = "Pending",
        ),
        Dues(
            duesId = "3",
            duesName = "Lab Fee",
            duesTotalAmount = 8000.0,
            duesDescription = "Fee for laboratory use",
            duesLevel = "100 Level",
            duesSemester = "1st Semester",
            duesTitle = "Laboratory",
            duesStatus = "Pending",
        )
    )

    val duesList = remember { mutableStateOf(dummyDuesList) }

    val selectedDues = remember { mutableStateListOf<Dues>() }
    val totalSelectedAmount = remember { mutableStateOf(0.0) }

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
                Text(
                    text = "Total Selected: $${DecimalFormat("#,###.00").format(totalSelectedAmount.value)}",
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(duesList.value) { due ->
                DuesItem(
                    dues = due,
                    isSelected = selectedDues.contains(due),
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            selectedDues.add(due)
                            totalSelectedAmount.value += due.duesTotalAmount
                        } else {
                            selectedDues.remove(due)
                            totalSelectedAmount.value -= due.duesTotalAmount
                        }
                    }
                )
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


@Composable
fun DuesItem(
    dues: Dues,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = dues.duesName,
            style = Typography.bodyMedium,
            fontSize = 16.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "$${DecimalFormat("#,###.00").format(dues.duesTotalAmount)}",
                style = Typography.bodyMedium,
                fontSize = 16.sp
            )
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onCheckedChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}