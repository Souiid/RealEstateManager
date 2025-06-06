package com.openclassrooms.realestatemanager.navigation

sealed class NavigationScreen(val route: String) {

    //Main
    data object HomeTablet: NavigationScreen("home_tablet_screen")
    data object Realties: NavigationScreen("realties_screen")
    data object RealtyDescription : NavigationScreen("realty_description_screen") {
        const val ARG_REALTY_ID = "realtyID"
        val routeWithArgs = "$route/{$ARG_REALTY_ID}"
        fun createRoute(realtyID: Int): String = "$route/$realtyID"
    }
    data object Map: NavigationScreen("map_screen")
    data object Mortgage: NavigationScreen("mortgage_screen") {
        const val ARG_PRICE = "price"
        const val ARG_IS_SAVED_IN_DOLLAR = "isSavedInDollar"
        val routeWithArgs = "$route/{$ARG_PRICE}/{$ARG_IS_SAVED_IN_DOLLAR}"
        fun createRoute(price: Int, isSavedInDollar: Boolean): String = "$route/$price/$isSavedInDollar"
    }

    //Form
    data object RealtyForm: NavigationScreen("realty_form_screen")
    data object SetRealtyPicture: NavigationScreen("set_realty_picture_screen")
    data object SelectAgentScreen: NavigationScreen("select_agent_screen")

}