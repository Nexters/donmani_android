package com.gowoon.starbottlelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.theme.pretendard_fontfamily
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.NoticeBanner

sealed class BottleState {
    data class OPENED(val count: Int, val total: Int) : BottleState()
    data object LOCKED : BottleState()
}

@Composable
internal fun StarBottleListScreen(
    viewModel: StarBottleListViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TransparentScaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.star_bottle_list_app_bar_title),
                onClickNavigation = onClickBack
            )
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(it),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(top = 16.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (state.showBanner) {
                item(span = { GridItemSpan(3) }) {
                    StarBottleListHeader { viewModel.setEvent(StarBottleListEvent.HideBanner) }
                }
            }
            items(8) {
                val state = if (it < 4) BottleState.OPENED(3, 30) else BottleState.LOCKED
                StarBottleListItem(it + 1, state)
            }
        }
    }
}

@Composable
private fun StarBottleListHeader(modifier: Modifier = Modifier, onClick: () -> Unit) {
    NoticeBanner(modifier = modifier) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.star_bottle_list_header_notice_message),
            style = DonmaniTheme.typography.Body1,
            color = DonmaniTheme.colors.Gray95
        )
        Icon(
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable { onClick() },
            imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.close),
            tint = DonmaniTheme.colors.DeepBlue99,
            contentDescription = null
        )
    }
}

@Composable
private fun StarBottleListItem(
    month: Int,
    state: BottleState
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .width(100.dp)
                .height(116.dp)
        ) {
            when (state) {
                is BottleState.LOCKED -> {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(com.gowoon.designsystem.R.drawable.star_bottle_thumbnail_locked),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }

                is BottleState.OPENED -> {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(com.gowoon.designsystem.R.drawable.star_bottle_thumbnail),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(com.gowoon.designsystem.R.drawable.star_thumbnail),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontFamily = pretendard_fontfamily,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = DonmaniTheme.colors.Gray80
                                    )
                                ) {
                                    append(state.count.toString())
                                }
                                append("/${state.total}")
                            },
                            style = DonmaniTheme.typography.Body3.copy(fontWeight = FontWeight.SemiBold),
                            color = DonmaniTheme.colors.DeepBlue80
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = "${month}월",
            style = DonmaniTheme.typography.Body2.copy(fontWeight = FontWeight.SemiBold),
            color = if (state is BottleState.LOCKED) DonmaniTheme.colors.DeepBlue80 else DonmaniTheme.colors.Gray99,
            textAlign = TextAlign.Center,
        )
    }
}