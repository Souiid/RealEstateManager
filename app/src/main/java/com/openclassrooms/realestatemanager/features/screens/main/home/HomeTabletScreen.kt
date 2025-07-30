package com.openclassrooms.realestatemanager.features.screens.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.features.screens.CurrencyViewModel
import com.openclassrooms.realestatemanager.features.screens.main.descrpition.DetailScreen
import com.openclassrooms.realestatemanager.features.screens.main.descrpition.RealtyDescriptionViewModel
import com.openclassrooms.realestatemanager.ui.composable.ThemeText
import com.openclassrooms.realestatemanager.ui.composable.ThemeTextStyle
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeTabletScreen(
    realitiesViewModel: RealitiesViewModel = koinViewModel(),
    detailViewModel: RealtyDescriptionViewModel = koinViewModel(),
    currencyViewModel: CurrencyViewModel = koinViewModel(),
    onSimulateClick: (Int, Boolean)-> Unit,
    criteria: SearchCriteria?,
) {
    val realities by realitiesViewModel.realties.collectAsState(initial = emptyList())
    val selectedRealty = detailViewModel.selectedRealty.collectAsState()
    var realtyAgent by remember { mutableStateOf<RealtyAgent?>(null) }
    val statusDateString = selectedRealty.value?.let {
        if (!it.isAvailable) it.saleDate?.let(detailViewModel::getTodayDate) ?: "N/A"
        else detailViewModel.getTodayDate(it.entryDate)
    } ?: "N/A"

    val isEuro by currencyViewModel.isEuroFlow.collectAsState()

    LaunchedEffect(criteria) {
        realitiesViewModel.setCriteria(criteria)
    }

    LaunchedEffect(Unit) {
        realitiesViewModel.initRealtyRepository()
    }

    LaunchedEffect(selectedRealty.value) {
        selectedRealty.value?.let { realty ->
            realtyAgent = detailViewModel.getAgentRepository(realty.agentId)
        }
    }


    if (realities.isNotEmpty()) {

        Row(modifier = Modifier
            .fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                RealtyLazyColumn(
                    realties = realities,
                    isEuro = isEuro,
                    onNext = { realtyID ->
                        val realty = realities.firstOrNull { it.id == realtyID }
                        detailViewModel.setSelectedRealty(realty)
                    }
                )
            }

            Box(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxHeight()
            ) {
                if (selectedRealty.value != null) {
                    DetailScreen(
                        realty = selectedRealty.value!!,
                        isEuro = isEuro,
                        realtyAgent = realtyAgent,
                        statusDateString = statusDateString,
                        isSavedInDollar = !selectedRealty.value!!.primaryInfo.isEuro,
                        onPrimaryButtonClick = { realty ->
                            detailViewModel.updateRealtyStatus(realty)
                        },
                        onSimulateClick = { price, isSavedInDollar -> onSimulateClick(price, isSavedInDollar)}
                    )
                }
            }
        }

    } else {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(20.dp))

            ThemeText(
                text = stringResource(R.string.no_realty),
                style = ThemeTextStyle.NORMAL
            )
        }
    }
}

@Preview
@Composable
fun HomeTabletPreview() {

}