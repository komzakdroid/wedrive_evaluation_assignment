package com.komzak.wedriveevaluationassignment.presentation.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.komzak.wedriveevaluationassignment.R
import com.komzak.wedriveevaluationassignment.data.local.DataStoreHelper
import com.komzak.wedriveevaluationassignment.presentation.navigation.NavRoute
import com.komzak.wedriveevaluationassignment.presentation.theme.primaryColor
import com.komzak.wedriveevaluationassignment.presentation.ui.dashboard.getActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

// Profile Screen Constants
object ProfileScreenConstants {
    val ScreenPadding = 16.dp
    val CardPadding = 16.dp
    val CompactCardPadding = 12.dp
    val CardCornerRadius = 12.dp
    val LargeCardCornerRadius = 16.dp
    val AvatarSize = 80.dp
    val CompactAvatarSize = 40.dp
    val FabSize = 56.dp
    val SpacingTiny = 4.dp
    val SpacingSmall = 8.dp
    val SpacingMedium = 12.dp
    val SpacingLarge = 16.dp
    val SpacingXLarge = 24.dp
}

// Gradient backgrounds consistent with HomeScreen
private val profileBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2)
    )
)

private val glassmorphismBrush = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.05f)
    )
)

private val cardGradient = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.95f),
        Color.White.copy(alpha = 0.85f)
    )
)

// Sample data models (replace with your actual models)
data class BalanceItem(
    val id: Int,
    val currencyType: String,
    val balance: Double,
    val isActive: Boolean = true
)

data class CityItem(
    val id: Int,
    val name: String,
    val isActive: Boolean = true
)

data class UserItem(
    val id: Int,
    val name: String,
    val role: String,
    val isActive: Boolean = true
)

enum class ManagementType {
    BALANCE, CITY, USER
}

enum class DialogType {
    ADD, EDIT, DELETE, LOGOUT
}

