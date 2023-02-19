package app.delivery.ownerapp.ui.screens.login

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.delivery.ownerapp.ui.Routes
import app.delivery.ownerapp.ui.components.ErrorComponent
import app.delivery.ownerapp.ui.components.LoaderDialog
import app.delivery.ownerapp.ui.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController, viewModel: LoginViewModel = hiltViewModel()
) {
    val loginNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.viewState.collectAsState()
    val activity = LocalContext.current as? Activity?

    Scaffold {
        Surface {
            NavHost(
                modifier = Modifier.padding(it),
                navController = loginNavController,
                startDestination = "phone"
            ) {
                composable("phone") {
                    LoginFormComponent(state = state.value, onNameEntered = { name ->
                        viewModel.onNameEntered(name)
                    }, onPhoneNumberEntered = { phone ->
                        viewModel.onPhoneNumberEntered(phone)
                    }, onContinue = {
                        activity?.let {
                            viewModel.requestOTP(activity)
                        }
                    })
                }
                composable("otp") {
                    LoginOTPComponent(state = state.value, onOTPEntered = { otp ->
                        viewModel.onOTPEntered(otp)
                    }, onContinue = {
                        activity?.let {
                            viewModel.verifyOTP(activity)
                        }
                    })
                }
            }
        }
    }

    LaunchedEffect(key1 = state.value.sendOTPSuccess) {
        if (state.value.sendOTPSuccess) {
            loginNavController.navigate("otp")
        }
    }

    LaunchedEffect(key1 = state.value.credential) {
        val credential = state.value.credential
        if (credential != null) {
            activity?.let {
                viewModel.signInWithPhoneAuthCredential(activity, credential)
            }
        }
    }

    if (state.value.sendingOTP) {
        LoaderDialog("Sending OTP")
    }
    if (state.value.verifyingOTP) {
        LoaderDialog(text = "Verifying OTP")
    }

    ErrorComponent(snackbarHostState = snackbarHostState)

    LaunchedEffect(key1 = state.value.actionError) {
        state.value.actionError?.consume()?.let { throwable ->
            snackbarHostState.showSnackbar(message = throwable.message ?: "Error")
        }
    }

    LaunchedEffect(key1 = state.value.userCreated) {
        if (state.value.userCreated) {
            navController.navigate(Routes.Home.route) {
                popUpTo(0)
            }
        }
    }
}