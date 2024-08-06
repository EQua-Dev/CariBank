package com.schoolprojects.caribank.screens.banker.dues

import SearchBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.schoolprojects.caribank.components.SemesterToggle
import com.schoolprojects.caribank.models.DueWithStudent
import com.schoolprojects.caribank.models.FeeWithStudent
import com.schoolprojects.caribank.models.schoolDues
import com.schoolprojects.caribank.models.schoolFees
import com.schoolprojects.caribank.screens.banker.DuesList
import com.schoolprojects.caribank.screens.banker.FeesList
import com.schoolprojects.caribank.ui.theme.Typography
import com.schoolprojects.caribank.viewmodels.BankerHomeViewModel

@Composable
fun BankerDuesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bankerHomeViewModel: BankerHomeViewModel = hiltViewModel()
) {

    var searchQuery by remember { mutableStateOf("") }
    var currentSemester by remember { mutableStateOf("1st Semester") }
    val paidDues by bankerHomeViewModel.paidDues.collectAsState(initial = emptyList())
    val searchResults by bankerHomeViewModel.searchDueResults.collectAsState(initial = emptyList())
    val studentData by remember {
        bankerHomeViewModel.studentInfo
    }.collectAsState()

    // Filter fees based on semester
    val semesterFilteredDues = paidDues.mapNotNull { paidDues ->
        // Find the corresponding Fee object in schoolFees based on feeId
        val fee =
            schoolDues.find { it.dueId == paidDues.paidDue.duesId && it.dueSemester == currentSemester }
        // Only return a FeeWithStudent object if the fee matches the current semester
        fee?.let {
            DueWithStudent(paidDues.paidDue, paidDues.student)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Top bar with search button
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChanged = { searchQuery = it },
            onSearch = { query ->
                bankerHomeViewModel.searchDues(query)
            }
        )

        // Semester toggle
        SemesterToggle(
            currentSemester = currentSemester,
            onSemesterChange = { semester ->
                currentSemester = semester
            }
        )

        // Display either search results or all paid fees
        val duesToDisplay = if (searchQuery.isNotEmpty()) searchResults else semesterFilteredDues


        // Fees list
        DuesList(
            dues = duesToDisplay,
            onVerifyDue = { dueWithStudent ->
                bankerHomeViewModel.verifyDue(dueWithStudent)
            }
        )
    }
}