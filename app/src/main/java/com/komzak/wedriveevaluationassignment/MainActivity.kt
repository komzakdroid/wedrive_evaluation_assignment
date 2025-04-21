package com.komzak.wedriveevaluationassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.komzak.wedriveevaluationassignment.common.ResourceProvider
import com.komzak.wedriveevaluationassignment.presentation.navigation.AppNavigation
import com.komzak.wedriveevaluationassignment.presentation.theme.Typography
import com.komzak.wedriveevaluationassignment.utils.NetworkMonitor
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val networkMonitor: NetworkMonitor by inject()
    private val resourceProvider: ResourceProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContent(
                networkMonitor = networkMonitor,
                resourceProvider = resourceProvider
            )
        }
    }
}

@Composable
fun AppContent(
    networkMonitor: NetworkMonitor,
    resourceProvider: ResourceProvider
) {
    val isConnected by networkMonitor.isConnected.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = resourceProvider.getString(R.string.no_internet),
                    actionLabel = resourceProvider.getString(R.string.dismiss)
                )
            }
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    MaterialTheme(typography = Typography) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { snackbarData ->
                        Snackbar(
                            snackbarData = snackbarData,
                            containerColor = Color.Red,
                            contentColor = Color.White,
                            actionColor = Color.White
                        )
                    }
                )
            }
        ) { padding ->
            AppNavigation(modifier = Modifier.padding(padding))
        }
    }
}