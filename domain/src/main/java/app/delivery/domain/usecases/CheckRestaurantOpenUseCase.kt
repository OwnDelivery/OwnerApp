package app.delivery.domain.usecases

import app.delivery.api.RestaurantApi
import app.delivery.contract.RestaurantInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CheckRestaurantOpenUseCase(private val restaurantApi: RestaurantApi) {

    fun getOpenStatus(): Flow<Result<RestaurantInfo>> {
        return restaurantApi.getRestaurant().map { restaurantResult ->
            restaurantResult.mapCatching { restaurantDto ->
                RestaurantInfo(
                    isOpen = restaurantDto.isOpen,
                    timing = restaurantDto.openTiming
                )
            }
        }
    }
}