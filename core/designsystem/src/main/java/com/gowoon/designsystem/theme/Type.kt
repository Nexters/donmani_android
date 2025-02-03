package com.gowoon.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gowoon.designsystem.R

val pretendard_fontfamily = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
)
@Immutable
data class Typography(
    val Title1: TextStyle = TextStyle(
        fontFamily = pretendard_fontfamily,
        fontSize = 30.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp
    ),
    val Heading1: TextStyle = TextStyle(
        fontFamily = pretendard_fontfamily,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    val Heading2: TextStyle = TextStyle(
        fontFamily = pretendard_fontfamily,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    val Heading3: TextStyle = TextStyle(
        fontFamily = pretendard_fontfamily,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    val Body1: TextStyle = TextStyle(
        fontFamily = pretendard_fontfamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    val Body2: TextStyle = TextStyle(
        fontFamily = pretendard_fontfamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    val Body3: TextStyle = TextStyle(
        fontFamily = pretendard_fontfamily,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    val Body4: TextStyle = TextStyle(
        fontFamily = pretendard_fontfamily,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp
    ),
)

val LocalTypography = staticCompositionLocalOf { Typography() }