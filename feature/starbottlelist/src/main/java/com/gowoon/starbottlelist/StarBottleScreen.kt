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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.Record
import com.gowoon.ui.BBSScaffold
import com.gowoon.ui.DecoratedBackground
import com.gowoon.ui.Decoration
import com.gowoon.ui.component.NoticeBanner
import com.gowoon.ui.component.StarBottle

@Composable
internal fun StarBottleScreen(
    viewModel: StarBottleViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickBottle: (List<Record>, Int, Int) -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    var targetRect by remember { mutableStateOf(Rect.Zero) }
    var player = remember { ExoPlayer.Builder(context).build() }

    LaunchedEffect(state.bbsState.bgm) {
        state.bbsState.bgm?.resourceUrl?.let {
            player.setMediaItem(MediaItem.fromUri(it))
            player.prepare()
            player.play()
        }
    }

    BBSScaffold(
        background = {
            DecoratedBackground(
                background = state.bbsState.background?.resourceUrl ?: "",
                effect = state.bbsState.effect?.resourceUrl ?: ""
            )
        },
        topBar = {
            AppBar(
                title = stringResource(
                    R.string.star_bottle_app_bar_title,
                    state.year.toString().takeLast(2),
                    state.month ?: -1
                ),
                onClickNavigation = onClickBack
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (state.bbsState.records.isEmpty()) {
                StarBottleHeader(modifier = Modifier.fillMaxWidth())
            }
            StarBottleContent(
                modifier = Modifier
                    .align(Alignment.Center)
                    .onGloballyPositioned { targetRect = it.boundsInRoot() },
                records = state.bbsState.records
            ) { onClickBottle(state.bbsState.records, state.year, state.month ?: -1) }
            Decoration(
                targetRect = targetRect,
                decoration = state.bbsState.effect
            )
        }
    }
}

@Composable
private fun StarBottleContent(
    modifier: Modifier = Modifier,
    records: List<Record>,
    onClickBottle: () -> Unit
) {
    if (records.isNotEmpty()) {
        StarBottle(
            modifier = modifier,
            records = records,
            onClickBottle = onClickBottle
        )
    } else {
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