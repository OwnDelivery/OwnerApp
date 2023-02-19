package app.delivery.domain.usecases

import app.delivery.api.OrderApi
import app.delivery.domain.mappers.OrderMapper
import app.delivery.domain.models.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchPastOrdersUseCase(
    private val orderApi: OrderApi,
    private val orderMapper: OrderMapper
) {

    fun fetchOrders(): Flow<Result<List<Order>>> {
        return orderApi.getPastOrders().map { ordersResult ->
            ordersResult.mapCatching { orders ->
                orders.map { orderDto ->
                    orderMapper.mapFrom(orderDto)
                }
            }
        }
    }
}