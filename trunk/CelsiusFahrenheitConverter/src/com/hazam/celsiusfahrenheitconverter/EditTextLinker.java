package com.hazam.celsiusfahrenheitconverter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class EditTextLinker implements TextWatcher {
	private final EditText from;
	private final EditText to;

	public EditTextLinker(EditText from, EditText to) {
		super();
		this.from = from;
		this.to = to;
	}

	@Override
	public void afterTextChanged(Editable s) {
		try {
		if (from.isInputMethodTarget()) {
			link(from, to);
		}
		} catch (Throwable th) {
			to.setText("");
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	public abstract void link(EditText from, EditText to);
}
