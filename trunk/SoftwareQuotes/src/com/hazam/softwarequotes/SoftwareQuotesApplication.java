package com.hazam.softwarequotes;

import android.app.Application;
import android.util.Log;
import com.hazam.util.L;

public class SoftwareQuotesApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		L.setDefaultLogger("SoftwareQuotes", Log.VERBOSE);
	}
}
