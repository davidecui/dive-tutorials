package com.hazam.concurrenttest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class ConcurrentTest extends Activity {
	protected static final int MSG_DONE = 0xFAFA;
	private TextView status = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		status = (TextView) findViewById(R.id.status);
	}

	public void asyncTask(View v) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			
			@Override
			protected void onPreExecute() {
				started();
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				longRunningThing();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				done();
			}
		};
		task.execute();
	}

	public void threadHandler(View v) {
		started();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MSG_DONE) {
					done();
				} else {
					super.handleMessage(msg);
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				longRunningThing();
				Message doneMsg = handler.obtainMessage(MSG_DONE);
				handler.sendMessage(doneMsg);
			}

		}.start();
	}

	public void wrongThread(View v) {
		started();
		new Thread() {
			@Override
			public void run() {
				longRunningThing();
				done();
			}

		}.start();
	}

	public void veryWrong(View v) {
		started();
		longRunningThing();
		done();
	}

	private void longRunningThing() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void done() {
		status.setText("DONE");
	}
	private void started() {
		status.setText("COMPUTING");
	}
}