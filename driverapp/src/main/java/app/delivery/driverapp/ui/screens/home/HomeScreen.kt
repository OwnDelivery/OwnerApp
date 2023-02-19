package app.delivery.driverapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.delivery.driverapp.services.OrderAlertService
import app.delivery.driverapp.ui.Routes
import app.delivery.driverapp.ui.components.ErrorComponent
import app.delivery.driverapp.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(
    navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        context.startService(OrderAlertService.createIntent(context))
        //Disabled for now. context.startService(LocationService.createIntent(context))
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchOrders()
    }

    LaunchedEffect(key1 = viewState.value.userLoggedOut) {
        if (viewState.value.userLoggedOut) {
            navController.navigate(Routes.Login.route) {
                popUpTo(0)
            }
        }
    }

    HomeScreenComponent(viewState.value,
        onLogoutClicked = { viewModel.logout() },
        onConfirmed = { id -> viewModel.changeOrderStatusToAcknowledged(id) },
        onPickedUp = { id -> viewModel.changeOrderStatusToPickedUp(id) },
        onDelivered = { id -> viewModel.changeOrderStatusToDelivered(id) },
        onCancel = { id ->
            viewModel.cancelOrder(id)
        },
        onActivate = { id ->
            viewModel.activateOrder(id)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenComponent(
    viewState: HomeScreenViewModel.ViewState,
    onLogoutClicked: () -> Unit,
    onConfirmed: (id: String) -> Unit,
    onPickedUp: (id: String) -> Unit,
    onDelivered: (id: String) -> Unit,
    onCancel: (String) -> Unit,
    onActivate: (String) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            var showMenu by remember { mutableStateOf(false) }
            TopAppBar(title = {
                Text(text = "Mayas Delivery App")
            }, actions = {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Default.MoreVert, "")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(text = { Text(text = "Logout") },
                        onClick = { onLogoutClicked() })
                }
            })
        },
    ) { paddingValues ->
        Surface {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (viewState.fetching || viewState.orders.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Waiting for Orders",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(viewState.orders.toList()
                            .sortedByDescending { it.second.createdAt }
                            .sortedBy { it.second.delivered }) { item ->
                            OrderItemComponent(order = item.second,
                                onAcknowledgeOrder = { onConfirmed(item.first) },
                                onPickUpOrder = { onPickedUp(item.first) },
                                onDeliverReady = { onDelivered(item.first) },
                                onCancelled = { onCancel(item.first) },
                                onActivate = { onActivate(item.first) }
                            )
                        }
                    }
                }
            }
        }
    }

    ErrorComponent(snackbarHostState = snackbarHostState)

    LaunchedEffect(key1 = viewState.actionError) {
        viewState.actionError?.consume()?.let { throwable ->
            snackbarHostState.showSnackbar(throwable.message ?: "Error")
        }
    }
}
