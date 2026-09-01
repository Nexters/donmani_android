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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gowoon.designsystem.component.Tooltip
import com.gowoon.designsystem.component.TooltipCaretAlignment
import com.gowoon.designsystem.component.TooltipDirection
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable
import com.gowoon.designsystem.util.pxToDp
import com.gowoon.model.record.Category
import com.gowoon.model.reward.BottleType
import com.gowoon.model.reward.DecorationAnimation
import com.gowoon.model.reward.DecorationPosition
import com.gowoon.model.reward.Gift
import com.gowoon.model.reward.getDecorationAnimation
import com.gowoon.model.reward.getDecorationPosition
import com.gowoon.ui.component.StarBottleMode
import com.gowoon.ui.util.getColor
import com.gowoon.designsystem.R as DesignR

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
            contentScale = ContentScale.Crop
        )
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun Decoration(
    modifier: Modifier = Modifier,
    targetRect: Rect,
    decoration: Gift?,
    starBottleMode: StarBottleMode = StarBottleMode.Default,
    bottleType: BottleType,
    onClickDecoration: (() -> Unit)? = null
) {
    decoration?.let {
        val topCenterX = targetRect.topCenter.x.pxToDp()
        val topCenterY = targetRect.topCenter.y.pxToDp()

        fun aboveBottleOffset(starBottleMode: StarBottleMode) = run {
            val default = when (bottleType) {
                BottleType.DEFAULT -> Pair(50.dp, (-15).dp)
                BottleType.CIRCLE -> Pair(0.dp, 0.dp)
                BottleType.HEART -> Pair(60.dp, 20.dp)
            }
            val additional = if (starBottleMode == StarBottleMode.Default) {
                when (bottleType) {
                    BottleType.DEFAULT -> Pair(20.dp, -5.dp)
                    BottleType.CIRCLE -> Pair(0.dp, 10.dp)
                    BottleType.HEART -> Pair(20.dp, 18.dp)
                }
            } else {
                Pair(0.dp, 0.dp)
            }
            Pair(
                topCenterX - 40.dp + default.first + additional.first,
                topCenterY - 40.dp + default.second + additional.second
            )
        }

        val decorationOffset = when (getDecorationPosition(it.id)) {
            DecorationPosition.TOP_START -> {
                when (starBottleMode) {
                    StarBottleMode.Default -> {
                        Pair(
                            targetRect.topLeft.x.pxToDp() - 10.dp,
                            targetRect.topLeft.y.pxToDp() - 80.dp
                        )
                    }

                    StarBottleMode.Preview -> {
                        Pair(
                            targetRect.topLeft.x.pxToDp() - 70.dp,
                            targetRect.topLeft.y.pxToDp() - 10.dp
                        )
                    }
                }
            }

            DecorationPosition.BOTTOM_END -> {
                when (starBottleMode) {
                    StarBottleMode.Default -> {
                        Pair(
                            targetRect.bottomRight.x.pxToDp() - 70.dp,
                            targetRect.bottomRight.y.pxToDp() - 10.dp
                        )
                    }

                    StarBottleMode.Preview -> {
                        Pair(
                            targetRect.bottomRight.x.pxToDp() - 20.dp,
                            targetRect.bottomRight.y.pxToDp() - 50.dp
                        )
                    }
                }
            }

            DecorationPosition.ABOVE_BOTTLE -> {
                aboveBottleOffset(starBottleMode)
            }
        }
        val fortuneCtaOffset = aboveBottleOffset(StarBottleMode.Default)
        val showFortuneCta =
            !it.hidden && starBottleMode == StarBottleMode.Default && onClickDecoration != null
        val showHiddenFortuneCta =
            it.hidden && starBottleMode == StarBottleMode.Default && onClickDecoration != null
        val fortuneTooltipAnchor = if (it.hidden) {
            Pair(
                decorationOffset.first + 50.dp,
                decorationOffset.second + 20.dp
            )
        } else {
            Pair(
                fortuneCtaOffset.first + 48.dp,
                fortuneCtaOffset.second + 20.dp
            )
        }
        var fortuneTooltipWidth by remember { mutableStateOf(0) }
        val animationOffset by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 12f,
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

            DecorationAnimation.DIAGONAL -> {
                Modifier.offset(
                    x = animationOffset.dp,
                    y = -animationOffset.dp,
                )
            }

            DecorationAnimation.NONE -> Modifier
        }
        Box(modifier.fillMaxSize()) {
            if (showFortuneCta) {
                AsyncImage(
                    modifier = Modifier
                        .offset(
                            x = fortuneCtaOffset.first,
                            y = fortuneCtaOffset.second
                        )
                        .width(96.dp)
                        .noRippleClickable(onClickDecoration),
                    model = DesignR.drawable.fortune_tobby_cta,
                    contentDescription = null
                )
            }
            AsyncImage(
                modifier = Modifier
                    .offset(
                        x = decorationOffset.first,
                        y = decorationOffset.second
                    )
                    .then(animatedModifier)
                    .size(if (it.hidden && starBottleMode == StarBottleMode.Default) 100.dp else 80.dp)
                    .then(
                        if (it.hidden && onClickDecoration != null) {
                            Modifier.noRippleClickable(onClickDecoration)
                        } else Modifier
                    ),
                model = it.resourceUrl,
                contentDescription = null
            )
            if (showFortuneCta || showHiddenFortuneCta) {
                Tooltip(
                    modifier = Modifier
                        .offset(
                            x = fortuneTooltipAnchor.first - (fortuneTooltipWidth / 2).pxToDp(),
                            y = fortuneTooltipAnchor.second - 44.dp
                        )
                        .onSizeChanged { size -> fortuneTooltipWidth = size.width },
                    direction = TooltipDirection.TopOf,
                    caretAlignment = TooltipCaretAlignment.Center,
                    backgroundColor = DonmaniTheme.colors.Green50,
                    showCloseButton = false,
                    verticalPadding = 3.dp,
                    horizontalPadding = 5.dp,
                    cornerRadius = 6.dp,
                    message = stringResource(R.string.fortune_tooltip_message)
                ) { }
            }
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
