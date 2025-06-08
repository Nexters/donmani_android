package com.gowoon.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.pxToDp
import com.gowoon.model.record.Category
import com.gowoon.model.reward.DecorationAnimation
import com.gowoon.model.reward.DecorationPosition
import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.getDecorationAnimation
import com.gowoon.model.reward.getDecorationPosition
import com.gowoon.ui.util.getColor

enum class BGMode { DEFAULT, SPECIAL }

@Composable
fun CategoryBackground(category: Category?) {
    Box(Modifier.fillMaxSize()) {
        GradientBackground()
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            category?.getColor() ?: Color.Transparent,
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun GradientBackground(
    mode: BGMode = BGMode.DEFAULT,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        if (mode == BGMode.DEFAULT) DonmaniTheme.colors.DeepBlue30 else Color(
                            0xFF020617
                        ),
                        if (mode == BGMode.DEFAULT) DonmaniTheme.colors.DeepBlue50 else Color(
                            0xFF091958
                        )
                    )
                )
            )
    ) {
        content()
    }
}

@Composable
fun DecoratedBackground(
    background: String,
    effect: String
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(effect))
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = background,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun Decoration(targetRect: Rect, decoration: Gift?) {
    decoration?.let {
        val decorationOffset = when (getDecorationPosition(it.id)) {
            DecorationPosition.TOP_START -> {
                Pair(
                    targetRect.topLeft.x.pxToDp() - 10.dp,
                    targetRect.topLeft.y.pxToDp() - 80.dp
                )
            }

            DecorationPosition.BOTTOM_END -> {
                Pair(
                    targetRect.bottomRight.x.pxToDp() - 70.dp,
                    targetRect.bottomRight.y.pxToDp()
                )
            }

            DecorationPosition.ABOVE_BOTTLE -> {
                Pair(
                    targetRect.topCenter.x.pxToDp() - 40.dp,
                    targetRect.topCenter.y.pxToDp() - 40.dp
                )
            }
        }
        val animationOffset by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 20f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val animatedModifier = when (getDecorationAnimation(it.id)) {
            DecorationAnimation.VERTICAL -> {
                Modifier.offset(y = animationOffset.dp)
            }

            DecorationAnimation.HORIZONTAL -> {
                Modifier.offset(x = animationOffset.dp)
            }

            DecorationAnimation.NONE -> Modifier
        }
        Box(Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier
                    .offset(
                        x = decorationOffset.first,
                        y = decorationOffset.second
                    )
                    .then(animatedModifier)
                    .size(80.dp),
                model = decoration,
                contentDescription = null
            )
        }
    }
}

@Composable
fun BBSScaffold(
    modifier: Modifier = Modifier,
    background: @Composable () -> Unit = {},
    applyPadding: Boolean = true,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        background()
        Scaffold(
            modifier = modifier
                .safeDrawingPadding()
                .padding(horizontal = if (applyPadding) DonmaniTheme.dimens.Margin20 else 0.dp),
            topBar = topBar,
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            containerColor = Color.Transparent,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            content = content
        )
    }
}