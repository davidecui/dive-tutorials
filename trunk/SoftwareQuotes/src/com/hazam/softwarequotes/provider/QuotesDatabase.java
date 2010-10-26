package com.hazam.softwarequotes.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.BaseColumns;

import static com.hazam.softwarequotes.provider.QuotesContract.QuotesColumns;
import static com.hazam.softwarequotes.provider.QuotesContract.Tables;

class QuotesDatabase extends SQLiteOpenHelper {

    private static final int VER_START = 1;

    private static final String DB_NAME = "quotes.db";
    
    QuotesDatabase(Context ctx) {
        super(ctx, Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + DB_NAME, null, VER_START);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDBScript(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
        //still don't have anything to upgrade
    }


    private static void createDBScript(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + Tables.QUOTES + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + QuotesColumns.QUOTE_ID + " TEXT NOT NULL,"
                + QuotesColumns.QUOTE_AUTHOR + " TEXT NOT NULL,"
                + QuotesColumns.QUOTE_SOURCE + " TEXT NOT NULL,"
                + QuotesColumns.QUOTE_BODY + " TEXT NOT NULL,"
                + "UNIQUE (" + QuotesColumns.QUOTE_ID + ") ON CONFLICT REPLACE)");
    }

}
