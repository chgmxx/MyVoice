package org.c0ders.myvoice;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
public class AboutActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.about);
	}
	
	/**
	 * call intents
	 * 
	 * @param View view
	 */
	public void callIntent(View view){
		Intent intent = null;
		
		if(R.id.callgithub == view.getId()){
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.getString(R.string.github)));
		} else if(R.id.gowitter == view.getId()){
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.getString(R.string.twitter)));
		} else if(R.id.iconset == view.getId()){
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.getString(R.string.faenza)));
		}
		
		startActivity(intent);
	}
}