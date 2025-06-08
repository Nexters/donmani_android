package com.gowoon.motivation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.GiftCategory
import com.gowoon.motivation.component.DecorationFirstAccessBottomSheet
import com.gowoon.motivation.component.GiftItemChip
import com.gowoon.ui.DecoratedBackground
import com.gowoon.ui.Decoration
import com.gowoon.ui.component.AlertDialog

@Composable
internal fun DecorationScreen(
    viewModel: DecorationViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    var player = remember { ExoPlayer.Builder(context).build() }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.savedItems[GiftCategory.BGM]) {
        state.savedItems[GiftCategory.BGM]?.resourceUrl?.let {
            player.setMediaItem(MediaItem.fromUri(it))
            player.prepare()
            player.play()
        }
    }

    if (state.showDialog) {
        AlertDialog(
            title = stringResource(R.string.decoration_confirm_dialog_title),
            description = stringResource(R.string.decoration_confirm_dialog_description),
            positiveTitle = stringResource(R.string.decoration_confirm_dialog_positive_button),
            negativeTitle = stringResource(R.string.decoration_confirm_dialog_negative_button),
            onClickPositive = { viewModel.setEvent(DecorationEvent.SaveDecoration) },
            onDismissRequest = { viewModel.setEvent(DecorationEvent.ShowDialog(false)) }
        )
    }

    if (state.showFirstOpenBottomSheet) {
        DecorationFirstAccessBottomSheet { viewModel.setEvent(DecorationEvent.HideFirstBottomSheet) }
    }
    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            DecoratedBackground(
                background = state.savedItems[GiftCategory.BACKGROUND]?.resourceUrl ?: "",
                effect = state.savedItems[GiftCategory.EFFECT]?.resourceUrl ?: ""
            )
            Column(modifier = Modifier.padding(horizontal = DonmaniTheme.dimens.Margin20)) {
                AppBar(
                    modifier = Modifier.statusBarsPadding(),
                    onClickNavigation = onClickBack,
                    title = stringResource(R.string.decoration_appbar_title),
                    actionButton = {
                        Text(
                            modifier = Modifier.noRippleClickable {
                                viewModel.setEvent(
                                    DecorationEvent.ShowDialog(true)
                                )
                            },
                            text = stringResource(R.string.decoration_appbar_action_button),
                            style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold),
                            color = DonmaniTheme.colors.Common0
                        )
                    }
                )
                DecorationResultContent(
                    bottleUrl = state.savedItems[GiftCategory.CASE]?.resourceUrl ?: "",
                    decoration = state.savedItems[GiftCategory.DECORATION]
                )
            }
        }
        DecorationItemContent(
            modifier = Modifier
                .weight(1f)
                .navigationBarsPadding(),
            currentSelectedInventory = InventoryState(
                currentCategory = state.currentSelectedCategory,
                categoryItems = state.inventoryList[state.currentSelectedCategory] ?: listOf(),
                currentSelectItem = state.savedItems[state.currentSelectedCategory]?.id
            ),
            onClickCategory = { viewModel.setEvent(DecorationEvent.OnClickCategory(it)) },
            onClickItem = { category, item ->
                viewModel.setEvent(DecorationEvent.OnClickItem(category, item))
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

}

@Composable
private fun DecorationResultContent(
    modifier: Modifier = Modifier,
    bottleUrl: String,
    decoration: Gift?
) {
    var targetRect by remember { mutableStateOf(Rect.Zero) }
    Box(modifier = modifier.fillMaxSize()) {
        // TODO 테스트 하면서 조정 필요
        AsyncImage(
            modifier = Modifier
                .width(208.dp)
                .height(260.dp)
                .align(Alignment.Center)
                .onGloballyPositioned { targetRect = it.boundsInRoot() },
            model = bottleUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Decoration(
            targetRect = targetRect,
            decoration = decoration
        )
    }
}

@Composable
private fun DecorationItemContent(
    modifier: Modifier = Modifier,
    currentSelectedInventory: InventoryState,
    onClickCategory: (GiftCategory) -> Unit,
    onClickItem: (GiftCategory, Gift?) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(DonmaniTheme.colors.DeepBlue60)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            GiftCategory.entries.forEach {
                Text(
                    modifier = Modifier.noRippleClickable { onClickCategory(it) },
                    text = it.title,
                    style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.Bold),
                    color = if (it == currentSelectedInventory.currentCategory) DonmaniTheme.colors.Common0 else DonmaniTheme.colors.DeepBlue80
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 20.dp)
        ) {
            items(currentSelectedInventory.categoryItems) {
                // TODO 기본 아이템 UI 처리 분기
//                GiftItemChip(
//                    selected = currentSelectedInventory.currentSelectItem.isNullOrEmpty(),
//                    isNew = false,
//                    onClick = {
//                        onClickItem(
//                            currentSelectedInventory.currentCategory,
//                            null
//                        )
//                    }
//                ) {
//                    Image(
//                        modifier = Modifier.align(Alignment.Center),
//                        painter = painterResource(com.gowoon.designsystem.R.drawable.item_none),
//                        contentDescription = null
//                    )
//                }
                GiftItemChip(
                    selected = it.id == currentSelectedInventory.currentSelectItem,
                    isNew = it.isNew,
                    onClick = { onClickItem(currentSelectedInventory.currentCategory, it) }
                ) {
                    // TODO change to coil with thumbnail
                }
            }
        }
    }
}