package com.hazam.util;

import android.content.Intent;

public class IntentUtils {
	public static Intent intentForPrefilledSms(String tel, String text) {
		Intent toreturn = new Intent(Intent.ACTION_VIEW);
		toreturn.putExtra("address", tel);
		toreturn.putExtra("sms_body", text);
		toreturn.setType("vnd.android-dir/mms-sms");
		return toreturn;
	}
}
