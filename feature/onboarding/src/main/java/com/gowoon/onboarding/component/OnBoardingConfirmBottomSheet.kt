package com.gowoon.onboarding.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.component.BottomSheetButtonType
import com.gowoon.onboarding.R

@Composable
internal fun OnBoardingConfirmBottomSheet(
    onDismissRequest: () -> Unit
) {
    BottomSheet(
        title = stringResource(R.string.onboarding_confirm_bottomsheet_title),
        buttonType = BottomSheetButtonType.Single(title = stringResource(R.string.onboarding_confirm_bottomsheet_btn)),
        content = {
            Icon(
                painter = painterResource(com.gowoon.designsystem.R.drawable.onboarding_confirm_img),
                tint = Color.Unspecified,
                contentDescription = null
            )
        },
        onDismissRequest = onDismissRequest,
        canDismiss = false,
        isSpaceBetweenBtn = false
    )
}