package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ThemeButton(onClick: () -> Unit, text: String, modifier: Modifier, enabled: Boolean = true) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(5.dp),
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue,
            contentColor = White,
            disabledContainerColor = LightGray,
            disabledContentColor = White
        )
    ) {
        Text(text = text, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
    }
}