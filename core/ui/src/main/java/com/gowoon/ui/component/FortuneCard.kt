package com.gowoon.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.fortune.Fortune
import com.gowoon.ui.R
import java.time.LocalDate

/**
 * 홈의 운세 팝업과 운세 리스트 카드 뒷면이 공유하는 본문.
 * 버튼처럼 화면마다 다른 부분은 [footer] 로 받는다.
 */
@Composable
fun FortuneCard(
    modifier: Modifier = Modifier,
    fortune: Fortune,
    contentMaxLines: Int = Int.MAX_VALUE,
    footer: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(color = DonmaniTheme.colors.PurpleBlue99, shape = RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FortuneCardHeader(date = fortune.date)
        FortuneCardBody(
            content = fortune.content,
            item = fortune.item,
            contentMaxLines = contentMaxLines
        )
        footer()
    }
}

@Composable
private fun FortuneCardHeader(date: LocalDate) {
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
                text = stringResource(R.string.fortune_card_title_prefix),
                style = DonmaniTheme.typography.Body2,
                color = DonmaniTheme.colors.PurpleBlue70
            )
            Text(
                text = stringResource(
                    R.string.fortune_card_date,
                    date.year % 100,
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
private fun FortuneCardBody(
    content: String,
    item: String,
    contentMaxLines: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = content,
            style = DonmaniTheme.typography.Body2,
            color = DonmaniTheme.colors.DeepBlue20,
            maxLines = contentMaxLines,
            overflow = TextOverflow.Ellipsis
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
                text = stringResource(R.string.fortune_card_item_prefix) + item,
                style = DonmaniTheme.typography.Body3,
                color = DonmaniTheme.colors.Common0
            )
        }
    }
}
