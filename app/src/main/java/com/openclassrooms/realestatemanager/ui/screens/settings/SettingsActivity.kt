package com.openclassrooms.realestatemanager.ui.screens.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.ui.composable.ThemeText
import com.openclassrooms.realestatemanager.ui.composable.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar
import com.openclassrooms.realestatemanager.ui.screens.CurrencyViewModel
import com.openclassrooms.realestatemanager.ui.theme.Black
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
    val context = LocalContext.current
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

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth().clickable {
                    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    context.startActivity(intent)
                },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ThemeText(
                    text = stringResource(R.string.network),
                    style = ThemeTextStyle.NORMAL
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    tint = Black,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
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