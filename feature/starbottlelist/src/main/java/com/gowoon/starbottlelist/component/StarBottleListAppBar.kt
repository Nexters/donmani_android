package com.gowoon.starbottlelist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.starbottlelist.R

@Composable
internal fun StarBottleListAppBar(
    modifier: Modifier = Modifier,
    year: String,
    onClickArrow: () -> Unit,
    onClickBack: () -> Unit
) {
    AppBar(
        modifier = modifier,
        onClickNavigation = onClickBack,
        title = { AppBarTitle(year = year, onClickArrow = onClickArrow) }
    )
}

@Composable
private fun AppBarTitle(
    modifier: Modifier = Modifier,
    year: String,
    onClickArrow: () -> Unit
) {
    Row(
        modifier = modifier.noRippleClickable { onClickArrow() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.star_bottle_list_app_bar_title, year),
            style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.Gray95
        )
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(com.gowoon.designsystem.R.drawable.arrow),
            tint = DonmaniTheme.colors.Gray95,
            contentDescription = null
        )
    }
}