package app.delivery.domain.mappers

import app.delivery.api.dtos.AddressDto
import app.delivery.api.dtos.DeliveryPartnerDto
import app.delivery.api.dtos.FoodItemDto
import app.delivery.api.dtos.OrderDto
import app.delivery.domain.models.Address
import app.delivery.domain.models.DeliveryPartner
import app.delivery.domain.models.FoodItem
import app.delivery.domain.models.Order

class OrderMapper {

    fun mapFrom(orderDto: OrderDto): Order {
        return Order(
            address = mapFrom(orderDto.address),
            foods = orderDto.foods.map {
                mapFrom(it)
            },
            orderedBy = orderDto.orderedBy,
            contactNumber = orderDto.contactNumber,
            contactUid = orderDto.contactUid,
            confirmed = orderDto.confirmed,
            cancelled = orderDto.cancelled,
            paymentDone = orderDto.paymentDone,
            foodReady = orderDto.foodReady,
            deliveryPartner = orderDto.deliveryPartner?.let { mapFrom(it) },
            pickedUp = orderDto.pickedUp,
            delivered = orderDto.delivered,
            refId = orderDto.refId,
            createdAt = orderDto.createdAt
        )
    }

    private fun mapFrom(addressDto: AddressDto): Address {
        return Address(
            lat = addressDto.lat,
            lon = addressDto.lon,
            locality = addressDto.locality,
            fullAddress = addressDto.fullAddress
        )
    }

    private fun mapFrom(foodItemDto: FoodItemDto): FoodItem {
        return FoodItem(
            foodType = foodItemDto.foodType,
            name = foodItemDto.name,
            price = foodItemDto.price,
            qty = foodItemDto.qty
        )
    }

    private fun mapFrom(deliveryPartnerDto: DeliveryPartnerDto): DeliveryPartner {
        return DeliveryPartner(
            name = deliveryPartnerDto.name,
            phone = deliveryPartnerDto.phone
        )
    }

    fun mapFrom(order: Order): OrderDto {
        return OrderDto(
            address = mapFrom(order.address),
            foods = order.foods.map { mapFrom(it) },
            orderedBy = order.orderedBy,
            contactNumber = order.contactNumber,
            contactUid = order.contactUid,
            refId = order.refId,
            confirmed = order.confirmed,
            cancelled = order.cancelled,
            paymentDone = order.paymentDone,
            foodReady = order.foodReady,
            deliveryPartner = order.deliveryPartner?.let { mapFrom(it) },
            pickedUp = order.pickedUp,
            delivered = order.delivered,
            createdAt = order.createdAt
        )
    }

    private fun mapFrom(address: Address): AddressDto {
        return AddressDto(
            lat = address.lat,
            lon = address.lon,
            locality = address.locality ?: "",
            fullAddress = address.fullAddress
        )
    }

    private fun mapFrom(foodItem: FoodItem): FoodItemDto {
        return FoodItemDto(
            foodType = foodItem.foodType,
            name = foodItem.name,
            price = foodItem.price,
            qty = foodItem.qty
        )
    }

    private fun mapFrom(deliveryPartner: DeliveryPartner): DeliveryPartnerDto {
        return DeliveryPartnerDto(
            phone = deliveryPartner.phone,
            name = deliveryPartner.name
        )
    }
}