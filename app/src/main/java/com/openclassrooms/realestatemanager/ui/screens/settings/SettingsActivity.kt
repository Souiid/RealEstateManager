package com.openclassrooms.realestatemanager.ui.screens.settings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.idrisssouissi.smartbait.presentation.components.ThemeText
import com.idrisssouissi.smartbait.presentation.components.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar
import com.openclassrooms.realestatemanager.ui.screens.CurrencyViewModel
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme
import org.koin.androidx.compose.koinViewModel


class SettingsActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RealEstateManagerTheme {
                SettingsScreen(onBackClick = { finish() })
            }
        }
    }
}

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    currencyViewModel: CurrencyViewModel = koinViewModel()
) {
    val isEuro by currencyViewModel.isEuroFlow.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            ThemeTopBar(
                title = stringResource(R.string.settings),
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            CurrencySection(
                isEuro = isEuro,
                onCurrencySelected = { currencyViewModel.setIsEuro(it) }
            )
        }
    }
}

@Composable
fun CurrencySection(
    isEuro: Boolean,
    onCurrencySelected: (Boolean) -> Unit
) {
    val selectedIndex = if (isEuro) 0 else 1
    val options = listOf("€", "$")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemeText(
            text = stringResource(R.string.selected_currency),
            style = ThemeTextStyle.NORMAL
        )

        Spacer(Modifier.width(5.dp))
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    selected = index == selectedIndex,
                    icon = {},
                    onClick = { onCurrencySelected(index == 0) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    label = {
                        ThemeText(
                            text = label,
                            style = ThemeTextStyle.NORMAL
                        )
                    }
                )
            }
        }
    }
}