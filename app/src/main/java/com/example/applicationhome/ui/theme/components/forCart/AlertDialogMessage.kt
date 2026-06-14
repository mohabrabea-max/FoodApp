package com.example.applicationhome.ui.theme.components.forCart

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.applicationhome.ui.theme.model.AddBoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogMessage(
    addBoxViewModel: AddBoxViewModel,
    resName : String,
    confirmButtonText : String,
    confirmButton : () -> Unit,
    dismissButtonText : String,
    dismissButton : () -> Unit
){
    AlertDialog(
        onDismissRequest = { addBoxViewModel.alertDialogFalse() },
        title = { Text(text = "Start a new cart?") },
        text = { Text(text = "A new order will clear your cart with '${resName}'") },
        confirmButton = {
            Button(
                onClick = { confirmButton() }
            ){Text(text = confirmButtonText)}
        },
        dismissButton = {
            Button(onClick = { dismissButton() })
            {Text(text = dismissButtonText)}
        }
    )
}