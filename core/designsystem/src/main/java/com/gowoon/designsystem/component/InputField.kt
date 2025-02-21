package com.gowoon.designsystem.component

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gowoon.designsystem.theme.DonmaniTheme

sealed interface InputFieldHeight {
    data class FIXED(val height: Dp) : InputFieldHeight
    data object WRAPCONENT : InputFieldHeight
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    height: InputFieldHeight,
    text: TextFieldState,
    placeholder: String? = null,
    textColor: Color = DonmaniTheme.colors.Gray95,
    placeholderColor: Color = DonmaniTheme.colors.DeepBlue80,
    brushColor: Color = DonmaniTheme.colors.PurpleBlue70,
    textStyle: TextStyle = DonmaniTheme.typography.Body1,
    maxLength: Int = 100,
    forceHaptic: Boolean = false
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(8.dp)
    ) {
        ScrollableInputField(
            height = height,
            text = text,
            placeholder = placeholder,
            textColor = textColor,
            placeholderColor = placeholderColor,
            brushColor = brushColor,
            textStyle = textStyle,
            maxLength = maxLength,
            forceHaptic = forceHaptic
        )
        Spacer(Modifier.height(4.dp))
        Text(
            modifier = Modifier.align(Alignment.End),
            text = "${text.text.length}/${maxLength}",
            color = placeholderColor,
            style = DonmaniTheme.typography.Body2
        )
    }
}

@Composable
private fun ScrollableInputField(
    modifier: Modifier = Modifier,
    height: InputFieldHeight,
    text: TextFieldState,
    placeholder: String?,
    textColor: Color,
    placeholderColor: Color,
    brushColor: Color,
    textStyle: TextStyle,
    maxLength: Int,
    forceHaptic: Boolean
) {
    val scrollState = rememberScrollState()
    val customTextSelectionColors = TextSelectionColors(
        handleColor = brushColor,
        backgroundColor = brushColor
    )
    val heightModifier = when (height) {
        is InputFieldHeight.FIXED -> Modifier.height(height.height)
        is InputFieldHeight.WRAPCONENT -> Modifier.wrapContentHeight()
    }

    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    LaunchedEffect(text.text) {
        if(text.text.isNotEmpty() && forceHaptic){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(50)
            }
        }
    }

    Box {
        if (text.text.isEmpty() && !placeholder.isNullOrEmpty()) {
            Text(
                text = placeholder,
                color = placeholderColor,
                style = textStyle
            )
        }
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            BasicTextField(
                modifier = modifier
                    .then(heightModifier)
                    .fillMaxWidth(),
                state = text,
                textStyle = textStyle.copy(color = textColor),
                cursorBrush = SolidColor(brushColor),
                scrollState = scrollState,
                inputTransformation = {
                    takeIf { it.length > maxLength }?.revertAllChanges()
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
    }
}

@Preview
@Composable
private fun InputFieldPreview() {
    val text = rememberTextFieldState()
    InputField(
        height = InputFieldHeight.FIXED(95.dp),
        text = text,
        placeholder = "placeholder"
    )
}