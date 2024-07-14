package com.schoolprojects.caribank.screens.student.transactions

import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.corrreps.viewmodels.StudentHomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentTransactions(
    studentHomeViewModel: StudentHomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val studentData by remember { studentHomeViewModel.studentInfo }
    var isBalanceVisible by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val tabTitles = listOf("Transactions In", "Transactions Out")
    val pagerState = rememberPagerState(pageCount = { 2 })


    val transactionsIn = listOf(
        AccountHistory(
            historyId = "1",
            date = "2024-07-01",
            accountId = "ACC1001",
            transactionType = "In",
            transactionAmount = 500.0,
            balance = 1500.0,
            description = "Payment received"
        ),
        AccountHistory(
            historyId = "2",
            date = "2024-07-03",
            accountId = "ACC1002",
            transactionType = "In",
            transactionAmount = 250.0,
            balance = 1250.0,
            description = "Deposit"
        ),
        AccountHistory(
            historyId = "3",
            date = "2024-07-05",
            accountId = "ACC1003",
            transactionType = "In",
            transactionAmount = 800.0,
            balance = 1800.0,
            description = "Salary"
        ),
        AccountHistory(
            historyId = "4",
            date = "2024-07-07",
            accountId = "ACC1004",
            transactionType = "In",
            transactionAmount = 300.0,
            balance = 1300.0,
            description = "Transfer from savings"
        ),
        AccountHistory(
            historyId = "5",
            date = "2024-07-10",
            accountId = "ACC1005",
            transactionType = "In",
            transactionAmount = 100.0,
            balance = 1100.0,
            description = "Online payment received"
        )
    )

    val transactionsOut = listOf(
        AccountHistory(
            historyId = "6",
            date = "2024-07-02",
            accountId = "ACC1006",
            transactionType = "Out",
            transactionAmount = -200.0,
            balance = 800.0,
            description = "Withdrawal"
        ),
        AccountHistory(
            historyId = "7",
            date = "2024-07-04",
            accountId = "ACC1007",
            transactionType = "Out",
            transactionAmount = -150.0,
            balance = 850.0,
            description = "Online purchase"
        ),
        AccountHistory(
            historyId = "8",
            date = "2024-07-06",
            accountId = "ACC1008",
            transactionType = "Out",
            transactionAmount = -400.0,
            balance = 600.0,
            description = "Utility bill payment"
        ),
        AccountHistory(
            historyId = "9",
            date = "2024-07-08",
            accountId = "ACC1009",
            transactionType = "Out",
            transactionAmount = -100.0,
            balance = 900.0,
            description = "Transfer to savings"
        ),
        AccountHistory(
            historyId = "10",
            date = "2024-07-11",
            accountId = "ACC1010",
            transactionType = "Out",
            transactionAmount = -50.0,
            balance = 950.0,
            description = "Subscription payment"
        )
    )

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
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
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
                        text = "Account Number: 2093505792",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
//                        val clip = ClipData.newPlainText("Account Number", studentData.accountNumber)
                        val clip = ClipData.newPlainText("Account Number", "2093505792")
                        clipboardManager.setText(AnnotatedString("2093505792"))
                        Toast.makeText(
                            context,
                            "Account number copied to clipboard",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy to clipboard",
                            tint = Color.White
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
                        text = if (isBalanceVisible) "Balance: 40000" else "Balance: ****",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {
                        isBalanceVisible = !isBalanceVisible
                    }) {
                        Icon(
                            imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle balance visibility",
                            tint = Color.White
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
            containerColor = Color.Gray,
            contentColor = Color.White,
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

@Composable
fun TransactionTile(transaction: AccountHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Date: ${transaction.date}", fontWeight = FontWeight.Bold)
            Text(text = "Amount: ${transaction.transactionAmount}")
            Text(text = "Narration: ${transaction.description}")
            Text(text = "Transaction Ref: ${transaction.historyId}", fontWeight = FontWeight.Bold)
        }
    }
}