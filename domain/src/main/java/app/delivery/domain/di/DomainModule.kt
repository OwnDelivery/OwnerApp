package app.delivery.domain.di

import app.delivery.api.*
import app.delivery.domain.mappers.OrderMapper
import app.delivery.domain.mappers.RestaurantMenuMapper
import app.delivery.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Provides
    fun provideCheckUserLoggedInUseCase(userApi: UserApi): CheckUserLoggedInUsecase {
        return CheckUserLoggedInUsecase(userApi)
    }

    @Provides
    fun provideUserLoginUseCase(userApi: UserApi): UserLoginUseCase {
        return UserLoginUseCase(userApi)
    }

    @Provides
    fun provideUserLogoutCase(userApi: UserApi): UserLogoutUseCase {
        return UserLogoutUseCase(userApi)
    }

    @Provides
    fun provideFetchMenuUseCase(
        restaurantApi: RestaurantApi
    ): FetchRestaurantMenuUseCase {
        return FetchRestaurantMenuUseCase(restaurantApi, RestaurantMenuMapper())
    }

    @Provides
    fun provideUpdateMenuUseCase(restaurantApi: RestaurantApi): UpdateAvailableMenuUseCase {
        return UpdateAvailableMenuUseCase(restaurantApi)
    }

    @Provides
    fun provideFetchOrderUseCase(orderApi: OrderApi): FetchOrdersUseCase {
        return FetchOrdersUseCase(orderApi, OrderMapper())
    }

    @Provides
    fun provideUpdateOrderUseCase(orderApi: OrderApi): UpdateOrderUseCase {
        return UpdateOrderUseCase(orderApi, OrderMapper())
    }

    @Provides
    fun provideAssignDeliveryPartnerUseCase(
        userApi: UserApi,
        orderApi: OrderApi
    ): AssignDeliveryPartnerUseCase {
        return AssignDeliveryPartnerUseCase(userApi, orderApi, OrderMapper())
    }

    @Provides
    fun provideUploadLocationUseCase(locationApi: LocationApi): UploadLocationUseCase {
        return UploadLocationUseCase(locationApi)
    }

    @Provides
    fun provideGetRestaurantUseCase(restaurantApi: RestaurantApi): CheckRestaurantOpenUseCase {
        return CheckRestaurantOpenUseCase(restaurantApi)
    }

    @Provides
    fun provideUpdateRestaurantUseCase(restaurantApi: RestaurantApi): UpdateRestaurantStatusUseCase {
        return UpdateRestaurantStatusUseCase(restaurantApi)
    }

    @Provides
    fun providePastOrderUseCase(orderApi: OrderApi): FetchPastOrdersUseCase {
        return FetchPastOrdersUseCase(orderApi, OrderMapper())
    }

    @Provides
    fun provideSyncMenuUseCase(restaurantApi: RestaurantApi): SyncMenuUseCase {
        return SyncMenuUseCase(restaurantApi)
    }
}