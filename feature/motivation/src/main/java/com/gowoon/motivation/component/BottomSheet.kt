package com.gowoon.motivation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.component.BottomSheetButtonType
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.motivation.R

@Composable
internal fun FirstAccessBottomSheet(
    onDismissRequest: () -> Unit,
    onClickGetFirstGift: () -> Unit
) {
    BottomSheet(
        buttonType = BottomSheetButtonType.Single(title = stringResource(R.string.reward_first_access_bottom_sheet_button_title)),
        onClick = { onClickGetFirstGift() },
        onDismissRequest = onDismissRequest,
        showCloseButton = false,
        content = { FirstAccessBottomSheetContent() }
    )
}

@Composable
private fun FirstAccessBottomSheetContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.reward_first_access_bottom_sheet_title),
            color = DonmaniTheme.colors.DeepBlue99,
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.reward_first_access_bottom_sheet_description),
            color = DonmaniTheme.colors.DeepBlue90,
            style = DonmaniTheme.typography.Body2
        )
        Spacer(Modifier.height(24.dp))
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            painter = painterResource(com.gowoon.designsystem.R.drawable.reward_first_access_img),
            contentDescription = null
        )
    }
}

@Composable
internal fun DecorationFirstAccessBottomSheet(onDismissRequest: () -> Unit) {
    BottomSheet(
        buttonType = BottomSheetButtonType.Single(title = stringResource(R.string.decoration_bottom_sheet_button_title)),
        onDismissRequest = onDismissRequest,
        showCloseButton = false,
        content = { DecorationFirstAccessBottomSheetContent() }
    )
}

@Composable
private fun DecorationFirstAccessBottomSheetContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.decoration_bottom_sheet_title),
            color = DonmaniTheme.colors.DeepBlue99,
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.decoration_bottom_sheet_description),
            color = DonmaniTheme.colors.DeepBlue90,
            style = DonmaniTheme.typography.Body2
        )
        Spacer(Modifier.height(24.dp))
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            painter = painterResource(com.gowoon.designsystem.R.drawable.decoration_first_bottom_sheet_img),
            contentDescription = null
        )
    }
}

@Composable
internal fun DecorationHiddenItemBottomSheet(onDismissRequest: () -> Unit) {
    BottomSheet(
        buttonType = BottomSheetButtonType.Single(title = stringResource(R.string.decoration_hidden_bottom_sheet_button_title)),
        onDismissRequest = onDismissRequest,
        showCloseButton = false,
        content = { DecorationHiddenItemBottomSheetContent() }
    )
}

@Preview
@Composable
private fun DecorationHiddenItemBottomSheetContent() {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components { add(AnimatedImageDecoder.Factory()) }
        .build()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.decoration_hidden_bottom_sheet_title),
            color = DonmaniTheme.colors.DeepBlue99,
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.decoration_hidden_bottom_sheet_description),
            color = DonmaniTheme.colors.DeepBlue90,
            style = DonmaniTheme.typography.Body2,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            imageLoader = imageLoader,
            model = "file:///android_asset/hidden_item.gif",
            contentDescription = null
        )
    }
}