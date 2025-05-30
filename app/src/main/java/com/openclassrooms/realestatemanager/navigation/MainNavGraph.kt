package com.openclassrooms.realestatemanager.navigation

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.ui.screens.main.HomeTabletScreen
import com.openclassrooms.realestatemanager.ui.screens.main.MainActivity
import com.openclassrooms.realestatemanager.ui.screens.map.MapScreen
import com.openclassrooms.realestatemanager.ui.screens.main.RealitiesScreen
import com.openclassrooms.realestatemanager.ui.screens.main.RealtyDescriptionScreen
import com.openclassrooms.realestatemanager.ui.screens.form.FormActivity
import com.openclassrooms.realestatemanager.ui.screens.main.MortgageScreen
import com.openclassrooms.realestatemanager.ui.screens.search.SearchScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    activity: MainActivity
) {

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
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

                    IconButton(onClick = { showSheet = true }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = { navController.navigate(NavigationScreen.Map.route) }) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                } else {
                    IconButton(onClick = {
                        val intent = Intent(activity, FormActivity::class.java)
                        intent.putExtra("isEditing", true)
                        activity.startActivity(intent)
                    }) {
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
        var criterias by remember { mutableStateOf<SearchCriteria?>(null) }
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                SearchScreen(
                    onNewSearchCriteria = { newCriteria ->
                        criterias = newCriteria
                    }
                )
            }
        }

        NavHost(navController, NavigationScreen.HomeTablet.route, modifier.padding(innerPadding)) {
            composable(NavigationScreen.HomeTablet.route,

                ) {
                HomeTabletScreen(
                    koinViewModel(),
                   koinViewModel(),
                    onSimulateClick = { price ->
                        navController.navigate(
                            NavigationScreen.Mortgage.createRoute(price)
                        )
                    }
                )
            }

            composable(NavigationScreen.Realties.route,

            ) {
                RealitiesScreen(
                    koinViewModel(),
                    onNext = { realtyID ->
                        navController.navigate(NavigationScreen.RealtyDescription.createRoute(realtyID))
                    },
                    criteria = criterias,
                    activity = activity
                )
            }

            composable(
                route = NavigationScreen.RealtyDescription.routeWithArgs,
                arguments = listOf(navArgument("realtyID") { type = NavType.IntType })
            ) { backStackEntry ->
                val realtyID = backStackEntry.arguments?.getInt("realtyID") ?: -1
                RealtyDescriptionScreen(
                    koinViewModel(),
                    realtyID = realtyID,
                    onSimulateClick = { price ->
                        navController.navigate(
                            NavigationScreen.Mortgage.createRoute(price)
                        )
                    }
                )
            }

            composable(
                route = NavigationScreen.Mortgage.routeWithArgs,
                arguments = listOf(
                    navArgument(NavigationScreen.Mortgage.ARG_PRICE) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val priceString = backStackEntry.arguments?.getString(NavigationScreen.Mortgage.ARG_PRICE)
                val price = priceString?.toDoubleOrNull() ?: 0.0
                MortgageScreen(price = price)
            }

            composable(NavigationScreen.Map.route) {
                MapScreen(koinViewModel(),
                    onMarkerClick = {
                        navController.navigate(NavigationScreen.RealtyDescription.route)
                    }
                )
            }
        }

    }

}
