package com.hazam.softwarequotes.sync;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.*;

import com.hazam.net.BetterHttpClient;
import com.hazam.softwarequotes.model.Quote;
import com.hazam.util.FileUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class QuotesSyncService extends IntentService {
	public static final String INTENT_QUOTES_SYNC_DONE = "com.hazam.softwarequotes.QUOTES_SYNC_DONE";
	public static final String EXTRA_RETURN_CHANNEL = "extra_return_channel";
	public static final String EXTRA_DATA_URL = "extra_data_url";
	public static final String EXTRA_ERROR_CAUSE = "extra_error_cause";
	private static final String DEFAULT_DATA_URL = "http://dl.dropbox.com/u/1575714/softwarequotes.json";
	public static final int RESULT_ERROR = 0xC;

	public QuotesSyncService() {
		super("QuotesSyncService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final HttpClient httpClient = new BetterHttpClient(this);
		final ResultReceiver return_channel = intent.getParcelableExtra(EXTRA_RETURN_CHANNEL);
		final String dataUrl = intent.hasExtra(EXTRA_DATA_URL) ? intent.getStringExtra(EXTRA_DATA_URL)
				: DEFAULT_DATA_URL;
		try {
			HttpUriRequest req = new HttpGet(new URI(dataUrl));

			HttpResponse resp = httpClient.execute(req);
			insertQuotesFromJSONStream(resp.getEntity().getContent());
			sendBroadcast(new Intent(INTENT_QUOTES_SYNC_DONE));
		} catch (JSONException e) {
			e.printStackTrace();
			reportError(return_channel, e);
		} catch (IOException e) {
			e.printStackTrace();
			reportError(return_channel, e);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			reportError(return_channel, e);
		}
	}
	
	private void reportError(final ResultReceiver return_channel, final Throwable th) {
		Bundle resultData = new Bundle();
		resultData.putString(EXTRA_ERROR_CAUSE, th.getMessage());
		return_channel.send(RESULT_ERROR, resultData);
	}

	private void insertQuotesFromJSONStream(InputStream jsonStream) throws JSONException {
		final ContentResolver ct = getContentResolver();
		JSONArray obj = new JSONArray(FileUtils.readAll(jsonStream));
		for (int i = 0; i < obj.length(); i++) {
			JSONArray obji = obj.getJSONArray(i);
			Quote q = Quote.fromJSONArray(i + "", obji);
			Uri quoteUri = q.buildUri();
			ContentValues cv = q.getContentValues();
			ct.insert(quoteUri, cv);
		}
	}
}
