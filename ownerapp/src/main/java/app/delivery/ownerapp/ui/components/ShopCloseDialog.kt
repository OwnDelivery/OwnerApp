package app.delivery.ownerapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import app.delivery.ownerapp.R
import app.delivery.ownerapp.ui.theme.OwnerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopCloseDialog(timing: String, onDismiss: () -> Unit, onConfirm: (timing: String) -> Unit) {

    val timingText = remember {
        mutableStateOf(timing)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.default_padding))) {
                Text(
                    text = "Confirm Closing the Shop?",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                TextField(value = timingText.value, onValueChange = { timingText.value = it })
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                    Button(
                        onClick = { onConfirm(timingText.value) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewShopClosingDialog() {
    OwnerAppTheme {
        ShopCloseDialog(timing = "12.30 AM to 09 PM", onDismiss = { }, onConfirm = {})
    }
}