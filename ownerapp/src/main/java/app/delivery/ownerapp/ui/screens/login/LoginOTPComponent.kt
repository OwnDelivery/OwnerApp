package app.delivery.ownerapp.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.constraintlayout.compose.ConstraintLayout
import app.delivery.ownerapp.R
import app.delivery.ownerapp.ui.components.LoaderDialog
import app.delivery.ownerapp.ui.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginOTPComponent(
    state: LoginViewModel.LoginViewState,
    onOTPEntered: (otp: String) -> Unit,
    onContinue: () -> Unit
) {

    ConstraintLayout(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.default_padding))
            .fillMaxSize(),
    ) {

        val (logoRef, descRef, otpRef, btnRef) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier
                .constrainAs(logoRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(top = dimensionResource(id = R.dimen.default_padding))
        )
        Text(
            text = "Enter OTP",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .constrainAs(descRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(logoRef.bottom)
                }
                .padding(top = dimensionResource(id = R.dimen.default_padding))
        )

        TextField(
            label = { Text(text = "OTP") },
            value = state.otp,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                if (it.length < 10) {
                    onOTPEntered(it)
                }
            },
            modifier = Modifier
                .constrainAs(otpRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(descRef.bottom)
                }
                .padding(top = dimensionResource(id = R.dimen.default_padding))
        )


        Button(
            onClick = { onContinue() },
            enabled = state.isFormValid(),
            modifier = Modifier
                .constrainAs(btnRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(otpRef.bottom)
                }
                .padding(top = dimensionResource(id = R.dimen.default_padding))
        ) {
            Text(text = "Verify")
        }

        if (state.verifyingOTP) {
            LoaderDialog("Verifying OTP")
        }
    }
}