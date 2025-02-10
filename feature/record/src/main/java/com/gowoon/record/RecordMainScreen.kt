package com.gowoon.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.RecordType
import com.gowoon.record.component.EmptyCard
import com.gowoon.record.component.MessageBox
import com.gowoon.record.component.RecordMainAppBar
import com.gowoon.ui.GradientBackground
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.CheckBoxWithTitle
import com.gowoon.ui.component.RoundedButton
import com.gowoon.ui.component.RoundedButtonRadius
import com.gowoon.ui.component.Title
import kotlin.math.truncate

@Composable
internal fun RecordMainScreen() {
    TransparentScaffold(
        topBar = { RecordMainAppBar() }
    ) { padding ->
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                Title(text = stringResource(R.string.record_main_title))
                Spacer(Modifier.height(60.dp))
                RecordMainContent(Modifier.weight(1f))
            }
            RecordMainFooter(
                Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f)
            )
        }
    }
}

@Composable
private fun RecordMainContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        RecordType.entries.forEach { type ->
            // TODO content 유무에 따라 분기
            EmptyCard(type = type) {
                // TODO onClick
            }
        }
        CheckBoxWithTitle(title = stringResource(R.string.no_consumption_message)) {
            // TODO onClick
        }
    }
}

@Composable
private fun RecordMainFooter(modifier: Modifier = Modifier) {
    // TODO message 문구 로직 > timer
    // TODO button enable 로직
    // TODO add tooltip
    val gradientBgColor = Color(0xFF091647)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(brush = Brush.verticalGradient(colors = arrayListOf(gradientBgColor.copy(0f), gradientBgColor)))
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageBox(message = stringResource(R.string.record_bottom_message_default))
        RoundedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            type = RoundedButtonRadius.Row,
            label = stringResource(R.string.btn_record_save),
            enable = true
        ) {
            // TOOD onClick
        }
    }
}

@Preview
@Composable
private fun RecordMainPreview() {
    DonmaniTheme {
        GradientBackground {
            RecordMainScreen()
        }
    }
}