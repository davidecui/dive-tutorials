package com.hazam.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	public static String readAll(InputStream is) {
		ByteArrayOutputStream stuff = new ByteArrayOutputStream(128);
		int c = 0;
		try {
			while ((c = is.read()) != -1) {
				stuff.write(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(is);
		}
		return stuff.toString();
	}

	public static void closeQuietly(InputStream is) {
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
