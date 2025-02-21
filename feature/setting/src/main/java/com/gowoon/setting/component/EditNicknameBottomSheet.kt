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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onClickDone: (result: String, hide: () -> Unit) -> Unit,
    onDismissRequest: () -> Unit
) {
    BottomSheet(
        content = { hide ->
            EditNicknameContent(
                initialText = currentNickname,
                onClickDone = { onClickDone(it, hide) }
            )
        },
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun EditNicknameContent(
    initialText: String,
    onClickDone: (String) -> Unit
) {
    val text = rememberTextFieldState(initialText)
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        InputField(
            height = InputFieldHeight.WRAP_CONENT,
            text = text,
            maxLength = NicknameUtil.NICKNAMEMAX_LENGTH
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
                enable = NicknameUtil.isValid(text.text.toString()),
            ) { onClickDone(text.text.toString()) }
        }
    }
}
