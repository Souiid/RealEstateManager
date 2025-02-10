package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.openclassrooms.realestatemanager.ui.theme.Black
import com.openclassrooms.realestatemanager.ui.theme.Transparent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeTopBar(
    barColor: Color = Transparent,
    itemColor: Color = Black,
    title: String,
    onBackClick: () -> Unit,
    isBackVisible: Boolean = true,
    actions: @Composable () -> Unit = {}
) {

    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = itemColor
            )
        },
        navigationIcon = {
            if (isBackVisible) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = itemColor,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            actions()
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = barColor
        )
    )
}