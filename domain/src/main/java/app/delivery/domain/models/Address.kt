package app.delivery.domain.models

import app.delivery.api.RemoteConfigApi
import app.delivery.contract.AppConfig
import app.delivery.contract.DistanceCalculator

data class Address(
    val lat: Double,
    val lon: Double,
    val locality: String?,
    val fullAddress: String
) {

    fun getDeliveryDistance(): Double {
        return DistanceCalculator.distance(
            lat,
            lon,
            AppConfig.MAYAS_LOC_LAT,
            AppConfig.MAYAS_LOC_LON
        ) * RemoteConfigApi.getDistanceFactor() //adjusting the road turning and more distance.
    }

    fun getDeliveryCharge(): Double {
        return (getDeliveryDistance() * RemoteConfigApi.getDeliveryChargePerKm()).toInt()
            .toDouble() //Rounding off
    }
}