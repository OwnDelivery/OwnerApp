package app.delivery.domain.usecases

import app.delivery.api.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CheckUserLoggedInUsecase(private val userApi: UserApi) {

    fun invoke(): Flow<Result<Boolean>> {
        return userApi.getUser().map { userResult ->
            userResult.mapCatching { user ->
                user != null
            }
        }
    }
}