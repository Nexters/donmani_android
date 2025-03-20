package com.gowoon.setting.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.component.InputField
import com.gowoon.designsystem.component.InputFieldHeight
import com.gowoon.designsystem.component.RoundedButton
import com.gowoon.designsystem.component.RoundedButtonRadius
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.domain.util.NicknameUtil
import com.gowoon.setting.R

@Composable
internal fun EditNicknameBottomSheet(
    currentNickname: String,
    focusRequester: FocusRequester,
    onClickDone: (result: String, hide: () -> Unit) -> Unit,
    onDismissRequest: () -> Unit,
    showToast: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    BottomSheet(
        content = { hide ->
            EditNicknameContent(
                initialText = currentNickname,
                onClickDone = { onClickDone(it, hide) },
                showToast = showToast,
                focusRequester = focusRequester,
            )
        },
        onDismissRequest = onDismissRequest,
        onExpanded = {
            focusRequester.requestFocus()
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun EditNicknameContent(
    initialText: String,
    onClickDone: (String) -> Unit,
    showToast: (String) -> Unit,
    focusRequester: FocusRequester,
) {
    val context = LocalContext.current
    val text = rememberTextFieldState(initialText)
    val isValid by remember { derivedStateOf { NicknameUtil.isValid(text.text.toString()) } }

    LaunchedEffect(isValid) {
        if (!isValid) {
            showToast(context.getString(com.gowoon.ui.R.string.toast_invalid_character))
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        InputField(
            height = InputFieldHeight.WRAPCONENT,
            text = text,
            maxLength = NicknameUtil.NICKNAMEMAX_LENGTH,
            focusRequester = focusRequester,
            showToast = {
                showToast(context.getString(com.gowoon.ui.R.string.toast_max_length))
            }
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.edit_nickname_additional_message),
                color = DonmaniTheme.colors.DeepBlue80,
                style = DonmaniTheme.typography.Body2
            )
            RoundedButton(
                type = RoundedButtonRadius.High,
                label = stringResource(R.string.btn_edit_nickname_done),
                enable = isValid,
            ) { onClickDone(text.text.toString()) }
        }
    }
}
