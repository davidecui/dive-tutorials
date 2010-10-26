package com.hazam.ui;

import android.app.ListActivity;
import android.os.Bundle;
import com.hazam.util.L;

public class LifeCycleLoggingListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
