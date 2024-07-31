package com.schoolprojects.caribank.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.schoolprojects.caribank.models.Account
import com.schoolprojects.caribank.models.AccountHistory
import com.schoolprojects.caribank.models.Loan
import com.schoolprojects.caribank.models.Savings
import com.schoolprojects.caribank.models.Student
import com.schoolprojects.caribank.utils.Common
import com.schoolprojects.caribank.utils.Common.accountsCollectionRef
import com.schoolprojects.caribank.utils.Common.loansCollectionRef
import com.schoolprojects.caribank.utils.Common.studentsCollectionRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class BankerHomeViewModel @Inject constructor() : ViewModel() {

    val studentInfo = mutableStateOf<Student>(Student())
    val showLoading = mutableStateOf<Boolean>(false)
    val openDialog = mutableStateOf<Boolean>(false)

    fun updateDialogStatus() {
        this.openDialog.value = !this.openDialog.value
    }

    val TAG = "BankerHomeViewModel"

    // Loans State
    private val _approvedLoans = MutableStateFlow<List<Loan>>(emptyList())
    val approvedLoans: StateFlow<List<Loan>> = _approvedLoans.asStateFlow()

    private val _pendingLoans = MutableStateFlow<List<Loan>>(emptyList())
    val pendingLoans: StateFlow<List<Loan>> = _pendingLoans.asStateFlow()

    private val _overdueLoans = MutableStateFlow<List<Loan>>(emptyList())
    val overdueLoans: StateFlow<List<Loan>> = _overdueLoans.asStateFlow()

    private val _totalMoney = MutableStateFlow(0.0)
    val totalMoney: StateFlow<Double> = _totalMoney.asStateFlow()

    private val _totalLoanOutMoney = MutableStateFlow(0.0)
    val totalLoanOutMoney: StateFlow<Double> = _totalMoney.asStateFlow()

    private val _activeAccounts = MutableStateFlow<List<Account>>(emptyList())
    val activeAccounts: StateFlow<List<Account>> = _activeAccounts.asStateFlow()

    private val _accountRequests = MutableStateFlow<List<Account>>(emptyList())
    val accountRequests: StateFlow<List<Account>> = _accountRequests.asStateFlow()

    init {
        fetchTotalMoney()
        fetchActiveAccounts()
        fetchAccountRequests()
        fetchLoans()
    }

    private fun fetchTotalMoney() {
        Common.accountsCollectionRef
            .get()
            .addOnSuccessListener { result ->
                val total = result.sumByDouble { it.getDouble("accountBalance") ?: 0.0 }
                _totalMoney.value = total
                fetchTotalLoanMoney()
            }
    }


    private fun fetchTotalLoanMoney() {
        Common.loansCollectionRef
            .get()
            .addOnSuccessListener { result ->
                val loanTotal = result.sumByDouble { it.getDouble("loanAmount") ?: 0.0 }
                Log.d(TAG, "fetchTotalLoanMoney: $loanTotal")
                _totalLoanOutMoney.value = loanTotal
            }
    }

    private fun fetchActiveAccounts() {
        Common.accountsCollectionRef
            .whereEqualTo("accountStatus", "active")
            .get()
            .addOnSuccessListener { result ->
                val accounts = result.map { it.toObject(Account::class.java) }
                _activeAccounts.value = accounts
            }
    }

    private fun fetchAccountRequests() {
        accountsCollectionRef
            .whereEqualTo("accountStatus", "pending")
            .get()
            .addOnSuccessListener { result ->
                val requests = result.map { it.toObject(Account::class.java) }
                _accountRequests.value = requests
            }
    }


    private fun fetchLoans() {
        loansCollectionRef
            .get()
            .addOnSuccessListener { result ->
                val loans = result.map { it.toObject(Loan::class.java) }

                // Categorize loans
                _approvedLoans.value = loans.filter { it.loanStatus == "approved" }
                _pendingLoans.value = loans.filter { it.loanStatus == "pending" }
                _overdueLoans.value = loans.filter {
                    it.payBackDate.toLong() < System.currentTimeMillis() && it.loanStatus != "paidBack"
                }
            }
            .addOnFailureListener { e ->
                // Handle error
                Log.e("BankerHomeViewModel", "Error fetching loans: ", e)
            }
    }

    fun getAccountOwner(accountOwnerId: String): StateFlow<Student?> {
        val userFlow = MutableStateFlow<Student?>(null)
        studentsCollectionRef.document(accountOwnerId)
            .get()
            .addOnSuccessListener { document ->
                userFlow.value = document.toObject(Student::class.java)
            }
        return userFlow
    }

    fun getRequester(requesterId: String): StateFlow<Student?> {
        val userFlow = MutableStateFlow<Student?>(null)
        studentsCollectionRef.document(requesterId)
            .get()
            .addOnSuccessListener { document ->
                userFlow.value = document.toObject(Student::class.java)
            }
        return userFlow
    }


    fun approveAccount(accountId: String) {
        accountsCollectionRef.document(accountId)
            .update("accountStatus", "active")
            .addOnSuccessListener {
                // Optionally refresh data or handle success
                fetchAccountRequests()
                fetchActiveAccounts()
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    fun declineAccount(accountId: String) {
        accountsCollectionRef.document(accountId)
            .update("accountStatus", "declined")
            .addOnSuccessListener {
                // Optionally refresh data or handle success
                fetchAccountRequests()
                fetchActiveAccounts()
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    fun topUpAccount(accountId: String, amount: Double) {
        Common.accountsCollectionRef.document(accountId)
            .get()
            .addOnSuccessListener { document ->
                val currentBalance = document.getDouble("accountBalance") ?: 0.0
                val newBalance = currentBalance + amount

                accountsCollectionRef.document(accountId)
                    .update("accountBalance", newBalance)
                    .addOnSuccessListener {
                        // Optionally refresh data or handle success
                        fetchActiveAccounts()
                        addTransactionToHistory(accountId, amount, newBalance)
                        fetchTotalMoney()

                    }
                    .addOnFailureListener { e ->
                        // Handle error
                    }
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }


    private fun addTransactionToHistory(accountId: String, amount: Double, newBalance: Double) {
        val transaction = AccountHistory(
            historyId = UUID.randomUUID().toString(),
            date = System.currentTimeMillis().toString(),
            accountId = accountId,
            transactionType = "Top Up",
            transactionAmount = amount,
            balance = newBalance,
            description = "Account topped up by banker"
        )

        Common.accountsHistoryCollectionRef.document(transaction.historyId)
            .set(transaction)
            .addOnSuccessListener {
                // Handle success if needed
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    fun approveLoan(loan: Loan) {
        val bankHasSufficientFunds = (_totalMoney.value >= (loan.loanAmount + 10000))

        if (bankHasSufficientFunds) {
            loansCollectionRef
                .whereEqualTo("loanId", loan.loanId)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        // Assuming there's only one document that matches
                        val document = result.documents.first()
                        val documentId = document.id

                        // Update the loan status and dateApproved in the found document
                        loansCollectionRef.document(documentId)
                            .update(
                                mapOf(
                                    "loanStatus" to "approved",
                                    "dateApproved" to System.currentTimeMillis().toString()
                                )
                            )
                            .addOnSuccessListener {
                                // Update the bank's total money by deducting the loan amount
                                _totalMoney.value -= loan.loanAmount

                                // Update the user's account balance
                                topUpAccount(loan.studentId, loan.loanAmount)

                                // Refresh the loans data
                                fetchLoans()
                            }
                            .addOnFailureListener { e ->
                                // Handle error
                                Log.e("BankerHomeViewModel", "Error approving loan: ", e)
                            }
                    } else {
                        // Handle case where no document is found
                        Log.e("BankerHomeViewModel", "No loan found with loanId: ${loan.loanId}")
                    }
                }
                .addOnFailureListener { e ->
                    // Handle query error
                    Log.e("BankerHomeViewModel", "Error querying loan: ", e)
                }
        }
    }

}

