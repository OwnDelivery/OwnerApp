package app.delivery.ownerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.delivery.contract.ViewEvent
import app.delivery.domain.usecases.CheckRestaurantOpenUseCase
import app.delivery.domain.usecases.SyncMenuUseCase
import app.delivery.domain.usecases.UpdateRestaurantStatusUseCase
import app.delivery.domain.usecases.UserLogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val checkRestaurantOpenUseCase: CheckRestaurantOpenUseCase,
    private val updateRestaurantStatusUseCase: UpdateRestaurantStatusUseCase,
    private val syncMenuUseCase: SyncMenuUseCase,
    private val logoutUseCase: UserLogoutUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    fun checkOnline() {
        viewModelScope.launch {
            checkRestaurantOpenUseCase.getOpenStatus()
                .onEach { result ->
                    result.fold(onSuccess = { restaurant ->
                        _viewState.emit(
                            _viewState.value.copy(
                                isOnline = restaurant.isOpen,
                                timing = restaurant.timing
                            )
                        )
                    }, onFailure = {
                        _viewState.emit(_viewState.value.copy(actionError = ViewEvent(it)))
                    })
                }
                .collect()
        }
    }

    fun updateStatus(isOpen: Boolean, timing: String) {
        viewModelScope.launch {
            updateRestaurantStatusUseCase.updateStatus(isOpen, timing).collect { result ->
                result.exceptionOrNull()?.let {
                    _viewState.emit(_viewState.value.copy(actionError = ViewEvent(it)))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.logout().collect {
                _viewState.emit(viewState.value.copy(userLoggedOut = true))
            }
        }
    }

    fun syncMenu() {
        viewModelScope.launch {
            syncMenuUseCase.syncSystemMenu().collect()
        }
    }

    data class ViewState(
        val isOnline: Boolean = false,
        val timing: String = "",
        val userLoggedOut: Boolean = false,
        val actionError: ViewEvent<Throwable>? = null
    )
}