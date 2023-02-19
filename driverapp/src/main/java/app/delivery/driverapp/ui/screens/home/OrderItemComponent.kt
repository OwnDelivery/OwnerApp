package app.delivery.driverapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import app.delivery.applib.Utils
import app.delivery.domain.models.Order
import app.delivery.driverapp.R

@Composable
fun OrderItemComponent(
    order: Order,
    onAcknowledgeOrder: () -> Unit,
    onPickUpOrder: () -> Unit,
    onDeliverReady: () -> Unit,
    onCancelled: () -> Unit,
    onActivate: () -> Unit
) {

    val context = LocalContext.current
    val pickupOrderDialog = remember { mutableStateOf(false) }
    val deliverOrderDialog = remember { mutableStateOf(false) }
    val dropDownExpanded = remember { mutableStateOf(false) }

    if (pickupOrderDialog.value) {
        AlertDialog(onDismissRequest = { pickupOrderDialog.value = false },
            text = { Text(text = "Are you sure, Pickup the order?") },
            confirmButton = {
                Button(onClick = {
                    onPickUpOrder()
                    pickupOrderDialog.value = false
                }) {
                    Text(text = "Pickup order")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { pickupOrderDialog.value = false }) {
                    Text(text = "Cancel")
                }
            })
    }

    if (deliverOrderDialog.value) {
        AlertDialog(
            onDismissRequest = { deliverOrderDialog.value = false },
            text = { Text(text = "Are you sure, Confirm Delivery?") },
            confirmButton = {
                Button(onClick = {
                    onDeliverReady()
                    deliverOrderDialog.value = false
                }) {
                    Text(text = "Confirm Delivery")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { deliverOrderDialog.value = false }) {
                    Text(text = "Cancel")
                }
            })
    }

    Card(
        modifier = Modifier.padding(
            start = dimensionResource(id = R.dimen.default_padding),
            end = dimensionResource(id = R.dimen.default_padding),
            bottom = dimensionResource(id = R.dimen.default_padding)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.small_padding))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "#${order.getReadableRefId()}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = order.getTimeCreated(), style = MaterialTheme.typography.bodyLarge
                )
                Box {
                    IconButton(onClick = { dropDownExpanded.value = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "")
                    }
                    DropdownMenu(
                        expanded = dropDownExpanded.value,
                        onDismissRequest = { dropDownExpanded.value = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            Utils.makePhoneCall(context, order.contactNumber)
                            dropDownExpanded.value = false
                        }, text = {
                            Text(text = "Contact Customer")
                        })
                        if (order.cancelled) {
                            DropdownMenuItem(onClick = {
                                onActivate()
                                dropDownExpanded.value = false
                            }, text = {
                                Text(text = "Activate Order")
                            })
                        } else {
                            DropdownMenuItem(onClick = {
                                onCancelled()
                                dropDownExpanded.value = false
                            }, text = {
                                Text(text = "Cancel Order")
                            })
                        }
                    }
                }
            }
            Text(text = "By ${order.orderedBy}", style = MaterialTheme.typography.bodyMedium)
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.small_padding),
                        bottom = dimensionResource(id = R.dimen.small_padding)
                    )
            )
            order.foods.map { food ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${food.qty} x ${food.name}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "₹${food.price * food.qty}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.small_padding),
                        bottom = dimensionResource(id = R.dimen.small_padding)
                    )
            )

            Text(text = order.address.fullAddress, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.space)))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Delivery Charge(" + "%.1f".format(order.address.getDeliveryDistance()) + " km)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "₹${order.address.getDeliveryCharge()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.space)))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Total Bill: ₹${order.getTotalAmount()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = order.address.locality ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.small_padding)))

            when {
                order.cancelled -> {
                    Text(text = "Cancelled", style = MaterialTheme.typography.bodyMedium)
                }
                order.delivered -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = "Delivered", style = MaterialTheme.typography.bodyLarge)
                        OutlinedIconButton(onClick = {
                            Utils.showDirection(context, order.address.lat, order.address.lon)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_navigation_24),
                                contentDescription = "Navigation"
                            )
                        }
                    }
                }
                order.pickedUp -> {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        FilledIconButton(onClick = {
                            Utils.showDirection(context, order.address.lat, order.address.lon)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_navigation_24),
                                contentDescription = "Navigation"
                            )
                        }
                        Button(modifier = Modifier.weight(1f), onClick = {
                            deliverOrderDialog.value = true
                        }) {
                            Text(text = "Set Delivered")
                        }
                    }
                }
                order.deliveryPartner != null -> {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            pickupOrderDialog.value = true
                        }) {
                        Text(text = "Set Picked up")
                    }
                }
                order.foodReady -> {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onAcknowledgeOrder()
                        }) {
                        Text(text = "Accept order")
                    }
                }
            }
        }
    }
}