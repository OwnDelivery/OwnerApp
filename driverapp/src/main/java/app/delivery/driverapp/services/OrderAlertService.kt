package app.delivery.driverapp.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import app.delivery.domain.usecases.FetchOrdersUseCase
import app.delivery.driverapp.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class OrderAlertService : Service() {

    @Inject
    lateinit var fetchOrdersUseCase: FetchOrdersUseCase

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var orderJob: Job? = null

    override fun onCreate() {
        super.onCreate()

        orderJob?.cancel()
        orderJob = scope.launch {
            fetchOrdersUseCase.fetchOrders().onEach { result ->
                result.getOrNull()?.let {
                    val order = it.second
                    if (order != null && order.foodReady) {
                        if (order.deliveryPartner == null && order.cancelled.not()) {
                            NotificationUtils.showNewOrderNotification(
                                this@OrderAlertService, order
                            )
                            //SoundUtils.triggerSound(this@OrderAlertService)
                        } else {
                            NotificationUtils.clearNotifications(this@OrderAlertService, order)
                        }
                    }
                }
            }.collect()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeGround()

        return START_STICKY
    }

    private fun startForeGround() {
        startForeground(
            1001, NotificationUtils.getOrderListeningNotification(this)
        )
    }

    private fun stopForeGround() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, OrderAlertService::class.java)
        }
    }
}