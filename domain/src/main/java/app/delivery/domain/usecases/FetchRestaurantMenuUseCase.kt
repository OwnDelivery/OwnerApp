package app.delivery.domain.usecases

import app.delivery.api.RestaurantApi
import app.delivery.contract.RestaurantMenu
import app.delivery.domain.mappers.RestaurantMenuMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchRestaurantMenuUseCase(
    private val restaurantApi: RestaurantApi,
    private val restaurantMenuMapper: RestaurantMenuMapper
) {
    fun fetchOwnerMenu(): Flow<Result<RestaurantMenu>> {
        return restaurantApi.getRestaurant().map { restaurantResult ->
            if (restaurantResult.isFailure) {
                Result.failure(restaurantResult.exceptionOrNull() ?: Exception(""))
            } else {
                try {
                    Result.success(
                        restaurantMenuMapper.mapFrom(
                            restaurantResult.getOrThrow().menu,
                        )
                    )
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }
    }
}