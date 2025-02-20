package com.gowoon.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.R
import com.gowoon.designsystem.theme.DonmaniTheme
import com.gowoon.designsystem.util.noRippleClickable

enum class TooltipDirection {
    TopOf, BottomOf
}

enum class TooltipCaretAlignment {
    Start, Center, End
}

@Composable
fun Tooltip(
    modifier: Modifier = Modifier,
    direction: TooltipDirection,
    caretAlignment: TooltipCaretAlignment,
    backgroundColor: Color = DonmaniTheme.colors.DeepBlue10,
    contentColor: Color = DonmaniTheme.colors.DeepBlue99,
    message: String,
    onClick: () -> Unit
) {
    Column(modifier = modifier.noRippleClickable { onClick() }) {
        when (direction) {
            TooltipDirection.TopOf -> {
                TooltipBody(
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
                    message = message
                )
                TooltipCaret(
                    caretAlignment = caretAlignment,
                    backgroundColor = backgroundColor,
                    direction = direction
                )
            }

            TooltipDirection.BottomOf -> {
                TooltipCaret(
                    caretAlignment = caretAlignment,
                    backgroundColor = backgroundColor,
                    direction = direction
                )
                TooltipBody(
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
                    message = message
                )
            }
        }
    }
}

@Composable
private fun TooltipBody(
    backgroundColor: Color,
    contentColor: Color,
    message: String,
) {
    Row(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = message,
            color = contentColor,
            style = DonmaniTheme.typography.Body3
        )
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ImageVector.vectorResource(R.drawable.close),
            tint = contentColor,
            contentDescription = null
        )
    }
}

@Composable
private fun ColumnScope.TooltipCaret(
    caretAlignment: TooltipCaretAlignment,
    direction: TooltipDirection,
    backgroundColor: Color,
) {
    val caretModifier = when (caretAlignment) {
        TooltipCaretAlignment.Start -> {
            Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp)
        }

        TooltipCaretAlignment.Center -> {
            Modifier.align(Alignment.CenterHorizontally)
        }

        TooltipCaretAlignment.End -> {
            Modifier
                .align(Alignment.End)
                .padding(end = 12.dp)
        }
    }.let {
        when (direction) {
            TooltipDirection.TopOf -> {
                it.offset(y = (-1).dp)
            }

            TooltipDirection.BottomOf -> {
                it
                    .offset(y = 1.dp)
                    .rotate(180f)
            }
        }
    }
    Icon(
        modifier = caretModifier,
        imageVector = ImageVector.vectorResource(R.drawable.tooltip_caret),
        tint = backgroundColor,
        contentDescription = null
    )
}

@Preview
@Composable
private fun TooltipPreview() {
    Tooltip(
        direction = TooltipDirection.TopOf,
        caretAlignment = TooltipCaretAlignment.Center,
        message = "가나다라마바사"
    ) {}
}