package com.sam_chordas.android.stockhawk.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.Constants;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.widget.StockWidgetIntentService;

/**
 * Created by Mahesh Gaya on 11/14/16.
 */

public class StockAppWidgetProvider extends AppWidgetProvider{
    private static final String TAG = StockAppWidgetProvider.class.getSimpleName();

    //opening detail activity
    public static final String CLICK_ACTION = "com.sam_chordas.android.stockhawk.ui.CLICK";
    public static final String EXTRA_ITEM = "com.sam_chordas.android.stockhawk.ui.item";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, StockWidgetIntentService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if (StockTaskService.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            context.startService(new Intent(context, StockWidgetIntentService.class));
        } else if (intent.getAction().equals(CLICK_ACTION)){
            //opens DetailsActivity
            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Intent detailIntent = new Intent(context, DetailActivity.class);
            detailIntent.putExtra(Constants.EXTRA_DETAIL, intent.getStringExtra(StockAppWidgetProvider.EXTRA_ITEM));
            context.startActivity(detailIntent);

        }
        super.onReceive(context, intent);



    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, StockWidgetIntentService.class));
    }
}
