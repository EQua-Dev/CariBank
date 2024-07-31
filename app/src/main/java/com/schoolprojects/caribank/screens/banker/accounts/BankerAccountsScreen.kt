package com.schoolprojects.caribank.screens.banker.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.models.Account
import com.schoolprojects.caribank.navigation.Screen
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.utils.Common
import com.schoolprojects.caribank.viewmodels.BankerHomeViewModel
import java.text.DecimalFormat

@Composable
fun BankerAccountsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bankerHomeViewModel: BankerHomeViewModel = hiltViewModel()
) {
    val totalMoney by bankerHomeViewModel.totalMoney.collectAsState()
    val activeAccounts by bankerHomeViewModel.activeAccounts.collectAsState()
    val accountRequests by bankerHomeViewModel.accountRequests.collectAsState()
    val selectedTab = remember { mutableStateOf("Active Accounts") }
    val selectedRequest = remember { mutableStateOf<Account?>(null) }
    val showDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Total Money in the Bank: ₦${DecimalFormat("#,###.00").format(totalMoney)}",
            style = Typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TabRow(selectedTabIndex = if (selectedTab.value == "Active Accounts") 0 else 1) {
            Tab(
                selected = selectedTab.value == "Active Accounts",
                onClick = { selectedTab.value = "Active Accounts" },
                text = { Text("Active Accounts") },
            )
            Tab(
                selected = selectedTab.value == "Account Requests",
                text = { Text("Account Requests") },
                onClick = { selectedTab.value = "Account Requests" })
        }

        when (selectedTab.value) {
            "Active Accounts" -> {
                LazyColumn {
                    items(activeAccounts) { account ->
                        ActiveAccountTile(account = account, viewModel = bankerHomeViewModel)
                    }
                }
            }

            "Account Requests" -> {
                LazyColumn {
                    items(accountRequests) { request ->
                        AccountRequestTile(request = request, viewModel = bankerHomeViewModel){
                            // Handle account request approval or rejection here
                            selectedRequest.value = request
                            showDialog.value = true
                        }
                    }
                }
            }
        }
    }

    if (showDialog.value && selectedRequest.value != null) {
        PendingAccountDialog(
            account = selectedRequest.value!!,
            onDismiss = { showDialog.value = false },
            onApprove = {
                bankerHomeViewModel.approveAccount(selectedRequest.value!!.accountId)
                showDialog.value = false
            },
            onDecline = {
                bankerHomeViewModel.declineAccount(selectedRequest.value!!.accountId)
                showDialog.value = false
            },
            viewModel = bankerHomeViewModel
        )
    }
}

@Composable
fun PendingAccountDialog(
    account: Account,
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onDecline: () -> Unit,
    viewModel: BankerHomeViewModel
) {
    val requester by viewModel.getRequester(account.accountOwner).collectAsState()

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onDismiss
        },
        title = {
            Text(text = "Account Request Details", style = Typography.titleLarge)
        },
        text = {
            requester?.let {
                Column {
                    Text(text = "Name: ${it.studentFirstName} ${it.studentLastName}")
                    Text(text = "Reg No: ${it.studentRegNo}")
                    Text(text = "Email: ${it.studentEmail}")
                    Text(text = "Gender: ${it.studentGender}")
                    Text(text = "Department: ${it.studentDepartment}")
                    Text(text = "Current Level: ${it.studentCurrentLevel}")
                }
            } ?: Text(text = "Loading...")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApprove()
                }
            ) {
                Text("Approve")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDecline()
                }
            ) {
                Text("Decline")
            }
        },

        )
}
