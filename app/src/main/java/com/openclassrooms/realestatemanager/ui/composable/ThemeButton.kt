package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.idrisssouissi.smartbait.presentation.components.ThemeText
import com.idrisssouissi.smartbait.presentation.components.ThemeTextStyle
import com.openclassrooms.realestatemanager.R

@Composable
fun ThemeButton(onClick: () -> Unit,
                text: String,
                modifier: Modifier = Modifier,
                enabled: Boolean = true,
                elevation: Dp = 0.dp,
                imageVector: ImageVector? = null) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(5.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation),
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue,
            contentColor = White,
            disabledContainerColor = LightGray,
            disabledContentColor = White
        )
    ) {

        if (imageVector != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "Calculatrice",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(5.dp))

                ThemeText(
                    text = text,
                    style = ThemeTextStyle.BUTTON,
                    color = White
                )
            }
        }else {
            ThemeText(
                text = text,
                style = ThemeTextStyle.BUTTON,
                color = White
            )
        }


    }
}