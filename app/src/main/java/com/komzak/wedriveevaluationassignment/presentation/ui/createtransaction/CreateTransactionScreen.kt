package com.komzak.wedriveevaluationassignment.presentation.ui.createtransaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.ui.dashboard.getActivity
import org.koin.androidx.compose.koinViewModel

object CreateTransactionConstants {
    val ScreenPadding = 16.dp
    val CardPadding = 12.dp
    val CardCornerRadius = 12.dp
    val LargeCardCornerRadius = 16.dp
    val FilterChipCornerRadius = 10.dp
    val SpacingTiny = 4.dp
    val SpacingSmall = 8.dp
    val SpacingMedium = 12.dp
    val SpacingLarge = 16.dp
    val SpacingXLarge = 20.dp
}

private val modernBackground = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2),
        Color(0xFFF093fb)
    )
)

private val whiteGlassCard = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.95f),
        Color.White.copy(alpha = 0.85f)
    )
)

@Composable
fun CreateTransactionScreen(
    viewModel: CreateTransactionViewModel = koinViewModel(),
    navController: NavController
) {
    val view = LocalView.current
    val window = view.context.getActivity()?.window

    LaunchedEffect(Unit) {
        window?.let {
            it.statusBarColor = 0xFF667eea.toInt()
            val controller = WindowCompat.getInsetsController(it, view)
            controller.isAppearanceLightStatusBars = false
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateTransactionContent(
        uiState = uiState,
        onAmountChange = { viewModel.updateAmount(it) },
        onServiceFeeChange = { viewModel.updateServiceFee(it) },
        onPhoneChange = { viewModel.updatePhone(it) },
        onReceiverNameChange = { viewModel.updateReceiverName(it) },
        onDetailsChange = { viewModel.updateDetails(it) },
        onTypeChange = { viewModel.updateType(it) },
        onUserSelected = { viewModel.selectUser(it) },
        onBalanceSelected = { viewModel.selectBalance(it) },
        onFromCitySelected = { viewModel.selectFromCity(it) },
        onToCitySelected = { viewModel.selectToCity(it) },
        onSubmit = { viewModel.createTransaction() },
        onDismissDialog = { viewModel.resetSuccess() },
        onClearError = { viewModel.clearError() },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateTransactionContent(
    uiState: CreateTransactionUiState,
    onAmountChange: (String) -> Unit,
    onServiceFeeChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onReceiverNameChange: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    onTypeChange: (Int) -> Unit,
    onUserSelected: (Int) -> Unit,
    onBalanceSelected: (Int) -> Unit,
    onFromCitySelected: (Int) -> Unit,
    onToCitySelected: (Int) -> Unit,
    onSubmit: () -> Unit,
    onDismissDialog: () -> Unit,
    onClearError: () -> Unit,
    navController: NavController
) {
    var showSuccessDialog by remember { mutableStateOf(uiState.isSuccess) }

    LaunchedEffect(uiState.isSuccess) {
        showSuccessDialog = uiState.isSuccess
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(modernBackground),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Транзакция Яратиш", // Translated: Create Transaction
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Орқага", // Translated: Back
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF667eea),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(CreateTransactionConstants.ScreenPadding)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(CreateTransactionConstants.SpacingLarge)
        ) {
            // Amount Input
            InputCard(
                title = "Сумма", // Translated: Amount
                value = uiState.amount,
                onValueChange = {
                    if (it.isEmpty() || it.toDoubleOrNull() != null) {
                        onAmountChange(it)
                    }
                },
                isError = uiState.errorMessage != null && (uiState.amount.isEmpty() || uiState.amount.toDoubleOrNull() == null),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                icon = Icons.Default.CheckCircle,
                supportingText = if (uiState.amount.isNotEmpty() && uiState.amount.toDoubleOrNull() == null) {
                    "Рақам киритинг" // Translated: Enter a valid number
                } else null
            )

            // Service Fee Input
            InputCard(
                title = "Хизмат ҳақи", // Translated: Service Fee
                value = uiState.serviceFee,
                onValueChange = {
                    if (it.isEmpty() || it.toDoubleOrNull() != null) {
                        onServiceFeeChange(it)
                    }
                },
                isError = uiState.errorMessage != null && (uiState.serviceFee.isEmpty() || uiState.serviceFee.toDoubleOrNull() == null),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                icon = Icons.Default.CheckCircle,
                supportingText = if (uiState.serviceFee.isNotEmpty() && uiState.serviceFee.toDoubleOrNull() == null) {
                    "Рақам киритинг" // Translated: Enter a valid number
                } else null
            )

            // Receiver Name Input
            InputCard(
                title = "Қабул қилувчи исми", // Translated: Receiver Name
                value = uiState.receiverName,
                onValueChange = onReceiverNameChange,
                isError = uiState.errorMessage != null && uiState.receiverName.isEmpty(),
                icon = Icons.Default.CheckCircle,
                supportingText = if (uiState.errorMessage != null && uiState.receiverName.isEmpty()) {
                    "Исми киритинг" // Translated: Enter a name
                } else null
            )

            // Receiver Phone Input
            InputCard(
                title = "Қабул қилувчи телефони", // Translated: Receiver Phone
                value = uiState.phone,
                onValueChange = onPhoneChange,
                isError = uiState.errorMessage != null && uiState.phone.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                icon = Icons.Default.CheckCircle,
                supportingText = if (uiState.errorMessage != null && uiState.phone.isEmpty()) {
                    "Телефон рақамини киритинг" // Translated: Enter a phone number
                } else null
            )

            // Details Input
            InputCard(
                title = "Тафсилотлар", // Translated: Details
                value = uiState.details,
                onValueChange = onDetailsChange,
                isError = uiState.errorMessage != null && uiState.details.isEmpty(),
                icon = Icons.Default.CheckCircle,
                supportingText = if (uiState.errorMessage != null && uiState.details.isEmpty()) {
                    "Тафсилотларни киритинг" // Translated: Enter details
                } else null
            )

            // Type Selection (Sotish/Olish)
            TypeSelection(
                isSotishSelected = uiState.type == 1,
                isOlishSelected = uiState.type == 2,
                onSotishClick = { onTypeChange(1) },
                onOlishClick = { onTypeChange(2) }
            )

            // User Dropdown
            ModernDropdown(
                label = "Фойдаланувчини танлаш", // Translated: Select User
                selected = uiState.users.find { it.id == uiState.selectedUserId }?.username,
                items = uiState.users.map { it.username },
                onItemSelected = { username ->
                    val user = uiState.users.find { it.username == username }
                    user?.id?.let { onUserSelected(it) }
                },
                icon = Icons.Default.CheckCircle,
                isError = uiState.errorMessage != null && uiState.selectedUserId == null,
                placeholder = "Фойдаланувчини танланг" // Translated: Select a user
            )

            // Balance Dropdown
            ModernDropdown(
                label = "Балансни танлаш", // Translated: Select Balance
                selected = uiState.balances.find { it.id == uiState.selectedBalanceId }?.currencyType,
                items = uiState.balances.map { it.currencyType ?: "Unknown" },
                onItemSelected = { currencyType ->
                    val balance = uiState.balances.find { it.currencyType == currencyType }
                    balance?.id?.let { onBalanceSelected(it) }
                },
                icon = Icons.Default.CheckCircle,
                isError = uiState.errorMessage != null && uiState.selectedBalanceId == null,
                placeholder = "Балансни танланг" // Translated: Select a balance
            )

            // From City Dropdown
            ModernDropdown(
                label = "Жўнатувчи шаҳар", // Translated: From City
                selected = uiState.cities.find { it.id == uiState.selectedFromCityId }?.name,
                items = uiState.cities.map { it.name },
                onItemSelected = { cityName ->
                    val city = uiState.cities.find { it.name == cityName }
                    city?.id?.let { onFromCitySelected(it) }
                },
                icon = Icons.Default.CheckCircle,
                isError = uiState.errorMessage != null && uiState.selectedFromCityId == null,
                placeholder = "Шаҳарни танланг" // Translated: Select a city
            )

            // To City Dropdown
            ModernDropdown(
                label = "Қабул қилувчи шаҳар", // Translated: To City
                selected = uiState.cities.find { it.id == uiState.selectedToCityId }?.name,
                items = uiState.cities.map { it.name },
                onItemSelected = { cityName ->
                    val city = uiState.cities.find { it.name == cityName }
                    city?.id?.let { onToCitySelected(it) }
                },
                icon = Icons.Default.CheckCircle,
                isError = uiState.errorMessage != null && uiState.selectedToCityId == null,
                placeholder = "Шаҳарни танланг" // Translated: Select a city
            )

            // Submit Button
            AnimatedVisibility(
                visible = !uiState.isLoading,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(CreateTransactionConstants.CardCornerRadius),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Яратиш", // Translated: Create
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Error Message
            uiState.errorMessage?.let { error ->
                ErrorCard(errorMessage = error)
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismissDialog()
                navController.popBackStack()
            },
            title = {
                Text(
                    text = "Муваффакият", // Translated: Success
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
            },
            text = {
                Text(
                    text = "Транзакция муваффакиятли яратилди!", // Translated: Transaction created successfully!
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismissDialog()
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = primaryColor)
                ) {
                    Text("OK", style = MaterialTheme.typography.labelMedium)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(CreateTransactionConstants.LargeCardCornerRadius)
        )
    }

    // Clear error when user starts interacting
    LaunchedEffect(
        uiState.amount,
        uiState.serviceFee,
        uiState.phone,
        uiState.receiverName,
        uiState.details,
        uiState.selectedUserId,
        uiState.selectedBalanceId,
        uiState.selectedFromCityId,
        uiState.selectedToCityId
    ) {
        if (uiState.errorMessage != null) {
            onClearError()
        }
    }
}

@Composable
private fun InputCard(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    icon: ImageVector,
    supportingText: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CreateTransactionConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(CreateTransactionConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(CreateTransactionConstants.SpacingSmall)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(CreateTransactionConstants.SpacingTiny)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(16.dp),
                        tint = primaryColor
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = primaryColor
                    )
                }
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError,
                    keyboardOptions = keyboardOptions,
                    supportingText = supportingText?.let {
                        {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFF44336)
                            )
                        }
                    },
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color.Black.copy(alpha = 0.1f),
                        errorBorderColor = Color(0xFFF44336),
                        cursorColor = Color.Black // Set cursor color to black
                    )
                )
            }
        }
    }
}

