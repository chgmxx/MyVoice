package org.c0ders.myvoice;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 *
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
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/manuw/MyVoice"));
		} else if(R.id.gowitter == view.getId()){
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/manuw"));
		} else if(R.id.iconset == view.getId()){
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tiheum.deviantart.com/art/Faenza-Icons-173323228"));
		}
		
		startActivity(intent);
	}
}