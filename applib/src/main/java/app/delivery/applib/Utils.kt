package app.delivery.applib

import android.content.Context
import android.content.Intent
import android.net.Uri

object Utils {

    fun showDirection(context: Context, lat: Double, lon: Double) {
        val gmmIntentUri =
            Uri.parse("google.navigation:q=$lat,$lon")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    fun showLocation(context: Context, lat: Double, lon: Double) {
        val gmmIntentUri =
            Uri.parse("geo:0,0?q=$lat,$lon(location)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    fun makePhoneCall(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        context.startActivity(intent)
    }
}