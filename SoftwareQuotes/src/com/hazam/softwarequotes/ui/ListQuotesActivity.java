package com.hazam.softwarequotes.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.hazam.softwarequotes.R;
import com.hazam.softwarequotes.model.ListQuotesAdapter;
import com.hazam.softwarequotes.model.Quote;
import com.hazam.softwarequotes.model.QuotesDAO;
import com.hazam.softwarequotes.sync.QuotesLoader;
import com.hazam.softwarequotes.sync.QuotesSyncService;
import com.hazam.ui.LifeCycleLoggingListActivity;
import com.hazam.util.L;

import java.util.List;

public class ListQuotesActivity extends LifeCycleLoggingListActivity implements QuotesDAO.QuotesDAOListener {

	private final static int DIALOG_SYNCING = 0xA;
	private final static int DIALOG_SYNC_ERROR = 0xB;
	private ListQuotesAdapter adapter;
	
	private final BroadcastReceiver receiverForSync = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			dao.getAllQuotes();
			dismissDialog(DIALOG_SYNCING);
		}
	};

	private QuotesDAO dao;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new QuotesDAO(getContentResolver(), this);
		handler = new Handler();
		setContentView(R.layout.list_quotes);
		Object last = getLastNonConfigurationInstance();
		if (last != null) {
			adapter = (ListQuotesAdapter) last;
		} else {
			adapter = new ListQuotesAdapter();
			dao.getAllQuotes();
		}
		setListAdapter(adapter);
		IntentFilter filter = new IntentFilter();
		filter.addAction(QuotesLoader.INTENT_QUOTES_SYNC_DONE);
		registerReceiver(receiverForSync, filter);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapter;
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiverForSync);
		super.onDestroy();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Object clicked = adapter.getItem(position);
		Intent tolaunch = new Intent(this, ShowQuoteActivity.class);
		tolaunch.putExtra(ShowQuoteActivity.EXTRA_QUOTE_ID, ((Quote) clicked).getId());
		startActivity(tolaunch);
	}

	public void onQuotesLoaded(List<Quote> target) {
		adapter.addAll(target);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.quotes_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sync:
			showDialog(DIALOG_SYNCING);
			Intent syncLauncher = new Intent(this, QuotesSyncService.class);
			syncLauncher.putExtra(QuotesSyncService.EXTRA_RETURN_CHANNEL, new ResultReceiver(handler) {
				@Override
				protected void onReceiveResult(int resultCode, Bundle resultData) {
					L.D(this, "received mex on return channel " + resultCode);
					if (resultCode == QuotesSyncService.RESULT_ERROR) {
						dismissDialog(DIALOG_SYNCING);
						showDialog(DIALOG_SYNC_ERROR);
					}
					// we could add a lot more error codes
				}
			});
			startService(syncLauncher);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SYNCING:
			ProgressDialog pd = new ProgressDialog(this);
			pd.setMessage(getText(R.string.sync_quotes));
			pd.setIndeterminate(true);
			return pd;
		case DIALOG_SYNC_ERROR:
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle(R.string.sync_error);
			//build.setMessage(b.getString(QuotesSyncService.EXTRA_ERROR_CAUSE));
			return build.create();
		default:
			return super.onCreateDialog(id);
		}
	}
}