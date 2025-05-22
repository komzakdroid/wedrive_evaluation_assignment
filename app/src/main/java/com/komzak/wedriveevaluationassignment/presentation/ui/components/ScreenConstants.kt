package com.komzak.wedriveevaluationassignment.presentation.ui.components

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.komzak.wedriveevaluationassignment.presentation.theme.secondaryBackground

object ScreenConstants {
    val ScreenPadding = 16.dp
    val CardPadding = 8.dp
    val CardCornerRadius = 16.dp
    val AvatarSize = 48.dp
    val FabSize = 56.dp
    val SpacingSmall = 8.dp
    val SpacingMedium = 16.dp
    val SpacingLarge = 24.dp
}

private val gradientBackground = Brush.verticalGradient(
    colors = listOf(
        secondaryBackground,
        secondaryBackground.copy(alpha = 0.8f)
    )
)
