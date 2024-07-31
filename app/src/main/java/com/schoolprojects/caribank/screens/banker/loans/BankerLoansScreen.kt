package com.schoolprojects.caribank.screens.banker.loans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.models.Loan
import com.schoolprojects.caribank.screens.banker.LoanItem
import com.schoolprojects.caribank.screens.banker.ShowLoanApprovalDialog
import com.schoolprojects.caribank.viewmodels.BankerHomeViewModel
import java.util.Locale

@Composable
fun BankerLoansScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bankerHomeViewModel: BankerHomeViewModel = hiltViewModel()
) {
    // State for Tab and ViewModel Observables
    var selectedTab by remember { mutableStateOf(0) }
    val approvedLoans by bankerHomeViewModel.approvedLoans.collectAsState()
    val pendingLoans by bankerHomeViewModel.pendingLoans.collectAsState()
    val overdueLoans by bankerHomeViewModel.overdueLoans.collectAsState()
    val totalMoney by bankerHomeViewModel.totalMoney.collectAsState()
    val totalLoanedMoney by bankerHomeViewModel.totalLoanOutMoney.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var loanToShow by remember { mutableStateOf(Loan()) }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // Total Loan Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Total Loan Given Out", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "₦${String.format(Locale("NG"),"%,.2f", totalLoanedMoney)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Tabs for Loan Status
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Approved") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Pending") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Overdue") }
            )
        }

        // Display Loans Based on Selected Tab
        when (selectedTab) {
            0 -> LoanList(loans = approvedLoans, onLoanClick = { /* Handle Click */ })
            1 -> LoanList(loans = pendingLoans, onLoanClick = { loan ->
                showDialog = true
                loanToShow = loan
            })

            2 -> LoanList(loans = overdueLoans, onLoanClick = { /* Handle Click */ })
        }
    }

    if (showDialog) {
        ShowLoanApprovalDialog(loanToShow, bankerHomeViewModel)
    }


}


@Composable
fun LoanList(loans: List<Loan>, onLoanClick: (Loan) -> Unit) {
    LazyColumn {
        items(loans) { loan ->
            LoanItem(loan = loan, onClick = { onLoanClick(loan) })
        }
    }
}