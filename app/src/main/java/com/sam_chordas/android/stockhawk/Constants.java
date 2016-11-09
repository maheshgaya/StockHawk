package com.sam_chordas.android.stockhawk;

import com.sam_chordas.android.stockhawk.data.QuoteColumns;

/**
 * Created by Mahesh Gaya on 11/8/16.
 */

public class Constants {
    public static final String EXTRA_DETAIL = "detail_extra";

    public static final String[] PROJECTION = {
            QuoteColumns._ID,
            QuoteColumns.SYMBOL,
            QuoteColumns.PERCENT_CHANGE,
            QuoteColumns.CHANGE,
            QuoteColumns.BIDPRICE,
            QuoteColumns.CREATED,
            QuoteColumns.ISUP,
            QuoteColumns.ISCURRENT
    };
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_SYMBOL = 1;
    public static final int COLUMN_PERCENT_CHANGE = 2;
    public static final int COLUMN_CHANGE = 3;
    public static final int COLUMN_BIDPRICE = 4;
    public static final int COLUMN_CREATED = 5;
    public static final int COLUMN_ISUP = 6;
    public static final int COLUMN_ISCURRENT = 7;
}
