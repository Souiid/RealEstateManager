package com.openclassrooms.realestatemanager.ui.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealtiesScreen(
    viewModel: RealtiesViewModel,
    onNext: () -> Unit,
    activity: MainActivity) {
    val context = LocalContext.current
    val realities by viewModel.realties.collectAsState(initial = emptyList())
    RealEstateManagerTheme {
        if (realities.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(realities.size) { index ->
                        val realty = realities[index]
                        RealtyItem(
                            realty = realty,
                            viewModel = viewModel,
                            context = context,
                            onClick = {
                                viewModel.setSelectedRealty(realty)
                                onNext()
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RealtyItem(
    realty: Realty,
    viewModel: RealtiesViewModel,
    context: Context,
    onClick: () -> Unit
) {
    val uri = Uri.parse(realty.pictures.first().uriString)
    val bitmap = viewModel.uriToBitmapLegacy(context, uri) ?: return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))

            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = realty.primaryInfo.realtyType.name,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text(text = realty.primaryInfo.realtyPlace.name, color = Color.Gray)
                Text(
                    text = realty.primaryInfo.price.toString() + "$",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
