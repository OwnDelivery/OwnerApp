package app.delivery.api.dtos

import app.delivery.contract.FoodType
import com.google.firebase.database.PropertyName

data class FoodMenuDto(
    @get:PropertyName("sections") @set:PropertyName("sections") var sections: Map<String, FoodMenuSectionDto> = emptyMap()
)

data class FoodMenuSectionDto(
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("rank") @set:PropertyName("rank") var rank: Int = 0,
    @get:PropertyName("items") @set:PropertyName("items") var items: Map<String, FoodMenuItemDto> = emptyMap()
)

data class FoodMenuItemDto(
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("food_type") @set:PropertyName("food_type") var foodType: FoodType = FoodType.VEG,
    @get:PropertyName("price") @set:PropertyName("price") var price: Double = 0.0,
    @get:PropertyName("available") @set:PropertyName("available") var available: Boolean = false,
    @get:PropertyName("rank") @set:PropertyName("rank") var rank: Int = 0
)