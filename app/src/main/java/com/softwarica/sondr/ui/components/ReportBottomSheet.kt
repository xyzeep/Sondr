package com.softwarica.sondr.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    onDismissRequest: () -> Unit,
    onSubmitReport: (reason: String, details: String) -> Unit,
) {
    val reasons = listOf("Spam", "Harassment", "Inappropriate Content", "Other")
    var selectedReason by remember { mutableStateOf(reasons[0]) }
    var details by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Report Post", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Reason")
            reasons.forEach { reason ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedReason = reason },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (reason == selectedReason),
                        onClick = { selectedReason = reason }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = reason)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Additional Details (optional)")
            TextField(
                value = details,
                onValueChange = { details = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = { Text("Enter any details...") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        onSubmitReport(selectedReason, details)
                        onDismissRequest()
                    }
                ) {
                    Text("Submit")
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
            // Handle report submission
        }
    )
}
