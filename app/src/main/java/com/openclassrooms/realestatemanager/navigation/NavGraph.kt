package com.openclassrooms.realestatemanager.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openclassrooms.realestatemanager.ui.RealtyFormScreen
import com.openclassrooms.realestatemanager.ui.SetRealtyPictureScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier,
    activity: Activity
) {
    NavHost(navController, NavigationScreen.RealtyForm.route, modifier) {

        composable(NavigationScreen.RealtyForm.route) {
            RealtyFormScreen().Screen(
                params = RealtyFormScreen.Params(
                    onNext = { navController.navigate(NavigationScreen.SetRealtyPicture.route) }
                ),
                navigationScreen = NavigationScreen.RealtyForm,
                activity = activity
            )
        }

        composable(NavigationScreen.SetRealtyPicture.route) {
            SetRealtyPictureScreen().Screen(
                params = SetRealtyPictureScreen.Params(
                    onBack = { navController.popBackStack() },
                    onNext = { }
                ),
                navigationScreen = NavigationScreen.SetRealtyPicture,
                activity = activity
            )
        }
    }
}