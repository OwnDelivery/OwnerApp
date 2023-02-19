package app.delivery.domain.usecases

import app.delivery.api.OrderApi
import app.delivery.api.UserApi
import app.delivery.contract.User
import app.delivery.domain.mappers.OrderMapper
import app.delivery.domain.models.DeliveryPartner
import app.delivery.domain.models.Order
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf

class AssignDeliveryPartnerUseCase(
    private val userApi: UserApi,
    private val orderApi: OrderApi,
    private val orderMapper: OrderMapper
) {

    @OptIn(FlowPreview::class)
    fun assign(orderId: String, order: Order): Flow<Result<Unit>> {
        return userApi.getUser().flatMapMerge { userResult ->
            userResult.fold(onSuccess = { user ->
                if (user == null) {
                    handleUserNotFound()
                } else {
                    updateOrder(orderId, order, user)
                }
            }, onFailure = {
                flowOf(Result.failure(it))
            })
        }
    }

    private fun updateOrder(
        orderId: String,
        order: Order,
        user: User
    ) = orderApi.updateOrder(
        orderId, orderMapper.mapFrom(
            order.copy(
                deliveryPartner = DeliveryPartner(
                    phone = user.phoneNumber, name = user.name
                )
            )
        )
    )

    private fun handleUserNotFound(): Flow<Result<Unit>> {
        val exception = Exception("User not found to perform this operation")
        FirebaseCrashlytics.getInstance().recordException(exception)
        return flowOf(Result.failure(exception))
    }
}