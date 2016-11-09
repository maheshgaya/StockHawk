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

import com.sam_chordas.android.stockhawk.Constants;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by Mahesh Gaya on 11/8/16.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private String mSymbol;
    private static final int CURSOR_LOADER_ID = 1;
    private Cursor mCursor;
    private static String TAG = DetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        Uri uri = Uri.parse(getIntent().getStringExtra(Constants.EXTRA_DETAIL));
        //Log.d(TAG, "onCreate: " + uri);
        mSymbol = uri.getLastPathSegment(); //gets the symbol from the calling activity

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        Utils.getCurrentDateTime();
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

