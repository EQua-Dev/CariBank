package com.schoolprojects.caribank.screens.banker.savings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.models.Savings
import com.schoolprojects.caribank.screens.banker.SavingItem
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.viewmodels.BankerHomeViewModel

@Composable
fun BankerSavingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bankerHomeViewModel: BankerHomeViewModel = hiltViewModel()
) {

    val savingsList by bankerHomeViewModel.savingsList.collectAsState()
    val savingsWithClosestDueDate = bankerHomeViewModel.getSavingsWithClosestDueDate()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Card for Savings with Closest Withdraw Date
        savingsWithClosestDueDate?.let { closestSaving ->
            ClosestWithdrawDateCard(saving = closestSaving, viewModel = bankerHomeViewModel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Savings List
        LazyColumn {
            items(savingsList) { saving ->
                SavingItem(saving = saving, viewModel = bankerHomeViewModel)
            }
        }
    }
}

@Composable
fun ClosestWithdrawDateCard(saving: Savings, viewModel: BankerHomeViewModel) {
    val withdrawAmount = viewModel.calculateWithdrawAmount(saving)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Upcoming Withdrawal",
                style = Typography.headlineMedium,
                color = Color.White
            )
            Text(
                text = saving.savingsTitle,
                style = Typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = "Due Date: ${saving.dueDate}",
                style = Typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = "Withdraw Amount: ₦$withdrawAmount",
                style = Typography.bodyMedium,
                color = Color.White
            )
        }
    }


}