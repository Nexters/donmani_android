package com.gowoon.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.AppBar
import com.gowoon.ui.noRippleClickable

@Stable
data class SettingItem(
    val title: String,
    val onClick: () -> Unit
)

@Composable
internal fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickPrivatePrivacy: () -> Unit,
    onClickFeedback: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TransparentScaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.appbar_title),
                onClickNavigation = onClickBack
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))
            ProfileHeader(
                nickname = state.nickname
            ) { }
            Spacer(Modifier.height(60.dp))
            SettingContent(
                listOf(
                    SettingItem(
                        stringResource(R.string.setting_private_privacy),
                        onClickPrivatePrivacy
                    ),
                    SettingItem(
                        stringResource(R.string.setting_feedback),
                        onClickFeedback
                    )
                )
            )
        }
    }

}

@Composable
private fun ProfileHeader(
    nickname: String,
    onClickEdit: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(28.dp)),
            imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.app_icon),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.noRippleClickable { onClickEdit() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nickname,
                color = DonmaniTheme.colors.DeepBlue99,
                style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold)
            )
            // TODO edit
//            Spacer(Modifier.width(6.dp))
//            Icon(
//                modifier = Modifier.size(20.dp),
//                imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.edit),
//                tint = Color.Unspecified,
//                contentDescription = null
//            )
        }
    }
}

@Composable
private fun SettingContent(
    settingItems: List<SettingItem>
) {
    Column(Modifier.fillMaxWidth()) {
        settingItems.forEach {
            SettingContentItem(
                title = it.title,
                onClick = it.onClick
            )
        }
    }
}

@Composable
private fun SettingContentItem(
    title: String,
    onClick: () -> Unit,
    button: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .noRippleClickable { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = title,
            color = DonmaniTheme.colors.DeepBlue99,
            style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold)
        )
        button?.let { it() }
    }
}