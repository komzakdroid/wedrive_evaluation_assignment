package com.komzak.wedriveevaluationassignment.presentation.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.komzak.wedriveevaluationassignment.presentation.theme.selectedColor
import com.komzak.wedriveevaluationassignment.presentation.theme.unselectedColor
import com.komzak.wedriveevaluationassignment.presentation.theme.whiteColor

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val switchWidth = 44.dp
    val switchHeight = 24.dp
    val toggleSize = 20.dp

    val toggleOffset by animateDpAsState(
        targetValue = if (checked) switchWidth - toggleSize - 2.dp else 2.dp,
        label = "Toggle Animation"
    )

    val trackColor = if (checked) selectedColor else unselectedColor

    Box(
        modifier = modifier
            .size(width = switchWidth, height = switchHeight)
            .clip(RoundedCornerShape(switchHeight / 2))
            .background(trackColor)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = toggleOffset)
                .size(toggleSize)
                .clip(CircleShape)
                .background(whiteColor)
        )
    }
}