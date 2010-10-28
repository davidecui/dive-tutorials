package com.hazam.softwarequotes.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Process;

import com.hazam.util.L;
import com.hazam.util.StorageUtils;

public class QuotesProvider extends ContentProvider {
	private QuotesDatabase mQuotesDatabase;

	public QuotesProvider() {
		L.D(this, "constructor()");
	}

	@Override
	public boolean onCreate() {
		L.D(this, "onCreate() " + Process.myPid());
		final Context ctx = getContext();
		if (!StorageUtils.hasWritableStorage(true)) {
			L.W(this, "SDCard is not Writable!");
			return false;
		}
		mQuotesDatabase = new QuotesDatabase(ctx);
		return true;
	}

	@Override
	public String getType(Uri uri) {
		return "x-quote/vnd.hazam.quote";
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
		L.D(this, "query() on provider");
		final SQLiteDatabase db = mQuotesDatabase.getReadableDatabase();
		if (db != null) {
			final String groupBy = null;
			final String having = null;
			return db.query(QuotesContract.Tables.QUOTES, projection, selection, selectionArgs, groupBy, having, sort);
		} else {
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		final SQLiteDatabase db = mQuotesDatabase.getWritableDatabase();
		if (db != null) {
			long id = db.insertOrThrow(QuotesContract.Tables.QUOTES, QuotesContract.QuotesColumns.QUOTE_ID,
					contentValues);
			return ContentUris.withAppendedId(QuotesContract.Quotes.CONTENT_URI, id);
		} else {
			return null;
		}
	}

	@Override
	public int delete(Uri uri, String s, String[] strings) {
		final SQLiteDatabase db = mQuotesDatabase.getWritableDatabase();
		if (db != null) {
			return db.delete(QuotesContract.Tables.QUOTES, s, strings);
		} else {
			return 0;
		}
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
		final SQLiteDatabase db = mQuotesDatabase.getWritableDatabase();
		if (db != null) {
			return db.update(QuotesContract.Tables.QUOTES, contentValues, s, strings);
		} else {
			return 0;
		}
	}
}
