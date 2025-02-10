package com.openclassrooms.realestatemanager.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openclassrooms.realestatemanager.RealtyFormScreen


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
                    onNext = {  }
                ),
                navigationScreen = NavigationScreen.RealtyForm,
                activity = activity
            )
        }
    }
}