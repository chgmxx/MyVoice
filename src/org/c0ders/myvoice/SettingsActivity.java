package org.c0ders.myvoice;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	private static String TAG = "myvoice settings";
	
	OnSharedPreferenceChangeListener listener;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);	
		addPreferencesFromResource(R.xml.preferences);
		
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				int flag=1;
			}
		};
	}
}