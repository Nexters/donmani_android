package com.gowoon.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.record.component.RecordMainAppBar
import com.gowoon.ui.GradientBackground
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.Title

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
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                Title(text = stringResource(R.string.record_main_title))
                Spacer(Modifier.height(60.dp))
                RecordMainContent(Modifier.weight(1f))
            }
            RecordMainFooter(Modifier.align(Alignment.BottomCenter).zIndex(1f))
        }
    }
}

@Composable
private fun RecordMainContent(modifier: Modifier = Modifier) {

}

@Composable
private fun RecordMainFooter(modifier: Modifier = Modifier) {

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