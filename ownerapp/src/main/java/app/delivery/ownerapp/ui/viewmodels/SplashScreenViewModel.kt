package app.delivery.ownerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.delivery.contract.ViewEvent
import app.delivery.domain.usecases.CheckUserLoggedInUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            checkUserLoggedInUsecase.invoke().collect { result ->
                result.fold(onSuccess = {
                    _viewState.emit(viewState.value.copy(isLoggedIn = it))
                }, onFailure = {
                    _viewState.emit(_viewState.value.copy(actionError = ViewEvent(it)))
                })
            }
        }
    }

    data class ViewState(
        val isLoggedIn: Boolean? = null,
        val actionError: ViewEvent<Throwable>? = null
    )
}