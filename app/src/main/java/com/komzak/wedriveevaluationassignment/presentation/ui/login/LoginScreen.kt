package com.komzak.wedriveevaluationassignment.presentation.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.presentation.navigation.NavRoute
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.theme.quaternaryTextColor
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle error messages
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            navController.navigate(NavRoute.Dashboard.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
            viewModel.clearSuccess() // Reset isSuccess to prevent repeated navigation
        }
    }

    LoginScreenContent(
        uiState = uiState,
        onPhoneChange = { viewModel.onPhoneNumberChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onLoginClick = { viewModel.login() }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LoginScreenContent(
    uiState: LoginUiState,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_pocket_logo),
            contentDescription = null,
            modifier = Modifier.size(165.dp)
        )

        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.headlineMedium,
            color = primaryColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Phone number input
        OutlinedTextField(
            value = uiState.phone,
            onValueChange = onPhoneChange,
            prefix = { Text("+998") },
            placeholder = { Text("000 00 00") },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = primaryColor,
                focusedIndicatorColor = primaryColor,
                unfocusedIndicatorColor = secondaryBackground,
                focusedContainerColor = secondaryBackground,
                unfocusedContainerColor = secondaryBackground,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.error
            ),
            label = { Text(stringResource(R.string.phone_number)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password input
        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = primaryColor,
                focusedIndicatorColor = primaryColor,
                unfocusedIndicatorColor = secondaryBackground,
                focusedContainerColor = secondaryBackground,
                unfocusedContainerColor = secondaryBackground,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.error
            ),
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            trailingIcon = {
                val icon = if (passwordVisible)
                    painterResource(R.drawable.ic_eye_open)
                else
                    painterResource(R.drawable.ic_eye_closed)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = icon,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = primaryColor
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Login button
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = whiteColor,
                disabledContainerColor = quaternaryTextColor,
                disabledContentColor = quaternaryTextColor
            ),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = whiteColor,
                    strokeWidth = 3.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.labelLarge,
                    color = whiteColor
                )
            }
        }
    }
}