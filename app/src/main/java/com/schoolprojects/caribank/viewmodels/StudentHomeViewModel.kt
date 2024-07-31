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
import com.schoolprojects.caribank.utils.Common.mAuth
import com.schoolprojects.caribank.utils.Common.savingsCollectionRef
import com.schoolprojects.caribank.utils.Common.studentsCollectionRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class StudentHomeViewModel @Inject constructor() : ViewModel() {

    private val _studentInfo = MutableStateFlow<Student?>(null)
    val studentInfo: StateFlow<Student?> = _studentInfo

    private val _accountInfo = MutableStateFlow<Account?>(null)
    val accountInfo: StateFlow<Account?> = _accountInfo

    private val _transactionsIn = MutableStateFlow<List<AccountHistory>>(emptyList())
    val transactionsIn: StateFlow<List<AccountHistory>> = _transactionsIn

    private val _transactionsOut = MutableStateFlow<List<AccountHistory>>(emptyList())
    val transactionsOut: StateFlow<List<AccountHistory>> = _transactionsOut

    private val _savingsList = MutableStateFlow<List<Savings>>(emptyList())
    val savingsList: StateFlow<List<Savings>> = _savingsList.asStateFlow()

    val showLoading = mutableStateOf<Boolean>(false)
    val openDialog = mutableStateOf<Boolean>(false)

    val TAG = "StudentHomeViewModel"

    private val _loanHistory = MutableStateFlow<List<Loan>>(emptyList())
    val loanHistory: StateFlow<List<Loan>> = _loanHistory

    init {
        fetchAccountInfo(mAuth.uid!!)
        fetchLoanHistory()
        fetchStudentInfo(mAuth.uid!!)

    }

    fun updateLoadingStatus(value: Boolean) {
        this.showLoading.value = value
    }

    fun updateDialogStatus() {
        this.openDialog.value = !this.openDialog.value
    }

    // Assume you have a function to get the current student's ID
    fun fetchStudentInfo(studentId: String) {
        studentsCollectionRef
            .document(studentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val student = document.toObject(Student::class.java)
                    _studentInfo.value = student
                    student?.studentId?.let { fetchAccountInfo(it) }
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }

    private fun fetchAccountInfo(studentId: String) {
        accountsCollectionRef
            .whereEqualTo("accountOwner", studentId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val account = document.toObject(Account::class.java)
                    _accountInfo.value = account
                    fetchTransactionHistory(account.accountId)
                    fetchSavings()
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }


    // Fetch savings belonging to a specific account
    fun fetchSavings() {
        savingsCollectionRef
            .whereEqualTo("accountId", accountInfo.value?.accountId)
            .get()
            .addOnSuccessListener { documents ->
                val savings = mutableListOf<Savings>()
                for (document in documents) {
                    val saving = document.toObject(Savings::class.java)
                    savings.add(saving)

                }
                _savingsList.value = savings
            }
            .addOnFailureListener { exception ->
                Log.e("SavingsViewModel", "Error fetching savings", exception)
            }
    }

    private fun addTransactionToHistory(
        accountId: String,
        amount: Double,
        newBalance: Double,
        transactionType: String,
        description: String
    ) {
        val transaction = AccountHistory(
            historyId = UUID.randomUUID().toString(),
            date = System.currentTimeMillis().toString(),
            accountId = accountId,
            transactionType = transactionType,
            transactionAmount = amount,
            balance = newBalance,
            description = description
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

    // Add a new savings entry
    fun addSavings(newSavings: Savings, onError: (String) -> Unit) {
        // Check if account balance is sufficient
        if (_accountInfo.value?.accountBalance!! < newSavings.savingsAmount) {
            onError("Insufficient balance to create savings.")
            return
        }

        // Add savings to Firestore
        savingsCollectionRef
            .add(newSavings)
            .addOnSuccessListener {
                Log.d("SavingsViewModel", "Savings added successfully")
                addTransactionToHistory(
                    newSavings.accountId,
                    0 - newSavings.savingsAmount,
                    accountInfo.value?.accountBalance!! - newSavings.savingsAmount,
                    Common.TransactionType.DEBIT.transactionType,
                    newSavings.savingsDescription
                )
                fetchSavings() // Refresh savings list
                updateAccountBalance(newSavings.accountId, newSavings.savingsAmount, onError)
            }
            .addOnFailureListener { exception ->
                Log.e("SavingsViewModel", "Error adding savings", exception)
                onError("Failed to create savings. Try again.")
            }
    }

    // Update account balance after savings deduction
    private fun updateAccountBalance(
        accountId: String,
        savingsAmount: Double,
        onError: (String) -> Unit
    ) {
        val newBalance = _accountInfo.value?.accountBalance!! - savingsAmount
        accountsCollectionRef
            .document(accountId)
            .update("accountBalance", newBalance)
            .addOnSuccessListener {
                Log.d("SavingsViewModel", "Account balance updated successfully")
                fetchAccountInfo(mAuth.uid!!)
                fetchSavings()
            }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage?.toString() ?: "Error updating account balance")
                Log.e("SavingsViewModel", "Error updating account balance", exception)
            }
    }

    private fun fetchTransactionHistory(accountId: String) {
        Common.accountsHistoryCollectionRef
            .whereEqualTo("accountId", accountId)
            .get()
            .addOnSuccessListener { documents ->
                val inTransactions = mutableListOf<AccountHistory>()
                val outTransactions = mutableListOf<AccountHistory>()

                for (document in documents) {
                    val transaction = document.toObject(AccountHistory::class.java)
                    if (transaction.transactionAmount > 0) {
                        inTransactions.add(transaction)
                    } else {
                        outTransactions.add(transaction)
                    }
                }

                _transactionsIn.value = inTransactions
                _transactionsOut.value = outTransactions
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }

    fun fetchLoanHistory() {
        loansCollectionRef
            .whereEqualTo("studentId", mAuth.uid.toString()) // Replace with actual student ID
            .get()
            .addOnSuccessListener { documents ->
                val loans = mutableListOf<Loan>()
                for (document in documents) {
                    val loan = document.toObject(Loan::class.java)
                    loans.add(loan)
                }
                _loanHistory.value = loans
            }
            .addOnFailureListener { exception ->
                Log.e("StudentLoanViewModel", "Error getting loan history: ", exception)
            }
    }

    fun applyForLoan(loan: Loan) {
        loansCollectionRef
            .add(loan)
            .addOnSuccessListener {
                Log.d("StudentLoanViewModel", "Loan application added successfully")
                fetchLoanHistory() // Refresh loan history
            }
            .addOnFailureListener { exception ->
                Log.e("StudentLoanViewModel", "Error adding loan application: ", exception)
            }
    }


}