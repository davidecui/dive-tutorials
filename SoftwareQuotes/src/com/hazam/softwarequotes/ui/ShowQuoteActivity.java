package com.hazam.softwarequotes.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hazam.softwarequotes.R;
import com.hazam.softwarequotes.model.Quote;
import com.hazam.softwarequotes.model.QuotesDAO;
import com.hazam.softwarequotes.sync.QuotesLoader;
import com.hazam.ui.LifeCycleLoggingActivity;
import com.hazam.util.IntentUtils;
import com.hazam.util.L;

public class ShowQuoteActivity extends LifeCycleLoggingActivity implements QuotesDAO.QuotesDAOListener {
	public static final String EXTRA_QUOTE_ID = "extra_quote_id";

	private static final int DIALOG_NO_QUOTE = 0x2;

	private static final int REQUEST_PICK_CONTACT = 0;
	private TextView tvBody = null;
	private TextView tvAuthor = null;
	private String targetQuoteId = null;
	private QuotesDAO dao;

	private final BroadcastReceiver receiverForSync = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			dao.getQuote(targetQuoteId);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupView();
		Intent caller = getIntent();
		targetQuoteId = caller.getStringExtra(EXTRA_QUOTE_ID);
		dao = new QuotesDAO(getContentResolver(), this);
		dao.getQuote(targetQuoteId);
		registerReceiver(receiverForSync, new IntentFilter(QuotesLoader.INTENT_QUOTES_SYNC_DONE));
	}

	private void setupView() {
		setContentView(R.layout.show_quote);
		tvBody = (TextView) findViewById(R.id.body);
		tvBody.setMovementMethod(ScrollingMovementMethod.getInstance());
		tvAuthor = (TextView) findViewById(R.id.author);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_NO_QUOTE:
			AlertDialog.Builder noq = new AlertDialog.Builder(this);
			noq.setMessage(R.string.no_quote);
			noq.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			noq.setOnCancelListener(new OnCancelListener() {

				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			return noq.create();
		}
		return super.onCreateDialog(id);
	}

	public void onQuotesLoaded(List<Quote> target) {
		if (target == null || target.isEmpty()) {
			// show not found
			showDialog(DIALOG_NO_QUOTE);
		} else {
			Quote q = target.get(0);
			tvBody.setText(q.getBody());
			tvAuthor.setText(q.getAuthor());
		}
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(receiverForSync);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.show_quote_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.share:
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
			startActivityForResult(intent, REQUEST_PICK_CONTACT);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void onShareClick(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		startActivityForResult(intent, REQUEST_PICK_CONTACT);
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			Uri phoneData = data.getData();
			String id = phoneData.getLastPathSegment();
			String tel = queryForNumber(id);
			startActivity(IntentUtils.intentForPrefilledSms(tel, createSmsText()));
		}
	}

	private String queryForNumber(String phoneId) {
		Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
				ContactsContract.CommonDataKinds.Phone._ID + " = ?", new String[] { phoneId }, null);
		if (pCur.moveToFirst()) {
			String toreturn = pCur.getString(0);
			pCur.close();
			return toreturn;
		} else {
			return "";
		}
	}

	private String createSmsText() {
		StringBuilder sb = new StringBuilder("\"" + tvBody.getText().toString() + "\"");
		sb.append("  -" + tvAuthor.getText().toString());
		return sb.toString();
	}
}