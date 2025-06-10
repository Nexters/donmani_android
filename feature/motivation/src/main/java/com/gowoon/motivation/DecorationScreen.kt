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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.record.Record
import com.gowoon.model.reward.BottleType
import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.GiftCategory
import com.gowoon.model.reward.getBottleType
import com.gowoon.motivation.component.DecorationFirstAccessBottomSheet
import com.gowoon.motivation.component.DecorationHiddenItemBottomSheet
import com.gowoon.motivation.component.GiftItemChip
import com.gowoon.ui.DecoratedBackground
import com.gowoon.ui.Decoration
import com.gowoon.ui.component.AlertDialog
import com.gowoon.ui.component.StarBottle
import com.gowoon.ui.component.StarBottleMode
import io.github.aakira.napier.Napier

@Composable
internal fun DecorationScreen(
    viewModel: DecorationViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickSave: (String) -> Unit
) {
    val context = LocalContext.current
    var player = remember { ExoPlayer.Builder(context).build() }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var targetRect by remember { mutableStateOf(Rect.Zero) }
    val enableConfirm by remember {
        derivedStateOf {
            viewModel.isChangedDecorationState(
                mapOf(
                    GiftCategory.BACKGROUND to state.bbsState.background,
                    GiftCategory.EFFECT to state.bbsState.effect,
                    GiftCategory.DECORATION to state.bbsState.decoration,
                    GiftCategory.CASE to state.bbsState.case,
                    GiftCategory.BGM to state.bbsState.bgm
                ),
                state.savedItems
            )
        }
    }

    LaunchedEffect(state) {
        Napier.d("gowoon state = $state")
    }
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
            onClickPositive = {
                viewModel.setEvent(DecorationEvent.SaveDecoration { onClickSave(state.savedItems.toString()) })
            },
            onDismissRequest = { viewModel.setEvent(DecorationEvent.ShowDialog(false)) }
        )
    }

    if (state.showFirstOpenBottomSheet) {
        DecorationFirstAccessBottomSheet { viewModel.setEvent(DecorationEvent.HideFirstBottomSheet) }
    }
    if (state.showHiddenGiftBottomSheet) {
        DecorationHiddenItemBottomSheet { viewModel.setEvent(DecorationEvent.HideHiddenBttomSheet) }
    }
    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1.2f)
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
                                if (enableConfirm) {
                                    viewModel.setEvent(
                                        DecorationEvent.ShowDialog(true)
                                    )
                                }
                            },
                            text = stringResource(R.string.decoration_appbar_action_button),
                            style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold),
                            color = DonmaniTheme.colors.Common0.copy(alpha = if (enableConfirm) 1f else 0.4f)
                        )
                    }
                )
                DecorationResultContent(
                    records = state.bbsState.records,
                    bottleType = getBottleType(state.savedItems[GiftCategory.CASE]?.id ?: ""),
                    isPlay = !state.savedItems[GiftCategory.BGM]?.resourceUrl.isNullOrEmpty()
                ) {
                    targetRect = it
                }
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

    Decoration(
        targetRect = targetRect,
        decoration = state.savedItems[GiftCategory.DECORATION],
        starBottleMode = StarBottleMode.Preview
    )

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

}

@Composable
private fun DecorationResultContent(
    modifier: Modifier = Modifier,
    records: List<Record>,
    bottleType: BottleType,
    isPlay: Boolean,
    onChangeStarBottleRect: (Rect) -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("sound.json"))
    var targetRect by remember { mutableStateOf(Rect.Zero) }
    Box(modifier = modifier.fillMaxSize()) {
        StarBottle(
            modifier = Modifier
                .align(Alignment.Center)
                .onGloballyPositioned { onChangeStarBottleRect(it.boundsInRoot()) },
            starBottleMode = StarBottleMode.Preview,
            bottleType = bottleType,
            records = records
        ) { }
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .size(24.dp)
                .alpha(if (isPlay) 1f else 0.2f)
                .align(Alignment.BottomStart),
            isPlaying = isPlay,
            iterations = LottieConstants.IterateForever
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
    val thumbnailModifier = Modifier
        .fillMaxSize()
        .then(
            when (currentSelectedInventory.currentCategory) {
                GiftCategory.BACKGROUND -> Modifier
                GiftCategory.EFFECT -> Modifier.padding(12.dp)
                GiftCategory.DECORATION -> Modifier.padding(12.dp)
                GiftCategory.CASE -> Modifier.padding(12.dp)
                GiftCategory.BGM -> Modifier.padding(30.dp)
            }
        )
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
                val finalModifier = if (it.resourceUrl.isNullOrEmpty()) {
                    Modifier.size(32.dp)
                } else {
                    if (it.hidden) {
                        Modifier.fillMaxSize()
                    } else {
                        thumbnailModifier
                    }
                }
                GiftItemChip(
                    selected = it.id == currentSelectedInventory.currentSelectItem,
                    isNew = it.isNew,
                    onClick = { onClickItem(currentSelectedInventory.currentCategory, it) }
                ) {
                    AsyncImage(
                        modifier = finalModifier.align(Alignment.Center),
                        model = it.thumbnailImageUrl,
                        contentScale = if (currentSelectedInventory.currentCategory == GiftCategory.BACKGROUND) ContentScale.FillBounds else ContentScale.Fit,
                        contentDescription = null
                    )
                }
            }
        }
    }
}