package com.sam_chordas.android.stockhawk.ui;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by Mahesh Gaya on 11/14/16.
 */

public class StockAppWidgetProvider extends AppWidgetProvider{
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String description = "Stock";

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.stock_appwidget_small;
            RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setTextViewText(R.id.widget, "Hello World!");


            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


}
