package app.delivery.driverapp.di

import app.delivery.domain.usecases.*
import app.delivery.driverapp.viewmodels.LoginViewModel
import app.delivery.driverapp.viewmodels.SplashScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DriverAppModule {

    @Provides
    fun provideSplashViewModel(checkUserLoggedInUsecase: CheckUserLoggedInUsecase): SplashScreenViewModel {
        return SplashScreenViewModel(checkUserLoggedInUsecase)
    }

    @Provides
    fun provideLoginViewModel(userLoginUseCase: UserLoginUseCase): LoginViewModel {
        return LoginViewModel(FirebaseAuth.getInstance(), userLoginUseCase)
    }

}