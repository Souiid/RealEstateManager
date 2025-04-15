package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ThemeDialog(
    title: String,
    description: String,
    primaryButtonTitle: String,
    onPrimaryButtonClick: () -> Unit,
    secondaryButtonTitle: String? = null,
    onSecondaryButtonClick: (() -> Unit)? = null,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = description,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Optional secondary button
                    if (secondaryButtonTitle != null && onSecondaryButtonClick != null) {
                        TextButton(onClick = onSecondaryButtonClick) {
                            Text(text = secondaryButtonTitle)
                        }
                    }
                    // Primary button
                    TextButton(onClick = onPrimaryButtonClick) {
                        Text(text = primaryButtonTitle)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewThemeDialog() {
    ThemeDialog(
        title = "Title",
        description = "Description",
        primaryButtonTitle = "Choisir dans la gallerie",
        onPrimaryButtonClick = {},
        secondaryButtonTitle = "Prendre une photo",
        onSecondaryButtonClick = {},
        onDismissRequest = {}
    )
}