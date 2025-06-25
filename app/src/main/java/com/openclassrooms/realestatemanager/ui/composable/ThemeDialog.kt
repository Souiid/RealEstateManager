package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.openclassrooms.realestatemanager.R

@Composable
fun ThemeDialog(
    title: String,
    description: String? = null,
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
                ThemeText(
                    text = title,
                    style = ThemeTextStyle.SUBTITLE
                )

                description?.let {
                    ThemeText(
                        text = it,
                        style = ThemeTextStyle.NORMAL
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onPrimaryButtonClick) {
                        ThemeButton(
                            onClick = onPrimaryButtonClick,
                            text = primaryButtonTitle,
                        )
                    }

                    if (secondaryButtonTitle != null && onSecondaryButtonClick != null) {
                        ThemeButton(
                            onClick = onSecondaryButtonClick,
                            text = secondaryButtonTitle,
                        )
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
        primaryButtonTitle = stringResource(R.string.choose_from_gallery),
        onPrimaryButtonClick = {},
        secondaryButtonTitle = stringResource(R.string.take_a_picture),
        onSecondaryButtonClick = {},
        onDismissRequest = {}
    )
}