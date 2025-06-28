package com.openclassrooms.realestatemanager.features.screens.main.home

import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.ui.composable.ThemeText
import com.openclassrooms.realestatemanager.ui.composable.ThemeTextStyle
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.features.screens.CurrencyViewModel
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RealitiesScreen(
    realitiesViewModel: RealitiesViewModel = koinViewModel(),
    currencyViewModel: CurrencyViewModel = koinViewModel(),
    onNext: (Int) -> Unit,
    criteria: SearchCriteria? = null,
) {
    val realities by realitiesViewModel.realties.collectAsState(initial = emptyList())

    val isEuro by currencyViewModel.isEuroFlow.collectAsState()

    LaunchedEffect(Unit) {
        realitiesViewModel.initRealtyRepository()
    }

    LaunchedEffect(criteria) {
        realitiesViewModel.setCriteria(criteria)
    }
    RealEstateManagerTheme {

        if (realities.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                RealtyLazyColumn(
                    realties = realities,
                    isEuro = isEuro,
                    onNext = { realtyID ->
                        onNext(realtyID)
                    })
            }
        }
    }
}

@Composable
fun RealtyLazyColumn(
    realties: List<Realty>,
    onNext: (Int) -> Unit,
    isEuro: Boolean
) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        items(realties.size) { index ->
            val realty = realties[index]
            val uri = Uri.parse(realty.pictures.first().uriString)
            val bitmap = Utils().uriToBitmapLegacy(context, uri) ?: return@items
            RealtyItem(
                realty = realty,
                bitmap = bitmap,
                isEuro = isEuro,
                onClick = {
                    onNext(realty.id)
                }
            )
        }
    }
}

@Composable
fun RealtyItem(
    realty: Realty,
    isEuro: Boolean,
    bitmap: Bitmap,
    onClick: () -> Unit
) {
    val isInDollar = !realty.primaryInfo.isEuro
    val priceComponent = Utils().getCorrectPriceComponent(realty.primaryInfo.price, isEuro, isInDollar)
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
                ThemeText(
                    text = realty.primaryInfo.realtyType.name,
                    style = ThemeTextStyle.SUBTITLE
                )
                ThemeText(
                    text = realty.primaryInfo.realtyPlace.name,
                    style = ThemeTextStyle.NORMAL
                )
                ThemeText(
                    text = "${priceComponent.price}${priceComponent.currency}",
                    style = ThemeTextStyle.NORMAL
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
