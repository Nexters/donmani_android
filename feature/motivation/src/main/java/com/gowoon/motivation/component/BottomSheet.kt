package com.gowoon.motivation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
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

@Preview
@Composable
private fun FirstAccessBottomSheetContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("reward_first_open.json"))
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
        LottieAnimation(
            modifier = Modifier.height(180.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
    }
}