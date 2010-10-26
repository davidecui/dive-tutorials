package org.whymca.dive;

import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//this object, declared in the manifest will receive notifications
//for state changes of the PHONE (call started, over etc)
public class DivePhoneProbe extends BroadcastReceiver {

	//remember, no more than 10 seconds...
	@Override
	public void onReceive(Context sender, Intent intent) {
		Log.i("PROBE", "........................");
		Log.i("PROBE", "Intent Received: "+intent);
		Iterator<String> it = intent.getExtras().keySet().iterator();
		while(it.hasNext()) {
			String value = it.next();
			Log.i("PROBE", "["+value+";"+intent.getExtras().get(value)+"]");
		}
		Log.i("PROBE", "........................");
	}
}
