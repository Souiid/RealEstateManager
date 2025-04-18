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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.famoco.projet9.Aspect_ratio
import com.famoco.projet9.Bathtub
import com.famoco.projet9.Bed
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
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
    var text = realty.primaryInfo.amenities.joinTo(separator = "\n")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.media),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(),
        )
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

        Text(
            text = stringResource(R.string.description),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Text(
            text = realty.primaryInfo.description, fontSize = 16.sp,
            fontWeight = FontWeight.W400
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.amenities),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(),
        )

        Text(
            text = realty.primaryInfo.description, fontSize = 16.sp,
            fontWeight = FontWeight.W400
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Column {
                RealtyProperty(Aspect_ratio, stringResource(R.string.surface), "${realty.primaryInfo.surface} m²")
                RealtyProperty(
                    Icons.Filled.Home,
                    stringResource(R.string.number_of_rooms),
                    "${realty.primaryInfo.roomsNbr}"
                )
                RealtyProperty(Bathtub, stringResource(R.string.number_of_bathrooms), "${realty.primaryInfo.bathroomsNbr}")
                RealtyProperty(Bed, stringResource(R.string.number_of_bedrooms), "${realty.primaryInfo.bedroomsNbr}")
            }

            RealtyProperty(
                Icons.Filled.Place,
                stringResource(R.string.location),
                realty.primaryInfo.realtyPlace.name
            )
        }

        LiteModeMapView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
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
fun RealtyProperty(imageVector: ImageVector, title: String, value: String) {

    Row {
        Image(imageVector = imageVector, contentDescription = null)
        Spacer(modifier = Modifier.size(5.dp))
        Column {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.W400)
        }

    }
}

@Composable
fun LiteModeMapView(
    modifier: Modifier = Modifier,
    realty: Realty
) {
    val location = realty.primaryInfo.realtyPlace.positionLatLng

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val options = GoogleMapOptions().liteMode(true)
            MapView(ctx, options).apply {
                onCreate(null)
                getMapAsync { googleMap ->
                    val latitude = location.latitude
                    val longitude = location.longitude
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
