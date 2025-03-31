package com.gowoon.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.component.BottomSheet
import com.gowoon.designsystem.component.BottomSheetButtonType
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.home.R
import java.time.LocalDate

@Composable
internal fun StarBottleOpenBottomSheet(
    onDismissRequest: () -> Unit,
    onClickGoToStarBottle: () -> Unit
) {
    BottomSheet(
        buttonType = BottomSheetButtonType.Single(title = stringResource(R.string.star_bottle_open_bottom_sheet_confirm)),
        onDismissRequest = onDismissRequest,
        canDismiss = false,
        content = {
            StarBottleOpenContent {
                onDismissRequest()
                onClickGoToStarBottle()
            }
        }
    )
}

@Composable
private fun StarBottleOpenContent(onClickGoToStarBottle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                R.string.star_bottle_open_bottom_sheet_title,
                LocalDate.now().monthValue
            ),
            color = DonmaniTheme.colors.DeepBlue99,
            style = DonmaniTheme.typography.Heading1.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.star_bottle_open_bottom_sheet_description),
            color = DonmaniTheme.colors.Gray95,
            style = DonmaniTheme.typography.Body1
        )
        Spacer(Modifier.height(24.dp))
        Icon(
            painter = painterResource(com.gowoon.designsystem.R.drawable.star_bottle_open_img),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.noRippleClickable { onClickGoToStarBottle() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.star_bottle_open_bottom_sheet_go_btn),
                color = DonmaniTheme.colors.DeepBlue90,
                style = DonmaniTheme.typography.Body1
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(com.gowoon.designsystem.R.drawable.arrow_right),
                tint = DonmaniTheme.colors.DeepBlue90,
                contentDescription = null
            )
        }
    }
}