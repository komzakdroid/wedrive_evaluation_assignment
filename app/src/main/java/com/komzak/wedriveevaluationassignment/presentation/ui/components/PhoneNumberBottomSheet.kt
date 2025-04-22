package com.komzak.wedriveevaluationassignment.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.presentation.theme.disableColor
import com.komzak.wedriveevaluationassignment.presentation.theme.enableColor
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberBottomSheet(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    sheetState: SheetState,
    isLoading: Boolean
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.enter_phone_number),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val focusRequester = FocusRequester()

            TextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                placeholder = { Text("00 000 00 00") },
                prefix = { Text("+998 ") },
                textStyle = MaterialTheme.typography.titleMedium,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = enableColor,
                    unfocusedIndicatorColor = enableColor,
                    cursorColor = enableColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = enableColor, disabledContainerColor = disableColor),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = whiteTextColor,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.displaySmall,
                        text = stringResource(R.string.save),
                        color = whiteTextColor
                    )
                }
            }
        }
    }
}