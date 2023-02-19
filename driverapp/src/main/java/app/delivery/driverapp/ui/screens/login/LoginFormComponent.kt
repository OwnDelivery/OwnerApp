package app.delivery.driverapp.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import app.delivery.driverapp.R
import app.delivery.driverapp.ui.theme.DriverAppTheme
import app.delivery.driverapp.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFormComponent(
    state: LoginViewModel.LoginViewState,
    onNameEntered: (name: String) -> Unit,
    onPhoneNumberEntered: (phoneNumber: String) -> Unit,
    onContinue: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.default_padding))
            .fillMaxSize(),
    ) {

        val (logoRef, introTextRef, nameRef, phoneRef, btnRef) = createRefs()

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
            text = "Mayas Delivery App",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .constrainAs(introTextRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(logoRef.bottom)
                }
                .padding(top = dimensionResource(id = R.dimen.default_padding))
        )

        TextField(
            label = { Text(text = "Name") },
            value = state.name,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            maxLines = 1,
            onValueChange = {
                if (it.length < 20) {
                    onNameEntered(it)
                }
            },
            modifier = Modifier
                .constrainAs(nameRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(introTextRef.bottom)
                }
                .padding(top = dimensionResource(id = R.dimen.default_padding))
        )

        TextField(
            label = { Text(text = "Phone Number") },
            value = state.phoneNumber,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(onGo = {
                if (state.isFormValid()) {
                    onContinue()
                }
            }),
            maxLines = 1,
            onValueChange = {
                if (it.length <= 10) {
                    onPhoneNumberEntered(it)
                }
            },
            modifier = Modifier
                .constrainAs(phoneRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(nameRef.bottom)
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
                    top.linkTo(phoneRef.bottom)
                }
                .padding(top = dimensionResource(id = R.dimen.default_padding))
        ) {
            Text(text = "Continue")
        }
    }
}

@Preview
@Composable
private fun PreviewLoginComponent() {
    DriverAppTheme {
        LoginFormComponent(LoginViewModel.LoginViewState(), {}, {}, {})
    }
}