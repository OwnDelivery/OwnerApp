package app.delivery.driverapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.delivery.contract.ViewEvent
import app.delivery.domain.usecases.CheckUserLoggedInUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val checkUserLoggedInUsecase: CheckUserLoggedInUsecase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    fun checkLogin() {
        viewModelScope.launch {
            checkUserLoggedInUsecase.invoke().onStart {
                _viewState.emit(_viewState.value.copy(isLoading = true))
            }.collect { result ->
                result.fold(onSuccess = {
                    _viewState.emit(_viewState.value.copy(isLoading = false, loginSuccess = it))
                }, onFailure = {
                    _viewState.emit(
                        _viewState.value.copy(
                            isLoading = false,
                            actionError = ViewEvent(it)
                        )
                    )
                })
            }
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val loginSuccess: Boolean? = null,
        val actionError: ViewEvent<Throwable>? = null
    )
}