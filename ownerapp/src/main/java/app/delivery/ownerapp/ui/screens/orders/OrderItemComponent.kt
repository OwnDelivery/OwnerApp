package app.delivery.ownerapp.ui.screens.orders

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
import app.delivery.domain.models.Order
import app.delivery.ownerapp.R

@Composable
fun OrderItemComponent(
    order: Order,
    onOrderConfirmed: () -> Unit,
    onPaymentConfirmed: () -> Unit,
    onOrderReady: () -> Unit,
    onCancelled: () -> Unit,
    onDelivered: () -> Unit,
    onNotDelivered: () -> Unit,
    onActivate: () -> Unit
) {
    val context = LocalContext.current
    val confirmPaymentDialog = remember { mutableStateOf(false) }
    val openFoodReadyDialog = remember { mutableStateOf(false) }
    val dropDownExpanded = remember { mutableStateOf(false) }

    if (confirmPaymentDialog.value) {
        AlertDialog(
            onDismissRequest = { openFoodReadyDialog.value = false },
            text = { Text(text = "Are you sure, Confirm payment of Rupees ${order.getTotalAmount()} for order id ${order.getReadableRefId()}?") },
            confirmButton = {
                Button(onClick = {
                    onPaymentConfirmed()
                    confirmPaymentDialog.value = false
                }) {
                    Text(text = "Confirm Payment")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { confirmPaymentDialog.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    if (openFoodReadyDialog.value) {
        AlertDialog(
            onDismissRequest = { openFoodReadyDialog.value = false },
            text = { Text(text = "Are you sure, Confirm Food Ready?") },
            confirmButton = {
                Button(onClick = {
                    onOrderReady()
                    openFoodReadyDialog.value = false
                }) {
                    Text(text = "Food Ready")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { openFoodReadyDialog.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
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
                            app.delivery.applib.Utils.makePhoneCall(context, order.contactNumber)
                            dropDownExpanded.value = false
                        }, text = {
                            Text(text = "Contact Customer")
                        })
                        DropdownMenuItem(onClick = {
                            app.delivery.applib.Utils.showLocation(
                                context,
                                order.address.lat,
                                order.address.lon
                            )
                            dropDownExpanded.value = false
                        }, text = {
                            Text(text = "Order Location")
                        })
                        if (order.cancelled) {
                            DropdownMenuItem(onClick = {
                                onActivate()
                                dropDownExpanded.value = false
                            }, text = {
                                Text(text = "Activate Order")
                            })
                        } else {
                            if (order.foodReady && order.delivered) {
                                DropdownMenuItem(onClick = {
                                    onNotDelivered()
                                    dropDownExpanded.value = false
                                }, text = {
                                    Text(text = "Mark as Not Delivered")
                                })
                            } else if (order.foodReady) {
                                DropdownMenuItem(onClick = {
                                    onDelivered()
                                    dropDownExpanded.value = false
                                }, text = {
                                    Text(text = "Mark as Delivered")
                                })
                            }
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
            Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.small_padding)))
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
                    Text(text = "Order Cancelled", style = MaterialTheme.typography.bodyMedium)
                }
                order.delivered -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Delivered",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                order.pickedUp -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Picked up",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedButton(modifier = Modifier.weight(2f), onClick = {
                            app.delivery.applib.Utils.makePhoneCall(
                                context,
                                order.deliveryPartner?.phone ?: ""
                            )
                        }) {
                            Text(text = "Call Driver")
                        }
                    }
                }
                order.foodReady -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Ready",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedButton(modifier = Modifier.weight(2f), onClick = {
                            app.delivery.applib.Utils.makePhoneCall(
                                context,
                                order.deliveryPartner?.phone ?: ""
                            )
                        }) {
                            Text(text = "Call Driver")
                        }
                    }
                }
                order.paymentDone || order.confirmed -> {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        openFoodReadyDialog.value = true
                    }) {
                        Text(text = "Food Ready")
                    }
                }
                /*order.confirmed -> {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            Utils.makePhoneCall(context, order.contactNumber)
                        }) {
                        Text(text = "Contact customer")
                    }
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        confirmPaymentDialog.value = true
                    }) {
                        Text(text = "Confirm Payment")
                    }
                }*/
                else -> {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        onOrderConfirmed()
                    }) {
                        Text(text = "Confirm Order")
                    }
                }
            }
        }
    }
}