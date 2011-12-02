package org.c0ders.myvoice.models;

import android.os.Environment;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
public class Utils {
	
	public static String dateFormat(String dateFormat){
		SimpleDateFormat sdfDate = new SimpleDateFormat(dateFormat);
		Date now = new Date();
		String strDate = sdfDate.format(now);

		return strDate;
	}
	
	public static String getStoragePath(){
		String extStorage = Environment.getExternalStorageDirectory().getPath();
		
		File folder = new File(extStorage + "/myvoice");
		
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		return extStorage + "/myvoice/";
	}
}
