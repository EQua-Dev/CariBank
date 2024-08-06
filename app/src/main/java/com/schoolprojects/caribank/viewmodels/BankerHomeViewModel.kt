package com.schoolprojects.caribank.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.schoolprojects.caribank.models.Account
import com.schoolprojects.caribank.models.AccountHistory
import com.schoolprojects.caribank.models.DueWithStudent
import com.schoolprojects.caribank.models.FeeWithStudent
import com.schoolprojects.caribank.models.Loan
import com.schoolprojects.caribank.models.PaidDues
import com.schoolprojects.caribank.models.PaidFee
import com.schoolprojects.caribank.models.Savings
import com.schoolprojects.caribank.models.Student
import com.schoolprojects.caribank.utils.Common
import com.schoolprojects.caribank.utils.Common.accountsCollectionRef
import com.schoolprojects.caribank.utils.Common.loansCollectionRef
import com.schoolprojects.caribank.utils.Common.paidDuesCollectionRef
import com.schoolprojects.caribank.utils.Common.paidFeesCollectionRef
import com.schoolprojects.caribank.utils.Common.savingsCollectionRef
import com.schoolprojects.caribank.utils.Common.studentsCollectionRef
import com.schoolprojects.caribank.utils.calculateInterestRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class BankerHomeViewModel @Inject constructor() : ViewModel() {

    val showLoading = mutableStateOf<Boolean>(false)
    val openDialog = mutableStateOf<Boolean>(false)

    fun updateDialogStatus() {
        this.openDialog.value = !this.openDialog.value
    }

    val TAG = "BankerHomeViewModel"

    private val _paidFees = MutableStateFlow<List<FeeWithStudent>>(emptyList())
    val paidFees: StateFlow<List<FeeWithStudent>> get() = _paidFees

    private val _paidDues = MutableStateFlow<List<DueWithStudent>>(emptyList())
    val paidDues: StateFlow<List<DueWithStudent>> get() = _paidDues

    private val _searchResults = MutableStateFlow<List<FeeWithStudent>>(emptyList())
    val searchResults: StateFlow<List<FeeWithStudent>> get() = _searchResults

    private val _searchDueResults = MutableStateFlow<List<DueWithStudent>>(emptyList())
    val searchDueResults: StateFlow<List<DueWithStudent>> get() = _searchDueResults


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

    private val _studentInfo = MutableStateFlow<Student?>(null)
    val studentInfo: StateFlow<Student?> = _studentInfo

    private val _savingsList = MutableStateFlow<List<Savings>>(emptyList())
    val savingsList: StateFlow<List<Savings>> = _savingsList


    init {
        fetchTotalMoney()
        fetchActiveAccounts()
        fetchAccountRequests()
        fetchLoans()
        fetchPaidFees()
        fetchPaidDues()
        fetchSavings()

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

    fun fetchStudentInfo(studentId: String) {
        studentsCollectionRef
            .document(studentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val student = document.toObject(Student::class.java)

                    _studentInfo.value = student
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
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

    private fun fetchPaidFees() {
        paidFeesCollectionRef
            .get()
            .addOnSuccessListener { feeDocs ->
                val feeList = mutableListOf<FeeWithStudent>()
                feeDocs.forEach { feeDoc ->
                    val paidFee = feeDoc.toObject(PaidFee::class.java)
                    // Fetch associated student
                    studentsCollectionRef
                        .whereEqualTo("studentId", paidFee.studentId)
                        .get()
                        .addOnSuccessListener { studentDocs ->
                            val student =
                                studentDocs.mapNotNull { it.toObject(Student::class.java) }
                                    .firstOrNull()
                            student?.let {
                                feeList.add(FeeWithStudent(paidFee = paidFee, student = it))
                                _paidFees.value = feeList
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    fun searchFees(query: String) {
        when {
            query.startsWith("REF") -> {
                // Search by payment reference
                paidFeesCollectionRef
                    .whereEqualTo("paymentRef", query)
                    .get()
                    .addOnSuccessListener { feeDocs ->
                        val feeList = mutableListOf<FeeWithStudent>()
                        feeDocs.forEach { feeDoc ->
                            val paidFee = feeDoc.toObject(PaidFee::class.java)
                            // Fetch associated student
                            studentsCollectionRef
                                .whereEqualTo("studentId", paidFee.studentId)
                                .get()
                                .addOnSuccessListener { studentDocs ->
                                    val student =
                                        studentDocs.mapNotNull { it.toObject(Student::class.java) }
                                            .firstOrNull()
                                    student?.let {
                                        feeList.add(FeeWithStudent(paidFee = paidFee, student = it))
                                        _searchResults.value = feeList
                                    }
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
            }

            isRegNumber(query) -> {
                // Search by registration number
                studentsCollectionRef
                    .whereEqualTo("studentRegNo", query)
                    .get()
                    .addOnSuccessListener { studentDocs ->
                        val student = studentDocs.mapNotNull { it.toObject(Student::class.java) }
                            .firstOrNull()
                        student?.let {
                            fetchFeesByStudentId(it.studentId)
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
            }

            else -> {
                // Search by student name
                studentsCollectionRef
                    .whereEqualTo("studentFirstName", query)
                    .get()
                    .addOnSuccessListener { studentDocs ->
                        val student = studentDocs.mapNotNull { it.toObject(Student::class.java) }
                            .firstOrNull()
                        student?.let {
                            fetchFeesByStudentId(it.studentId)
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
            }
        }
    }

    private fun fetchFeesByStudentId(studentId: String) {
        paidFeesCollectionRef
            .whereEqualTo("studentId", studentId)
            .get()
            .addOnSuccessListener { feeDocs ->
                val feeList = mutableListOf<FeeWithStudent>()
                feeDocs.forEach { feeDoc ->
                    val paidFee = feeDoc.toObject(PaidFee::class.java)
                    studentsCollectionRef
                        .whereEqualTo("studentId", studentId)
                        .get()
                        .addOnSuccessListener { studentDocs ->
                            val student =
                                studentDocs.mapNotNull { it.toObject(Student::class.java) }
                                    .firstOrNull()
                            student?.let {
                                feeList.add(FeeWithStudent(paidFee = paidFee, student = it))
                                _searchResults.value = feeList
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    fun verifyFee(feeWithStudent: FeeWithStudent) {
        val updatedFee = feeWithStudent.paidFee.copy(isVerified = true)
        paidFeesCollectionRef
            .document(updatedFee.paidFeeId)
            .set(updatedFee)
            .addOnSuccessListener {
                fetchPaidFees()
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }


    private fun fetchPaidDues() {
        paidDuesCollectionRef
            .get()
            .addOnSuccessListener { dueDocs ->
                val dueList = mutableListOf<DueWithStudent>()
                dueDocs.forEach { dueDoc ->
                    val paidDue = dueDoc.toObject(PaidDues::class.java)
                    // Fetch associated student
                    studentsCollectionRef
                        .whereEqualTo("studentId", paidDue.studentId)
                        .get()
                        .addOnSuccessListener { studentDocs ->
                            val student =
                                studentDocs.mapNotNull { it.toObject(Student::class.java) }
                                    .firstOrNull()
                            student?.let {
                                dueList.add(DueWithStudent(paidDue = paidDue, student = it))
                                _paidDues.value = dueList
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    fun searchDues(query: String) {
        when {
            query.startsWith("REF") -> {
                // Search by payment reference
                paidDuesCollectionRef
                    .whereEqualTo("paymentRef", query)
                    .get()
                    .addOnSuccessListener { dueDocs ->
                        val dueList = mutableListOf<DueWithStudent>()
                        dueDocs.forEach { feeDoc ->
                            val paidDue = feeDoc.toObject(PaidDues::class.java)
                            // Fetch associated student
                            studentsCollectionRef
                                .whereEqualTo("studentId", paidDue.studentId)
                                .get()
                                .addOnSuccessListener { studentDocs ->
                                    val student =
                                        studentDocs.mapNotNull { it.toObject(Student::class.java) }
                                            .firstOrNull()
                                    student?.let {
                                        dueList.add(DueWithStudent(paidDue = paidDue, student = it))
                                        _searchDueResults.value = dueList
                                    }
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
            }

            isRegNumber(query) -> {
                // Search by registration number
                studentsCollectionRef
                    .whereEqualTo("studentRegNo", query)
                    .get()
                    .addOnSuccessListener { studentDocs ->
                        val student = studentDocs.mapNotNull { it.toObject(Student::class.java) }
                            .firstOrNull()
                        student?.let {
                            fetchDuesByStudentId(it.studentId)
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
            }

            else -> {
                // Search by student name
                studentsCollectionRef
                    .whereEqualTo("studentFirstName", query)
                    .get()
                    .addOnSuccessListener { studentDocs ->
                        val student = studentDocs.mapNotNull { it.toObject(Student::class.java) }
                            .firstOrNull()
                        student?.let {
                            fetchDuesByStudentId(it.studentId)
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
            }
        }
    }

    private fun fetchDuesByStudentId(studentId: String) {
        paidDuesCollectionRef
            .whereEqualTo("studentId", studentId)
            .get()
            .addOnSuccessListener { dueDocs ->
                val dueList = mutableListOf<DueWithStudent>()
                dueDocs.forEach { feeDoc ->
                    val paidDue = feeDoc.toObject(PaidDues::class.java)
                    studentsCollectionRef
                        .whereEqualTo("studentId", studentId)
                        .get()
                        .addOnSuccessListener { studentDocs ->
                            val student =
                                studentDocs.mapNotNull { it.toObject(Student::class.java) }
                                    .firstOrNull()
                            student?.let {
                                dueList.add(DueWithStudent(paidDue = paidDue, student = it))
                                _searchDueResults.value = dueList
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    fun verifyDue(dueWithStudent: DueWithStudent) {
        val updatedDue = dueWithStudent.paidDue.copy(isVerified = true)
        paidDuesCollectionRef
            .document(updatedDue.paidDuesId)
            .set(updatedDue)
            .addOnSuccessListener {
                fetchPaidDues()
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    // Fetch savings from Firestore
    private fun fetchSavings() {
        savingsCollectionRef
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle errors
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val savings = snapshot.documents.mapNotNull { document ->
                        document.toObject(Savings::class.java)
                    }
                    _savingsList.value = savings
                }
            }
    }

    // Function to calculate withdrawal amount
    fun calculateWithdrawAmount(savings: Savings): Double {
        val interest = calculateInterestRate(savings.savingsAmount, savings.dueDate)
        return savings.savingsAmount + (savings.savingsAmount * interest / 100)
    }

    // Function to find savings with the closest due date
    fun getSavingsWithClosestDueDate(): Savings? {
        return _savingsList.value.minByOrNull { savings ->
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayDate = dateFormat.parse(today)
            val dueDate = dateFormat.parse(savings.dueDate)
            dueDate.time - todayDate.time
        }
    }

    private fun isRegNumber(query: String): Boolean {
        val regNumberPattern = Regex("^[A-Z]{2,3}/\\d{4}/\\d{3,4}\$")
        return regNumberPattern.matches(query)
    }

}

