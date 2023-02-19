package app.delivery.api

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

object RemoteConfigApi {

    fun getDeliveryChargePerKm(): Double {
        return FirebaseRemoteConfig.getInstance().getDouble("delivery_charge_per_km")
    }

    fun getDistanceFactor(): Double {
        return FirebaseRemoteConfig.getInstance().getDouble("distance_factor")
    }
}