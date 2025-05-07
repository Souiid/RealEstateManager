package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeTabletScreen(
    realitiesViewModel: RealitiesViewModel,
    detailViewModel: RealtyDescriptionViewModel,
) {
    val realities by realitiesViewModel.realities.collectAsState(initial = emptyList())
    var selectedID by remember { mutableStateOf<Int?>(null) }
    if (realities.isNotEmpty()) {
        Row() {

          //  RealtiesLazyColumn(
          //      modifier = Modifier.width(300.dp),
          //      realties = realities,
          //      onNext = { id ->
          //          selectedID = id
          //          detailViewModel.getRealtyFromID(id)
          //      }
          //
          //  )


            Text(text = "SelectedID: $selectedID")

          // Column(modifier = Modifier.weight(2f)) {
          //
          //         RealtyDescriptionScreen(
          //             viewModel = detailViewModel,
          //             realtyID = selectedID!!
          //         )
          //
          // }
        }
    } else {
        Text("Chargement en cours...")
    }
}

@Preview
@Composable
fun HomeTabletPreview() {

}