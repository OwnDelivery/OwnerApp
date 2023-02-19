package app.delivery.domain.models

import app.delivery.contract.FoodType
import java.util.*

data class Order(
    val address: Address,
    val foods: List<FoodItem>,
    val orderedBy: String,
    val contactNumber: String,
    val contactUid: String,
    val refId: String,
    val confirmed: Boolean,
    val cancelled: Boolean,
    val paymentDone: Boolean,
    val foodReady: Boolean,
    val deliveryPartner: DeliveryPartner?,
    val pickedUp: Boolean,
    val delivered: Boolean,
    val createdAt: Long
) {
    fun getTimeCreated(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = createdAt
        return "(${calendar.get(Calendar.DATE)}/${calendar.get(Calendar.MONTH) + 1}) ${
            calendar.get(Calendar.HOUR).toString().let {
                if (it.length == 1) {
                    "0$it"
                } else {
                    it
                }
            }
        }:${
            calendar.get(Calendar.MINUTE).toString().let {
                if (it.length == 1) {
                    "0$it"
                } else {
                    it
                }
            }
        } ${
            when (calendar.get(
                Calendar.AM_PM
            )) {
                0 -> "AM"
                1 -> "PM"
                else -> "AM"
            }
        }"
    }

    fun getTotalAmount(): Double {
        var amount = 0.0
        foods.forEach { foodItem ->
            amount += foodItem.price * foodItem.qty
        }
        amount += address.getDeliveryCharge()
        return amount
    }

    fun getReadableRefId(): String {
        return if (refId.length > 4) {
            refId.substring(0, 4)
        } else {
            refId
        }
    }
}

data class FoodItem(
    val foodType: FoodType,
    val name: String,
    val price: Double,
    val qty: Int
)

data class DeliveryPartner(
    val phone: String,
    val name: String
)

