package com.hazam.softwarequotes.util;

import com.hazam.util.L;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SDCardEventInterceptor extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		L.I(this, "Just Received Intent: "+intent);
	}

}
