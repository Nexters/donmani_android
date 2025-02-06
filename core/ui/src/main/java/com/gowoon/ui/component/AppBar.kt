package com.gowoon.ui.component

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
import com.gowoon.ui.noRippleClickable

@Composable
fun BaseAppBar(
    navigationIcon: ImageVector,
    onClickNavigation: () -> Unit,
    actionButton: (@Composable () -> Unit)? = null,
    title: String? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 20.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp).align(Alignment.CenterStart).noRippleClickable { onClickNavigation() },
            imageVector = navigationIcon,
            tint = DonmaniTheme.colors.DeepBlue99,
            contentDescription = null,
        )
        title?.let {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = it,
                style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.Bold),
                color = DonmaniTheme.colors.Gray95
            )
        }
        actionButton?.let {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) { it() }
        }
    }
}

@Preview
@Composable
fun AppBarPreview() {
    BaseAppBar(
        navigationIcon = ImageVector.vectorResource(R.drawable.setting),
        onClickNavigation = {},
        actionButton = {},
        title = "라벨"
    )
}