package com.komzak.wedriveevaluationassignment.presentation.ui.addcard

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryTextColor
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteTextColor
import com.komzak.wedriveevaluationassignment.presentation.ui.components.KeypadButton
import com.komzak.wedriveevaluationassignment.utils.CreditCardVisualTransformation
import com.komzak.wedriveevaluationassignment.utils.ExpiryDateVisualTransformation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddCardScreen(
    navController: NavController,
    viewModel: AddCardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }


    AddScreenContent(
        uiState = uiState,
        onBack = { navController.popBackStack() },
        onKeypadInput = viewModel::handleKeypadInput,
        addCard = viewModel::addCard
    )
}

@Composable
private fun AddScreenContent(
    uiState: AddCardUiState,
    onBack: () -> Unit,
    onKeypadInput: (String) -> Unit,
    addCard: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onBack() },
                shape = CircleShape,
                color = whiteColor,
                shadowElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_left),
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.add_card_title),
                color = primaryTextColor,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(Modifier.padding(16.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = whiteColor,
            shadowElevation = 2.dp
        ) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .background(secondaryBackground, shape = RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.cardNumber,
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        placeholder = { Text("0000 0000 0000 0000", color = Color.Gray) },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = if (uiState.cardNumber.isEmpty()) Color.Gray else Color.Black,
                            fontWeight = if (uiState.cardNumber.isEmpty()) FontWeight.Normal else FontWeight.Bold
                        ),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray,
                            disabledBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = Color.Transparent
                        ),
                        visualTransformation = if (uiState.cardNumber.isEmpty()) VisualTransformation.None
                        else CreditCardVisualTransformation()
                    )

                    Spacer(Modifier.padding(8.dp))

                    OutlinedTextField(
                        value = uiState.expiryDate,
                        onValueChange = {},
                        modifier = Modifier.width(100.dp),
                        readOnly = true,
                        placeholder = { Text("MM/YY", color = Color.Gray) },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = if (uiState.expiryDate.isEmpty()) Color.Gray else Color.Black,
                            fontWeight = if (uiState.expiryDate.isEmpty()) FontWeight.Normal else FontWeight.Bold
                        ),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray,
                            disabledBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = Color.Transparent
                        ),
                        visualTransformation = if (uiState.expiryDate.isEmpty()) VisualTransformation.None
                        else ExpiryDateVisualTransformation()
                    )
                }
            }
        }

        Spacer(Modifier.padding(40.dp))

        Button(
            onClick = { addCard() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.isFormFilled) Color.Black else Color.Gray.copy(alpha = 0.3f),
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = uiState.isFormFilled && !uiState.isLoading
        ) {
            if (uiState.isLoading) {
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(9) { index ->
                val number = (index + 1).toString()
                KeypadButton(number) { onKeypadInput(number) }
            }

            item {
                Spacer(Modifier.size(48.dp))
            }
            item {
                KeypadButton("0") { onKeypadInput("0") }
            }
            item {
                KeypadButton(
                    icon = R.drawable.ic_backspace
                ) {
                    onKeypadInput("backspace")
                }
            }
        }
    }
}