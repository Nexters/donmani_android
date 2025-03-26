package com.gowoon.starbottlelist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.Record
import com.gowoon.ui.BGMode
import com.gowoon.ui.GradientBackground
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.NoticeBanner
import com.gowoon.ui.component.StarBottle

@Composable
internal fun StarBottleScreen() {
    val year = "25"
    val month = 3
    val records = null
    TransparentScaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.star_bottle_app_bar_title, year, month),
                onClickNavigation = {
                    // TODO
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (records == null) {
                StarBottleHeader(modifier = Modifier.fillMaxWidth())
            }
            StarBottleContent(
                modifier = Modifier.align(Alignment.Center),
                records = records
            ) {
                // TODO
            }
        }
    }
}

@Composable
private fun StarBottleContent(
    modifier: Modifier = Modifier,
    records: List<Record>? = null,
    onClickBottle: () -> Unit
) {
    records?.let {
        StarBottle(
            modifier = modifier,
            records = it,
            onClickBottle = onClickBottle
        )
    } ?: run {
        Icon(
            modifier = modifier
                .width(300.dp)
                .height(400.dp),
            painter = painterResource(com.gowoon.designsystem.R.drawable.star_bottle_thumbnail_locked),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}

@Composable
private fun StarBottleHeader(modifier: Modifier = Modifier) {
    NoticeBanner(modifier = modifier) {
        Text(
            text = stringResource(R.string.star_bottle_list_header_notice_message),
            style = DonmaniTheme.typography.Body1,
            color = DonmaniTheme.colors.Gray95
        )
    }
}

@Preview
@Composable
private fun StarBottlePreview() {
    GradientBackground(mode = BGMode.SPECIAL) {
        StarBottleScreen()
    }
}