package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idrisssouissi.smartbait.presentation.components.ThemeText
import com.idrisssouissi.smartbait.presentation.components.ThemeTextStyle
import com.openclassrooms.realestatemanager.ui.theme.Black

//For datePickerDialog
@Composable
fun ThemeOutlinedTFForDPD(
    value: String,
    labelID: Int,
    iconText: String? = null,
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    height: Dp = 56.dp,
    isClearable: Boolean = false,
    onClick: () -> Unit,
    onClear: (() -> Unit)? = null,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(height)
) {
    OutlinedTextField(
        value = value,
        onValueChange = {}, // champ en lecture seule
        enabled = false,
        readOnly = true,
        singleLine = singleLine,
        modifier = modifier.clickable { onClick() },
        label = {
            ThemeText(
                text = stringResource(labelID),
                style = ThemeTextStyle.NORMAL
            )
        },
        textStyle = TextStyle(
            color = DarkGray,
            fontSize = 16.sp
        ),
        trailingIcon = {
            when {
                isClearable && onClear != null -> {
                    IconButton(onClick = { onClear() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
                iconText != null -> {
                    Text(text = iconText)
                }
                else -> {
                    trailingIcon?.invoke()
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Black,
            unfocusedLabelColor = Black
        )
    )
}
