package com.gowoon.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import kotlinx.coroutines.launch

sealed interface BottomSheetButtonType {
    data class Single(val title: String, val enable: Boolean = true) : BottomSheetButtonType
    data class Double(val positiveTitle: String, val negativeTitle: String) : BottomSheetButtonType
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    title: String? = null,
    buttonType: BottomSheetButtonType? = null,
    content: @Composable (hide: () -> Unit) -> Unit,
    onClick: ((Boolean) -> Unit)? = null,
    onDismissRequest: () -> Unit,
    onExpanded: () -> Unit = {},
    canDismiss: Boolean = true,
    snackbarHostState: SnackbarHostState? = null
) {
    val scope = rememberCoroutineScope()
    val state =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { canDismiss }
        )
    LaunchedEffect(state.isVisible) {
        if (state.isVisible) {
            onExpanded()
        }
    }
    ModalBottomSheet(
        sheetState = state,
        dragHandle = null,
        containerColor = DonmaniTheme.colors.DeepBlue60,
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = DonmaniTheme.dimens.Margin20)
                .padding(top = 12.dp, bottom = 8.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                if (canDismiss) {
                    CloseButton(modifier = Modifier.align(Alignment.End)) {
                        scope.launch {
                            state.hide()
                            onDismissRequest()
                        }
                    }
                } else {
                    Spacer(Modifier.height(20.dp))
                }
                title?.let {
                    Text(
                        text = it,
                        color = DonmaniTheme.colors.Gray95,
                        style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold)
                    )
                }
                content {
                    scope.launch {
                        state.hide()
                        onDismissRequest()
                    }
                }
                buttonType?.let {
                    when (buttonType) {
                        is BottomSheetButtonType.Single -> {
                            RoundedButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                type = RoundedButtonRadius.Row,
                                label = buttonType.title,
                                enable = buttonType.enable,
                            ) {
                                scope.launch {
                                    state.hide()
                                    onClick?.invoke(true)
                                    onDismissRequest()
                                }
                            }
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
                                ) {
                                    scope.launch {
                                        state.hide()
                                        onClick?.invoke(false)
                                        onDismissRequest()
                                    }
                                }
                                PositiveButton(
                                    modifier = Modifier.weight(1f),
                                    label = buttonType.positiveTitle
                                ) {
                                    scope.launch {
                                        state.hide()
                                        onClick?.invoke(true)
                                        onDismissRequest()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            snackbarHostState?.let {
                CustomSnackBarHost(
                    snackbarHostState = it,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
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