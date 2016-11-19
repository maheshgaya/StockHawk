package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.Constants;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by Mahesh Gaya on 11/18/16.
 */

public class StockRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private static final String TAG = StockRemoteViewsFactory.class.getSimpleName();
    private Cursor mCursor;
    private Context mContext;
    private int mAppWidgetId;

    /*

    Cursor data  = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
            new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                    QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
            QuoteColumns.ISCURRENT + " = ?",
            new String[]{"1"},
            null);

    if (data == null){
        return;
    }

    if (!data.moveToFirst()){
        data.close();
        return;
    }

    //get data from cursor
    String quoteName = data.getString(Constants.COLUMN_SYMBOL);
    data.close();

    */

    public StockRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        onDataSetChanged();
    }

    @Override
    public void onDataSetChanged() {
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor  = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null){
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Construct a remote views item based on the app widget item XML file,
        // and set the text based on the position.
        RemoteViews itemRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
        mCursor.moveToPosition(position);
        String symbol = mCursor.getString(Constants.COLUMN_SYMBOL);
        String bidPrice = mCursor.getString(Constants.COLUMN_BIDPRICE);
        String change = mCursor.getString(Constants.COLUMN_PERCENT_CHANGE);
        Log.d(TAG, "getViewAt: " + position + " " + symbol);
        itemRemoteViews.setTextViewText(R.id.stock_symbol, symbol);
        itemRemoteViews.setTextViewText(R.id.bid_price, bidPrice);
        itemRemoteViews.setTextViewText(R.id.change, change);

        //itemRemoteViews.setOnClickPendingIntent();

        // Return the remote views object.
        return itemRemoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