@Composable
private fun TypeSelection(
    isSotishSelected: Boolean,
    isOlishSelected: Boolean,
    onSotishClick: () -> Unit,
    onOlishClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CreateTransactionConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGlassCard)
                .padding(CreateTransactionConstants.CardPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(CreateTransactionConstants.SpacingSmall)
            ) {
                Text(
                    text = "Тур", // Translated: Type
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = primaryColor
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(CreateTransactionConstants.SpacingSmall)
                ) {
                    FilterChipButton(
                        label = "Сотиш", // Translated: Sotish
                        icon = Icons.Default.CheckCircle,
                        isSelected = isSotishSelected,
                        onClick = onSotishClick,
                        modifier = Modifier.weight(1f)
                    )
                    FilterChipButton(
                        label = "Олиш", // Translated: Olish
                        icon = Icons.Default.CheckCircle,
                        isSelected = isOlishSelected,
                        onClick = onOlishClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChipButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) primaryColor else Color(0xFFE0E0E0)
    val contentColor = if (isSelected) Color.White else Color.DarkGray

    Card(
        modifier = modifier
            .padding(2.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(CreateTransactionConstants.FilterChipCornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = CreateTransactionConstants.SpacingSmall,
                vertical = CreateTransactionConstants.SpacingTiny
            ),
            horizontalArrangement = Arrangement.spacedBy(CreateTransactionConstants.SpacingTiny),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = contentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ModernDropdown(
    label: String,
    selected: String?,
    items: List<String>,
    onItemSelected: (String?) -> Unit,
    icon: ImageVector,
    isError: Boolean,
    placeholder: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CreateTransactionConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = if (isError) BorderStroke(0.5.dp, Color(0xFFF44336)) else BorderStroke(
            0.5.dp,
            Color.Black.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .background(whiteGlassCard)
                .padding(CreateTransactionConstants.CardPadding),
            verticalArrangement = Arrangement.spacedBy(CreateTransactionConstants.SpacingTiny)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(CreateTransactionConstants.SpacingTiny)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(16.dp),
                    tint = primaryColor
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = primaryColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Black.copy(alpha = 0.6f)
                )
            }
            Text(
                text = selected ?: placeholder,
                style = MaterialTheme.typography.bodySmall,
                color = if (selected == null) Color.Gray else Color.Black
            )
        }

        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(CreateTransactionConstants.CardCornerRadius))
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                },
                onClick = {
                    onItemSelected(null)
                    expanded = false
                }
            )
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(errorMessage: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CreateTransactionConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CreateTransactionConstants.CardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CreateTransactionConstants.SpacingTiny)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Хато", // Translated: Error
                tint = Color(0xFFF44336),
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE65100)
            )
        }
    }
}