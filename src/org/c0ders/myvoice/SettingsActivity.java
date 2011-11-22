package org.c0ders.myvoice;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Setttings
 * @author Manuel Wildauer <m.wildauer@intellishop.ag>
 */
public class SettingsActivity extends PreferenceActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);	
		addPreferencesFromResource(R.xml.preferences);
		PreferenceManager.setDefaultValues(SettingsActivity.this, R.xml.preferences, false);
	}
}
