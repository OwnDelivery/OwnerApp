package app.delivery.domain.usecases

import app.delivery.api.OrderApi
import app.delivery.domain.mappers.OrderMapper
import app.delivery.domain.models.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchOrdersUseCase(
    private val orderApi: OrderApi,
    private val orderMapper: OrderMapper
) {

    fun fetchOrders(): Flow<Result<Pair<String, Order?>>> {
        return orderApi.getOrders().map { orderPairResult ->
            orderPairResult.mapCatching { orderPair ->
                Pair(orderPair.first, orderPair.second?.let { orderMapper.mapFrom(it) })
            }
        }
    }
}