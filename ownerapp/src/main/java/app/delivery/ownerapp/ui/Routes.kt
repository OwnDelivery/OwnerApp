package app.delivery.ownerapp.ui

sealed class Routes(val route: String) {
    object Splash : Routes("Splash")
    object Login : Routes("Login")
    object Home : Routes("Home")
    object PastOrders : Routes("Past Orders")
}