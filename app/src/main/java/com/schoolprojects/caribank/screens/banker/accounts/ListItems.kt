package com.schoolprojects.caribank.screens.banker.accounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.schoolprojects.caribank.models.Account
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.utils.getDate
import com.schoolprojects.caribank.viewmodels.BankerHomeViewModel


@Composable
fun ActiveAccountTile(account: Account, viewModel: BankerHomeViewModel) {
    val accountOwner by viewModel.getAccountOwner(account.accountOwner).collectAsState()
    val showDetailsDialog = remember { mutableStateOf(false) }
    val showTopUpDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.6f)) {
                val accountOwnerName =
                    "${accountOwner?.studentFirstName ?: "Unknown"} ${accountOwner?.studentLastName ?: "Unknown"}"

                Text(
                    text = "Account Number: ${account.accountNumber}",
                    style = Typography.bodyMedium
                )
                Text(text = "Owner: $accountOwnerName", style = Typography.bodyMedium)
            }

            Column (modifier = Modifier.weight(0.4f)){
                Button(
                    onClick = { showDetailsDialog.value = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Details")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showTopUpDialog.value = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Top Up")
                }
            }
        }
        if (showDetailsDialog.value) {
            AccountDetailsDialog(
                account = account,
                accountOwner = accountOwner,
                onDismiss = { showDetailsDialog.value = false }
            )
        }

        if (showTopUpDialog.value) {
            TopUpDialog(
                account = account,
                viewModel = viewModel,
                onDismiss = { showTopUpDialog.value = false }
            )
        }
    }
}

@Composable
fun AccountRequestTile(request: Account, viewModel: BankerHomeViewModel, onClick: () -> Unit) {
    val requester by viewModel.getRequester(request.accountOwner).collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val requesterName =
                "${requester?.studentFirstName ?: "Unknown"}${requester?.studentLastName ?: "Unknown"}"
            val requestDate = getDate(request.dateCreated.toLong(), "EEE dd MMM yyyy")
            Text(text = "Requested On: $requestDate", style = Typography.bodyMedium)
            Text(text = "Requester: $requesterName", style = Typography.bodyMedium)
        }
    }
}