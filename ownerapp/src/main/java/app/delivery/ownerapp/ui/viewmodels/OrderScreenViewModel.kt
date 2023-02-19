package app.delivery.ownerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.delivery.contract.ViewEvent
import app.delivery.domain.models.Order
import app.delivery.domain.usecases.FetchOrdersUseCase
import app.delivery.domain.usecases.UpdateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderScreenViewModel @Inject constructor(
    private val fetchOrdersUseCase: FetchOrdersUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    fun changeOrderStatusToConfirmed(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                updateOrderUseCase.updateOrder(orderId, order.copy(confirmed = true))
                    .collect { result ->
                        result.exceptionOrNull()?.let {
                            _viewState.value = viewState.value.copy(actionError = ViewEvent(it))
                        }
                    }
            }
        }
    }

    fun changeOrderStatusToPickedUp(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                updateOrderUseCase.updateOrder(orderId, order.copy(foodReady = true))
                    .collect { result ->
                        result.exceptionOrNull()?.let {
                            _viewState.value = viewState.value.copy(actionError = ViewEvent(it))
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

    fun changeOrderStatusToPaid(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                updateOrderUseCase.updateOrder(
                    orderId,
                    order.copy(paymentDone = true)
                ).collect { result ->
                    result.exceptionOrNull()?.let {
                        _viewState.value = viewState.value.copy(actionError = ViewEvent(it))
                    }
                }
            }
        }
    }

    fun fetchOrders() {
        viewModelScope.launch {
            fetchOrdersUseCase.fetchOrders()
                .onStart {
                    _viewState.emit(_viewState.value.copy(fetching = true))
                }
                .onEach { result ->
                    result.fold(onSuccess = { pair ->
                        val map = viewState.value.orders.toMutableMap()
                        val id = pair.first
                        val order = pair.second
                        if (order == null) {
                            map.remove(id)
                        } else {
                            map[id] = order
                        }
                        _viewState.emit(_viewState.value.copy(fetching = false, orders = map))
                    }, onFailure = {
                        _viewState.value =
                            viewState.value.copy(fetching = false, actionError = ViewEvent(it))
                    })
                }.collect()
        }
    }

    fun deliverOrder(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                updateOrderUseCase.updateOrder(
                    orderId,
                    order.copy(delivered = true)
                ).collect { result ->
                    result.exceptionOrNull()?.let {
                        _viewState.value = viewState.value.copy(actionError = ViewEvent(it))
                    }
                }
            }
        }
    }

    fun notDeliverOrder(orderId: String) {
        val order = _viewState.value.orders[orderId]
        if (order != null) {
            viewModelScope.launch {
                updateOrderUseCase.updateOrder(
                    orderId,
                    order.copy(delivered = false)
                ).collect { result ->
                    result.exceptionOrNull()?.let {
                        _viewState.value = viewState.value.copy(actionError = ViewEvent(it))
                    }
                }
            }
        }
    }

    data class ViewState(
        val orders: Map<String, Order> = emptyMap(),
        val fetching: Boolean = false,
        val actionError: ViewEvent<Throwable>? = null
    )
}