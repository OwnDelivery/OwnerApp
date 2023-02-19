package app.delivery.ownerapp.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import app.delivery.ownerapp.ui.components.ErrorComponent
import app.delivery.ownerapp.ui.viewmodels.OrderScreenViewModel

@Composable
fun OrderScreen(viewModel: OrderScreenViewModel = hiltViewModel()) {
    val viewState = viewModel.viewState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchOrders()
    }

    OrderScreenComponent(viewState = viewState.value,
        onConfirmOrder = {
            viewModel.changeOrderStatusToConfirmed(it)
        },
        onConfirmPayment = {
            viewModel.changeOrderStatusToPaid(it)
        },
        onFoodReady = {
            viewModel.changeOrderStatusToPickedUp(it)
        },
        onCancel = {
            viewModel.cancelOrder(it)
        },
        onActivate = {
            viewModel.activateOrder(it)
        },
        onDelivered = {
            viewModel.deliverOrder(it)
        },
        onNotDelivered = {
            viewModel.notDeliverOrder(it)
        }
    )
}

@Composable
private fun OrderScreenComponent(
    viewState: OrderScreenViewModel.ViewState,
    onConfirmOrder: (String) -> Unit,
    onConfirmPayment: (String) -> Unit,
    onFoodReady: (String) -> Unit,
    onDelivered: (String) -> Unit,
    onNotDelivered: (String) -> Unit,
    onCancel: (String) -> Unit,
    onActivate: (String) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        if (viewState.fetching) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Waiting for Orders", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    viewState.orders.toList().sortedByDescending { it.second.createdAt }
                        .sortedBy { it.second.foodReady }) { item ->
                    OrderItemComponent(
                        order = item.second,
                        onOrderConfirmed = { onConfirmOrder(item.first) },
                        onPaymentConfirmed = { onConfirmPayment(item.first) },
                        onOrderReady = { onFoodReady(item.first) },
                        onDelivered = { onDelivered(item.first) },
                        onCancelled = { onCancel(item.first) },
                        onActivate = { onActivate(item.first) },
                        onNotDelivered = { onNotDelivered(item.first) },
                    )
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

@Preview
@Composable
private fun PreviewOrderScreenComponent() {
    OrderScreenComponent(OrderScreenViewModel.ViewState(), {}, {}, {}, {}, {}, {}, {})
}