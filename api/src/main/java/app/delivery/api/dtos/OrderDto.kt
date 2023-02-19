package app.delivery.api.dtos

import app.delivery.contract.FoodType
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
data class OrderDto(
    @get:PropertyName("address") @set:PropertyName("address") var address: AddressDto = AddressDto(),
    @get:PropertyName("foods") @set:PropertyName("foods") var foods: List<FoodItemDto> = emptyList(),
    @get:PropertyName("ordered_by") @set:PropertyName("ordered_by") var orderedBy: String = "",
    @get:PropertyName("contact_number") @set:PropertyName("contact_number") var contactNumber: String = "",
    @get:PropertyName("contact_uid") @set:PropertyName("contact_uid") var contactUid: String = "",
    @get:PropertyName("ref_id") @set:PropertyName("ref_id") var refId: String = "",
    @get:PropertyName("confirmed") @set:PropertyName("confirmed") var confirmed: Boolean = false,
    @get:PropertyName("cancelled") @set:PropertyName("cancelled") var cancelled: Boolean = false,
    @get:PropertyName("payment_done") @set:PropertyName("payment_done") var paymentDone: Boolean = false,
    @get:PropertyName("food_ready") @set:PropertyName("food_ready") var foodReady: Boolean = false,
    @get:PropertyName("delivery_partner") @set:PropertyName("delivery_partner") var deliveryPartner: DeliveryPartnerDto? = null,
    @get:PropertyName("picked_up") @set:PropertyName("picked_up") var pickedUp: Boolean = false,
    @get:PropertyName("delivered") @set:PropertyName("delivered") var delivered: Boolean = false,
    @get:PropertyName("created_at") @set:PropertyName("created_at") var createdAt: Long = 0L,
) {
    @Exclude
    fun toUpdateFieldsMap(): Map<String, Any?> {
        return mapOf(
            "confirmed" to confirmed,
            "cancelled" to cancelled,
            "payment_done" to paymentDone,
            "food_ready" to foodReady,
            "delivery_partner" to deliveryPartner,
            "picked_up" to pickedUp,
            "delivered" to delivered
        )
    }
}

data class AddressDto(
    @get:PropertyName("lat") @set:PropertyName("lat") var lat: Double = 0.0,
    @get:PropertyName("lon") @set:PropertyName("lon") var lon: Double = 0.0,
    @get:PropertyName("locality") @set:PropertyName("locality") var locality: String = "",
    @get:PropertyName("full_address") @set:PropertyName("full_address") var fullAddress: String = ""
)

data class FoodItemDto(
    @get:PropertyName("food_type") @set:PropertyName("food_type") var foodType: FoodType = FoodType.VEG,
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("price") @set:PropertyName("price") var price: Double = 0.0,
    @get:PropertyName("qty") @set:PropertyName("qty") var qty: Int = 0
)

data class DeliveryPartnerDto(
    @get:PropertyName("phone") @set:PropertyName("phone") var phone: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
)

