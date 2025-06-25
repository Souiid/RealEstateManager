package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.idrisssouissi.smartbait.presentation.components.ThemeText
import com.idrisssouissi.smartbait.presentation.components.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentDropdown(
    agents: List<RealtyAgent>,
    selectedAgent: RealtyAgent?,
    onAgentSelected: (RealtyAgent) -> Unit,
    modifier: Modifier = Modifier,
    isForSearch: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    if (agents.isNotEmpty()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            OutlinedTextField(
                value = selectedAgent?.name ?: if (!isForSearch) agents.first().name else "",
                onValueChange = {},
                label = { ThemeText(
                    text = stringResource(R.string.agent),
                    style = ThemeTextStyle.NORMAL
                ) },
                singleLine = true,
                textStyle = TextStyle(
                    color = DarkGray,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                agents.forEach { agent ->
                    DropdownMenuItem(
                        text = { Text(agent.name) },
                        onClick = {
                            onAgentSelected(agent)
                            expanded = false
                        }
                    )
                }
            }
        }
    } else {
        ThemeText(
            text = stringResource(R.string.no_agent),
            style = ThemeTextStyle.NORMAL
        )
    }
}