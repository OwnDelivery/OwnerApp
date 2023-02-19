package app.delivery.contract

data class RestaurantMenu(
    val sections: Map<String, RestaurantMenuSection>
)

data class RestaurantMenuSection(
    val name: String = "",
    val items: Map<String, RestaurantMenuItem> = emptyMap(),
    val rank: Int = 0
)

data class RestaurantMenuItem(
    val name: String = "",
    val price: Double = 0.0,
    val foodType: FoodType = FoodType.VEG,
    val available: Boolean = false,
    val rank: Int = 0
)