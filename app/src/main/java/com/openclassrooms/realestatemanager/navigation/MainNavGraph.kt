package com.openclassrooms.realestatemanager.navigation

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.openclassrooms.realestatemanager.ui.composable.ThemeText
import com.openclassrooms.realestatemanager.ui.composable.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.features.screens.form.FormActivity
import com.openclassrooms.realestatemanager.features.screens.main.MainActivity
import com.openclassrooms.realestatemanager.features.screens.main.descrpition.RealtyDescriptionScreen
import com.openclassrooms.realestatemanager.features.screens.main.home.HomeTabletScreen
import com.openclassrooms.realestatemanager.features.screens.main.home.RealitiesScreen
import com.openclassrooms.realestatemanager.features.screens.main.mortgage.MortgageScreen
import com.openclassrooms.realestatemanager.features.screens.map.MapScreen
import com.openclassrooms.realestatemanager.features.screens.search.SearchScreen
import com.openclassrooms.realestatemanager.features.screens.settings.SettingsActivity
import kotlinx.coroutines.launch

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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val startRoute = if (Utils().isTablet(activity)) NavigationScreen.HomeTablet.route else NavigationScreen.Realties.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentDestination != NavigationScreen.Map.route,
        drawerContent = {
            ModalDrawerSheet {
                ListItem(
                    headlineContent = { ThemeText(
                        text = stringResource(R.string.settings),
                        style = ThemeTextStyle.NORMAL
                    ) },
                    leadingContent = {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    },
                    modifier = Modifier.clickable {
                        activity.startActivity(Intent(activity, SettingsActivity::class.java))
                    }
                )
            }
        },

    ){
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
                    when (currentDestination) {
                        NavigationScreen.HomeTablet.route -> {
                            IconButton(onClick = {
                                val intent = Intent(activity, FormActivity::class.java)
                                activity.startActivity(intent)
                            }) {
                                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
                            }

                            IconButton(onClick = { showSheet = true }) {
                                Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
                            }

                            IconButton(onClick = { navController.navigate(NavigationScreen.Map.route) }) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = Color.White)
                            }

                            IconButton(onClick = {
                                val intent = Intent(activity, FormActivity::class.java)
                                intent.putExtra("isEditing", true)
                                activity.startActivity(intent)
                            }) {
                                Icon(Icons.Filled.Create, contentDescription = null, tint = Color.White)
                            }
                        }

                        NavigationScreen.Realties.route -> {
                            IconButton(onClick = {
                                val intent = Intent(activity, FormActivity::class.java)
                                activity.startActivity(intent)
                            }) {
                                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
                            }

                            IconButton(onClick = { showSheet = true }) {
                                Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
                            }

                            IconButton(onClick = { navController.navigate(NavigationScreen.Map.route) }) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = Color.White)
                            }
                        }

                        else -> if (currentDestination?.startsWith(NavigationScreen.RealtyDescription.route) == true) {
                            IconButton(onClick = {
                                val intent = Intent(activity, FormActivity::class.java)
                                intent.putExtra("isEditing", true)
                                activity.startActivity(intent)
                            }) {
                                Icon(Icons.Filled.Create, contentDescription = null, tint = Color.White)
                            }
                        }
                    }
                },
                navigationIcon = {
                    if (currentDestination == NavigationScreen.HomeTablet.route ||
                        currentDestination == NavigationScreen.Realties.route) {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                if (drawerState.isClosed) drawerState.open()
                                else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = null, tint = Color.White)
                        }
                    } else {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
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

            NavHost(navController, startRoute, modifier.padding(innerPadding)) {
                composable(NavigationScreen.HomeTablet.route,

                    ) {
                    HomeTabletScreen(
                        onSimulateClick = { price, isSavedInDollar ->
                            navController.navigate(NavigationScreen.Mortgage.createRoute(price, isSavedInDollar))
                        },
                        criteria = criterias
                    )
                }

                composable(NavigationScreen.Realties.route,

                    ) {
                    RealitiesScreen(
                        onNext = { realtyID ->
                            navController.navigate(NavigationScreen.RealtyDescription.createRoute(realtyID))
                        },
                        criteria = criterias,
                    )
                }

                composable(
                    route = NavigationScreen.RealtyDescription.routeWithArgs,
                    arguments = listOf(navArgument("realtyID") { type = NavType.IntType })
                ) { backStackEntry ->
                    val realtyID = backStackEntry.arguments?.getInt("realtyID") ?: -1
                    RealtyDescriptionScreen(
                        realtyID = realtyID,
                        onSimulateClick = { price, isSavedInDollar ->
                            navController.navigate(
                                NavigationScreen.Mortgage.createRoute(price, isSavedInDollar)
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
                    val price = priceString?.toIntOrNull() ?: 0
                    MortgageScreen(price = price)
                }

                composable(NavigationScreen.Map.route) {
                    MapScreen(
                        criteria = criterias,
                        onMarkerClick = { realtyID ->
                            navController.navigate(NavigationScreen.RealtyDescription.createRoute(realtyID))
                        }
                    )
                }
            }
        }
    }
}
