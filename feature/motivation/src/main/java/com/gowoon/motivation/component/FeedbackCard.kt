package com.gowoon.motivation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.Card
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.reward.Feedback
import com.gowoon.motivation.R
import com.gowoon.ui.component.CardCategoryChip
import com.gowoon.ui.util.getColor
import com.gowoon.ui.util.getNoConsumptionColor

@Composable
internal fun FeedbackCard(modifier: Modifier = Modifier, feedback: Feedback) {
    Card(
        modifier = modifier
            .width(260.dp)
            .height(350.dp)
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            ),
        backgroundColor = (feedback.category?.getColor()
            ?: getNoConsumptionColor()).copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = DonmaniTheme.colors.Common100.copy(0.2f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp),

                ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.star_shape),
                    tint = DonmaniTheme.colors.Common0.copy(0.2f),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = stringResource(R.string.reward_feedback_card_topper),
                    style = DonmaniTheme.typography.Body3.copy(fontWeight = FontWeight.Bold),
                    color = DonmaniTheme.colors.DeepBlue99
                )
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = ImageVector.vectorResource(com.gowoon.designsystem.R.drawable.star_shape),
                    tint = DonmaniTheme.colors.Common0.copy(0.2f),
                    contentDescription = null
                )
            }
            Spacer(Modifier.height(40.dp))
            CardCategoryChip(category = feedback.category)
            Spacer(Modifier.height(16.dp))
            Text(
                text = feedback.title,
                style = DonmaniTheme.typography.Heading2.copy(fontWeight = FontWeight.Bold),
                color = DonmaniTheme.colors.Common0
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = feedback.description,
                style = DonmaniTheme.typography.Body2,
                color = DonmaniTheme.colors.Gray95,
                textAlign = TextAlign.Center
            )
        }
    }
}