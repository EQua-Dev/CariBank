package com.schoolprojects.caribank.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.schoolprojects.caribank.models.Account
import com.schoolprojects.caribank.models.Student
import com.schoolprojects.caribank.utils.Common
import com.schoolprojects.caribank.utils.Common.accountsCollectionRef
import com.schoolprojects.caribank.utils.Common.studentsCollectionRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class BankerHomeViewModel @Inject constructor() : ViewModel() {

    val studentInfo = mutableStateOf<Student>(Student())
    val showLoading = mutableStateOf<Boolean>(false)
    val openDialog = mutableStateOf<Boolean>(false)

    fun updateStudentInfo(value: Student) {
        this.studentInfo.value = value
    }

    fun updateLoadingStatus(value: Boolean) {
        this.showLoading.value = value
    }

    fun updateDialogStatus() {
        this.openDialog.value = !this.openDialog.value
    }

    private val _totalMoney = MutableStateFlow(0.0)
    val totalMoney: StateFlow<Double> = _totalMoney.asStateFlow()

    private val _activeAccounts = MutableStateFlow<List<Account>>(emptyList())
    val activeAccounts: StateFlow<List<Account>> = _activeAccounts.asStateFlow()

    private val _accountRequests = MutableStateFlow<List<Account>>(emptyList())
    val accountRequests: StateFlow<List<Account>> = _accountRequests.asStateFlow()

    init {
        fetchTotalMoney()
        fetchActiveAccounts()
        fetchAccountRequests()
    }

    private fun fetchTotalMoney() {
        Common.accountsCollectionRef
            .get()
            .addOnSuccessListener { result ->
                val total = result.sumByDouble { it.getDouble("accountBalance") ?: 0.0 }
                _totalMoney.value = total
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
                    }
                    .addOnFailureListener { e ->
                        // Handle error
                    }
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

}