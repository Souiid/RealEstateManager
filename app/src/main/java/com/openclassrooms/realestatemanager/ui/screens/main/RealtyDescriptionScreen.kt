package com.openclassrooms.realestatemanager.ui.screens.main

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.ui.composable.ThemeDialog
import java.util.Date

@Composable
fun RealtyDescriptionScreen(
    viewModel: RealtyDescriptionViewModel,
    realtyID: Int,
    onSimulateClick: (Int) -> Unit
) {

    val realty by viewModel.selectedRealty.collectAsState()
    var realtyAgent by remember { mutableStateOf<RealtyAgent?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getRealtyFromID(realtyID)
        realtyAgent = viewModel.getAgentRepository(realty?.agentId ?: 0)
    }

    val statusDateString = realty?.let {
        if (!it.isAvailable) {
            realty!!.saleDate?.let { viewModel.getTodayDate(it) } ?: "N/A"
        } else {
            viewModel.getTodayDate(realty!!.entryDate)
        }
    }

    if (realty != null) {
        DetailScreen(
            realty = realty!!,
            realtyAgent = realtyAgent,
            statusDateString = statusDateString!!,
            onPrimaryButtonClick = { viewModel.updateRealtyStatus(it) },
            onSimulateClick = { price-> onSimulateClick(price) })
    }
}


@Composable
fun DetailScreen(
    realty: Realty,
    realtyAgent: RealtyAgent?,
    statusDateString: String,
    onPrimaryButtonClick: (Realty) -> Unit,
    onSimulateClick: (Int) -> Unit
) {
    Log.d("DEBUG", "DetailScreen loaded for ID=${realty.id}, isAvailable=${realty.isAvailable}")
    val amenitiesLabels = realty.primaryInfo.amenities.map { amenity ->
        stringResource(id = amenity.labelResId)
    }

    val amenitiesText = if (amenitiesLabels.size <= 1) {
        amenitiesLabels.joinToString("")
    } else {
        amenitiesLabels.dropLast(1).joinToString(", ") + ", " + amenitiesLabels.last()
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                modifier = Modifier.fillMaxWidth(),
            )
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(realty.pictures.size) { index ->
                    Spacer(modifier = Modifier.size(5.dp))
                    RealtyPictureUI(realty.pictures[index])
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp), color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))

            StatusSection(realty = realty,
                agent = realtyAgent,
                statusDateString = statusDateString,
                onPrimaryButtonClick = { realty ->
                    onPrimaryButtonClick(realty)
                })
            DescriptionSection(realty)
            AmenitiesSection(amenitiesText)
            SpecificationSection(realty)
            MapSection(realty)

        }

        Button(
            onClick = { onSimulateClick(realty.primaryInfo.price) },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = "Calculatrice",
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(5.dp))
            Text(stringResource(R.string.simulate))
        }
    }

}

