package com.gowoon.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.ui.noRippleClickable

enum class RoundedButtonRadius(val radius: Dp, val verticalPadding: Dp, val horizontalPadding: Dp) {
    Row(radius = 16.dp, verticalPadding = 16.dp, horizontalPadding = 16.dp),
    High(radius = 25.dp, verticalPadding = 8.dp, horizontalPadding = 16.dp)
}

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    type: RoundedButtonRadius,
    label: String,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = if (enable) DonmaniTheme.colors.Gray95 else DonmaniTheme.colors.DeepBlue20,
                shape = RoundedCornerShape(type.radius)
            )
            .padding(vertical = type.verticalPadding, horizontal = type.horizontalPadding)
            .noRippleClickable { if (enable) onClick() }
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = label,
            color = if (enable) DonmaniTheme.colors.DeepBlue20 else DonmaniTheme.colors.DeepBlue70,
            style = DonmaniTheme.typography.Heading3.copy(fontWeight = FontWeight.Bold)
        )
    }
}

enum class CircleButtonSize(val size: Dp) {
    Small(32.dp), Big(70.dp)
}

@Composable
fun CircleButton(
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
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
            imageVector = ImageVector.vectorResource(R.drawable.plus),
            tint = contentColor,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun ButtonPreview(){
    RoundedButton(type = RoundedButtonRadius.Row, label = "button") {

    }
}