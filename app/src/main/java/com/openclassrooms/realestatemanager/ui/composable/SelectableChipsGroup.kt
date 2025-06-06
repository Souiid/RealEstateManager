package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.idrisssouissi.smartbait.presentation.components.ThemeText
import com.idrisssouissi.smartbait.presentation.components.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.room.Amenity

@Composable
fun SelectableChipsGroup(
    selectedOptions: List<Amenity>,
    onSelectionChanged: (List<Amenity>) -> Unit
) {
    val amenityList = Amenity.entries.map { it to stringResource(it.labelResId) }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp
    ) {
        amenityList.forEach { (amenity, label) ->
            val isSelected = amenity in selectedOptions
            Chip(
                label = label,
                selected = isSelected,
                onClick = {
                    val updated = if (isSelected) {
                        selectedOptions - amenity
                    } else {
                        selectedOptions + amenity
                    }
                    onSelectionChanged(updated)
                }
            )
        }
    }
}

@Composable
fun Chip(label: String, selected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = true,
                onClick = { onClick() }
            ),
        shape = RoundedCornerShape(50),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
        border = BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary else Color.Gray
        )) {
        ThemeText(
            text = label,
            style = ThemeTextStyle.CHIP,
            padding = 6.dp
        )
    }
}