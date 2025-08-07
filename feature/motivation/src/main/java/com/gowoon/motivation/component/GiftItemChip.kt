package com.gowoon.motivation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.ui.component.NewBadge

@Composable
internal fun GiftItemChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    isNew: Boolean,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val borderModifier = if (selected) modifier.then(
        Modifier
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
    ) else modifier
    Box(
        modifier = borderModifier
            .aspectRatio(1f)
//            .size(105.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DonmaniTheme.colors.DeepBlue50.copy(alpha = 0.5f))
            .noRippleClickable { onClick() }
    ) {
        content()
        if (isNew) {
            NewBadge(modifier = Modifier.padding(10.dp))
        }
    }
}