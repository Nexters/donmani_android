package com.gowoon.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.gowoon.designsystem.R
import com.gowoon.model.record.Category
import com.gowoon.model.record.GoodCategory
import com.gowoon.ui.util.getColor

@Composable
fun RecordStar(
    modifier: Modifier = Modifier,
    category: Category
) {
    Box(modifier = modifier) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.star_shape),
            tint = category.getColor(),
            contentDescription = null
        )
        Image(
            painter = painterResource(R.drawable.highlighter_left),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun Star() {
    RecordStar(
        category = GoodCategory.Energy
    )
}