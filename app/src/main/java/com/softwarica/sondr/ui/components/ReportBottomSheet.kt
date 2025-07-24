package com.softwarica.sondr.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.ui.theme.InterFont
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    onDismissRequest: () -> Unit,
    onSubmitReport: (reason: String, details: String) -> Unit,
) {
    val reasons = listOf("Spam", "Harassment", "Inappropriate Content", "Other")
    var selectedReason by remember { mutableStateOf(reasons[0]) }
    var details by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFF121212), // Background color
        tonalElevation = 0.dp,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding() // helps keyboard visibility
                .imePadding() // fixes keyboard overlap
                .verticalScroll(rememberScrollState()) // makes it scrollable
        ) {
            Text(
                text = "Report Post",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = InterFont,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Reason", color = Color.White, fontFamily = InterFont)
            reasons.forEach { reason ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedReason = reason },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (reason == selectedReason),
                        onClick = { selectedReason = reason },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(text = reason, color = Color.White, fontFamily = InterFont)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Additional Details (optional)", color = Color.White, fontFamily = InterFont)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = details,
                onValueChange = { details = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                shape = RoundedCornerShape(8.dp),
                singleLine = false,
                placeholder = {
                    Text(
                        text = "Enter any details...",
                        color = Color.White.copy(alpha = 0.5f),
                        fontFamily = InterFont,
                        fontSize = 16.sp
                    )
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = InterFont
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E),
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions.Default,
                maxLines = 5
            )


            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel", color = Color.Gray, fontFamily = InterFont)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        onSubmitReport(selectedReason, details)
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Submit", color = Color.Black, fontFamily = InterFont)
                }
            }
        }
    }
}


@Composable
@Preview
fun ReportPreview() {
    ReportBottomSheet(
        onDismissRequest = {},
        onSubmitReport = { reason, details ->
            // handle report submission
        }
    )
}
