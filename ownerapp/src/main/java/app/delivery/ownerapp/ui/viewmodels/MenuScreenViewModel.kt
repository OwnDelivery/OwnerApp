package app.delivery.ownerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.delivery.contract.ViewEvent
import app.delivery.domain.usecases.FetchRestaurantMenuUseCase
import app.delivery.domain.usecases.UpdateAvailableMenuUseCase
import app.delivery.ownerapp.mappers.MenuListMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuScreenViewModel @Inject constructor(
    private val fetchRestaurantMenuUseCase: FetchRestaurantMenuUseCase,
    private val updateAvailableMenuUseCase: UpdateAvailableMenuUseCase,
    private val menuListMapper: MenuListMapper
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    fun fetchFoodMenu() {
        viewModelScope.launch {
            fetchRestaurantMenuUseCase.fetchOwnerMenu()
                .onStart {
                    _viewState.emit(viewState.value.copy(loading = true))
                }
                .catch { e ->
                    _viewState.emit(_viewState.value.copy(actionError = ViewEvent(e)))
                }
                .onEach { result ->
                    result.fold(onSuccess = { ownerMenu ->
                        _viewState.emit(
                            viewState.value.copy(
                                loading = false,
                                menuItemList = menuListMapper.mapFrom(ownerMenu)
                            )
                        )
                    }, onFailure = {
                        _viewState.emit(
                            viewState.value.copy(
                                loading = false,
                                actionError = ViewEvent(it)
                            )
                        )
                    })
                }.collect()
        }
    }

    fun changeAvailability(menuItem: MenuItem.Food, available: Boolean) {

        viewModelScope.launch {
            updateAvailableMenuUseCase.updateAvailableFoods(
                menuItem.sectionId,
                menuItem.foodId,
                available
            )
                .collect { result ->
                    result.exceptionOrNull()?.let {
                        _viewState.value = viewState.value.copy(actionError = ViewEvent(it))
                    }

                }
        }
    }

    data class ViewState(
        val loading: Boolean = false,
        val menuItemList: List<MenuItem> = emptyList(),
        val actionError: ViewEvent<Throwable>? = null
    )

    sealed class MenuItem {
        data class Section(val id: String, val name: String, val rank: Int) : MenuItem()
        data class Food(
            val sectionId: String,
            val foodId: String,
            val name: String,
            val price: Double,
            val isAvailable: Boolean,
            val rank: Int
        ) : MenuItem()
    }
}