@Composable
fun StatusSection(
    realty: Realty,
    agent: RealtyAgent?,
    statusDateString: String,
    onPrimaryButtonClick: (Realty) -> Unit
) {
    val newAvailability = !realty.isAvailable
    val statusText =
        if (!realty.isAvailable) stringResource(R.string.sold) else stringResource(R.string.for_sale)
    val statusColor = if (!realty.isAvailable) Color.Red else Color(0xFF4CAF50)
    val statusDateLabel =
        if (!realty.isAvailable) stringResource(R.string.sold_on) else stringResource(R.string.listed_on)

    var isShowDialog by remember { mutableStateOf(false) }

    if (isShowDialog) {
        ThemeDialog(
            title = stringResource(R.string.put_realty_on_sale),
            description = "",
            primaryButtonTitle = stringResource(R.string.yes),
            secondaryButtonTitle = stringResource(R.string.cancel),

            onPrimaryButtonClick = {
                val newAvailability = !realty.isAvailable
                val updatedRealty = realty.copy(
                    isAvailable = newAvailability,
                    saleDate = if (!newAvailability) Date() else null
                )
                Log.d("DEBUG", "Clicked on sale status for ID=${realty.id}, new isAvailable=$newAvailability")
                onPrimaryButtonClick(updatedRealty)
                isShowDialog = false
            },
            onSecondaryButtonClick = {
                isShowDialog = false
            },

            onDismissRequest = {
                isShowDialog = false
            })
    }

    ExpandableSection(title = stringResource(R.string.status), expandedP = true) {
        Column {
            Button(
                onClick = { isShowDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                elevation = null
            ) {
                Text(
                    text = statusText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.price_colon) + " ${realty.primaryInfo.price} €",
                fontSize = 16.sp,
                fontWeight = FontWeight.W400
            )

            agent?.let {
                Text(
                    text = stringResource(R.string.agent_colon) + " ${it.name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400
                )
            }

            Text(
                text = "$statusDateLabel $statusDateString",
                fontSize = 16.sp,
                fontWeight = FontWeight.W400
            )
        }
    }
}

@Composable
fun MapSection(realty: Realty) {
    ExpandableSection(title = stringResource(R.string.location)) {
        LiteModeMapView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), realty = realty
        )
    }
}

@Composable
fun SpecificationSection(realty: Realty) {
    ExpandableSection(title = stringResource(R.string.specification)) {
        Row {
            Column {
                RealtyProperty(
                    Aspect_ratio,
                    stringResource(R.string.surface),
                    "${realty.primaryInfo.surface} m²"
                )
                RealtyProperty(
                    Icons.Filled.Home,
                    stringResource(R.string.number_of_rooms),
                    "${realty.primaryInfo.roomsNbr}"
                )
                RealtyProperty(
                    Bathtub,
                    stringResource(R.string.number_of_bathrooms),
                    "${realty.primaryInfo.bathroomsNbr}"
                )
                RealtyProperty(
                    Bed,
                    stringResource(R.string.number_of_bedrooms),
                    "${realty.primaryInfo.bedroomsNbr}"
                )
            }

            RealtyProperty(
                Icons.Filled.Place,
                stringResource(R.string.location),
                realty.primaryInfo.realtyPlace.name
            )
        }
    }
}

@Composable
fun AmenitiesSection(amenitiesText: String) {
    ExpandableSection(title = stringResource(R.string.amenities)) {
        Text(
            text = amenitiesText, fontSize = 16.sp, fontWeight = FontWeight.W400
        )
    }
}

@Composable
fun DescriptionSection(realty: Realty) {
    ExpandableSection(title = stringResource(R.string.description)) {
        Text(
            text = realty.primaryInfo.description, fontSize = 16.sp, fontWeight = FontWeight.W400
        )
    }
}

@Composable
fun ExpandableSection(
    title: String,
    expandedP: Boolean = false,
    content: @Composable () -> Unit,

    ) {
    var expanded by remember { mutableStateOf(expandedP) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                content()
            }
        }
    }
}


@Composable
fun RealtyPictureUI(realtyPicture: RealtyPicture) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.size(150.dp)
    ) {
        Image(
            bitmap = Utils().uriToBitmapLegacy(context, Uri.parse(realtyPicture.uriString))
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
    modifier: Modifier = Modifier, realty: Realty
) {
    val location = realty.primaryInfo.realtyPlace.positionLatLng

    AndroidView(modifier = modifier, factory = { ctx ->
        val options = GoogleMapOptions().liteMode(true)
        MapView(ctx, options).apply {
            onCreate(null)
            getMapAsync { googleMap ->
                val latitude = location.latitude
                val longitude = location.longitude
                googleMap.addMarker(
                    MarkerOptions().position(LatLng(latitude, longitude))
                )
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            latitude, longitude
                        ), 15f
                    )
                )
            }
        }
    }, update = { mapView ->
        mapView.onResume()
    })
}
