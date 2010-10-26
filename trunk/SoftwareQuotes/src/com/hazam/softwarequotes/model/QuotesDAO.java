package com.hazam.softwarequotes.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.hazam.softwarequotes.provider.QuotesContract;
import com.hazam.util.L;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class QuotesDAO extends AsyncQueryHandler {
    private WeakReference<QuotesDAOListener> mListener;

    public QuotesDAO(ContentResolver c, QuotesDAOListener l) {
        super(c);
        setQueryListener(l);
    }

    public void setQueryListener(QuotesDAOListener listener) {
        mListener = new WeakReference<QuotesDAOListener>(listener);
    }

    public void clearQueryListener() {
        mListener = null;
    }

    public void startInsertQuote(Quote c) {
        final ContentValues ret = new ContentValues();
        final String id = c.getId();
        ret.put(QuotesContract.QuotesColumns.QUOTE_ID, id);
        ret.put(QuotesContract.QuotesColumns.QUOTE_AUTHOR, c.getAuthor());
        ret.put(QuotesContract.QuotesColumns.QUOTE_BODY, c.getBody());
        ret.put(QuotesContract.QuotesColumns.QUOTE_SOURCE, c.getSource());
        Uri uri = Uri.parse(QuotesContract.Quotes.CONTENT_URI + "/" + id);
        startInsert(-1, null, uri, ret);
    }

    public void getQuote(final String id) {
        final Uri uri = QuotesContract.Quotes.CONTENT_URI.buildUpon().appendPath(id).build();
        startQuery(-1, null, uri, null, QuotesContract.QuotesColumns.QUOTE_ID + " = " + id, null, null);
    }

    public void getAllQuotes() {
        L.D(this, "getAllQuotes()");
        startQuery(-1, null, QuotesContract.Quotes.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        L.D(this, "onQueryComplete()");
        final ArrayList<Quote> lastQueried = new ArrayList<Quote>();
        while (cursor != null && cursor.moveToNext()) {
            int columnId = cursor.getColumnIndex(QuotesContract.QuotesColumns.QUOTE_ID);
            int columnAuthor = cursor.getColumnIndex(QuotesContract.QuotesColumns.QUOTE_AUTHOR);
            int columnSource = cursor.getColumnIndex(QuotesContract.QuotesColumns.QUOTE_SOURCE);
            int columnBody = cursor.getColumnIndex(QuotesContract.QuotesColumns.QUOTE_BODY);
            lastQueried.add(new Quote(cursor.getString(columnId), cursor.getString(columnAuthor), cursor.getString(columnSource), cursor.getString(columnBody)));
        }
        if (cursor != null) {
            cursor.close();
        }
        final QuotesDAOListener listener = mListener == null ? null : mListener.get();
        if (listener != null) {
            listener.onQuotesLoaded(lastQueried);
        }
    }


    public static interface QuotesDAOListener {
        public void onQuotesLoaded(List<Quote> arg);
    }
}
