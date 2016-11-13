package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.sam_chordas.android.stockhawk.Constants;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.AxisFormatter;
import com.sam_chordas.android.stockhawk.rest.Utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Mahesh Gaya on 11/8/16.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private String mSymbol;
    private static final int CURSOR_LOADER_ID = 1;
    private Cursor mCursor;
    private Toolbar mToolbar;
    private LineChart mChart;
    private static String TAG = DetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        Uri uri = Uri.parse(getIntent().getStringExtra(Constants.EXTRA_DETAIL));
        //Log.d(TAG, "onCreate: " + uri);
        mSymbol = uri.getLastPathSegment(); //gets the symbol from the calling activity
        configureToolbar(mSymbol);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        mChart = (LineChart) findViewById(R.id.linechart);

        configureChart();
    }

    /**
     * adds the toolbar
     * @param title
     */
    private void configureToolbar(String title){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(title + " " + getString(R.string.detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    /**
     * configure the chart texts and colors
     */
    private void configureChart(){
        XAxis xAxis = mChart.getXAxis();
        YAxis leftAxis = mChart.getAxisLeft();
        YAxis rightAxis = mChart.getAxisRight();
        Legend legend = mChart.getLegend();
        Description description = mChart.getDescription();
        description.setText(getString(R.string.graph_description));
        mChart.setHorizontalScrollBarEnabled(true);
        mChart.setVerticalScrollBarEnabled(true);


        if (Build.VERSION.SDK_INT > 22) {
            //legend
            legend.setTextColor(getResources().getColor(R.color.material_gray_100, null));
            //axes
            leftAxis.setTextColor(getResources().getColor(R.color.material_gray_100, null));
            rightAxis.setTextColor(getResources().getColor(R.color.material_gray_100, null));
            xAxis.setTextColor(getResources().getColor(R.color.material_gray_100, null));
            //description
            description.setTextColor(getResources().getColor(R.color.material_gray_100, null));
        } else {
            //legend
            legend.setTextColor(getResources().getColor(R.color.material_gray_100));
            //axes
            leftAxis.setTextColor(getResources().getColor(R.color.material_gray_100));
            rightAxis.setTextColor(getResources().getColor(R.color.material_gray_100));
            xAxis.setTextColor(getResources().getColor(R.color.material_gray_100));
            //description
            description.setTextColor(getResources().getColor(R.color.material_gray_100));
        }


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

    /**
     * sets data on graph
     * @param loader
     * @param data
     */

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data; //transfer data to cursor member

        //initialization
        ArrayList<Float> values = new ArrayList<Float>(){};
        ArrayList<String> timesXAxisValues = new ArrayList<String>(){};
        ArrayList<String> dateXAxisValues = new ArrayList<String>(){};
        int isUp = 0;

        //get data from cursor
        if (mCursor.getCount() != 0){
            while (mCursor.moveToNext()){
                Float bidPrice = mCursor.getFloat(Constants.COLUMN_BIDPRICE);
                values.add(bidPrice);
                Date date = new Date(Timestamp.valueOf(mCursor.getString(Constants.COLUMN_CREATED)).getTime());
                isUp = mCursor.getInt(Constants.COLUMN_ISUP);
                //get time only
                DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                String timeFormatted = timeFormatter.format(date);
                timesXAxisValues.add(timeFormatted);

                DateFormat dateFormatter = new SimpleDateFormat("MM-dd");
                String dateFormatted = dateFormatter.format(date);
                dateXAxisValues.add(dateFormatted);

            }
        }

        String[] timeArray = timesXAxisValues.toArray(new String[timesXAxisValues.size()]);
        String[] dateArray = dateXAxisValues.toArray(new String[dateXAxisValues.size()]);

        //Add time to x-axis
        AxisFormatter formatter = new AxisFormatter(getApplicationContext(), dateArray, timeArray);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(2f); // minimum axis-step (interval) is 2
        xAxis.setValueFormatter(formatter);

        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < values.size(); i++) {

            // turn your data into Entry objects
            entries.add(new Entry(i, values.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Bid Price "); // add entries to dataset
        //disable value text for each point
        dataSet.setDrawValues(false);
        //allow graph to be filled up with color
        dataSet.setDrawFilled(true);

        //don't draw the circles
        if (timeArray.length != 1) {
            dataSet.setDrawCircles(false);
        }

        //fills graph depending on whether it is up or not
        if (isUp == 0) {
            if (Build.VERSION.SDK_INT > 22) {
                dataSet.setColor(getResources().getColor(android.R.color.holo_red_light, null));
                dataSet.setFillColor(getResources().getColor(android.R.color.holo_red_light, null));
            } else {
                dataSet.setColor(getResources().getColor(android.R.color.holo_red_light));
                dataSet.setFillColor(getResources().getColor(android.R.color.holo_red_light));
            }
        } else {
            if (Build.VERSION.SDK_INT > 22) {
                dataSet.setColor(getResources().getColor(android.R.color.holo_green_light, null));
                dataSet.setFillColor(getResources().getColor(android.R.color.holo_green_light, null));
            } else {
                dataSet.setColor(getResources().getColor(android.R.color.holo_green_light));
                dataSet.setFillColor(getResources().getColor(android.R.color.holo_green_light));
            }
        }


        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        mChart.invalidate(); // refresh

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

