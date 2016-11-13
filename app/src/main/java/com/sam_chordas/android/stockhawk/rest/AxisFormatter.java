package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Mahesh Gaya on 11/12/16.
 */

public class AxisFormatter implements IAxisValueFormatter {
    private static final String TAG = AxisFormatter.class.getSimpleName();
    private Context mContext;
    private String newDate = "";
    private String oldDate = "";
    private String[] mDateArray;
    private String[] mTimeArray;
    private String mFirstTime;

    public AxisFormatter(Context context, String[] dateArray, String[] timeArray){
        mContext = context;
        mDateArray = dateArray;
        mTimeArray = timeArray;
        mFirstTime = timeArray[0];
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            newDate = mDateArray[(int) value];

            if (!oldDate.equals(newDate)) {
                oldDate = newDate;
                return mDateArray[(int) value] + " " + mTimeArray[(int) value];
            } else {
                return mTimeArray[(int) value];
            }

        } catch (java.lang.ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "getFormattedValue: ", e);
            return mFirstTime;
        }

    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
