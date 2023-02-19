package app.delivery.driverapp.services

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import app.delivery.domain.usecases.UploadLocationUseCase
import app.delivery.driverapp.utils.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var updateLocationUseCase: UploadLocationUseCase

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var locationUpdateJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeGround()

        if (checkPermission()) {
            locationUpdateJob?.cancel()
            locationUpdateJob = scope.launch {
                updateLocationUseCase.invoke().collect()
            }
        }


        return START_STICKY
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startForeGround() {
        startForeground(
            2, NotificationUtils.getLocationTrackingNotification(this)
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
            return Intent(context, LocationService::class.java)
        }
    }

}