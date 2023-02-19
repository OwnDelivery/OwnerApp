package app.delivery.domain.mappers

import app.delivery.api.dtos.FoodMenuDto
import app.delivery.contract.RestaurantMenu
import app.delivery.contract.RestaurantMenuItem
import app.delivery.contract.RestaurantMenuSection
import com.google.firebase.crashlytics.FirebaseCrashlytics

class RestaurantMenuMapper {

    fun mapFrom(
        foodMenuDto: FoodMenuDto
    ): RestaurantMenu {
        return RestaurantMenu(
            sections = foodMenuDto.sections.mapValues { sectionDto ->
                try {
                    RestaurantMenuSection(
                        name = sectionDto.value.name,
                        rank = sectionDto.value.rank,
                        items = sectionDto.value.items.mapValues { foodDto ->
                            try {
                                RestaurantMenuItem(
                                    name = foodDto.value.name,
                                    price = foodDto.value.price,
                                    foodType = foodDto.value.foodType,
                                    available = foodDto.value.available,
                                    rank = foodDto.value.rank
                                )
                            } catch (e: Exception) {
                                FirebaseCrashlytics.getInstance().recordException(e)
                                RestaurantMenuItem()
                            }
                        }
                    )
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    RestaurantMenuSection()
                }
            }
        )
    }
}