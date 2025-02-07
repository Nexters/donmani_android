package com.gowoon.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.ui.noRippleClickable

enum class CircleButtonSize(val size: Dp) {
    Small(32.dp), Big(70.dp)
}

@Composable
fun PlusCircleButton(
    modifier: Modifier = Modifier,
    buttonSize: CircleButtonSize,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(buttonSize.size)
            .background(
                shape = CircleShape,
                color = backgroundColor
            )
            .noRippleClickable { onClick() }
    ) {
        Icon(
            modifier = Modifier.size(24.dp).align(Alignment.Center),
            imageVector = ImageVector.vectorResource(R.drawable.plus),
            tint = contentColor,
            contentDescription = null
        )
    }
}