package com.schoolprojects.corrreps.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.schoolprojects.caribank.models.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StudentHomeViewModel @Inject constructor() : ViewModel() {

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

}