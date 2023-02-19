package app.delivery.domain.usecases

import app.delivery.api.RestaurantApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

class SyncMenuUseCase(
    private val restaurantApi: RestaurantApi
) {

    fun syncSystemMenu(): Flow<Result<Unit>> {
        return restaurantApi.getSystemMenu().flatMapMerge { menu ->
            restaurantApi.updateMenu(menu)
        }
    }
}