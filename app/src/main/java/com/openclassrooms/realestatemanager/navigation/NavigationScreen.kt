package com.openclassrooms.realestatemanager.navigation

sealed class NavigationScreen(val route: String) {

    //Main
    data object Realties: NavigationScreen("realties_screen")
    data object RealtyDescription: NavigationScreen("realty_description_screen")
    data object Map: NavigationScreen("map_screen")

    //Form
    data object RealtyForm: NavigationScreen("realty_form_screen")
    data object SetRealtyPicture: NavigationScreen("set_realty_picture_screen")
    data object SelectAgentScreen: NavigationScreen("select_agent_screen")

}