package com.openclassrooms.realestatemanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openclassrooms.realestatemanager.ui.screens.MainActivity
import com.openclassrooms.realestatemanager.ui.screens.RealtiesScreen
import com.openclassrooms.realestatemanager.ui.screens.RealtyDescriptionScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    activity: MainActivity
) {
    NavHost(navController, NavigationScreen.Realties.route, modifier) {

        composable(NavigationScreen.Realties.route) {
            RealtiesScreen(
                koinViewModel(),
                onNext = {navController.navigate(NavigationScreen.RealtyDescription.route)},
                activity = activity
            )
        }

        composable(NavigationScreen.RealtyDescription.route) {
            RealtyDescriptionScreen(
                koinViewModel(),
                onBack = {navController.popBackStack()}
            )
        }


    }
}
