package com.openclassrooms.realestatemanager.ui.screens.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.navigation.MainNavGraph
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val isInternetAvailable by remember { mutableStateOf(Utils().isInternetAvailable(this)) }
            RealEstateManagerTheme {
                MainNavGraph(
                    navController = navController,
                    modifier = Modifier,
                    activity = this@MainActivity
                )



               if (isInternetAvailable) {
                   Toast.makeText(this, "Internet is available", Toast.LENGTH_SHORT).show()
                   Log.d("aaa", "Internet is available")
               }else {
                   Toast.makeText(this, "OH SHIT !", Toast.LENGTH_SHORT).show()
                   Log.d("aaa", " OH SHIT, Internet is not available")
               }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("aaa", "OnResume")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RealEstateManagerTheme {

    }
}