@Composable
fun ProfileScreen(
    navController: NavController,
    dataStoreHelper: DataStoreHelper = koinInject()
) {
    val view = LocalView.current
    val window = view.context.getActivity()?.window

    // Set status bar color consistent with HomeScreen
    androidx.compose.runtime.LaunchedEffect(Unit) {
        window?.let {
            it.statusBarColor = 0xFF667eea.toInt()
            val controller = WindowCompat.getInsetsController(it, view)
            controller.isAppearanceLightStatusBars = false
        }
    }

    val phoneNumber by dataStoreHelper.getPhoneNumber().collectAsState(initial = null)

    // Dialog states
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.ADD) }
    var managementType by remember { mutableStateOf(ManagementType.BALANCE) }
    var selectedItemId by remember { mutableStateOf<Int?>(null) }

    // Sample data (replace with actual data from ViewModels)
    val balances = remember {
        listOf(
            BalanceItem(1, "USD", 1500.00),
            BalanceItem(2, "UZS", 150000.00),
            BalanceItem(3, "EUR", 800.00)
        )
    }

    val cities = remember {
        listOf(
            CityItem(1, "Toshkent"),
            CityItem(2, "Samarqand"),
            CityItem(3, "Buxoro")
        )
    }

    val users = remember {
        listOf(
            UserItem(1, "Adminь", "Administrator"),
            UserItem(2, "Manager", "Manager"),
            UserItem(3, "User", "Employee")
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(profileBackgroundGradient)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = ProfileScreenConstants.ScreenPadding,
                end = ProfileScreenConstants.ScreenPadding,
                top = ProfileScreenConstants.SpacingLarge,
                bottom = ProfileScreenConstants.FabSize + ProfileScreenConstants.ScreenPadding * 2
            ),
            verticalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingMedium)
        ) {
            // Profile Header
            item {
                ProfileHeader(phoneNumber = phoneNumber ?: "Loading...")
            }

            // Management Sections
            item {
                Text(
                    text = "Бошқарув панели",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = ProfileScreenConstants.SpacingSmall)
                )
            }

            // Balance Management Section
            item {
                ManagementSectionCard(
                    title = "Баланслар",
                    icon = Icons.Outlined.AccountBox,
                    onAddClick = {
                        managementType = ManagementType.BALANCE
                        dialogType = DialogType.ADD
                        showDialog = true
                    }
                )
            }

            /*   items(balances) { balance ->
                   BalanceManagementItem(
                       balance = balance,
                       onEditClick = {
                           managementType = ManagementType.BALANCE
                           dialogType = DialogType.EDIT
                           selectedItemId = balance.id
                           showDialog = true
                       },
                       onDeleteClick = {
                           managementType = ManagementType.BALANCE
                           dialogType = DialogType.DELETE
                           selectedItemId = balance.id
                           showDialog = true
                       }
                   )
               }*/

            // City Management Section
            item {
                Spacer(modifier = Modifier.height(ProfileScreenConstants.SpacingLarge))
                ManagementSectionCard(
                    title = "Шаҳарлар",
                    icon = Icons.Outlined.LocationOn,
                    onAddClick = {
                        managementType = ManagementType.CITY
                        dialogType = DialogType.ADD
                        showDialog = true
                    }
                )
            }

            /*        items(cities) { city ->
                        CityManagementItem(
                            city = city,
                            onEditClick = {
                                managementType = ManagementType.CITY
                                dialogType = DialogType.EDIT
                                selectedItemId = city.id
                                showDialog = true
                            },
                            onDeleteClick = {
                                managementType = ManagementType.CITY
                                dialogType = DialogType.DELETE
                                selectedItemId = city.id
                                showDialog = true
                            }
                        )
                    }*/

            // User Management Section
            item {
                Spacer(modifier = Modifier.height(ProfileScreenConstants.SpacingLarge))
                ManagementSectionCard(
                    title = "Фойдаланувчилар",
                    icon = Icons.Outlined.Person,
                    onAddClick = {
                        managementType = ManagementType.USER
                        dialogType = DialogType.ADD
                        showDialog = true
                    }
                )
            }

            /* items(users) { user ->
                 UserManagementItem(
                     user = user,
                     onEditClick = {
                         managementType = ManagementType.USER
                         dialogType = DialogType.EDIT
                         selectedItemId = user.id
                         showDialog = true
                     },
                     onDeleteClick = {
                         managementType = ManagementType.USER
                         dialogType = DialogType.DELETE
                         selectedItemId = user.id
                         showDialog = true
                     }
                 )
             }*/

            // Logout Section
            item {
                Spacer(modifier = Modifier.height(ProfileScreenConstants.SpacingXLarge))
                LogoutButton(
                    onLogoutClick = {
                        dialogType = DialogType.LOGOUT
                        showDialog = true
                    }
                )
            }
        }

        // Settings FAB
        AnimatedVisibility(
            visible = true,
            enter = scaleIn(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ) + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(ProfileScreenConstants.ScreenPadding)
        ) {
            FloatingActionButton(
                onClick = {
                    // Navigate to settings or show settings menu
                },
                modifier = Modifier.size(ProfileScreenConstants.FabSize),
                shape = CircleShape,
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Созламалар",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Dialogs
        if (showDialog) {
            when (dialogType) {
                DialogType.ADD -> AddItemDialog(
                    managementType = managementType,
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        // Handle add logic
                        showDialog = false
                    }
                )

                DialogType.EDIT -> EditItemDialog(
                    managementType = managementType,
                    itemId = selectedItemId,
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        // Handle edit logic
                        showDialog = false
                    }
                )

                DialogType.DELETE -> DeleteConfirmationDialog(
                    managementType = managementType,
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        // Handle delete logic
                        showDialog = false
                    }
                )

                DialogType.LOGOUT -> LogoutConfirmationDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStoreHelper.clearCache()
                            CoroutineScope(Dispatchers.Main).launch {
                                navController.navigate(NavRoute.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(phoneNumber: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ProfileScreenConstants.LargeCardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(glassmorphismBrush)
                .padding(ProfileScreenConstants.CardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingMedium)
                ) {
                    Box(
                        modifier = Modifier
                            .size(ProfileScreenConstants.AvatarSize)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_hisobchi),
                            contentDescription = "Профиль расми",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Профиль",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = phoneNumber,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "Ҳисобчи тизими",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ManagementSectionCard(
    title: String,
    icon: ImageVector,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ProfileScreenConstants.CardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingMedium)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(primaryColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = primaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.9f)
                )
            }

            IconButton(
                onClick = onAddClick,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50).copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Қўшиш",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun BalanceManagementItem(
    balance: BalanceItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ProfileScreenConstants.CompactCardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingMedium),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = balance.currencyType.first().toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }

                Column {
                    Text(
                        text = balance.currencyType,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.9f)
                    )
                    Text(
                        text = String.format("%.2f", balance.balance),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingTiny)
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Таҳрирлаш",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Ўчириш",
                        tint = Color(0xFFF44336),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CityManagementItem(
    city: CityItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ProfileScreenConstants.CompactCardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingMedium),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Шаҳар",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = city.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.9f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingTiny)
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Таҳрирлаш",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Ўчириш",
                        tint = Color(0xFFF44336),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun UserManagementItem(
    user: UserItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ProfileScreenConstants.CompactCardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingMedium),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9C27B0).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Фойдаланувчи",
                        tint = Color(0xFF9C27B0),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.9f)
                    )
                    Text(
                        text = user.role,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingTiny)
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Таҳрирлаш",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Ўчириш",
                        tint = Color(0xFFF44336),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LogoutButton(onLogoutClick: () -> Unit) {
    // State for animation on click
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f, // Subtler scale effect
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale) // Subtle scale animation
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color(0xFFF44336)) // Ripple effect
            ) { onLogoutClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp), // Minimal external padding
        shape = RoundedCornerShape(12.dp), // Softer, smaller corners
        color = Color(0xFFF44336).copy(alpha = 0.2f), // Very subtle red background
        contentColor = Color(0xFFF44336) // Consistent content color
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp), // Compact vertical padding
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Logout",
                tint = Color(0xFFF44336),
                modifier = Modifier.size(24.dp) // Smaller icon for minimalism
            )
            Spacer(modifier = Modifier.width(8.dp)) // Tighter spacing
            Text(
                text = "Logout",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp, // Smaller, cleaner text
                    fontWeight = FontWeight.Medium // Less bold for simplicity
                ),
                color = Color(0xFFF44336)
            )
        }
    }
}

