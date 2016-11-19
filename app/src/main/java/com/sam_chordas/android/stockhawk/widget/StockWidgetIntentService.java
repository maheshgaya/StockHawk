package com.sam_chordas.android.stockhawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.Constants;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.ui.DetailActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;
import com.sam_chordas.android.stockhawk.ui.StockAppWidgetProvider;

/**
 * Created by Mahesh Gaya on 11/15/16.
 */

public class StockWidgetIntentService extends IntentService {

    public StockWidgetIntentService(){
        super("StockWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                StockAppWidgetProvider.class));

        String description = "Stock";
        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.stock_appwidget;


            // Set up the intent that starts the StockWidgetService, which will
            // provide the views for this collection.
            Intent remoteViewsIntent = new Intent(getApplicationContext(), StockWidgetService.class);

            // Add the app widget ID to the intent extras.
            remoteViewsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            remoteViewsIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(getPackageName(), layoutId);
            // Add the adapter to the RemoteViews
            remoteViews.setRemoteAdapter(R.id.widget_stock_list, remoteViewsIntent); //for listview
            //empty list
            remoteViews.setEmptyView(R.id.widget_stock_list, R.id.empty_view);

            //set individual clicks
            Intent detailIntent = new Intent(getApplicationContext(), StockAppWidgetProvider.class);
            // Set the action for the intent.
            detailIntent.setAction(StockAppWidgetProvider.CLICK_ACTION);
            detailIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent detailPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, detailIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_stock_list, detailPendingIntent);

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_title, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
