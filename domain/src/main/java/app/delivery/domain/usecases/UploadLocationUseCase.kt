package app.delivery.domain.usecases

import android.Manifest
import androidx.annotation.RequiresPermission
import app.delivery.api.LocationApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class UploadLocationUseCase(
    private val locationApi: LocationApi,
) {
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun invoke() = flow<Unit> {
        locationApi.listen().onEach { location ->
            locationApi.updateLocation(location).collect()
        }.collect()
    }
}