package app.delivery.api.di

import android.content.Context
import android.location.LocationManager
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import app.delivery.api.*
import app.delivery.api.config.MenuConfig
import app.delivery.api.proto.AddressProtoSerializer
import app.delivery.api.protos.AddressProto
import app.delivery.api.repositories.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private val Context.addressDataStore: DataStore<AddressProto> by dataStore(
    fileName = "address.pb",
    serializer = AddressProtoSerializer
)


@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Provides
    fun provideUserApi(): UserApi {
        return UserApi(UserRepository(FirebaseAuth.getInstance()))
    }

    @Provides
    fun provideOrderApi(): OrderApi {
        return OrderApi(
            OrderRepository(
                FirebaseDatabase.getInstance().getReference("orders"),
                FirebaseDatabase.getInstance().getReference("past_orders")
            )
        )
    }

    @Provides
    fun provideLocationApi(@ApplicationContext context: Context): LocationApi {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationApi(
            locationManager,
            LocationRepository(
                FirebaseDatabase.getInstance().getReference("delivery_partner_location")
            )
        )
    }

    @Provides
    fun provideRemoteConfigApi(): RemoteConfigApi {
        return RemoteConfigApi
    }

    @Provides
    fun provideRestaurantApi(): RestaurantApi {
        return RestaurantApi(
            RestaurantRepository(
                FirebaseDatabase.getInstance().getReference("restaurant"),
                MenuConfig
            )
        )
    }
}