package app.delivery.ownerapp.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.delivery.ownerapp.R
import app.delivery.ownerapp.ui.components.ErrorComponent
import app.delivery.ownerapp.ui.screens.orders.PastOrderItemComponent
import app.delivery.ownerapp.ui.viewmodels.PastOrderViewModel

@Composable
fun PastOrdersScreen(
    navController: NavController, viewModel: PastOrderViewModel = hiltViewModel()
) {

    val viewState = viewModel.viewState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchOrders()
    }

    PastOrderScreen(viewState = viewState.value, onBackPressed = {
        navController.popBackStack()
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PastOrderScreen(
    viewState: PastOrderViewModel.ViewState,
    onBackPressed: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Past Orders")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "")
                    }
                })
        },
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            if (viewState.fetching || viewState.twoDayOrders.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Waiting for Past Orders",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Column {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(
                            viewState.twoDayOrders.toList()
                                .sortedByDescending { it.createdAt }) { item ->
                            PastOrderItemComponent(order = item)
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_padding)))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding))) {
                            Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                Text(
                                    text = "Yesterday Sale:",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Rs.${viewState.getYesterdaySale()}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                Text(
                                    text = "Today Sale:",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Rs.${viewState.getTodaySale()}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }

            }

        }
    }

    ErrorComponent(snackbarHostState = snackbarHostState)

    LaunchedEffect(key1 = viewState.actionError) {
        viewState.actionError?.consume()?.let { throwable ->
            snackbarHostState.showSnackbar(message = throwable.message ?: "Error")
        }

    }
}