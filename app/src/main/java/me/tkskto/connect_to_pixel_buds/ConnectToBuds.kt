package me.tkskto.connect_to_pixel_buds

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

val INTENT_NAME = "me.tkskto.connect_to_pixel_buds.click_action"

/**
 * Implementation of App Widget functionality.
 */
class ConnectToBuds : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // TODO: apply current status.
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (context == null || intent == null) return

        when (intent.action) {
            INTENT_NAME ->
                Log.d("intent.action:" , INTENT_NAME)
                // TODO: connect to buds through Bluetooth.
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.connect_to_buds)

    val intent = Intent(context, ConnectToBuds::class.java).apply { action = INTENT_NAME }

    val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    views.setOnClickPendingIntent(R.id.button, pendingIntent);

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
