package com.gowoon.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.common.util.FirebaseAnalyticsUtil
import com.gowoon.common.util.NotificationPermissionUtil
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.component.CustomSnackBarHost
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.setting.component.EditNicknameBottomSheet
import com.gowoon.setting.component.Reddot
import com.gowoon.ui.BBSScaffold
import com.gowoon.ui.GradientBackground
import com.gowoon.ui.component.BBSRuleBottomSheet
import kotlinx.coroutines.flow.collectLatest

@Stable
data class SettingItem(
    val title: String,
    val showReddot: Boolean = false,
    val toggleState: Boolean? = null,
    val gaEventName: String? = null,
    val onClick: () -> Unit
)

@Composable
internal fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickDecoration: () -> Unit,
    onClickNotice: () -> Unit,
    onClickPrivatePrivacy: () -> Unit,
    onClickFeedback: () -> Unit,
    onClickPush: () -> Unit
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusRequester = remember { FocusRequester() }
    var notificationStatus by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.uiEffect.collectLatest {
            if (it is SettingEffect.ShowToast) {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        notificationStatus = NotificationPermissionUtil.isNotificationPermissionGranted(context)
    }

    BBSScaffold(
        snackbarHost = { CustomSnackBarHost(snackbarHostState) },
        background = { GradientBackground() },
        topBar = {
            AppBar(
                title = stringResource(R.string.setting_appbar_title),
                onClickNavigation = onClickBack
            )
        }
    ) {
        state.dialogState?.let { dialogState ->
            when (dialogState) {
                SettingDialogType.BBS_RULE -> {
                    BBSRuleBottomSheet {
                        viewModel.setEvent(SettingEvent.ShowDialog(null))
                    }
                }

                SettingDialogType.EDIT_NICKNAME -> {
                    EditNicknameBottomSheet(
                        currentNickname = state.nickname,
                        onClickDone = { result, hide ->
                            viewModel.setEvent(SettingEvent.OnChangeNickName(result) { succeed ->
                                if (succeed) hide()
                            })
                        },
                        onDismissRequest = { viewModel.setEvent(SettingEvent.ShowDialog(null)) },
                        showToast = { viewModel.showToast(it) },
                        focusRequester = focusRequester,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))
            ProfileHeader(
                nickname = state.nickname
            ) {
                FirebaseAnalyticsUtil.sendEvent(
                    trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                    eventName = "setting_nickname"
                )
                viewModel.setEvent(SettingEvent.ShowDialog(SettingDialogType.EDIT_NICKNAME))
            }
            Spacer(Modifier.height(60.dp))
            SettingContent(
                listOf(
                    SettingItem(
                        title = stringResource(R.string.setting_decoration),
                        showReddot = state.newItem,
                        gaEventName = "", // TODO
                        onClick = {
                            viewModel.setEvent(SettingEvent.UpdateDecorationStatusAsRead)
                            onClickDecoration()
                        }
                    ),
//                    SettingItem(
//                        title = stringResource(R.string.setting_sound),
//                        toggleState = state.soundState,
//                        gaEventName = "", // TODO
//                        onClick = {
//                            viewModel.setEvent(
//                                SettingEvent.OnClickSoundToggle(
//                                    context.getString(
//                                        R.string.no_bgm_toast_message
//                                    )
//                                )
//                            )
//                        }
//                    ),
                    SettingItem(
                        title = stringResource(R.string.setting_push),
                        toggleState = notificationStatus,
                        gaEventName = "setting_apppush",
                        onClick = onClickPush
                    ),
                    SettingItem(
                        title = stringResource(R.string.setting_notice),
                        showReddot = state.newNotice,
                        gaEventName = "setting_notice",
                        onClick = {
                            viewModel.setEvent(SettingEvent.UpdateNoticeStatusAsRead)
                            onClickNotice()
                        }
                    ),
                    SettingItem(
                        title = stringResource(R.string.setting_bbs_rule),
                        gaEventName = "setting_rules",
                    ) { viewModel.setEvent(SettingEvent.ShowDialog(SettingDialogType.BBS_RULE)) },
                    SettingItem(
                        title = stringResource(R.string.setting_feedback),
                        onClick = onClickFeedback
                    ),
                    SettingItem(
                        title = stringResource(R.string.setting_private_privacy),
                        onClick = onClickPrivatePrivacy
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
            painter = painterResource(com.gowoon.designsystem.R.drawable.profile_default),
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
            Spacer(Modifier.width(6.dp))
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.edit),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SettingContent(
    settingItems: List<SettingItem>
) {
    Column(Modifier.fillMaxWidth()) {
        settingItems.forEach { item ->
            SettingContentItem(
                title = item.title,
                showReddot = item.showReddot,
                onClick = {
                    item.gaEventName?.let { eventName ->
                        FirebaseAnalyticsUtil.sendEvent(
                            trigger = FirebaseAnalyticsUtil.EventTrigger.CLICK,
                            eventName = eventName
                        )
                    }
                    item.onClick()
                },
                button = {
                    item.toggleState?.let { state ->
                        Switch(
                            checked = state,
                            onCheckedChange = { item.onClick() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DonmaniTheme.colors.DeepBlue30,
                                checkedTrackColor = DonmaniTheme.colors.DeepBlue99,
                                uncheckedThumbColor = DonmaniTheme.colors.DeepBlue30,
                                uncheckedTrackColor = DonmaniTheme.colors.DeepBlue70,
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingContentItem(
    title: String,
    showReddot: Boolean,
    onClick: () -> Unit,
    button: @Composable () -> Unit = { }
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .noRippleClickable { onClick() }
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = title,
                color = DonmaniTheme.colors.DeepBlue99,
                style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold)
            )
            if (showReddot) {
                Spacer(Modifier.width(4.dp))
                Reddot()
            }
            Spacer(Modifier.weight(1f))
            button()
        }
    }
}