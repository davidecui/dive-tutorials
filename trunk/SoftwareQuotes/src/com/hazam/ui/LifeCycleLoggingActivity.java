package com.hazam.ui;

import com.hazam.util.L;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LifeCycleLoggingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.i("MYACT","OnCreate()");
		L.V(this, "onCreate()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		L.V(this, "onRestart()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		L.V(this, "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		L.V(this, "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		L.V(this, "onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		L.V(this, "onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		L.V(this, "onDestroy()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		L.V(this, "onSaveInstanceState()");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		L.V(this, "onRestoreInstanceState()");
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		L.V(this, "onRetainNonConfigurationInstance()");
		return super.onRetainNonConfigurationInstance();
	}
}
