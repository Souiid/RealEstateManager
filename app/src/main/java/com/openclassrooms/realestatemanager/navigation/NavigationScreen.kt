package com.openclassrooms.realestatemanager.navigation

sealed class NavigationScreen(val route: String) {

    data object RealtyForm: NavigationScreen("realty_form_screen")
    data object SetRealtyPicture: NavigationScreen("set_realty_picture_screen")
    data object SelectAgentScreen: NavigationScreen("select_agent_screen")

}