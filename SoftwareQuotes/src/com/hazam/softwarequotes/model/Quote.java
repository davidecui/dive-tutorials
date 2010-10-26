package com.hazam.softwarequotes.model;

import android.content.ContentValues;
import android.net.Uri;
import com.hazam.softwarequotes.provider.QuotesContract;
import org.json.JSONArray;
import android.database.Cursor;

public class Quote {
    private final String id;
    private final String author;
    private final String source;
    private final String body;

    public Quote(String id, String author, String source, String body) {
        super();
        this.id = id;
        this.author = author;
        this.source = source;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    public String getBody() {
        return body;
    }

    public static Quote fromJSONArray(String id, JSONArray obji) {
        return new Quote(id, obji.optString(0), obji.optString(1), obji.optString(2));
    }

    public static Quote fromCursor(Cursor c) {
        if (c.moveToFirst()) {
            int columnId = c.getColumnIndex(QuotesContract.QuotesColumns.QUOTE_ID);
            int columnAuthor = c.getColumnIndex(QuotesContract.QuotesColumns.QUOTE_AUTHOR);
            int columnSource = c.getColumnIndex(QuotesContract.QuotesColumns.QUOTE_SOURCE);
            int columnBody = c.getColumnIndex(QuotesContract.QuotesColumns.QUOTE_BODY);
            return new Quote(c.getString(columnId), c.getString(columnAuthor), c.getString(columnSource), c.getString(columnBody));
        } else {
            return null;
        }
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    /**
     * Very inefficient and generated equals!
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Quote other = (Quote) obj;
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (body == null) {
            if (other.body != null)
                return false;
        } else if (!body.equals(other.body))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        return true;
    }


    public Uri buildUri() {
        return Uri.parse(QuotesContract.Quotes.CONTENT_URI + "/" + id);
    }

    public ContentValues getContentValues() {
        ContentValues ret = new ContentValues();
        ret.put(QuotesContract.QuotesColumns.QUOTE_ID, id);
        ret.put(QuotesContract.QuotesColumns.QUOTE_AUTHOR, author);
        ret.put(QuotesContract.QuotesColumns.QUOTE_BODY, body);
        ret.put(QuotesContract.QuotesColumns.QUOTE_SOURCE, source);
        return ret;
    }
}
