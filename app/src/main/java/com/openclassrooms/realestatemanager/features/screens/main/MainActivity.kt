package com.openclassrooms.realestatemanager.features.screens.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.navigation.MainNavGraph
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val utils = Utils()
        setContent {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                utils.startInternetMonitoring(applicationContext)
            }

            val isOnline by utils.internetStatus.collectAsState()

            RealEstateManagerTheme {
                MainNavGraph(
                    navController = navController,
                    modifier = Modifier,
                    activity = this@MainActivity
                )

                LaunchedEffect(isOnline) {
                    if (isOnline) {
                        Toast.makeText(this@MainActivity, getString(R.string.internet_available), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, getString(R.string.not_internet), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RealEstateManagerTheme {

    }
}