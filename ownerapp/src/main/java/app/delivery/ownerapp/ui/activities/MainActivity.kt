package app.delivery.ownerapp.ui.activities

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.delivery.ownerapp.BuildConfig
import app.delivery.ownerapp.R
import app.delivery.ownerapp.services.OrderAlertService
import app.delivery.ownerapp.ui.NavComponent
import app.delivery.ownerapp.ui.theme.OwnerAppTheme
import app.delivery.ownerapp.utils.NotificationUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OwnerAppTheme {
                NavComponent()
            }
        }

        remoteConfig()
        startService(OrderAlertService.createIntent(this))
        turnScreenOn()
    }

    override fun onResume() {
        super.onResume()
        NotificationUtils.clearAllNotifications(this)
    }

    private fun remoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build()
        )
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (remoteConfig.getLong("minimum_app_version") > BuildConfig.VERSION_CODE) {
                AlertDialog.Builder(this)
                    .setTitle("Update the App")
                    .setMessage("This app is no longer supported. Contact admin to update the app")
                    .setPositiveButton(
                        "Close"
                    ) { _, _ -> finish() }
                    .create().show()
            }
        }
    }

    private fun turnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }
    }
}