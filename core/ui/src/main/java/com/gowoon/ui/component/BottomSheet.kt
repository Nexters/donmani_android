package com.gowoon.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.ui.noRippleClickable

sealed interface BottomSheetButtonType {
    data class Single(val title: String, val enable: Boolean = true) : BottomSheetButtonType
    data class Double(val positiveTitle: String, val negativeTitle: String) : BottomSheetButtonType
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    title: String,
    buttonType: BottomSheetButtonType,
    content: @Composable () -> Unit,
    onClick: (Boolean) -> Unit
) {
    val state = rememberModalBottomSheetState()
    ModalBottomSheet(
        sheetState = state,
        dragHandle = null,
        containerColor = DonmaniTheme.colors.DeepBlue60,
        onDismissRequest = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = DonmaniTheme.dimens.Margin20)
                .padding(top = 12.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CloseButton(modifier = Modifier.align(Alignment.End)) { }
            Text(
                text = title,
                color = DonmaniTheme.colors.Gray95,
                style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold)
            )
            content()
            when (buttonType) {
                is BottomSheetButtonType.Single -> {
                    RoundedButton(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        type = RoundedButtonRadius.Row,
                        label = buttonType.title,
                        enable = buttonType.enable,
                    ) { onClick(true) }
                }

                is BottomSheetButtonType.Double -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NegativeButton(
                            modifier = Modifier.weight(1f),
                            label = buttonType.negativeTitle
                        ) { onClick(false) }
                        PositiveButton(
                            modifier = Modifier.weight(1f),
                            label = buttonType.positiveTitle
                        ) { onClick(true) }
                    }
                }
            }
        }
    }
}

@Composable
private fun CloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .background(
                shape = CircleShape,
                color = DonmaniTheme.colors.DeepBlue50
            )
            .noRippleClickable { onClick() }
    ) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = ImageVector.vectorResource(R.drawable.close_round),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun BottomSheetPreview() {
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold { padding ->

        if (showBottomSheet) {
            BottomSheet(
                title = "타이틀",
                buttonType = BottomSheetButtonType.Single("완료"),
                {},
                {}
            )
        }

        Column(Modifier.padding(padding)) {
            RoundedButton(
                type = RoundedButtonRadius.Row,
                label = "click"
            ) {
                showBottomSheet = true
            }
        }
    }
}