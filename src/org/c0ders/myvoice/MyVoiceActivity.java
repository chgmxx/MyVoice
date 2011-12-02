package org.c0ders.myvoice;

import org.c0ders.myvoice.models.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;	
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MyVoiceActivity extends Activity implements OnSharedPreferenceChangeListener {
	
	private static String TAG = "myvoice";
	
	private Button clearButton;
	private Button speakButton;
	private EditText text2speechInput;
	
	private TextToSpeechModel ttsm;

	SharedPreferences prefs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.main);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
			
		
		this.clearButton = (Button)  this.findViewById(R.id.clearButton);
		this.speakButton = (Button)  this.findViewById(R.id.speakButton);
		this.text2speechInput = (EditText) this.findViewById(R.id.text2speechInput);
		
		this.ttsm = new TextToSpeechModel(this);
		
		/**
		 * OnClickListener for clearButton
		 */
		this.clearButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				EditText text2speechInput = (EditText) findViewById(R.id.text2speechInput);
				text2speechInput.setText("");
			}
		});
		
		/**
		 * OnClickListener for 'speakButton'
		 */
		this.speakButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				EditText text2speechInput = (EditText) findViewById(R.id.text2speechInput);
				ttsm.speak(text2speechInput.getText().toString());
				Log.i(TAG, "try to speak: "+text2speechInput.getText().toString());
			}
		});
    }

	@Override
	public void onDestroy() {
		this.ttsm.destroy();
		super.onDestroy();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(Menu.NONE, 0, 0, "Settings");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(0==item.getItemId()){
			startActivity(new Intent(getBaseContext(), SettingsActivity.class));
			return true;
		}
		return false;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		this.ttsm = new TextToSpeechModel(this);
	}
}
