package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ThemeOutlinedTextField(
    onValueChanged: (String) -> Unit,
    value: String,
    labelID: Int,
    imeAction: ImeAction,
    iconText: String? = null,
    keyboardType: KeyboardType,
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
        onValueChange = { newValue ->
            onValueChanged(newValue)
        },
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
                Text(
                    text = iconText,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(end = 5.dp)
                )
            } else {
                trailingIcon?.invoke()

            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (!singleLine) ImeAction.Default else imeAction,
            keyboardType = keyboardType
        ),
        maxLines = if (!singleLine) 5 else 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Green,
        ),
    )
}
