package com.gowoon.record.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChip(
    resId: Int,
    size: Dp,
    radius: Dp,
    border: Dp = 0.dp
) {
    Icon(
        modifier = Modifier
            .size(size)
            .clip(shape = RoundedCornerShape(radius))
            .border(width = border, color = Color.White, shape = RoundedCornerShape(radius)),
        imageVector = ImageVector.vectorResource(resId),
        tint = Color.Unspecified,
        contentDescription = null
    )
}