// Dialog Composables
@Composable
private fun AddItemDialog(
    managementType: ManagementType,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var inputText2 by remember { mutableStateOf("") }

    val title = when (managementType) {
        ManagementType.BALANCE -> "Баланс қўшиш"
        ManagementType.CITY -> "Шаҳар қўшиш"
        ManagementType.USER -> "Фойдаланувчи қўшиш"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(ProfileScreenConstants.LargeCardCornerRadius),
        containerColor = Color.White,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingMedium)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = {
                        Text(
                            when (managementType) {
                                ManagementType.BALANCE -> "Валюта тури"
                                ManagementType.CITY -> "Шаҳар номи"
                                ManagementType.USER -> "Фойдаланувчи номи"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor
                    )
                )

                if (managementType == ManagementType.BALANCE) {
                    OutlinedTextField(
                        value = inputText2,
                        onValueChange = { inputText2 = it },
                        label = { Text("Бошланғич баланс") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor
                        )
                    )
                } else if (managementType == ManagementType.USER) {
                    OutlinedTextField(
                        value = inputText2,
                        onValueChange = { inputText2 = it },
                        label = { Text("Лавозим") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius)
            ) {
                Text(
                    text = "Қўшиш",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius)
            ) {
                Text(
                    text = "Бекор қилиш",
                    color = primaryColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
private fun EditItemDialog(
    managementType: ManagementType,
    itemId: Int?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var inputText2 by remember { mutableStateOf("") }

    val title = when (managementType) {
        ManagementType.BALANCE -> "Балансни таҳрирлаш"
        ManagementType.CITY -> "Шаҳарни таҳрирлаш"
        ManagementType.USER -> "Фойдаланувчини таҳрирлаш"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(ProfileScreenConstants.LargeCardCornerRadius),
        containerColor = Color.White,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(ProfileScreenConstants.SpacingMedium)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = {
                        Text(
                            when (managementType) {
                                ManagementType.BALANCE -> "Валюта тури"
                                ManagementType.CITY -> "Шаҳар номи"
                                ManagementType.USER -> "Фойдаланувчи номи"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor
                    )
                )

                if (managementType == ManagementType.BALANCE) {
                    OutlinedTextField(
                        value = inputText2,
                        onValueChange = { inputText2 = it },
                        label = { Text("Баланс миқдори") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor
                        )
                    )
                } else if (managementType == ManagementType.USER) {
                    OutlinedTextField(
                        value = inputText2,
                        onValueChange = { inputText2 = it },
                        label = { Text("Лавозим") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius)
            ) {
                Text(
                    text = "Сақлаш",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius)
            ) {
                Text(
                    text = "Бекор қилиш",
                    color = primaryColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
private fun DeleteConfirmationDialog(
    managementType: ManagementType,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val itemName = when (managementType) {
        ManagementType.BALANCE -> "балансни"
        ManagementType.CITY -> "шаҳарни"
        ManagementType.USER -> "фойдаланувчини"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(ProfileScreenConstants.LargeCardCornerRadius),
        containerColor = Color.White,
        icon = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF44336).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Огоҳлантириш",
                    tint = Color(0xFFF44336),
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = {
            Text(
                text = "Ўчиришни тасдиқланг",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "Ҳақиқатан ҳам ушбу $itemName ўчирмоқчимисиз? Бу амал бекор қилиб бўлмайди.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius)
            ) {
                Text(
                    text = "Ўчириш",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius)
            ) {
                Text(
                    text = "Бекор қилиш",
                    color = Color.Black.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
private fun LogoutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(ProfileScreenConstants.LargeCardCornerRadius),
        containerColor = Color.White,
        icon = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF44336).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Чиқиш",
                    tint = Color(0xFFF44336),
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        title = {
            Text(
                text = "Тизимдан чиқиш",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "Ҳақиқатан ҳам тизимдан чиқмоқчимисиз? Сиз қайтадан кириш учун логин қилишингиз керак бўлади.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius)
            ) {
                Text(
                    text = "Чиқиш",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(ProfileScreenConstants.CardCornerRadius)
            ) {
                Text(
                    text = "Бекор қилиш",
                    color = Color.Black.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}