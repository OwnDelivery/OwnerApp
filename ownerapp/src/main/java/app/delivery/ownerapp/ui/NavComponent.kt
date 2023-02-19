package app.delivery.ownerapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.delivery.ownerapp.ui.screens.admin.PastOrdersScreen
import app.delivery.ownerapp.ui.screens.home.HomeScreen
import app.delivery.ownerapp.ui.screens.login.LoginScreen
import app.delivery.ownerapp.ui.screens.splash.SplashScreen

@Composable
fun NavComponent() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(
                navController = navController
            )
        }
        composable(Routes.Login.route) {
            LoginScreen(
                navController = navController
            )
        }
        composable(Routes.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Routes.PastOrders.route) {
            PastOrdersScreen(navController = navController)
        }
    }
}