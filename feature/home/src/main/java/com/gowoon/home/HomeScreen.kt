package com.gowoon.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.common.di.FeatureJson
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.home.component.HomeAppBar
import com.gowoon.home.component.StarBottle
import com.gowoon.model.record.Record
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.CircleButton
import com.gowoon.ui.component.CircleButtonSize
import com.gowoon.ui.component.MessageBox
import com.gowoon.ui.component.Title
import com.gowoon.ui.component.Tooltip
import com.gowoon.ui.component.TooltipCaretAlignment
import com.gowoon.ui.component.TooltipDirection
import com.gowoon.ui.pxToDp
import com.gowoon.ui.util.rememberHiltJson
import kotlinx.serialization.json.Json
import java.time.LocalDate

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    @FeatureJson json: Json = rememberHiltJson(),
    resultFromRecord: String?,
    onClickSetting: () -> Unit,
    onClickAdd: (Boolean, Boolean) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val hasToday by remember { mutableStateOf(viewModel.hasRecordOfDay(LocalDate.now())) }
    val hasYesterday by remember {
        mutableStateOf(
            viewModel.hasRecordOfDay(
                LocalDate.now().minusDays(1)
            )
        )
    }
    var tooltipOffset by remember { mutableStateOf(Offset.Zero) }
    var tooltipSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(resultFromRecord) {
        resultFromRecord?.let {
            viewModel.setEvent(HomeEvent.OnAddRecord(json.decodeFromString<Record>(it)))
        }
    }
    TransparentScaffold(
        topBar = { HomeAppBar(onClickSetting = onClickSetting) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Title(text = stringResource(R.string.home_title, state.nickname))
            Spacer(Modifier.height(95.dp))
            HomeContent(records = state.records)
            HomeFooter(
                modifier = Modifier.weight(1f),
                hasToday = hasToday,
                hasYesterday = hasYesterday,
                changedCircleButtonPosition = { tooltipOffset = it },
                onClickAdd = { onClickAdd(hasToday, hasYesterday) }
            )
        }
    }

    if (hasToday && state.showTooltip) {
        Tooltip(
            modifier = Modifier
                .padding(
                    top = tooltipOffset.y.pxToDp(),
                    start = tooltipOffset.x.pxToDp()
                )
                .onSizeChanged { tooltipSize = it }
                .offset(
                    x = -(tooltipSize / 2).width.pxToDp(),
                    y = (-45).dp // calculated with button size and margin
                ),
            direction = TooltipDirection.TopOf,
            caretAlignment = TooltipCaretAlignment.Center,
            message = stringResource(R.string.tooltip_message_for_yesterday)
        ) {
            viewModel.setEvent(HomeEvent.HideTooltip)
        }
    }

}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    records: List<Record>
) {
    Box(
        modifier = modifier
            .width(300.dp)
            .height(400.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            painter = painterResource(com.gowoon.designsystem.R.drawable.bottle_background),
            contentDescription = null
        )
        StarBottle(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .padding(3.dp)
                .background(color = Color.Transparent, shape = RoundedCornerShape(65.dp)),
            records = records
        )
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(com.gowoon.designsystem.R.drawable.bottle),
            contentDescription = null
        )
    }
}

@Composable
private fun HomeFooter(
    modifier: Modifier = Modifier,
    hasToday: Boolean,
    hasYesterday: Boolean,
    changedCircleButtonPosition: (Offset) -> Unit,
    onClickAdd: () -> Unit
) {
    Box(modifier) {
        if (hasToday && hasYesterday) {
            MessageBox(
                modifier = Modifier.align(Alignment.Center),
                message = stringResource(R.string.footer_message_when_has_today_and_yesterday)
            )
        } else {
            CircleButton(
                modifier = Modifier
                    .align(Alignment.Center)
                    .onGloballyPositioned {
                        changedCircleButtonPosition(it.boundsInRoot().topCenter)
                    },
                buttonSize = CircleButtonSize.Big,
                backgroundColor = DonmaniTheme.colors.PurpleBlue70,
                contentColor = DonmaniTheme.colors.PurpleBlue99
            ) { onClickAdd() }
        }
    }
}