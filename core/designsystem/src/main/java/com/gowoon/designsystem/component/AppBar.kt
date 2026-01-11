package com.gowoon.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = ImageVector.vectorResource(R.drawable.arrow_left),
    onClickNavigation: () -> Unit,
    actionButton: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit),
    applyPadding: Boolean = false
) {
    AppBarBase(
        modifier = modifier,
        navigationButton = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable { onClickNavigation() },
                imageVector = navigationIcon,
                tint = DonmaniTheme.colors.DeepBlue99,
                contentDescription = null,
            )
        },
        actionButton = actionButton,
        title = title,
        applyPadding = applyPadding
    )
}

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = ImageVector.vectorResource(R.drawable.arrow_left),
    onClickNavigation: () -> Unit,
    actionButton: (@Composable () -> Unit)? = null,
    title: String? = null,
    applyPadding: Boolean = false
) {
    AppBarBase(
        modifier = modifier,
        navigationButton = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable { onClickNavigation() },
                imageVector = navigationIcon,
                tint = DonmaniTheme.colors.DeepBlue99,
                contentDescription = null,
            )
        },
        actionButton = actionButton,
        title = title?.let{
            {
                Text(
                    text = it,
                    style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.Bold),
                    color = DonmaniTheme.colors.Gray95
                )
            }
        },
        applyPadding = applyPadding
    )
}

@Composable
fun AppBarBase(
    modifier: Modifier = Modifier,
    navigationButton: (@Composable () -> Unit)? = null,
    actionButton: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    applyPadding: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = if (applyPadding) DonmaniTheme.dimens.Margin20 else 0.dp)
    ) {
        Box(Modifier.align(Alignment.CenterStart)){
            navigationButton?.invoke()
        }
        Box(Modifier.align(Alignment.Center)){
            title?.invoke()
        }
        Box(Modifier.align(Alignment.CenterEnd)){
            actionButton?.invoke()
        }
    }
}

@Preview
@Composable
fun AppBarPreview() {
    AppBar(
        navigationIcon = ImageVector.vectorResource(R.drawable.setting),
        onClickNavigation = {},
        actionButton = {},
        title = "라벨"
    )
}