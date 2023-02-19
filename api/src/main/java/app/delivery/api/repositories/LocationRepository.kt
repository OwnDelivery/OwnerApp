package app.delivery.api.repositories

import app.delivery.api.dtos.LocationDto
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepository(private val locationDatabaseReference: DatabaseReference) {

    fun updateLocation(locationDto: LocationDto): Flow<Result<Unit>> = callbackFlow {
        locationDatabaseReference.setValue(locationDto).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(Result.success(Unit))
            } else {
                val exception = task.exception ?: Exception("Unable to update location")
                FirebaseCrashlytics.getInstance().recordException(exception)
                trySend(Result.failure(exception))
            }
        }

        awaitClose()
    }
}