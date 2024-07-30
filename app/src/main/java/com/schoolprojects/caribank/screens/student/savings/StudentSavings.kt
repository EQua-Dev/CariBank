package com.schoolprojects.caribank.screens.student.savings

import android.app.DatePickerDialog
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.models.Savings
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.utils.calculateInterest
import com.schoolprojects.caribank.utils.calculateInterestRate
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit

@Composable
fun StudentSavings(modifier: Modifier = Modifier, navController: NavHostController) {

    val showDialog = remember { mutableStateOf(false) }


    val dummySavingsList = mutableListOf(
        Savings(
            savingsId = "1",
            accountId = "A001",
            dateCreated = "2024-01-15",
            savingsAmount = 10000.0,
            interestRate = 5.0,
            dueDate = "2024-06-15",
            savingsTitle = "Holiday Fund",
            savingsDescription = "Savings for holiday travel",
            savingsStatus = "Completed"
        ),
        Savings(
            savingsId = "2",
            accountId = "A002",
            dateCreated = "2024-02-01",
            savingsAmount = 15000.0,
            interestRate = 4.5,
            dueDate = "2024-07-01",
            savingsTitle = "Emergency Fund",
            savingsDescription = "Savings for emergencies",
            savingsStatus = "Active"
        ),
        Savings(
            savingsId = "3",
            accountId = "A003",
            dateCreated = "2024-03-10",
            savingsAmount = 5000.0,
            interestRate = 3.0,
            dueDate = "2024-08-10",
            savingsTitle = "Education Fund",
            savingsDescription = "Savings for education",
            savingsStatus = "Active"
        ),
        Savings(
            savingsId = "4",
            accountId = "A004",
            dateCreated = "2024-04-05",
            savingsAmount = 20000.0,
            interestRate = 6.0,
            dueDate = "2024-09-05",
            savingsTitle = "New Car Fund",
            savingsDescription = "Savings for buying a new car",
            savingsStatus = "Completed"
        ),
        Savings(
            savingsId = "5",
            accountId = "A005",
            dateCreated = "2024-05-20",
            savingsAmount = 25000.0,
            interestRate = 5.5,
            dueDate = "2024-10-20",
            savingsTitle = "House Renovation",
            savingsDescription = "Savings for house renovation",
            savingsStatus = "Active"
        )
    )

    // Calculate total amount made from interests
    val totalAmountMade = remember {
        mutableStateOf(dummySavingsList.sumOf { it.savingsAmount * it.interestRate / 100 })
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Card with Title and Create Savings Button
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Savings",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Button(
                        onClick = { showDialog.value = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Create Savings")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Total Amount Made: ₦${DecimalFormat("#,###.00").format(totalAmountMade.value)}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // History Title
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // List of Savings History
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(dummySavingsList) { savings ->
                SavingsItem(savings = savings)
            }
        }
    }
    // Call dialog component with the state and callback
    CreateSavingsDialog(showDialog = showDialog, onCreateSavings = { newSavings ->
        // Update savings list (dummy update, replace with actual logic)
        dummySavingsList.add(newSavings)
        totalAmountMade.value = dummySavingsList.sumOf { it.savingsAmount * it.interestRate / 100 }
    })
}

