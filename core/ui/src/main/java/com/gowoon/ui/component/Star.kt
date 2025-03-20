package com.gowoon.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import com.gowoon.designsystem.R
import com.gowoon.model.record.Category
import com.gowoon.model.record.Record
import com.gowoon.ui.util.getColor
import com.gowoon.ui.util.getNoConsumptionColor

@Composable
fun Star(
    modifier: Modifier = Modifier,
    showGlow: Boolean = false,
    size: Dp,
    record: Record
) {
    Box(modifier = modifier.size(size)) {
        if (showGlow) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.star_glow),
                contentDescription = null
            )
        }
        when (record) {
            is Record.NoConsumption -> {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    imageVector = ImageVector.vectorResource(R.drawable.star_shape),
                    tint = getNoConsumptionColor(),
                    contentDescription = null
                )
            }

            is Record.ConsumptionRecord -> {
                val brushGradient =
                    Brush.linearGradient(
                        listOf(
                            record.goodRecord?.category?.getColor() ?: Color.Unspecified,
                            record.badRecord?.category?.getColor() ?: Color.Unspecified
                        )
                    )
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.Offscreen
                        }
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(brushGradient, blendMode = BlendMode.SrcAtop)
                            }
                        },
                    tint = Color.Unspecified,
                    imageVector = ImageVector.vectorResource(R.drawable.star_shape),
                    contentDescription = null
                )
            }
        }
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.highlighter),
            contentDescription = null
        )
    }
}

@Composable
fun RecordStar(
    modifier: Modifier = Modifier,
    category: Category
) {
    Box(modifier = modifier.wrapContentSize()) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = ImageVector.vectorResource(R.drawable.star_shape),
            tint = category.getColor(),
            contentDescription = null
        )
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(R.drawable.highlighter_left),
            contentDescription = null
        )
    }
}