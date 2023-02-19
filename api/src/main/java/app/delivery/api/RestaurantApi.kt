package app.delivery.api

import app.delivery.api.dtos.FoodMenuDto
import app.delivery.api.repositories.RestaurantRepository

class RestaurantApi(private val restaurantRepository: RestaurantRepository) {
    fun getRestaurant() = restaurantRepository.getRestaurant()

    fun updateRestaurant(isOpen: Boolean, message: String) =
        restaurantRepository.updateRestaurant(isOpen, message)

    fun updateMenu(foodMenuDto: FoodMenuDto) = restaurantRepository.updateMenu(foodMenuDto)

    fun updateFoodAvailability(sectionName: String, foodName: String, isAvailable: Boolean) =
        restaurantRepository.updateAvailability(sectionName, foodName, isAvailable)

    fun getSystemMenu() = restaurantRepository.getSystemMenu()
}