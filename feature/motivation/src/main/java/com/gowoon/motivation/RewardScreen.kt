package com.gowoon.motivation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.component.NegativeButton
import com.gowoon.designsystem.component.PositiveButton
import com.gowoon.designsystem.component.RoundedButton
import com.gowoon.designsystem.component.RoundedButtonRadius
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.motivation.component.RewardBackground
import com.gowoon.ui.BBSScaffold
import com.gowoon.ui.GradientBackground
import com.gowoon.ui.component.MessageBox

@Composable
internal fun RewardScreen(
    viewModel: RewardViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickGoToRecord: () -> Unit,
    onClickReview: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BBSScaffold(
        background = {
            if (state.step is Step.Main || state.step is Step.Feedback) {
                RewardBackground()
            } else {
                GradientBackground()
            }
        },
        topBar = { AppBar(onClickNavigation = onClickBack) }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(top = 16.dp)
        ) {
            when (val step = state.step) {
                is Step.Main -> {
                    MainContent(
                        state = step.state,
                        dayStreakCount = state.dayStreakCount,
                        onClickGetGift = { viewModel.setEvent(RewardEvent.GoToNextStep) },
                        onClickGoToRecord = onClickGoToRecord,
                        onClickGoToHome = onClickBack,
                        onClickReview = onClickReview
                    )
                }

                is Step.Feedback -> {
                    FeedbackContent()
                }

                is Step.GiftOpen -> {
                    GiftOpenContent()
                }

                is Step.GiftConfirm -> {
                    GiftConfirmContent()
                }

                null -> {}
            }
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    state: MainState,
    dayStreakCount: Int,
    onClickGetGift: () -> Unit,
    onClickGoToRecord: () -> Unit,
    onClickGoToHome: () -> Unit,
    onClickReview: () -> Unit
) {
    val (title, description) = when (state) {
        MainState.NO_RECORD -> {
            Pair(
                stringResource(R.string.reward_main_title_no_record),
                stringResource(R.string.reward_main_description_no_record)
            )
        }

        MainState.NO_AVAILABLE_GIFT -> {
            Pair(
                stringResource(R.string.reward_main_title_no_gift),
                stringResource(R.string.reward_main_description_default)
            )
        }

        MainState.AVAILABLE_GIFT -> {
            Pair(
                stringResource(R.string.reward_main_title_default, dayStreakCount),
                stringResource(R.string.reward_main_description_default)
            )
        }

        MainState.DONE -> {
            Pair(
                stringResource(R.string.reward_main_title_done),
                stringResource(R.string.reward_main_description_done)
            )
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = title,
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.DeepBlue99
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        Text(
            text = description,
            style = DonmaniTheme.typography.Body2,
            color = DonmaniTheme.colors.DeepBlue90
        )
        Image(
            modifier = Modifier.weight(1f),
            painter = painterResource(
                if (state == MainState.DONE) {
                    com.gowoon.designsystem.R.drawable.reward_tobby_done
                } else {
                    com.gowoon.designsystem.R.drawable.reward_tobby_default
                }
            ),
            contentDescription = null
        )
        if (state == MainState.DONE) {
            PositiveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                label = stringResource(R.string.reward_main_go_to_review_button_title),
                onClick = onClickReview
            )
            Spacer(Modifier.height(10.dp))
            NegativeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                label = stringResource(R.string.reward_main_go_to_home_button_title),
                onClick = onClickGoToHome
            )
        } else {
            if (state == MainState.NO_AVAILABLE_GIFT) {
                MessageBox(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    message = stringResource(R.string.reward_main_no_gift_message)
                )
            }
            RoundedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                type = RoundedButtonRadius.Row,
                label = if (state == MainState.NO_RECORD) {
                    stringResource(R.string.reward_main_go_to_record_button_title)
                } else {
                    stringResource(R.string.reward_main_get_gift_button_title)
                },
                enable = state != MainState.NO_AVAILABLE_GIFT,
                onClick = if (state == MainState.NO_RECORD) {
                    onClickGoToRecord
                } else {
                    onClickGetGift
                }
            )
        }
    }
}

@Composable
private fun FeedbackContent(modifier: Modifier = Modifier) {
}

@Composable
private fun GiftOpenContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.reward_open_title),
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.DeepBlue99
        )
    }
}

@Composable
private fun GiftConfirmContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.reward_open_confirm_title),
            style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
            color = DonmaniTheme.colors.DeepBlue99
        )
    }
}