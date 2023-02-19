package app.delivery.domain.usecases

import app.delivery.api.RestaurantApi
import kotlinx.coroutines.flow.Flow

class UpdateAvailableMenuUseCase(
    private val restaurantApi: RestaurantApi
) {

    fun updateAvailableFoods(
        sectionId: String,
        foodId: String,
        availability: Boolean
    ): Flow<Result<Unit>> {
        return restaurantApi.updateFoodAvailability(sectionId, foodId, availability)
    }
}