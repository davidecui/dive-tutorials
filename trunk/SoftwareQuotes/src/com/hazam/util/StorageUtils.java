package com.hazam.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class StorageUtils {
	public static boolean hasWritableStorage(boolean requireWriteAccess) {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			if (requireWriteAccess) {
				boolean writable = checkFsWritable();
				return writable;
			}
		} if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			return true;
		}
		return false;
	}

	// Create a temporary file to see whether a volume is really writeable. It's important not to
	// put it in the root directory which may have a limit on the number of files.
	static private boolean checkFsWritable() {
		String directoryName = Environment.getExternalStorageDirectory().toString() + "/DCIM";
		File directory = new File(directoryName);
		if (!directory.isDirectory()) {
			if (!directory.mkdirs()) {
				return false;
			}
		}
		File f = new File(directoryName, ".probe");
		try {
			// Remove stale file if any
			if (f.exists()) {
				f.delete();
			}
			if (!f.createNewFile())
				return false;
			f.delete();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}
}
