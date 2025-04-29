package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.ui.theme.Black
import com.openclassrooms.realestatemanager.ui.theme.Green

@Composable
fun ThemeOutlinedTFForDPD(
    value: String,
    labelID: Int,
    iconText: String? = null,
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    height: Dp = 56.dp,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(height),
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {},
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = stringResource(id = labelID),
            )
        },
        singleLine = singleLine,
        textStyle = TextStyle(
            color = DarkGray,
            fontSize = 16.sp
        ),
        trailingIcon = {
            if (iconText != null) {
                Text(text = iconText)
            } else {
                trailingIcon?.invoke()

            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Black,
            unfocusedLabelColor = Black
        ),
    )
}
