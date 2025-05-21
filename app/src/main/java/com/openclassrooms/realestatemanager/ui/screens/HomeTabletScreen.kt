package com.openclassrooms.realestatemanager.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.ui.screens.RealtyLazyColumn

@Composable
fun HomeTabletScreen(
    realitiesViewModel: RealitiesViewModel,
    detailViewModel: RealtyDescriptionViewModel,
) {
    val realities by realitiesViewModel.realities.collectAsState(initial = emptyList())
    val selectedRealty = detailViewModel.selectedRealty.collectAsState().value
    var selectedID by remember { mutableStateOf<Int?>(null) }
    var realtyAgent by remember { mutableStateOf<RealtyAgent?>(null) }

    LaunchedEffect(selectedID) {
        selectedID?.let {
            detailViewModel.getRealtyFromID(it)
        }
    }

// 2. Quand selectedRealty change, on met à jour l’agent
    LaunchedEffect(selectedRealty) {
        selectedRealty?.let {
            realtyAgent = detailViewModel.getAgentRepository(it.agentId)
        }
    }

    val statusDateString = remember(selectedRealty) {
        selectedRealty?.let {
            if (!it.isAvailable) {
                it.saleDate?.let { date -> detailViewModel.getTodayDate(date) } ?: "N/A"
            } else {
                detailViewModel.getTodayDate(it.entryDate)
            }
        } ?: "N/A"
    }

    if (realities.isNotEmpty()) {
        Row() {

            Box(modifier = Modifier.width(300.dp)) {
                RealtyLazyColumn(
                    realties = realities,
                    onNext = { realtyID ->
                        selectedID = realtyID

                    },
                )
            }


            Text(text = "SelectedID: $selectedID")

            Box(modifier = Modifier.width(700.dp)) {
                Log.d("aaa", "Selected realty: $selectedRealty")

                if (selectedRealty != null) {
                    Log.d("aaa", "Selected realty: $selectedRealty")
                    DetailScreen(
                        realty = selectedRealty,
                        realtyAgent = realtyAgent,
                        statusDateString = statusDateString ?: "N/A",
                        onPrimaryButtonClick = { realty ->
                            detailViewModel.updateRealtyStatus(realty)
                        }
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