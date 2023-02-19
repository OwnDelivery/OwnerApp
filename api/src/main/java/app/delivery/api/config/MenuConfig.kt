package app.delivery.api.config

import app.delivery.api.dtos.FoodMenuDto
import app.delivery.api.dtos.FoodMenuItemDto
import app.delivery.api.dtos.FoodMenuSectionDto
import app.delivery.contract.FoodType

object MenuConfig {

    fun getCompleteMenu(): FoodMenuDto {
        return FoodMenuDto(
            sections = linkedMapOf(
                "veg_gravy" to FoodMenuSectionDto(
                    rank = 0,
                    name = "Veg Gravy", items = linkedMapOf(
                        "sambar" to FoodMenuItemDto(
                            name = "Sambar", foodType = FoodType.VEG, price = 45.0,
                            rank = 1
                        ),
                        "vatha_kuzhambu" to FoodMenuItemDto(
                            name = "Vatha Kuzhambu", foodType = FoodType.VEG, price = 50.0,
                            rank = 2
                        ),
                        "kara_kuzhambu" to FoodMenuItemDto(
                            name = "Kara Kuzhambu", foodType = FoodType.VEG, price = 60.0,
                            rank = 3
                        ),
                        "mor_kuzhambu" to FoodMenuItemDto(
                            name = "Mor Kuzhambu", foodType = FoodType.VEG, price = 35.0,
                            rank = 4
                        ),
                        "milagu_rasam" to FoodMenuItemDto(
                            name = "Milagu Rasam", foodType = FoodType.VEG, price = 25.0,
                            rank = 5
                        ),
                        "channa_masala" to FoodMenuItemDto(
                            name = "Channa Masala", foodType = FoodType.VEG, price = 70.0,
                            rank = 6
                        ),
                        "mushroom_masala" to FoodMenuItemDto(
                            name = "Mushroom Masala", foodType = FoodType.VEG, price = 70.0,
                            rank = 7
                        ),
                        "paneer_masala" to FoodMenuItemDto(
                            name = "Paneer Masala", foodType = FoodType.VEG, price = 90.0,
                            rank = 8
                        ),
                        "veg_kuruma" to FoodMenuItemDto(
                            name = "Veg Kuruma", foodType = FoodType.VEG, price = 60.0,
                            rank = 9
                        ),
                        "thakkali_thokku" to FoodMenuItemDto(
                            name = "Thakkali Thokku", foodType = FoodType.VEG, price = 35.0,
                            rank = 10
                        ),
                    )
                ),
                "poriyal" to FoodMenuSectionDto(
                    rank = 1,
                    name = "Poriyal",
                    items = linkedMapOf(
                        "beans_carrot" to FoodMenuItemDto(
                            name = "Beans Carrot",
                            foodType = FoodType.VEG,
                            price = 40.0,
                            rank = 0
                        ),
                        "cabbage" to FoodMenuItemDto(
                            name = "Cabbage",
                            foodType = FoodType.VEG,
                            price = 30.0,
                            rank = 0
                        ),
                        "beetroot" to FoodMenuItemDto(
                            name = "Beetroot",
                            foodType = FoodType.VEG,
                            price = 30.0,
                            rank = 0
                        ),
                        "potato" to FoodMenuItemDto(
                            name = "Potato",
                            foodType = FoodType.VEG,
                            price = 30.0,
                            rank = 0
                        ),
                        "other" to FoodMenuItemDto(
                            name = "Other",
                            foodType = FoodType.VEG,
                            price = 30.0,
                            rank = 0
                        ),
                    )
                ),
                "kootu" to FoodMenuSectionDto(
                    rank = 1,
                    name = "Kootu",
                    items = linkedMapOf(
                        "surakkai" to FoodMenuItemDto(
                            name = "Surakkai",
                            foodType = FoodType.VEG,
                            price = 45.0,
                            rank = 0
                        ),
                        "keerai" to FoodMenuItemDto(
                            name = "Keerai",
                            foodType = FoodType.VEG,
                            price = 45.0,
                            rank = 0
                        ),
                        "other" to FoodMenuItemDto(
                            name = "Other",
                            foodType = FoodType.VEG,
                            price = 30.0,
                            rank = 0
                        ),
                    )
                ),
                "rice_breads" to FoodMenuSectionDto(
                    rank = 1,
                    name = "Rice and Breads",
                    items = linkedMapOf(
                        "white_rice_small" to FoodMenuItemDto(
                            name = "White Rice(small)",
                            foodType = FoodType.VEG,
                            price = 20.0,
                            rank = 0
                        ),
                        "white_rice_big" to FoodMenuItemDto(
                            name = "White Rice(big)",
                            foodType = FoodType.VEG,
                            price = 30.0,
                            rank = 0
                        ),
                        "chapati" to FoodMenuItemDto(
                            name = "Chapati(2)",
                            foodType = FoodType.VEG,
                            price = 30.0,
                            rank = 0
                        ),
                        "idly" to FoodMenuItemDto(
                            name = "Idly(2)",
                            foodType = FoodType.VEG,
                            price = 30.0,
                            rank = 0
                        ),
                    )
                ),
                "non_veg_gravy" to FoodMenuSectionDto(
                    rank = 1,
                    name = "Non Veg Gravy",
                    items = linkedMapOf(
                        "naatu_Kozhi_kuzhambu" to FoodMenuItemDto(
                            name = "Naatu Kozhi Kuzhambu",
                            foodType = FoodType.NON_VEG,
                            price = 110.0,
                            rank = 0
                        ),
                        "chicken_phall" to FoodMenuItemDto(
                            name = "Chicken Phall",
                            foodType = FoodType.NON_VEG,
                            price = 130.0,
                            rank = 0
                        ),
                        "eral_kuzhambu" to FoodMenuItemDto(
                            name = "Eral Kuzhambu",
                            foodType = FoodType.NON_VEG,
                            price = 70.0,
                            rank = 0
                        ),
                        "aatu_kaal_paaya" to FoodMenuItemDto(
                            name = "Aatu Kaal Paaya",
                            foodType = FoodType.NON_VEG,
                            price = 120.0,
                            rank = 0
                        ),
                        "chicken_kuzhambu" to FoodMenuItemDto(
                            name = "Chicken Kuzhambu",
                            foodType = FoodType.NON_VEG,
                            price = 90.0,
                            rank = 0
                        ),
                        "mutton_kuzhambu" to FoodMenuItemDto(
                            name = "Mutton Kuzhambu",
                            foodType = FoodType.NON_VEG,
                            price = 130.0,
                            rank = 0
                        ),
                        "meen_kuzhambu" to FoodMenuItemDto(
                            name = "Meen Kuzhambu",
                            foodType = FoodType.NON_VEG,
                            price = 90.0,
                            rank = 0
                        ),
                        "egg_kuzhambu" to FoodMenuItemDto(
                            name = "Egg Kuzhambu",
                            foodType = FoodType.EGG,
                            price = 70.0,
                            rank = 0
                        ),
                        "prawn_thokku_100" to FoodMenuItemDto(
                            name = "Prawn Thokku(100 gms)",
                            foodType = FoodType.NON_VEG,
                            price = 100.0,
                            rank = 0
                        ),
                        "prawn_thokku_250" to FoodMenuItemDto(
                            name = "Prawn Thokku(250 gms)",
                            foodType = FoodType.NON_VEG,
                            price = 250.0,
                            rank = 0
                        ),
                    )
                ),
                "fish_fry" to FoodMenuSectionDto(
                    rank = 1,
                    name = "Fish Fry",
                    items = linkedMapOf(
                        "sankara_small" to FoodMenuItemDto(
                            name = "Sankara small",
                            foodType = FoodType.NON_VEG,
                            price = 70.0,
                            rank = 0
                        ),
                        "sankara_big" to FoodMenuItemDto(
                            name = "Sankara Big",
                            foodType = FoodType.NON_VEG,
                            price = 90.0,
                            rank = 0
                        ),
                        "vanjaram_small" to FoodMenuItemDto(
                            name = "Vanjaram Small",
                            foodType = FoodType.NON_VEG,
                            price = 150.0,
                            rank = 0
                        ),
                        "vanjaram_big" to FoodMenuItemDto(
                            name = "Vanjaram Big",
                            foodType = FoodType.NON_VEG,
                            price = 250.0,
                            rank = 0
                        ),
                        "para_small" to FoodMenuItemDto(
                            name = "Para Small",
                            foodType = FoodType.NON_VEG,
                            price = 80.0,
                            rank = 0
                        ),
                        "para_big" to FoodMenuItemDto(
                            name = "Para Big",
                            foodType = FoodType.NON_VEG,
                            price = 120.0,
                            rank = 0
                        )
                    )
                ),
                "non_veg_side_dish" to FoodMenuSectionDto(
                    rank = 1,
                    name = "Non Veg Side Dish",
                    items = linkedMapOf(
                        "nandu_varuval" to FoodMenuItemDto(
                            name = "Nandu varuval",
                            foodType = FoodType.NON_VEG,
                            price = 120.0,
                            rank = 0
                        ),
                        "chicken_chukka" to FoodMenuItemDto(
                            name = "Chicken Chukka",
                            foodType = FoodType.NON_VEG,
                            price = 150.0,
                            rank = 0
                        ),
                        "mutton_fry" to FoodMenuItemDto(
                            name = "Mutton Fry",
                            foodType = FoodType.NON_VEG,
                            price = 210.0,
                            rank = 0
                        ),
                        "chicken_65" to FoodMenuItemDto(
                            name = "Chicken 65",
                            foodType = FoodType.NON_VEG,
                            price = 90.0,
                            rank = 0
                        ),
                        "egg_podimas" to FoodMenuItemDto(
                            name = "Egg Podimas",
                            foodType = FoodType.NON_VEG,
                            price = 35.0,
                            rank = 0
                        ),
                    )
                )
            )
        )
    }
}