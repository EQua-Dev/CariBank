package com.schoolprojects.caribank.screens.student.payfees

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolprojects.caribank.models.Fee
import com.schoolprojects.caribank.models.PaidFee
import com.schoolprojects.caribank.models.schoolFees
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.utils.Common.mAuth
import com.schoolprojects.caribank.utils.HelpMe.generatePaymentRef
import com.schoolprojects.caribank.viewmodels.StudentHomeViewModel
import timber.log.Timber

@Composable
fun FeesPaymentScreen(
    level: String,
    semester: String,
    onBack: () -> Unit,
    studentHomeViewModel: StudentHomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val TAG = "FeesPaymentScreen"

    val paidFees = remember { mutableStateOf<List<PaidFee>>(emptyList()) }
    val selectedItems = remember { mutableStateOf<Set<String>>(emptySet()) }
    val accountDetails by remember {
        studentHomeViewModel.accountInfo
    }.collectAsState()

    val feeWithMultipleItems = schoolFees.first {
        it.feeLevel == level && it.feeSemester == semester
    }
    // Retrieve all paid items from previous payments
    val paidItems = remember {
        mutableStateOf(setOf<String>())
    }

    // Filter items to show only unpaid ones
    val unpaidItems = remember {
        mutableStateOf(mapOf<String, Double>())
    }

    val amountPaid = remember {
        mutableStateOf(
            0.0
        )
    }
    val amountRemaining = remember {
        mutableStateOf(0.0)
    }


    // Fetch paid fees when the screen is first composed
    LaunchedEffect(level, semester) {
        studentHomeViewModel.getPaidFeesForStudent(
            mAuth.uid!!,
            feeWithMultipleItems.feeId,
            { fees ->
                Log.d(TAG, "FeesPaymentScreen: $fees")
                paidFees.value = fees
                amountPaid.value =
                    paidFees.value.firstOrNull { it.status == "Paid" }?.amountPaid ?: 0.0
                amountRemaining.value = feeWithMultipleItems.feeTotalAmount - amountPaid.value
                paidItems.value = paidFees.value.flatMap { it.itemsPaid }.toSet()
                unpaidItems.value =
                    feeWithMultipleItems.feeItems.filterKeys { it !in paidItems.value }
            },
            { exception ->
                Timber.tag("Error fetching paid fees ").e(exception)
            })
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Column {
                Text(text = level, style = Typography.bodyLarge, modifier = Modifier.padding(2.dp))
                Text(
                    text = semester,
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(2.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(4.dp)) {
                Text(
                    text = "Total: ₦${feeWithMultipleItems.feeTotalAmount}",
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    text = "Paid: ₦${amountPaid.value}",
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    text = "Remaining: ₦${amountRemaining.value}",
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (paidItems.value.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val paymentRef = paidFees.value.first().paymentRef
                Text(text = "Receipt REF: $paymentRef", style = Typography.bodyLarge)


                IconButton(onClick = { clipboardManager.setText(AnnotatedString(paymentRef)) }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Text(text = "Select items to pay", style = Typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }


        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(unpaidItems.value.toList()) { (item, amount) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item in selectedItems.value,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedItems.value += item
                            } else {
                                selectedItems.value -= item
                            }
                        }
                    )
                    Text(text = item, style = Typography.bodyMedium)
                    Text(text = "₦$amount", style = Typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (amountRemaining.value != 0.0) {
            Button(
                onClick = {
                    val selectedItemsList = selectedItems.value.toList()
                    val totalAmount =
                        selectedItemsList.sumOf { feeWithMultipleItems.feeItems[it] ?: 0.0 }
                    if (accountDetails?.accountBalance!! >= totalAmount) {
                        // Generate a payment reference
                        val paymentRef = generatePaymentRef()

                        // Save or update the paid fee
                        studentHomeViewModel.saveOrUpdatePaidFee(
                            studentId = mAuth.uid!!,
                            feeId = feeWithMultipleItems.feeId,
                            selectedItems = selectedItemsList,
                            amountPaid = totalAmount,
                            paymentRef = paymentRef,
                            callback = { status, message ->
                                if (status) {
                                    Toast.makeText(
                                        context,
                                        "Fees Paid Successfully",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    onBack()
                                }

                            }
                        )

                    } else {
                        // Show error message for insufficient balance
                        Toast.makeText(context, "Insufficient balance", Toast.LENGTH_LONG).show()
                        Log.e("Payment", "Insufficient balance")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Pay Fees")
            }
        } else {
            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Congratulations! 🥳 \nAll Fees have been paid for this semester")
            }
        }


    }
}