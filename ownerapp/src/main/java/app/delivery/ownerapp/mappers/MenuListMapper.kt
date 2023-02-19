package app.delivery.ownerapp.mappers

import app.delivery.contract.RestaurantMenu
import app.delivery.contract.RestaurantMenuItem
import app.delivery.ownerapp.ui.viewmodels.MenuScreenViewModel

class MenuListMapper {

    fun mapFrom(ownerMenu: RestaurantMenu): List<MenuScreenViewModel.MenuItem> {
        val menuItemList: MutableList<MenuScreenViewModel.MenuItem> = mutableListOf()

        ownerMenu.sections.toList().sortedBy { it.second.rank }.forEach { section ->
            menuItemList.add(
                MenuScreenViewModel.MenuItem.Section(
                    section.first,
                    section.second.name,
                    section.second.rank
                )
            )
            section.second.items.toList().sortedBy { it.second.rank }.forEach { menuItem ->
                menuItemList.add(mapFrom(section.first, menuItem.first, menuItem.second))
            }
        }

        return menuItemList
    }

    private fun mapFrom(
        sectionId: String,
        foodId: String,
        restaurantMenuItem: RestaurantMenuItem
    ): MenuScreenViewModel.MenuItem {
        return MenuScreenViewModel.MenuItem.Food(
            sectionId = sectionId,
            foodId = foodId,
            name = restaurantMenuItem.name,
            price = restaurantMenuItem.price,
            isAvailable = restaurantMenuItem.available,
            rank = restaurantMenuItem.rank
        )
    }
}