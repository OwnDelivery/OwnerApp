package app.delivery.api.dtos

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
data class RestaurantDto(
    @get:PropertyName("is_open") @set:PropertyName("is_open") var isOpen: Boolean = false,
    @get:PropertyName("timing") @set:PropertyName("timing") var openTiming: String = "",
    @get:PropertyName("menu") @set:PropertyName("menu") var menu: FoodMenuDto = FoodMenuDto()
)