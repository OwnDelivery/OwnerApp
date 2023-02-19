package app.delivery.domain.usecases

import app.delivery.api.OrderApi
import app.delivery.domain.mappers.OrderMapper
import app.delivery.domain.models.Order
import kotlinx.coroutines.flow.Flow

class UpdateOrderUseCase(
    private val orderApi: OrderApi,
    private val orderMapper: OrderMapper
) {

    fun updateOrder(orderId: String, order: Order): Flow<Result<Unit>> {
        return orderApi.updateOrder(orderId, orderMapper.mapFrom(order))
    }
}