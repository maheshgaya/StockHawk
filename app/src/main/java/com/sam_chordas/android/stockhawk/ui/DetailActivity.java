package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.Constants;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mahesh Gaya on 11/8/16.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private String mSymbol;
    private static final int CURSOR_LOADER_ID = 1;
    private static final String REGEX = "[^\\d.]+|\\.(?!\\d)";
    private Cursor mCursor;
    private LineChart mChart;
    private static String TAG = DetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        Uri uri = Uri.parse(getIntent().getStringExtra(Constants.EXTRA_DETAIL));
        //Log.d(TAG, "onCreate: " + uri);
        mSymbol = uri.getLastPathSegment(); //gets the symbol from the calling activity
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        mChart = (LineChart) findViewById(R.id.linechart);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (mSymbol != null) {
            return new CursorLoader(this,
                    QuoteProvider.Quotes.CONTENT_URI,
                    Constants.PROJECTION,
                    QuoteColumns.SYMBOL + " = ?",
                    new String[]{mSymbol},
                    null
            );
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        ArrayList<Float> values = new ArrayList<Float>(){};
        ArrayList<Float> labels = new ArrayList<Float>(){};


        if (mCursor.getCount() != 0){
            while (mCursor.moveToNext()){
                Float bidPrice = mCursor.getFloat(Constants.COLUMN_BIDPRICE);
                values.add(bidPrice);
                Pattern pattern = Pattern.compile(REGEX);
                Matcher matcher = pattern.matcher(mCursor.getString(Constants.COLUMN_PERCENT_CHANGE));
                //Log.d(TAG, "onLoadFinished: " + matcher.toString());
                //labels.add(percentChange);
            }
        }

        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < values.size(); i++) {

            // turn your data into Entry objects
            //labels.get(i)
            entries.add(new Entry(i, values.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(getResources().getColor(R.color.material_blue_700));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black)); // styling, ...
        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);

        mChart.invalidate(); // refresh




    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

