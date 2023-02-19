package app.delivery.ownerapp.di

import app.delivery.domain.usecases.*
import app.delivery.ownerapp.mappers.MenuListMapper
import app.delivery.ownerapp.ui.viewmodels.*
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class OwnerAppModule {

    @Provides
    fun provideSplashViewModel(checkUserLoggedInUsecase: CheckUserLoggedInUsecase): SplashScreenViewModel {
        return SplashScreenViewModel(checkUserLoggedInUsecase)
    }

    @Provides
    fun provideLoginViewModel(userLoginUseCase: UserLoginUseCase): LoginViewModel {
        return LoginViewModel(FirebaseAuth.getInstance(), userLoginUseCase)
    }

    @Provides
    fun provideHomeScreenViewModel(
        checkRestaurantOpenUseCase: CheckRestaurantOpenUseCase,
        updateRestaurantStatusUseCase: UpdateRestaurantStatusUseCase,
        syncMenuUseCase: SyncMenuUseCase,
        userLogoutUseCase: UserLogoutUseCase
    ): HomeScreenViewModel {
        return HomeScreenViewModel(
            checkRestaurantOpenUseCase,
            updateRestaurantStatusUseCase,
            syncMenuUseCase,
            userLogoutUseCase
        )
    }

    @Provides
    fun provideMenuScreenViewModel(
        fetchRestaurantMenuUseCase: FetchRestaurantMenuUseCase,
        updateAvailableMenuUseCase: UpdateAvailableMenuUseCase
    ): MenuScreenViewModel {
        return MenuScreenViewModel(
            fetchRestaurantMenuUseCase,
            updateAvailableMenuUseCase,
            MenuListMapper()
        )
    }

    @Provides
    fun provideOrderScreenViewModel(
        fetchOrdersUseCase: FetchOrdersUseCase,
        updateOrderUseCase: UpdateOrderUseCase
    ): OrderScreenViewModel {
        return OrderScreenViewModel(fetchOrdersUseCase, updateOrderUseCase)
    }

    @Provides
    fun providePastOrderScreenViewModel(
        fetchPastOrdersUseCase: FetchPastOrdersUseCase
    ): PastOrderViewModel {
        return PastOrderViewModel(fetchPastOrdersUseCase)
    }
}