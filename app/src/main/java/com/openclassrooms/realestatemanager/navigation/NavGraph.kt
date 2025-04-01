package com.openclassrooms.realestatemanager.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openclassrooms.realestatemanager.ui.RealtyFormScreen
import com.openclassrooms.realestatemanager.ui.SelectAgentScreen
import com.openclassrooms.realestatemanager.ui.SetRealtyPictureScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(navController, NavigationScreen.RealtyForm.route, modifier) {

        composable(NavigationScreen.RealtyForm.route) {
            RealtyFormScreen(koinViewModel(), onNext = {
                navController.navigate(NavigationScreen.SetRealtyPicture.route)
            })
        }

        composable(NavigationScreen.SetRealtyPicture.route) {
            SetRealtyPictureScreen(
                koinViewModel(),
                onNext = {navController.navigate(NavigationScreen.SelectAgentScreen.route)},
                onBack = {
                navController.popBackStack()
            })
        }

        composable(NavigationScreen.SelectAgentScreen.route) {
            SelectAgentScreen(
                onBack = {
                    navController.popBackStack()
                })
        }
    }
}