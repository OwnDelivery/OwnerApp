package app.delivery.domain.usecases

import app.delivery.api.UserApi
import app.delivery.contract.User
import kotlinx.coroutines.flow.Flow

class UserLoginUseCase(private val userApi: UserApi) {

    fun performPostLogin(name: String): Flow<Result<User>> {
        return userApi.login(name)
    }
}