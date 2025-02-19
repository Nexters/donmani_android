package com.gowoon.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.model.record.Category
import com.gowoon.ui.util.getColor

enum class BGMode { DEFAULT, MAIN }

@Composable
fun CategoryBackground(category: Category?, content: @Composable () -> Unit) {
    GradientBackground {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .background(
                        shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp),
                        brush = Brush.verticalGradient(
                            listOf(
                                category?.getColor() ?: Color.Transparent,
                                Color.Transparent
                            )
                        )
                    )
            )
            content()
        }
    }
}

@Composable
fun GradientBackground(mode: BGMode = BGMode.DEFAULT, content: @Composable () -> Unit) {
    GradientBackground(
        startColor = if (mode == BGMode.DEFAULT) DonmaniTheme.colors.DeepBlue30 else Color(
            0xFF020617
        ),
        endColor = if (mode == BGMode.DEFAULT) DonmaniTheme.colors.DeepBlue50 else Color(
            0xFF091958
        ),
        showStarBg = mode == BGMode.MAIN,
        content = content
    )
}

@Composable
fun GradientBackground(
    startColor: Color,
    endColor: Color,
    showStarBg: Boolean,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        startColor,
                        endColor
                    )
                )
            )
    ) {
        if (showStarBg) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = ImageVector.vectorResource(R.drawable.star_background),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
        content()
    }
}

@Composable
fun TransparentScaffold(
    modifier: Modifier = Modifier,
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
    Scaffold(
        modifier = modifier
            .safeDrawingPadding()
            .padding(horizontal = DonmaniTheme.dimens.Margin20),
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