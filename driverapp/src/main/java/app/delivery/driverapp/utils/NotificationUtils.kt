package app.delivery.driverapp.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.delivery.domain.models.Order
import app.delivery.driverapp.BuildConfig
import app.delivery.driverapp.R
import app.delivery.driverapp.ui.activities.MainActivity

object NotificationUtils {

    private const val FOREGROUND_CHANNEL_ID = "Order Sync"
    private const val FOREGROUND_LOC_CHANNEL_ID = "Location"
    private const val NEW_ORDER_CHANNEL_ID = "New Order"
    private const val REQ_CODE_OPEN_ACTIVITY = 1

    private fun checkAndCreateChannel(
        context: Context,
        channelName: String,
        importance: Int = NotificationManager.IMPORTANCE_LOW,
        customSound: Boolean = false
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            context.getSystemService(NotificationManager::class.java)?.let { notificationManager ->

                if (notificationManager.getNotificationChannel(channelName) == null) {
                    val serviceChannel = NotificationChannel(
                        channelName,
                        channelName,
                        importance
                    )

                    if (customSound) {
                        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build()
                        serviceChannel.setSound(
                            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.new_order),
                            audioAttributes
                        )
                    }

                    notificationManager.createNotificationChannel(serviceChannel)
                }
            }
        }

    }

    fun getLocationTrackingNotification(
        context: Context,
    ): Notification {

        checkAndCreateChannel(context, FOREGROUND_CHANNEL_ID)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            REQ_CODE_OPEN_ACTIVITY,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(context, FOREGROUND_CHANNEL_ID)
            .setContentTitle("Mayas Delivery")
            .setContentText("Listening for location")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_stat_name)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(pendingIntent)
            .setOngoing(true)

        return notificationBuilder.build()
    }

    fun getOrderListeningNotification(
        context: Context,
    ): Notification {

        checkAndCreateChannel(context, FOREGROUND_LOC_CHANNEL_ID)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            REQ_CODE_OPEN_ACTIVITY,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(context, FOREGROUND_LOC_CHANNEL_ID)
            .setContentTitle("Mayas App")
            .setContentText("Listening for orders")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(true)

        return notificationBuilder.build()
    }

    fun showNewOrderNotification(context: Context, order: Order) {

        checkAndCreateChannel(
            context = context,
            channelName = NEW_ORDER_CHANNEL_ID,
            importance = NotificationManager.IMPORTANCE_HIGH,
            customSound = true
        )
        val pendingIntent = PendingIntent.getActivity(
            context,
            REQ_CODE_OPEN_ACTIVITY,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, NEW_ORDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("New Order Received ${order.getReadableRefId()} ")
            .setContentText("${order.getReadableRefId()} received from ${order.orderedBy} for Rs.${order.getTotalAmount()}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(pendingIntent, true)
            .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.new_order))
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(order.refId.hashCode(), builder.build())
        }
    }

    fun clearNotifications(context: Context, order: Order) {
        with(NotificationManagerCompat.from(context)) {
            cancel(order.refId.hashCode())
        }
    }

    fun clearAllNotifications(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            cancelAll()
        }
    }
}