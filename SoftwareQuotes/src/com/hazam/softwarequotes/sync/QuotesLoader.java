package com.hazam.softwarequotes.sync;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import com.hazam.softwarequotes.R;
import com.hazam.softwarequotes.model.Quote;
import com.hazam.util.FileUtils;
import com.hazam.util.L;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuotesLoader extends AsyncTask<Void, Void, List<Quote>> {
    private final String targetPath;
    private Context ctx;
    private ProgressDialog pd;
    public static final String INTENT_QUOTES_SYNC_DONE = "com.hazam.softwarequotes.QUOTES_SYNC_DONE";

    public QuotesLoader(Context c, String path) {
        targetPath = path;
        ctx = c;
    }

    @Override
    protected void onPreExecute() {
        pd = createSyncProgressDialog(ctx);
        pd.show();
    }

    @Override
    protected List<Quote> doInBackground(Void... params) {
        ArrayList<Quote> toret = new ArrayList<Quote>();
        final ContentResolver ct = ctx.getContentResolver();
        try {
            InputStream jsonStream = ctx.getAssets().open(targetPath);
            JSONArray obj = new JSONArray(FileUtils.readAll(jsonStream));
            for (int i = 0; i < obj.length(); i++) {
                JSONArray obji = obj.getJSONArray(i);
                Quote q =  Quote.fromJSONArray(i+"", obji);
                Uri quoteUri = q.buildUri();
                ContentValues cv = q.getContentValues();
                ct.insert(quoteUri, cv);
                toret.add(q);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return toret;
    }

    @Override
    protected void onPostExecute(List<Quote> result) {
        super.onPostExecute(result);
        L.I(this, "Loaded Quotes: " + result.size());
        pd.dismiss();
        ctx.sendBroadcast(new Intent(INTENT_QUOTES_SYNC_DONE));
    }

    public void detach() {
        pd.dismiss();
        ctx = null;
    }

    public void attach(Context c) {
        ctx = c;
        if (getStatus() != AsyncTask.Status.FINISHED) {
            pd = createSyncProgressDialog(ctx);
            pd.show();
        }
    }

    private static ProgressDialog createSyncProgressDialog(Context ctx) {
        ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage(ctx.getText(R.string.sync_quotes));
        pd.setIndeterminate(true);
        return pd;
    }
}