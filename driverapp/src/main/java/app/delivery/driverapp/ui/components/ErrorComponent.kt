package app.delivery.driverapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import app.delivery.driverapp.R

@Composable
fun ErrorComponent(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
        Card(modifier = Modifier.padding(dimensionResource(id = R.dimen.default_padding))) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

                val (iconRef, messageRef, closeRef) = createRefs()

                Icon(painter = painterResource(id = R.drawable.ic_baseline_error_24),
                    contentDescription = "",
                    modifier = Modifier
                        .constrainAs(iconRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(start = dimensionResource(id = R.dimen.small_padding))
                )

                Text(
                    text = snackbarData.visuals.message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .constrainAs(messageRef) {
                            start.linkTo(iconRef.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(closeRef.start)
                            width = Dimension.fillToConstraints
                        }
                        .padding(start = dimensionResource(id = R.dimen.small_padding)),
                    maxLines = 3,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    onClick = {
                        snackbarData.dismiss()
                    },
                    modifier = Modifier
                        .constrainAs(closeRef) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .padding(start = dimensionResource(id = R.dimen.small_padding)),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_close_24),
                        contentDescription = ""
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun PreviewErrorComponent() {
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        ErrorComponent(snackbarHostState)
    }


    LaunchedEffect(key1 = Unit) {
        snackbarHostState.showSnackbar(
            message = "Unable to fetch data from internet, Please check your connection",
            duration = SnackbarDuration.Indefinite
        )
    }
}