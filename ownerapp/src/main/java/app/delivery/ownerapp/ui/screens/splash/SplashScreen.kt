package app.delivery.ownerapp.ui.screens.splash

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.delivery.ownerapp.ui.Routes
import app.delivery.ownerapp.ui.viewmodels.SplashScreenViewModel

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
        if (state.value.isLoggedIn != null) {
            if (state.value.isLoggedIn == true) {
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

    LaunchedEffect(key1 = state.value.actionError) {
        state.value.actionError?.consume()?.let { throwable ->
            snackbarHostState.showSnackbar(message = throwable.message ?: "Error")
        }
    }
}