package app.delivery.driverapp.ui.screens.splash

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.delivery.driverapp.ui.Routes
import app.delivery.driverapp.ui.components.ErrorComponent
import app.delivery.driverapp.viewmodels.SplashScreenViewModel

@Composable
fun SplashScreen(
    navController: NavController, viewModel: SplashScreenViewModel = hiltViewModel()
) {
    val state = viewModel.viewState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.checkLogin()
    }

    LaunchedEffect(key1 = state.value) {
        if (state.value.loginSuccess != null) {
            if (state.value.loginSuccess == true) {
                navController.navigate(Routes.Home.route) {
                    popUpTo(0)
                }
            } else {
                navController.navigate(Routes.Login.route) {
                    popUpTo(0)
                }
            }
        }
    }

    ErrorComponent(snackbarHostState = snackbarHostState)

    LaunchedEffect(key1 = state.value.actionError) {
        state.value.actionError?.consume()?.let { throwable ->
            val result = snackbarHostState.showSnackbar(
                message = throwable.message ?: "Error",
                duration = SnackbarDuration.Indefinite,
                actionLabel = "Retry",
                withDismissAction = false
            )
            when (result) {
                SnackbarResult.Dismissed -> {
                    navController.popBackStack()
                }
                SnackbarResult.ActionPerformed -> {
                    viewModel.checkLogin()
                }
            }
        }
    }
}