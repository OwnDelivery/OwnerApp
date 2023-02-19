package app.delivery.api.repositories

import app.delivery.contract.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(private val firebaseAuth: FirebaseAuth) {

    fun login(name: String): Flow<Result<User>> = callbackFlow<Result<User>> {
        val user = firebaseAuth.currentUser

        if (user != null) {
            user.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(
                        Result.success(
                            User(
                                id = user.uid,
                                name = name,
                                phoneNumber = user.phoneNumber ?: ""
                            )
                        )
                    )
                } else {
                    val exception = it.exception ?: Exception("unable to login")
                    FirebaseCrashlytics.getInstance().recordException(exception)
                    trySend(Result.failure(exception))
                }
            }
        } else {
            val exception = Exception("User not found")
            FirebaseCrashlytics.getInstance().recordException(exception)
            trySend(Result.failure(exception))
        }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    fun getUser(): Flow<Result<User?>> = flow {
        val currentUser = firebaseAuth.currentUser

        if (currentUser == null) {
            emit(Result.success(null))
        } else if (currentUser.displayName == null || currentUser.phoneNumber == null) {
            val exception = Exception("User created without name and phone number")
            FirebaseCrashlytics.getInstance().recordException(exception)
            emit(Result.failure(exception))
        } else {
            emit(
                Result.success(
                    User(
                        id = currentUser.uid,
                        name = currentUser.displayName ?: "",
                        phoneNumber = currentUser.phoneNumber ?: ""
                    )
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    fun logout(): Flow<Unit> = flow {
        emit(firebaseAuth.signOut())
    }
}