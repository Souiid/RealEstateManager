package com.openclassrooms.realestatemanager.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openclassrooms.realestatemanager.ui.screens.form.realtyform.RealtyFormScreen
import com.openclassrooms.realestatemanager.ui.screens.form.selectagent.SelectAgentScreen
import com.openclassrooms.realestatemanager.ui.screens.form.setrealtypicture.SetRealtyPictureScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun FormNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    activity: Activity,
    isEditing: Boolean = false
) {
    NavHost(navController, NavigationScreen.RealtyForm.route, modifier) {

        composable(NavigationScreen.RealtyForm.route) {
            RealtyFormScreen(koinViewModel(),
                onBack = { activity.finish() },
                onNext = {
                    navController.navigate(NavigationScreen.SetRealtyPicture.route)
                })
        }

        composable(NavigationScreen.SetRealtyPicture.route) {
            SetRealtyPictureScreen(
                koinViewModel(),
                onNext = {
                    if (!isEditing) {
                        navController.navigate(NavigationScreen.SelectAgentScreen.route)
                    }else {
                        activity.finish()
                    }
                },
                onBack = {
                    navController.popBackStack()
                })
        }

        composable(NavigationScreen.SelectAgentScreen.route) {
            SelectAgentScreen(
                koinViewModel(),
                onBack = {
                    navController.popBackStack()
                },
                onFinish = { activity.finish() })
        }
    }
}