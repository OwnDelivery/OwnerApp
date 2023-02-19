package app.delivery.api.repositories

import app.delivery.api.config.MenuConfig
import app.delivery.api.dtos.FoodMenuDto
import app.delivery.api.dtos.RestaurantDto
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

class RestaurantRepository(
    private val restaurantDatabaseRef: DatabaseReference,
    private val menuConfig: MenuConfig
) {

    fun updateRestaurant(isOpen: Boolean, message: String): Flow<Result<Unit>> =
        callbackFlow {
            restaurantDatabaseRef.updateChildren(
                mapOf(
                    "is_open" to isOpen,
                    "timing" to message
                )
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Result.success(Unit))
                } else {
                    val exception = task.exception ?: Exception("Unable to update restaurant")
                    FirebaseCrashlytics.getInstance().recordException(exception)
                    trySend(Result.failure(exception))
                }
            }

            awaitClose()
        }

    fun getRestaurant(): Flow<Result<RestaurantDto>> = callbackFlow {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val restaurantDto = snapshot.getValue(RestaurantDto::class.java)
                    if (restaurantDto != null) {
                        trySend(Result.success(restaurantDto))
                    } else {
                        val exception = Exception("Unable to fetch restaurant")
                        FirebaseCrashlytics.getInstance().recordException(exception)
                        trySend(Result.failure(exception))
                    }
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    trySend(Result.failure(e))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.message)
                FirebaseCrashlytics.getInstance().recordException(error.toException())
                trySend(Result.failure(error.toException()))
            }
        }

        restaurantDatabaseRef.addValueEventListener(valueEventListener)

        awaitClose { restaurantDatabaseRef.removeEventListener(valueEventListener) }
    }

    fun updateMenu(foodMenuDto: FoodMenuDto): Flow<Result<Unit>> = callbackFlow {
        restaurantDatabaseRef.child("menu").setValue(foodMenuDto).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(Result.success(Unit))
            } else {
                val exception = task.exception ?: Exception("Unable to update")
                FirebaseCrashlytics.getInstance().recordException(exception)
                trySend(Result.failure(exception))
            }
        }

        awaitClose()
    }

    fun updateAvailability(
        sectionName: String,
        foodName: String,
        isAvailable: Boolean
    ): Flow<Result<Unit>> =
        callbackFlow {
            restaurantDatabaseRef.child("menu").child("sections").child(sectionName).child("items")
                .child(foodName)
                .child("available")
                .setValue(isAvailable).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Result.success(Unit))
                    } else {
                        val exception = task.exception ?: Exception("Unable to update")
                        FirebaseCrashlytics.getInstance().recordException(exception)
                        trySend(Result.failure(exception))
                    }
                }

            awaitClose()
        }

    fun getSystemMenu(): Flow<FoodMenuDto> {
        return flowOf(menuConfig.getCompleteMenu())
    }
}