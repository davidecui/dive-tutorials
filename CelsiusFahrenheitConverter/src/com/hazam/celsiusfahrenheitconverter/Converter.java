package com.hazam.celsiusfahrenheitconverter;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class Converter extends Activity {
	private static final String TAG = "Converter";
	private EditText tvCelsius = null;
	private EditText tvFahrenheit = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tvCelsius = (EditText) findViewById(R.id.celsius);
		tvFahrenheit = (EditText) findViewById(R.id.fahrenheit);
		tvCelsius.addTextChangedListener(new EditTextLinker(tvCelsius, tvFahrenheit) {

			@Override
			public void link(EditText from, EditText to) {
				float fromV = Float.parseFloat(from.getText().toString());
				float toV = ((fromV + 40) * 1.8f) - 40;
				to.setText(Float.toString(toV));
			}
		});
		tvFahrenheit.addTextChangedListener(new EditTextLinker(tvFahrenheit, tvCelsius) {

			@Override
			public void link(EditText from, EditText to) {
				float fromV = Float.parseFloat(from.getText().toString());
				float toV = ((fromV + 40) / 1.8f) - 40;
				to.setText(Float.toString(toV));
			}
		});
	}
}