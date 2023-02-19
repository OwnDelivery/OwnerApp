package app.delivery.api

import app.delivery.api.dtos.OrderDto
import app.delivery.api.repositories.OrderRepository

class OrderApi(private val orderRepository: OrderRepository) {

    fun updateOrder(id: String, orderDto: OrderDto) = orderRepository.updateOrder(id, orderDto)

    fun getOrders() = orderRepository.getOrders()

    fun getPastOrders() = orderRepository.getPastOrders()
}