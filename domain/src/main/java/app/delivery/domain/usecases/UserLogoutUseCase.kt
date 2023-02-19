package app.delivery.domain.usecases

import app.delivery.api.UserApi
import kotlinx.coroutines.flow.Flow

class UserLogoutUseCase(private val userApi: UserApi) {

    fun logout(): Flow<Unit> {
        return userApi.logout()
    }
}