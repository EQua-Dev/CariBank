package com.schoolprojects.caribank.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoolprojects.caribank.models.Account
import com.schoolprojects.caribank.models.AccountHistory
import com.schoolprojects.caribank.models.Fee
import com.schoolprojects.caribank.models.Loan
import com.schoolprojects.caribank.models.PaidFee
import com.schoolprojects.caribank.models.Savings
import com.schoolprojects.caribank.models.Student
import com.schoolprojects.caribank.models.schoolFees
import com.schoolprojects.caribank.utils.Common
import com.schoolprojects.caribank.utils.Common.accountsCollectionRef
import com.schoolprojects.caribank.utils.Common.loansCollectionRef
import com.schoolprojects.caribank.utils.Common.mAuth
import com.schoolprojects.caribank.utils.Common.paidFeesCollectionRef
import com.schoolprojects.caribank.utils.Common.savingsCollectionRef
import com.schoolprojects.caribank.utils.Common.studentsCollectionRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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
    val openSearchDialog = mutableStateOf<Boolean>(false)
    private val _matchingFee = MutableStateFlow<PaidFee?>(null)
    val matchingFee: StateFlow<PaidFee?> = _matchingFee

    private val _schoolFee = MutableStateFlow<Fee?>(null)
    val schoolFee: StateFlow<Fee?> = _schoolFee

    fun searchFeeByPaymentRef(paymentRef: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            paidFeesCollectionRef
                .whereEqualTo("paymentRef", paymentRef)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document == null) {
                            onError("No matching fee found for paymentRef: $paymentRef ")
                        } else {

                            val fee = document.toObject(PaidFee::class.java)
                            _schoolFee.value = schoolFees.find { it.feeId == fee.feeId }
                            _matchingFee.value = fee
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    _matchingFee.value = null
                    onError("No matching fee found for paymentRef: $paymentRef ")
                }
        }
    }

    fun debitAccount(
        accountId: String,
        amount: Double,
        description: String,
        callback: (Boolean, String) -> Unit
    ) {
        Common.accountsCollectionRef.document(accountId)
            .get()
            .addOnSuccessListener { document ->
                val currentBalance = document.getDouble("accountBalance") ?: 0.0
                val newBalance = currentBalance - amount

                accountsCollectionRef.document(accountId)
                    .update("accountBalance", newBalance)
                    .addOnSuccessListener {
                        // Optionally refresh data or handle success
                        fetchAccountInfo(mAuth.uid!!)
                        addTransactionToHistory(
                            accountId,
                            0 - amount,
                            newBalance,
                            Common.TransactionType.DEBIT.transactionType,
                            description,
                            callback = { status, message ->
                                callback(status, message)
                            }
                        )
                    }
                    .addOnFailureListener { e ->
                        // Handle error
                    }
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    fun clearMatchingFee() {
        _matchingFee.value = null
    }


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

    fun updateShowSearchDialogStatus() {
        this.openSearchDialog.value = !this.openSearchDialog.value
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
        description: String,
        callback: (Boolean, String) -> Unit
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
                callback(true, "Transaction added successfully.")
            }
            .addOnFailureListener { e ->
                // Handle error
                callback(false, "Failed to add transaction. Try again.")
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
                    newSavings.savingsDescription, callback = { status, message ->
                        if (status) {
                            fetchSavings() // Refresh savings list
                            updateAccountBalance(
                                newSavings.accountId,
                                newSavings.savingsAmount,
                                onError
                            )
                        } else {
                            onError(message)
                        }
                    }
                )

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

    fun getPaidFeesForStudent(
        studentId: String,
        feeId: String,
        onResult: (List<PaidFee>) -> Unit,
        onError: (String) -> Unit
    ) {
        paidFeesCollectionRef
            .whereEqualTo("studentId", studentId)
            .whereEqualTo("feeId", feeId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val paidFees = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(PaidFee::class.java)
                }
                onResult(paidFees)
            }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage ?: "Some error occurred")
                Log.e("Firestore", "Error fetching paid fees", exception)
            }
    }

    // Firestore function to check for existing PaidFee
    private fun checkExistingPaidFee(
        studentId: String,
        feeId: String,
        onResult: (PaidFee?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        paidFeesCollectionRef
            .whereEqualTo("studentId", studentId)
            .whereEqualTo("feeId", feeId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    onResult(null) // No existing document
                } else {
                    val paidFee = querySnapshot.documents.first().toObject(PaidFee::class.java)
                    onResult(paidFee) // Existing document
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }


    // Firestore function to update or save PaidFee
    fun saveOrUpdatePaidFee(
        studentId: String,
        feeId: String,
        selectedItems: List<String>,
        amountPaid: Double,
        paymentRef: String,
        callback: (Boolean, String) -> Unit
    ) {
        checkExistingPaidFee(studentId, feeId, { existingPaidFee ->

            // If an existing paid fee is found, update it
            if (existingPaidFee != null) {
                val updatedItems = existingPaidFee.itemsPaid + selectedItems
                val updatedAmount = existingPaidFee.amountPaid + amountPaid

                val updatedPaidFee = existingPaidFee.copy(
                    itemsPaid = updatedItems,
                    amountPaid = updatedAmount,
                    datePaid = System.currentTimeMillis().toString()
                )

                paidFeesCollectionRef
                    .document(existingPaidFee.paidFeeId)
                    .set(updatedPaidFee)
                    .addOnSuccessListener {
                        debitAccount(
                            _accountInfo.value!!.accountId,
                            amountPaid,
                            "Fees payment for ${selectedItems.joinToString(",")}",
                            callback = { status, message ->
                                callback(status, message)
                            }
                        )
                        Timber.d("Paid fee updated successfully")
                    }
                    .addOnFailureListener { exception ->
                        Timber.e(exception, "Error updating paid fee")
                    }
            } else {
                // Create a new paid fee if none exists
                val paidFeeId = paidFeesCollectionRef.document().id
                val newPaidFee = PaidFee(
                    paidFeeId = paidFeeId,
                    feeId = feeId,
                    studentId = studentId,
                    amountPaid = amountPaid,
                    datePaid = System.currentTimeMillis().toString(),
                    status = "Paid",
                    feeDescription = "Payment for selected fee items",
                    itemsPaid = selectedItems,
                    paymentRef = paymentRef
                )

                paidFeesCollectionRef
                    .document(paidFeeId)
                    .set(newPaidFee)
                    .addOnSuccessListener {
                        Timber.d("New paid fee saved successfully")
                        callback(true, "New paid fee saved successfully")
                    }
                    .addOnFailureListener { exception ->
                        Timber.e(exception, "Error saving new paid fee")
                    }
            }
        }, { exception ->
            Timber.e(exception, "Error checking existing paid fee")
        })
    }


}