package com.openclassrooms.realestatemanager.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.famoco.projet9.Aspect_ratio
import com.famoco.projet9.Bathtub
import com.famoco.projet9.Bed
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent

@Composable
fun RealtyDescriptionScreen(
    viewModel: RealtyDescriptionViewModel,
    onBack: () -> Unit
) {

    val realty = viewModel.getSelectedRealty()
    var realtyAgent by remember { mutableStateOf<RealtyAgent?>(null) }

    LaunchedEffect(Unit) {
        realtyAgent = viewModel.getAgentRepository(realty?.agentId ?: 0)
    }

    if (realty != null) {
        DetailScreen(realty, viewModel)
    }
}

@Composable
fun DetailScreen(realty: Realty, viewModel: RealtyDescriptionViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Media")
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(realty.pictures.size) { index ->
                Spacer(modifier = Modifier.size(5.dp))
                RealtyPictureUI(realty.pictures[index], viewModel)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .height(1.dp), color = Color.Gray
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text("Description")
        Text(realty.primaryInfo.description)

        Spacer(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Column {
                RealtyPropertie(Aspect_ratio, "Surface", "${realty.primaryInfo.surface} m2")
                RealtyPropertie(Icons.Filled.Home, "Number of rooms", "${realty.primaryInfo.roomsNbr}")
                RealtyPropertie(Bathtub, "Number of bathrooms", "${realty.primaryInfo.bathroomsNbr}")
                RealtyPropertie(Bed, "Number of bedrooms", "${realty.primaryInfo.bedroomsNbr}")
            }

            RealtyPropertie(
                Icons.Filled.Place,
                "Location",
                realty.primaryInfo.realtyPlace.name
            )
        }

        LiteModeMapView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            viewModel = viewModel,
            realty = realty
        )


    }
}


@Composable
fun RealtyPictureUI(realtyPicture: RealtyPicture, viewModel: RealtyDescriptionViewModel) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(150.dp)
    ) {
        Image(
            bitmap = viewModel.uriToBitmapLegacy(context, Uri.parse(realtyPicture.uriString))
                ?.asImageBitmap() ?: return,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(12.dp)
        ) {
            Text(
                text = realtyPicture.description,
                color = Color.White,
            )
        }
    }
}



@Composable
fun RealtyPropertie(imageVector: ImageVector, title: String, value: String) {

    Row {
        Image(imageVector = imageVector, contentDescription = null)
        Column {
            Text(title)
            Text(value)
        }

    }
}

@Composable
fun LiteModeMapView(
    modifier: Modifier = Modifier,
    viewModel: RealtyDescriptionViewModel,
    realty: Realty
) {
    var location by remember { mutableStateOf<LatLng?>(null) }
    LaunchedEffect(Unit) {
        location = viewModel.fetchPlaceLatLng(realty.primaryInfo.realtyPlace.id)
    }
    if (location != null) {
        AndroidView(
            modifier = modifier,
            factory = { ctx ->
                val options = GoogleMapOptions().liteMode(true)
                MapView(ctx, options).apply {
                    onCreate(null)
                    getMapAsync { googleMap ->
                        val latitude = location?.latitude ?: return@getMapAsync
                        val longitude = location?.longitude ?: return@getMapAsync
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(latitude, longitude))
                        )
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    latitude,
                                    longitude
                                ), 15f
                            )
                        )
                    }
                }
            },
            update = { mapView ->
                mapView.onResume()
            }
        )
    }

}
