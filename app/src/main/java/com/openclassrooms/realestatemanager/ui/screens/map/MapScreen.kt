package com.openclassrooms.realestatemanager.ui.screens.map

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState


@Composable
fun MapScreen(viewModel: MapViewModel) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            getLastKnownLocation(context, fusedLocationClient) { latLng ->
                userLocation = latLng
                latLng?.let {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                }
            }
        }
    }

    val realities = viewModel.getSortedRealities()



    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastKnownLocation(context, fusedLocationClient) { latLng ->
                userLocation = latLng
                latLng?.let {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                }
            }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = userLocation != null
        )
    ) {
        realities?.forEach {
            val latitude = it.primaryInfo.realtyPlace.positionLatLng.latitude
            val longitude = it.primaryInfo.realtyPlace.positionLatLng.longitude
            Marker(
                state = rememberMarkerState(position = LatLng(latitude, longitude)),
            )
        }
    }
}

private fun getLastKnownLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationFound: (LatLng?) -> Unit
) {
    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationFound(LatLng(location.latitude, location.longitude))
            } else {
                onLocationFound(null)
            }
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
        onLocationFound(null)
    }
}

