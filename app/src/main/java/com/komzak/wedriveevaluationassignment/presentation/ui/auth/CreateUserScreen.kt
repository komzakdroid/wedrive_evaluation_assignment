package com.komzak.wedriveevaluationassignment.presentation.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.presentation.navigation.NavRoute
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateUserScreen(
    viewModel: CreateUserVM = koinViewModel(),
    navController: NavController
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            snackbarHostState.showSnackbar("User created successfully")
            navController.navigate(NavRoute.Wallet.route) {
                popUpTo(NavRoute.CreateUser.route) { inclusive = true }
            }
        }
    }

    CreateUserScreenContent(
        uiState = uiState,
        onCreateUser = viewModel::createUser,
        onPhoneEdit = viewModel::onPhoneEdit,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun CreateUserScreenContent(
    uiState: CreateUserUiState,
    onCreateUser: (String) -> Unit,
    onPhoneEdit: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = uiState.phone,
                onValueChange = { newValue ->
                    onPhoneEdit(newValue)
                },
                label = { Text("Phone Number") },
                placeholder = { Text("00 000 00 00") },
                prefix = { Text("+998 ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                isError = uiState.errorMessage != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    val phoneWithPrefix = "+998${uiState.phone.replace(" ", "")}"
                    onCreateUser(phoneWithPrefix)
                },
                enabled = !uiState.isLoading && uiState.phone.replace(" ", "").length == 9,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.height(24.dp))
                } else {
                    Text("Create User")
                }
            }
        }
    }
}