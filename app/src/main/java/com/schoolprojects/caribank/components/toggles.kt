package com.schoolprojects.caribank.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SemesterToggle(
    currentSemester: String,
    onSemesterChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Semester:")
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { onSemesterChange("1st Semester") },
            /*colors = ButtonDefaults.buttonColors(
                containerColor = if (currentSemester == "1st Semester") Color.Gray else Color.LightGray
            )*/
        ) {
            Text("1st Semester")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { onSemesterChange("2nd Semester") },
            /*colors = ButtonDefaults.buttonColors(
                containerColor = if (currentSemester == "2nd Semester") Color.Gray else Color.LightGray
            )*/
        ) {
            Text("2nd Semester")
        }
    }
}
