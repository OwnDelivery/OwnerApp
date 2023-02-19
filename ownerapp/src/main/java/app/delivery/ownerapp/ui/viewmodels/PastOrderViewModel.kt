package app.delivery.ownerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.delivery.contract.ViewEvent
import app.delivery.domain.models.Order
import app.delivery.domain.usecases.FetchPastOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PastOrderViewModel @Inject constructor(
    private val fetchPastOrdersUseCase: FetchPastOrdersUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    fun fetchOrders() {
        viewModelScope.launch {
            fetchPastOrdersUseCase.fetchOrders().onStart {
                _viewState.emit(_viewState.value.copy(fetching = true))
            }.onEach { result ->
                result.fold(onSuccess = { orderList ->
                    val pastTwoDaysOrder = orderList.toList().sortedByDescending { it.createdAt }
                        .filter { it.createdAt >= getPastTwoDaysTime() }
                    _viewState.emit(
                        _viewState.value.copy(
                            fetching = false, twoDayOrders = pastTwoDaysOrder
                        )
                    )
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

    private fun getPastTwoDaysTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.add(Calendar.DATE, -1)
        return calendar.timeInMillis
    }

    data class ViewState(
        val twoDayOrders: List<Order> = emptyList(),
        val fetching: Boolean = false,
        val actionError: ViewEvent<Throwable>? = null
    ) {
        fun getTodaySale(): Double {
            return twoDayOrders.filter { it.createdAt > getTodayStartTime() }
                .sumOf { it.getTotalAmount() }
        }

        fun getYesterdaySale(): Double {
            val totalSale = twoDayOrders.sumOf { it.getTotalAmount() }
            val todaySale = getTodaySale()
            return totalSale - todaySale
        }

        private fun getTodayStartTime(): Long {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            return calendar.timeInMillis
        }
    }
}