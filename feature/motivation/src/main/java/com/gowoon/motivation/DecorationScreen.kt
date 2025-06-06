package com.gowoon.motivation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.reward.GiftCategory
import com.gowoon.motivation.component.GiftItemChip
import com.gowoon.ui.GradientBackground

@Composable
internal fun DecorationScreen(
    viewModel: DecorationViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            GradientBackground()
            Column(modifier = Modifier.padding(horizontal = DonmaniTheme.dimens.Margin20)) {
                AppBar(
                    modifier = Modifier.statusBarsPadding(),
                    onClickNavigation = onClickBack,
                    title = stringResource(R.string.decoration_appbar_title),
                    actionButton = {
                        Text(
                            modifier = Modifier.noRippleClickable {
                                viewModel.setEvent(
                                    DecorationEvent.OnClickDone
                                )
                            },
                            text = stringResource(R.string.decoration_appbar_action_button),
                            style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold),
                            color = DonmaniTheme.colors.Common0
                        )
                    }
                )
            }
        }
        DecorationItemContent(
            modifier = Modifier.weight(1f),
            currentSelectedInventory = state.currentSelectedInventory
        ) {
            viewModel.setEvent(DecorationEvent.OnClickCategory(it))
        }
    }

}

@Composable
private fun DecorationResultContent(modifier: Modifier = Modifier) {

}

@Composable
private fun DecorationItemContent(
    modifier: Modifier = Modifier,
    currentSelectedInventory: InventoryState,
    onClickCategory: (GiftCategory) -> Unit
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
            when (currentSelectedInventory.currentCategory) {
                GiftCategory.EFFECT, GiftCategory.DECORATION, GiftCategory.BGM -> {
                    item {
                        GiftItemChip(
                            selected = currentSelectedInventory.currentSelectItem?.id.isNullOrEmpty(),
                            isNew = false
                        ) {
                            Image(
                                modifier = Modifier.align(Alignment.Center),
                                painter = painterResource(com.gowoon.designsystem.R.drawable.item_none),
                                contentDescription = null
                            )
                        }
                    }
                }

                else -> {

                }
            }
            items(currentSelectedInventory.categoryItems) {
                GiftItemChip(
                    selected = it.id == currentSelectedInventory.currentSelectItem?.id,
                    isNew = it.isNew
                ) {
                    // TODO change to coil with thumbnail
                }
            }
        }
    }
}