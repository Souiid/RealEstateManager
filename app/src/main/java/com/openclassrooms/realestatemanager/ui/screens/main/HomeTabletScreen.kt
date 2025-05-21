package com.openclassrooms.realestatemanager.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent

@Composable
fun HomeTabletScreen(
    realitiesViewModel: RealitiesViewModel,
    detailViewModel: RealtyDescriptionViewModel,
    onSimulateClick: (Double)-> Unit
) {
    val realities by realitiesViewModel.realties.collectAsState(initial = emptyList())
    val selectedRealty = detailViewModel.selectedRealty.collectAsState()
    var selectedID by remember { mutableStateOf<Int?>(null) }
    var realtyAgent by remember { mutableStateOf<RealtyAgent?>(null) }
    val statusDateString = selectedRealty.value?.let {
        if (!it.isAvailable) it.saleDate?.let(detailViewModel::getTodayDate) ?: "N/A"
        else detailViewModel.getTodayDate(it.entryDate)
    } ?: "N/A"
    val sortedRealities = realitiesViewModel.getFilteredRealties()

    val realitiesToUse: List<Realty> = sortedRealities.ifEmpty {
        realities
    }

    LaunchedEffect(Unit) {
        realitiesViewModel.initRealtyRepository()
    }

    LaunchedEffect(selectedID, selectedRealty.value) {
        selectedID?.let { id ->
            detailViewModel.getRealtyFromID(id)
        }
        selectedRealty.value?.let { realty ->
            realtyAgent = detailViewModel.getAgentRepository(realty.agentId)
        }
    }


    if (realities.isNotEmpty()) {
        realitiesViewModel.setAllRealities(realitiesToUse)

        Row(modifier = Modifier
            .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                RealtyLazyColumn(
                    realties = realitiesToUse,
                    onNext = { realtyID -> selectedID = realtyID }
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
                        realtyAgent = realtyAgent,
                        statusDateString = statusDateString,
                        onPrimaryButtonClick = { realty ->
                            detailViewModel.updateRealtyStatus(realty)
                        },
                        onSimulateClick = { price -> onSimulateClick(price)}
                    )
                }
            }
        }

    } else {
        Text("Chargement en cours...")
    }
}

@Preview
@Composable
fun HomeTabletPreview() {

}