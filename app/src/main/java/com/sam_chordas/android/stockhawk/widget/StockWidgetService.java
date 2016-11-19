package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Mahesh Gaya on 11/18/16.
 */

/**
 * Credits: https://github.com/commonsguy/cw-advandroid,
 * https://developer.android.com/guide/topics/appwidgets/index.html
 */
public class StockWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new StockRemoteViewsFactory(this.getApplicationContext(),
                intent));
    }
}
