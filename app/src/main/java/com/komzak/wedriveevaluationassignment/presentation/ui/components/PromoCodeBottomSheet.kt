package com.komzak.wedriveevaluationassignment.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.presentation.theme.bottomSheetColor
import com.komzak.wedriveevaluationassignment.presentation.theme.disableColor
import com.komzak.wedriveevaluationassignment.presentation.theme.enableColor
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryTextColor
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoCodeBottomSheet(
    promoCode: String,
    onPromoCodeChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    sheetState: SheetState,
    isLoading: Boolean
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = bottomSheetColor,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bottomSheetColor)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(
                            color = whiteColor,
                            shape = CircleShape
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_left),
                        contentDescription = null
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.enter_promo),
                    color = primaryTextColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }


            val focusRequester = FocusRequester()

            TextField(
                value = promoCode,
                onValueChange = onPromoCodeChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.Characters
                ),
                visualTransformation = VisualTransformation { text ->
                    TransformedText(
                        text = AnnotatedString(text.text.uppercase()),
                        offsetMapping = OffsetMapping.Identity
                    )
                },
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