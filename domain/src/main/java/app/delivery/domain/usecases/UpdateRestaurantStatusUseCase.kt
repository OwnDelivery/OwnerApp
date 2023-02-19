package app.delivery.domain.usecases

import app.delivery.api.RestaurantApi
import kotlinx.coroutines.flow.Flow

class UpdateRestaurantStatusUseCase(private val restaurantApi: RestaurantApi) {

    fun updateStatus(isOpen: Boolean, timing: String): Flow<Result<Unit>> {
        return restaurantApi.updateRestaurant(isOpen = isOpen, message = timing)
    }
}