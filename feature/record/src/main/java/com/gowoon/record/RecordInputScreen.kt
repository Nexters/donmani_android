package com.gowoon.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.Category
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.getTitle
import com.gowoon.record.component.InputCategoryChip
import com.gowoon.ui.CategoryBackground
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.AppBar
import com.gowoon.ui.component.InputField
import com.gowoon.ui.component.RoundedButton
import com.gowoon.ui.component.RoundedButtonRadius
import com.gowoon.ui.noRippleClickable

@Composable
internal fun RecordInputScreen(
    viewModel: RecordInputViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val enabled by remember { derivedStateOf { state.category != null && state.memo.text.isNotEmpty() } }
    var showBottomSheet by remember { mutableStateOf(true) }

    CategoryBackground(state.category) {
        TransparentScaffold(
            topBar = {
                AppBar(
                    onClickNavigation = onClickBack,
                    title = stringResource(R.string.record_detail_appbar_title, state.type.title)
                )
            }
        ) {
            if (showBottomSheet) {
                CategorySelectBottomSheet(
                    type = state.type,
                    selected = state.category,
                    onChangedValue = { selected ->
                        viewModel.setEvent(RecordInputEvent.OnChangeCagegory(selected))
                    }
                ) { showBottomSheet = false }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                RecordInputContent(
                    modifier = Modifier.weight(1f),
                    type = state.type,
                    category = state.category,
                    memo = state.memo
                ) {
                    showBottomSheet = true
                }
                RoundedButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(vertical = 12.dp),
                    type = RoundedButtonRadius.High,
                    label = stringResource(R.string.btn_record_input_done),
                    enable = enabled

                ) {
                    // TODO pass consumption data
                }
            }
        }
    }
}

@Composable
private fun RecordInputContent(
    modifier: Modifier = Modifier,
    type: ConsumptionType,
    category: Category?,
    memo: TextFieldState,
    onClickEdit: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(Modifier
            .width(192.dp)
            .noRippleClickable { onClickEdit() }) {
            InputCategoryChip(
                modifier = Modifier.align(Alignment.Center),
                type = type,
                category = category
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .zIndex(1f),
                imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.edit_round),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
        Text(
            text = category?.getTitle(type) ?: "",
            style = DonmaniTheme.typography.Heading3.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.Common0
        )
        InputField(
            text = memo,
            placeholder = when (type) {
                ConsumptionType.GOOD -> stringResource(R.string.good_record_input_memo_placeholder)
                ConsumptionType.BAD -> stringResource(R.string.bad_record_input_memo_placeholder)
            }
        )
    }
}