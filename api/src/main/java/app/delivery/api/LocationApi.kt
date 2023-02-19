package app.delivery.api

import android.Manifest.permission
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.annotation.RequiresPermission
import app.delivery.api.dtos.LocationDto
import app.delivery.api.repositories.LocationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationApi(
    private val locationManager: LocationManager,
    private val locationRepository: LocationRepository
) {

    @RequiresPermission(anyOf = [permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION])
    fun listen(): Flow<LocationDto> = callbackFlow {
        val locationListener = LocationListener { location ->
            trySend(
                LocationDto(
                    location.latitude,
                    location.longitude,
                    location.time
                )
            )
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            60000,
            250f,
            locationListener,
            Looper.getMainLooper()
        )
        awaitClose { locationManager.removeUpdates(locationListener) }
    }

    fun updateLocation(locationDto: LocationDto) = locationRepository.updateLocation(locationDto)
}