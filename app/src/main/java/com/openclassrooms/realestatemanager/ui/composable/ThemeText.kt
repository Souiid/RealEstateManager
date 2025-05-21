package com.idrisssouissi.smartbait.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.ui.theme.Black

@Composable
fun ThemeText(
    text: String,
    style: ThemeTextStyle,
    color: Color = Black
) {
    val (fontSize, fontWeight) = when (style) {
        ThemeTextStyle.TITLE -> 36.sp to FontWeight.Bold
        ThemeTextStyle.NORMAL -> 26.sp to FontWeight.Bold
        ThemeTextStyle.LITTLE -> 24.sp to FontWeight.SemiBold
    }

    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        lineHeight = lineHeightFor(fontSize),
        textAlign = TextAlign.Start,
        color = color
    )
}

fun lineHeightFor(fontSize: TextUnit): TextUnit {
    return (fontSize.value * 1.4).sp
}