package me.tkskto.connect_to_pixel_buds

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

val INTENT_NAME = "me.tkskto.connect_to_pixel_buds.click_action"

/**
 * Implementation of App Widget functionality.
 */
class ConnectToBuds : AppWidgetProvider() {
    val bluetoothController = BluetoothController()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // TODO: apply current status.
    }

    /**
     * when the widget was tapped
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (context == null || intent == null) return

        if (INTENT_NAME == intent.action) {
            Log.d("intent.action:", INTENT_NAME)
            bluetoothController.scan()
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.connect_to_buds)

    val intent = Intent(context, ConnectToBuds::class.java).apply {
        action = INTENT_NAME
    }

    val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    views.setOnClickPendingIntent(R.id.button, pendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
