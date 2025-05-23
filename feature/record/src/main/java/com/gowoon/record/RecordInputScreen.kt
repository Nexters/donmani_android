package com.gowoon.record

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.common.di.FeatureJson
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.component.CustomSnackBarHost
import com.gowoon.designsystem.component.InputField
import com.gowoon.designsystem.component.InputFieldHeight
import com.gowoon.designsystem.component.RoundedButton
import com.gowoon.designsystem.component.RoundedButtonRadius
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.record.Category
import com.gowoon.model.record.Consumption
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.getTitle
import com.gowoon.record.component.ExitWarningBottomSheet
import com.gowoon.record.navigation.InputToMainArgumentKey
import com.gowoon.ui.CategoryBackground
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.InputCategoryChip
import com.gowoon.ui.util.rememberHiltJson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json

@Composable
internal fun RecordInputScreen(
    viewModel: RecordInputViewModel = hiltViewModel(),
    @FeatureJson json: Json = rememberHiltJson(),
    onClickBack: () -> Unit,
    onClickDone: (String, String) -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val enabled by remember { derivedStateOf { state.category != null && state.memo.text.isNotEmpty() } }

    val snackbarHostState = remember { SnackbarHostState() }
    val focusRequester = remember { FocusRequester() }

    BackHandler {
        FirebaseAnalyticsUtil.sendEvent(
            trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
            eventName = "record_back_button",
            params = mutableListOf(
                Pair("screentype", viewModel.GA4GetScreenType())
            ).apply {
                add(viewModel.GA4GetRecordType())
                viewModel.GA4GetCategory()?.let { add(it) }
                add(viewModel.GA4GetRecord())
            }
        )
        if (viewModel.changedRecord()) {
            viewModel.setEvent(RecordInputEvent.ShowExitWaringBottomSheet(true))
        } else {
            onClickBack()
        }
    }

    LaunchedEffect(true) {
        viewModel.uiEffect.collectLatest {
            if (it is RecordInputEffect.ShowToast) {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    CategoryBackground(state.category) {
        TransparentScaffold(
            topBar = {
                AppBar(
                    onClickNavigation = {
                        if (viewModel.changedRecord()) {
                            viewModel.setEvent(RecordInputEvent.ShowExitWaringBottomSheet(true))
                        } else {
                            onClickBack()
                        }
                    },
                    title = stringResource(R.string.record_detail_appbar_title, state.type.title)
                )
            },
            snackbarHost = { CustomSnackBarHost(snackbarHostState) }
        ) {
            if (state.showExitWarningBottomSheet) {
                ExitWarningBottomSheet(
                    onExpanded = {
                        FirebaseAnalyticsUtil.sendEvent(
                            trigger = FirebaseAnalyticsUtil.EventTrigger.VIEW,
                            eventName = "recordmain_back_bottomsheet",
                            params = mutableListOf(
                                Pair("referrer", "기록작성"),
                                viewModel.GA4GetRecordType()
                            )
                        )
                    },
                    onClick = { isPositive ->
                        val midfix = if (isPositive) "nexttime" else "continue"
                        FirebaseAnalyticsUtil.sendEvent(
                            trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                            eventName = "record_${midfix}_button",
                            params = mutableListOf(
                                Pair("screentype", viewModel.GA4GetScreenType()),
                                viewModel.GA4GetRecordType()
                            )
                        )
                        if (isPositive) onClickBack()
                    }
                ) { isUserClicked ->
                    if (!isUserClicked) {
                        FirebaseAnalyticsUtil.sendEvent(
                            trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                            eventName = "record_close_button",
                            params = mutableListOf(
                                Pair("screentype", viewModel.GA4GetScreenType()),
                                viewModel.GA4GetRecordType()
                            )
                        )
                    }
                    viewModel.setEvent(RecordInputEvent.ShowExitWaringBottomSheet(false))
                }
            }

            if (state.showCategoryDialog) {
                CategorySelectBottomSheet(
                    type = state.type,
                    selected = state.category,
                    onChangedValue = { selected ->
                        viewModel.setEvent(RecordInputEvent.OnChangeCategory(selected))
                    }
                ) {
                    viewModel.setEvent(RecordInputEvent.ShowCategoryDialog(false))
                    focusRequester.requestFocus()
                }
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
                    memo = state.memo,
                    focusRequester = focusRequester,
                    onClickEdit = { viewModel.setEvent(RecordInputEvent.ShowCategoryDialog(true)) },
                    showToast = { viewModel.showToast(context.getString(com.gowoon.ui.R.string.toast_max_length)) }
                )
                RoundedButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 12.dp),
                    type = RoundedButtonRadius.High,
                    label = stringResource(R.string.btn_record_input_done),
                    enable = enabled

                ) {
                    state.category?.let { category ->
                        onClickDone(
                            InputToMainArgumentKey,
                            json.encodeToString(
                                Consumption(
                                    type = state.type,
                                    category = category,
                                    description = state.memo.text.toString()
                                )
                            )
                        )
                    }
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
    focusRequester: FocusRequester,
    onClickEdit: () -> Unit,
    showToast: () -> Unit
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
                painter = painterResource(com.gowoon.designsystem.R.drawable.edit_round),
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
            height = InputFieldHeight.FIXED(120.dp),
            text = memo,
            placeholder = stringResource(R.string.category_select_title, type.title),
            forceHaptic = true,
            focusRequester = focusRequester,
            showToast = showToast
        )
    }
}