package com.schoolprojects.caribank.screens.auth

import CustomTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolprojects.caribank.components.CustomSnackbar
import com.schoolprojects.caribank.components.DropdownField
import com.schoolprojects.caribank.components.FlatButton
import com.schoolprojects.caribank.components.PasswordStrengthIndicator
import com.schoolprojects.caribank.navigation.Screen
import com.schoolprojects.caribank.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(
    onNavigationRequested: (String, Boolean) -> Unit,
    onAccountCreated: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var showSnackbar by remember { mutableStateOf(false) }

    val errorMessage = remember {
        mutableStateOf("")
    }
    val firstName by remember { authViewModel.studentFirstName }
    val lastName by remember { authViewModel.studentLastName }
    val regNumber by remember { authViewModel.matricNo }
    val email by remember { authViewModel.email }
    val password by remember { authViewModel.password }
    val passwordStrength by remember { authViewModel.passwordStrength }
    val selectedDepartment by remember { authViewModel.studentDepartment }
    val selectedGender by remember { authViewModel.gender }
    val selectedLevel by remember { authViewModel.studentLevel }
    val selectedSemester by remember { authViewModel.studentSemester }

    val departments = listOf(
        "Computer Science",
    )
    val genders = listOf("Male", "Female")
    val levels = listOf("100", "200", "300", "400")
    val semesters = listOf("1st", "2nd")


    val showLoading = remember { authViewModel.showLoading }



    Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = firstName,
                onValueChange = { authViewModel.updateFirstName(it) },
                label = "First Name",
                placeholder = "First Name",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = lastName,
                onValueChange = { authViewModel.updateLastName(it) },
                label = "Last Name",
                placeholder = "Last Name",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = regNumber,
                onValueChange = { authViewModel.updateMatricNo(it) },
                label = "Reg Number",
                placeholder = "Reg Number",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = email,
                onValueChange = { authViewModel.updateEmail(it) },
                label = "Email",
                placeholder = "Email",
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = password,
                onValueChange = { authViewModel.updatePassword(it) },
                label = "Password",
                placeholder = "Password",
                keyboardType = KeyboardType.Password,
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )
            PasswordStrengthIndicator(passwordStrength)


            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                selectedValue = selectedDepartment,
                onValueChange = { authViewModel.updateStudentDepartment(it) },
                label = "School Department",
                options = departments,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "School Department"
            )


            Spacer(modifier = Modifier.height(16.dp))

            DropdownField(
                selectedValue = selectedLevel,
                onValueChange = { authViewModel.updateStudentLevel(it) },
                label = "Level",
                options = levels,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Current Level"
            )

            Spacer(modifier = Modifier.height(16.dp))
            DropdownField(
                selectedValue = selectedGender,
                onValueChange = { authViewModel.updateGender(it) },
                label = "Gender",
                options = genders,
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Gender"
            )
            Spacer(modifier = Modifier.height(8.dp))


            FlatButton(
                text = "Sign Up",
                onClick = {
                    authViewModel.createStudent(onLoading = {
                        authViewModel.updateLoadingStatus(it)
                    },
                        onStudentCreated = {
                            onNavigationRequested(Screen.Login.route, false)
                        }, onStudentNotCreated = { error ->
                            errorMessage.value = error
                            showSnackbar = true
                        })
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { onNavigationRequested(Screen.Login.route, false) }) {
                    Text(" Have Account? Login Instead")
                }


            }
            if (showSnackbar) {
                CustomSnackbar(
                    message = errorMessage.value,
                    actionLabel = "",
                    onActionClick = {
                        // Handle action click
                        showSnackbar = false
                    },
                    onDismiss = {
                        // Handle dismiss
                        showSnackbar = false
                    }
                )
            }
        }
        if (showLoading.value) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }

    }
}
