package com.idrisssouissi.smartbait.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.ui.theme.Black
import com.openclassrooms.realestatemanager.ui.theme.Gray

@Composable
fun ThemeText(
    text: String,
    style: ThemeTextStyle,
    color: Color = Black,
    padding: Dp = 0.dp,
    textAlign: TextAlign = TextAlign.Start,
) {
    val (fontSize, fontWeight) = when (style) {
        ThemeTextStyle.TITLE -> 28.sp to FontWeight.Bold
        ThemeTextStyle.SUBTITLE -> 20.sp to FontWeight.Bold
        ThemeTextStyle.NORMAL -> 18.sp to FontWeight.Normal
        ThemeTextStyle.LITTLE -> 14.sp to FontWeight.Normal
        ThemeTextStyle.SECTION_TITLE -> 18.sp to FontWeight.Bold
        ThemeTextStyle.BUTTON -> 18.sp to FontWeight.SemiBold
        ThemeTextStyle.CHIP -> 14.sp to FontWeight.Normal
        ThemeTextStyle.LABEL -> 16.sp to FontWeight.Normal


    }

    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        lineHeight = lineHeightFor(fontSize),
        textAlign = textAlign,
        color = if (style == ThemeTextStyle.LITTLE) Gray else color,
        modifier = Modifier.padding(padding)

    )
}

fun lineHeightFor(fontSize: TextUnit): TextUnit {
    return (fontSize.value * 1.4).sp
}