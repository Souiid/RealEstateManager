package com.openclassrooms.realestatemanager.ui.screens.form

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.realestatemanager.navigation.FormNavGraph
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme

class FormActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealEstateManagerTheme  {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    FormNavGraph(
                        navController = navController,
                        modifier = Modifier,
                        activity = this
                    )
                }
            }
        }
    }
}


