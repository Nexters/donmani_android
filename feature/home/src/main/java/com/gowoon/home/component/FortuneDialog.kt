package com.gowoon.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gowoon.designsystem.component.BaseRoundedButton
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.home.R
import com.gowoon.model.fortune.Fortune
import java.time.LocalDate

@Composable
internal fun FortuneDialog(
    fortuneData: Fortune,
    showAdditionalInfo: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        FortuneDialogContent(
            modifier = modifier,
            fortuneData = fortuneData,
            showAdditionalInfo = showAdditionalInfo,
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
private fun FortuneDialogContent(
    modifier: Modifier = Modifier,
    fortuneData: Fortune,
    showAdditionalInfo: Boolean,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = DonmaniTheme.colors.PurpleBlue99, shape = RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FortuneHeader(date = fortuneData.date)
        FortuneContent(fortuneContent = fortuneData.content, fortuneItem = fortuneData.item)
        FortuneButton(showAdditionalInfo = showAdditionalInfo, onClick = onDismissRequest)
    }
}

@Composable
private fun FortuneHeader(date: LocalDate) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            modifier = Modifier.size(68.dp),
            painter = painterResource(com.gowoon.designsystem.R.drawable.fortune_tobby),
            contentDescription = null
        )
        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.fortune_title_prefix),
                style = DonmaniTheme.typography.Body2,
                color = DonmaniTheme.colors.PurpleBlue70
            )
            Text(
                text = stringResource(
                    R.string.fortune_title,
                    date.year,
                    date.monthValue,
                    date.dayOfMonth
                ),
                style = DonmaniTheme.typography.Heading3.copy(fontWeight = FontWeight.Bold),
                color = DonmaniTheme.colors.DeepBlue20
            )
        }
    }
}

@Composable
private fun FortuneContent(fortuneContent: String, fortuneItem: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = fortuneContent,
            style = DonmaniTheme.typography.Body2,
            color = DonmaniTheme.colors.DeepBlue20
        )
        Box(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(100.dp),
                    color = DonmaniTheme.colors.PurpleBlue60
                )
                .padding(vertical = 6.dp, horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.fortune_item_prefix) + fortuneItem,
                style = DonmaniTheme.typography.Body3,
                color = DonmaniTheme.colors.Common0
            )
        }
    }
}

@Composable
private fun FortuneButton(
    showAdditionalInfo: Boolean,
    onClick: () -> Unit
) {
    Column {
        BaseRoundedButton(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = DonmaniTheme.colors.Gray99,
            contentColor = DonmaniTheme.colors.DeepBlue20,
            radius = 16.dp,
            verticalPadding = 16.dp,
            horizontalPadding = 16.dp,
            label = stringResource(R.string.fortune_button),
            onClick = onClick
        )
        if (showAdditionalInfo) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                text = stringResource(R.string.fortune_additional_info),
                style = DonmaniTheme.typography.Body3,
                color = DonmaniTheme.colors.Gray40
            )
        }
    }
}
