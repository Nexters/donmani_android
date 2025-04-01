package com.gowoon.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gowoon.designsystem.component.AppBar
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.model.record.BadCategory
import com.gowoon.model.record.Category
import com.gowoon.model.record.ConsumptionType
import com.gowoon.model.record.GoodCategory
import com.gowoon.model.record.getTitle
import com.gowoon.model.record.isDeleted
import com.gowoon.statistics.component.PercentageIndicator
import com.gowoon.ui.TransparentScaffold
import com.gowoon.ui.component.NoticeBanner
import com.gowoon.ui.component.StatisticsCategoryChip

@Composable
internal fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    onClickRequest: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TransparentScaffold(
        topBar = {
            AppBar(
                title = stringResource(
                    R.string.statistics_app_bar_title,
                    state.year.toString().takeLast(2),
                    state.month
                ),
                onClickNavigation = onClickBack
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))
            HeaderNoticeBanner(onClick = onClickRequest)
            Spacer(Modifier.height(24.dp))
            StatisticsContent(categoryCounts = state.categoryCounts)
            Spacer(Modifier.height(118.dp))
        }

    }
}

@Composable
private fun HeaderNoticeBanner(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    NoticeBanner(modifier = modifier) {
        Column {
            Text(
                text = stringResource(R.string.notice_banner_title),
                style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.SemiBold),
                color = DonmaniTheme.colors.Gray95
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.notice_banner_description),
                style = DonmaniTheme.typography.Body2,
                color = DonmaniTheme.colors.DeepBlue95
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.noRippleClickable { onClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.notice_banner_request_btn),
                    style = DonmaniTheme.typography.Body2,
                    color = DonmaniTheme.colors.DeepBlue99
                )
                Spacer(Modifier.width(2.dp))
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.arrow_right),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun StatisticsContent(
    modifier: Modifier = Modifier,
    categoryCounts: Map<Category, Int>
) {
    Column(modifier.fillMaxWidth()) {
        StatisticsCard(
            type = ConsumptionType.GOOD,
            categoryCounts = categoryCounts.filter {
                it.key is GoodCategory
            }
        )
        Spacer(Modifier.height(20.dp))
        StatisticsCard(
            type = ConsumptionType.BAD,
            categoryCounts = categoryCounts.filter { it.key is BadCategory }
        )
    }
}

@Composable
private fun StatisticsCard(
    type: ConsumptionType,
    categoryCounts: Map<Category, Int>
) {
    var totalCount = categoryCounts.filterKeys { !it.isDeleted() }.values.sum()
    Column(
        Modifier
            .fillMaxWidth()
            .background(color = DonmaniTheme.colors.DeepBlue70, shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        if (totalCount > 0) {
            Text(
                text = stringResource(R.string.statistics_card_title, type.title, totalCount),
                style = DonmaniTheme.typography.Body1.copy(fontWeight = FontWeight.Bold),
                color = DonmaniTheme.colors.Gray99
            )
            when (type) {
                ConsumptionType.GOOD -> {
                    GoodCategory.entries.filterNot { it.deleted }.forEach {
                        StatisticsCategoryItem(
                            type = ConsumptionType.GOOD,
                            category = it,
                            count = categoryCounts[it] ?: 0,
                            totalCount = totalCount
                        )
                    }
                }

                ConsumptionType.BAD -> {
                    BadCategory.entries.filterNot { it.deleted }.forEach {
                        StatisticsCategoryItem(
                            type = ConsumptionType.BAD,
                            category = it,
                            count = categoryCounts[it] ?: 0,
                            totalCount = totalCount
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticsCategoryItem(
    type: ConsumptionType,
    category: Category,
    count: Int,
    totalCount: Int
) {
    val percentage = count.toFloat() / totalCount.toFloat()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatisticsCategoryChip(category = category)
            Spacer(Modifier.width(12.dp))
            Text(
                text = category.getTitle(type),
                style = DonmaniTheme.typography.Body2,
                color = DonmaniTheme.colors.Gray95
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            PercentageIndicator(
                modifier = Modifier
                    .width(120.dp)
                    .height(6.dp),
                trackColor = DonmaniTheme.colors.DeepBlue80,
                targetColor = DonmaniTheme.colors.DeepBlue99,
                progress = percentage
            )
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier.width(40.dp),
                text = "${(percentage * 100).toInt()}%",
                style = DonmaniTheme.typography.Body2,
                color = DonmaniTheme.colors.Gray95,
                textAlign = TextAlign.End
            )
        }
    }
}