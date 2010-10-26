package com.hazam.softwarequotes.provider;

import android.net.Uri;

public class QuotesContract {


    private static final String PATH_QUOTES = "quotes";
    public static final String CONTENT_AUTHORITY = "com.hazam.softwarequotes";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class Quotes {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUOTES).build();
    }

    public static final class QuotesColumns {
        public static final String QUOTE_ID = "quote_id";
        public static final String QUOTE_AUTHOR = "author";
        public static final String QUOTE_SOURCE = "source";
        public static final String QUOTE_BODY = "body";
    }

    public static final class Tables {
        public static final String QUOTES = "quotes";
    }
}
