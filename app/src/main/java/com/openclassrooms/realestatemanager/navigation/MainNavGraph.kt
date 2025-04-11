package com.openclassrooms.realestatemanager.navigation

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.openclassrooms.realestatemanager.ui.screens.MainActivity
import com.openclassrooms.realestatemanager.ui.screens.RealtiesScreen
import com.openclassrooms.realestatemanager.ui.screens.RealtyDescriptionScreen
import com.openclassrooms.realestatemanager.ui.screens.form.FormActivity
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    activity: MainActivity
) {

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Gray,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            ),
            title = { Text(text = "Real Estate") },
            actions = {

                if (currentDestination == NavigationScreen.Realties.route) {
                    IconButton(onClick = {
                        val intent = Intent(activity, FormActivity::class.java)
                        activity.startActivity(intent)
                    }) {

                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                } else {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Create,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            },
            navigationIcon = {
                if (currentDestination == NavigationScreen.Realties.route) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                } else {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        )
    }) { innerPadding ->
        NavHost(navController, NavigationScreen.Realties.route, modifier.padding(innerPadding)) {

            composable(NavigationScreen.Realties.route) {
                RealtiesScreen(
                    koinViewModel(),
                    onNext = { navController.navigate(NavigationScreen.RealtyDescription.route) },
                    activity = activity
                )
            }

            composable(NavigationScreen.RealtyDescription.route) {
                RealtyDescriptionScreen(
                    koinViewModel(),
                    onBack = { navController.popBackStack() }
                )
            }


        }

    }

}
