package app.delivery.api

import app.delivery.api.repositories.UserRepository

class UserApi(private val userRepository: UserRepository) {

    fun getUser() = userRepository.getUser()

    fun login(name: String) = userRepository.login(name)

    fun logout() = userRepository.logout()
}