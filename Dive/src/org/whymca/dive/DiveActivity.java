package org.whymca.dive;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;

import org.whymca.dive.model.Dive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DiveActivity extends Activity {
	private static final int FIN_SITE_DIALOG = 12;
	private DiveAdapter adapter = null;

	//creating a fake Dive object with random values
	private static Dive createFakeDive() {
		Random r = new Random( System.currentTimeMillis() );
		DecimalFormat df = new DecimalFormat("#.#");
		r.nextFloat();
		float coeff = r.nextFloat() * 3.0f + 1.0f;
		float mark = r.nextFloat() * 5.0f + 5.0f;
		try {
			coeff = df.parse(df.format(coeff)).floatValue();
			mark = df.parse(df.format(mark)).floatValue();
			return new Dive(coeff, mark);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		adapter = new DiveAdapter(this);
		for (int i = 0; i < 15; i++) {
			adapter.add(createFakeDive());
		}

		ListView list = (ListView) findViewById(R.id.dive_list);
		list.setAdapter(adapter);
		//on click on a Item, open the FIN site in browser
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View clickedView, int position, long id) {
				showDialog(FIN_SITE_DIALOG);
			}
		});
	}

	//OnCreate dialog is the method that should create a Dialog with an ID
	//if the dialog inb requested again, i will be reused (check OnPrepareDialog instad)
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == FIN_SITE_DIALOG) {
			//AlertDialog is built leveraging a Builder object
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setMessage("Do you want to see the dive ehm whatever?");
			b.setNegativeButton("Not Really", null);
			b.setPositiveButton("Yes sure!", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent see = new Intent(Intent.ACTION_VIEW);
					see.setData(Uri.parse("http://www.federnuoto.it/tuffi.asp"));
					DiveActivity.this.startActivity(see);
				}
			});
			b.setCancelable(false);
			return b.create();
		} else {
			return super.onCreateDialog(id);
		}
	}
	

	/** SAMPLE CODE for UPDATING THE UI CORRECTLY **/
	static final int UPDATE_UI = 12;
	private TextView textView = null;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UPDATE_UI:
				textView.setText("Now I can update my UI!");
				break;
			}
		}
	};
	
	Runnable task = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (Exception e) {}
			//do various async stuff...
			Message tosend = handler.obtainMessage(UPDATE_UI);
			handler.sendMessage(tosend);
		}
	};
	/** END SAMPLE CODE **/
}
