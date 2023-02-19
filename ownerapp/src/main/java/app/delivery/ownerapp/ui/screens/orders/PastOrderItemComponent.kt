package app.delivery.ownerapp.ui.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import app.delivery.domain.models.Order
import app.delivery.ownerapp.R

@Composable
fun PastOrderItemComponent(
    order: Order
) {
    val context = LocalContext.current

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
                    Text(text = "Cancelled", style = MaterialTheme.typography.bodyMedium)
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
                        OutlinedButton(
                            modifier = Modifier.weight(2f),
                            onClick = {
                                app.delivery.applib.Utils.makePhoneCall(
                                    context,
                                    order.contactNumber
                                )
                            }) {
                            Text(text = "Call Customer")
                        }
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
                            Text(text = "Driver")
                        }
                        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.space)))
                        OutlinedButton(modifier = Modifier.weight(2f), onClick = {
                            app.delivery.applib.Utils.makePhoneCall(context, order.contactNumber)
                        }) {
                            Text(text = "Customer")
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
                            Text(text = "Driver")
                        }
                        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.space)))
                        OutlinedButton(modifier = Modifier.weight(2f), onClick = {
                            app.delivery.applib.Utils.makePhoneCall(context, order.contactNumber)
                        }) {
                            Text(text = "Customer")
                        }
                    }
                }
                order.paymentDone || order.confirmed -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Confirmed",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        OutlinedButton(
                            modifier = Modifier.weight(2f),
                            onClick = {
                                app.delivery.applib.Utils.makePhoneCall(
                                    context,
                                    order.contactNumber
                                )
                            }) {
                            Text(text = "Call Customer")
                        }
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "New Order",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        OutlinedButton(
                            modifier = Modifier.weight(2f),
                            onClick = {
                                app.delivery.applib.Utils.makePhoneCall(
                                    context,
                                    order.contactNumber
                                )
                            }) {
                            Text(text = "Call Customer")
                        }
                    }
                }
            }
        }
    }
}