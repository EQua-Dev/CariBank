package com.schoolprojects.caribank.screens.student.transactions

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.components.TabRowComponent
import com.schoolprojects.caribank.models.AccountHistory
import com.schoolprojects.caribank.screens.components.EmptyListView
import com.schoolprojects.caribank.utils.Common.mAuth
import com.schoolprojects.caribank.utils.getDate
import com.schoolprojects.caribank.viewmodels.StudentHomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentTransactions(
    studentHomeViewModel: StudentHomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val studentData by remember { studentHomeViewModel.studentInfo }.collectAsState()
    val accountData by remember { studentHomeViewModel.accountInfo }.collectAsState()
    val transactionsIn by remember { studentHomeViewModel.transactionsIn }.collectAsState()
    val transactionsOut by remember { studentHomeViewModel.transactionsOut }.collectAsState()

    var isBalanceVisible by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val tabTitles = listOf("Transactions In", "Transactions Out")
    val pagerState = rememberPagerState(pageCount = { 2 })

    LaunchedEffect(Unit) {
        // Assuming you have a way to get the studentId (e.g., from the current user session)
        studentHomeViewModel.fetchStudentInfo(mAuth.uid!!)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
//                        text = "Account Number: ${studentData.accountNumber}",
                        text = "Account Number: ${accountData?.accountNumber ?: "Unknown"}",
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
//                        val clip = ClipData.newPlainText("Account Number", studentData.accountNumber)
                        val clip = ClipData.newPlainText("Account Number", "2093505792")
                        clipboardManager.setText(AnnotatedString(accountData?.accountNumber ?: ""))
                        Toast.makeText(
                            context,
                            "Account number copied to clipboard",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy to clipboard",
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
//                        text = if (isBalanceVisible) "Balance: ${studentData.balance}" else "Balance: ****",
                        text = if (isBalanceVisible) "Balance: ₦${accountData?.accountBalance ?: "0.0"}" else "Balance: ****",
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
                        isBalanceVisible = !isBalanceVisible
                    }) {
                        Icon(
                            imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle balance visibility",
                        )
                    }
                }
            }
        }

        // Tabs for Transactions In and Out
        TabRowComponent(
            tabs = tabTitles,
            contentScreens = listOf(
                { TransactionsList(transactions = transactionsIn) },  // Content screen for Tab 1
                { TransactionsList(transactions = transactionsOut) },      // Content screen for Tab 2
            ),
            modifier = Modifier.fillMaxSize(),
            contentColor = Color.Black,
            indicatorColor = Color.DarkGray
        )


        // Transaction Lists
        HorizontalPager(
            state = pagerState
        ) { page ->
            when (page) {
                0 -> TransactionsList(transactions = transactionsIn)
                1 -> TransactionsList(transactions = transactionsOut)
            }
        }
    }
}


@Composable
fun TransactionsList(transactions: List<AccountHistory>) {
    if (transactions.isEmpty()){
        EmptyListView(message = "No transactions records")
    }else{
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(transactions) { transaction ->
                TransactionTile(transaction = transaction)
            }
        }
    }

}

@Composable
fun TransactionTile(transaction: AccountHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFCBD0D3))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Date: ${getDate(transaction.date.toLong(), "EEE dd MMM, yyyy")}",
                fontWeight = FontWeight.Bold
            )
            Text(text = "Amount: ₦${transaction.transactionAmount}")
            Text(text = "Narration: ${transaction.description}")
            Text(text = "Transaction Ref: ${transaction.historyId}", fontWeight = FontWeight.Bold)
        }
    }
}