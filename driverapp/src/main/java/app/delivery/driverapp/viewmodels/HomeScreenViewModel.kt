package app.delivery.driverapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.delivery.contract.ViewEvent
import app.delivery.domain.models.Order
import app.delivery.domain.usecases.AssignDeliveryPartnerUseCase
import app.delivery.domain.usecases.FetchOrdersUseCase
import app.delivery.domain.usecases.UpdateOrderUseCase
import app.delivery.domain.usecases.UserLogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val fetchOrdersUseCase: FetchOrdersUseCase,
    private val assignDeliveryPartnerUseCase: AssignDeliveryPartnerUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase,
    private val logoutUseCase: UserLogoutUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    fun changeOrderStatusToAcknowledged(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                assignDeliveryPartnerUseCase.assign(orderId, order).collect {
                    it.exceptionOrNull()?.let { throwable ->
                        _viewState.value = viewState.value.copy(actionError = ViewEvent(throwable))
                    }
                }
            }
        }
    }

    fun changeOrderStatusToPickedUp(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                updateOrderUseCase.updateOrder(orderId, order.copy(pickedUp = true)).collect {
                    it.exceptionOrNull()?.let { throwable ->
                        _viewState.value = viewState.value.copy(actionError = ViewEvent(throwable))
                    }
                }
            }
        }
    }

    fun changeOrderStatusToDelivered(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                updateOrderUseCase.updateOrder(orderId, order.copy(delivered = true)).collect {
                    it.exceptionOrNull()?.let { throwable ->
                        _viewState.value = viewState.value.copy(actionError = ViewEvent(throwable))
                    }
                }
            }
        }
    }


    fun activateOrder(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                updateOrderUseCase.updateOrder(orderId, order.copy(cancelled = false))
                    .collect { result ->
                        result.exceptionOrNull()?.let {
                            _viewState.value = viewState.value.copy(actionError = ViewEvent(it))
                        }
                    }
            }
        }
    }


    fun cancelOrder(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                updateOrderUseCase.updateOrder(orderId, order.copy(cancelled = true))
                    .collect { result ->
                        result.exceptionOrNull()?.let {
                            _viewState.value = viewState.value.copy(actionError = ViewEvent(it))
                        }
                    }
            }
        }
    }


    fun fetchOrders() {
        viewModelScope.launch {
            fetchOrdersUseCase.fetchOrders().onStart {
                _viewState.emit(_viewState.value.copy(fetching = true))
            }.onEach { result ->
                result.fold(onSuccess = { pair ->
                    val map = viewState.value.orders.toMutableMap()
                    val id = pair.first
                    val order = pair.second
                    if (order == null) {
                        map.remove(id)
                    } else if (order.foodReady) {
                        map[id] = order
                    }
                    _viewState.emit(_viewState.value.copy(fetching = false, orders = map))
                }, onFailure = {
                    _viewState.emit(
                        _viewState.value.copy(
                            fetching = false,
                            actionError = ViewEvent(it)
                        )
                    )
                })
            }.collect()
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.logout().collect {
                _viewState.emit(viewState.value.copy(userLoggedOut = true))
            }
        }
    }

    data class ViewState(
        val actionError: ViewEvent<Throwable>? = null,
        val fetching: Boolean = false,
        val userLoggedOut: Boolean = false,
        val orders: Map<String, Order> = emptyMap(),
    )
}