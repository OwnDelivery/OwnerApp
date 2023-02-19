package app.delivery.driverapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.delivery.driverapp.ui.screens.home.HomeScreen
import app.delivery.driverapp.ui.screens.login.LoginScreen
import app.delivery.driverapp.ui.screens.splash.SplashScreen

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
    }
}