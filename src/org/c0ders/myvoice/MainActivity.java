package org.c0ders.myvoice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;	
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
	
	private static String TAG = "myvoice";
	
	private Button clearButton;
	private Button speakButton;
	private EditText text2speechInput;
	
	private TextToSpeech mTts;
	
	private Locale locale;
	private float pitch;
	private float speechRate;
	private boolean save;
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setSpeechRate(float speechRate){
		this.speechRate = speechRate;
	}
	
	public void setSave(boolean save){
		this.save = save;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.main);
		
		/**
		 * Load preferences
		 */
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		if("german".equals(prefs.getString("localesPref", "localeValues"))){
			this.setLocale(Locale.GERMAN);
		}else if("english".equals(prefs.getString("localesPref", "localeValues"))){
			this.setLocale(Locale.ENGLISH);
		} else {
			this.setLocale(Locale.getDefault());
		}
		
		this.setPitch(Float.valueOf(prefs.getString("pitchPref", "1")));
		this.setSpeechRate(Float.valueOf(prefs.getString("speechRatePref", "1")));
		this.setSave(prefs.getBoolean("savePref", false));
		
		
		this.clearButton = (Button)  this.findViewById(R.id.clearButton);
		this.speakButton = (Button)  this.findViewById(R.id.speakButton);
		this.text2speechInput = (EditText) this.findViewById(R.id.text2speechInput);
		
		mTts = new TextToSpeech(this, this);
		
		/**
		 * OnClickListener for 'clearButton'
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
				speak(text2speechInput.getText().toString());
				Log.i(TAG, "try to speak: "+text2speechInput.getText().toString());
			}
		});
    }

	/**
	 * send String to TTS
	 * 
	 * @param String whatsHappening 
	 */
	private void speak(String whatsHappening) {
		mTts.speak(whatsHappening, TextToSpeech.QUEUE_FLUSH, null);
		
		/**
		 * @TODO crap 
		 */
		if(true==this.save){
			this.saveToFile(whatsHappening);
		}
	}
	
	/**
	 * save to file - e.g. /sdcard/myvoice/yyyyMMdd-HHmmssSSS.wav
	 * 
	 * @param String whatsHappening 
	 */
	private void saveToFile(String whatsHappening){
		
		String extStorage = Environment.getExternalStorageDirectory().getPath();
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		
		
		File folder = new File(extStorage + "/myvoice");
		
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		String saveToFile = extStorage + "/myvoice/" + strDate + ".wav";

		int i = mTts.synthesizeToFile(whatsHappening, null, saveToFile);
	}
	
	/**
	 * Init TTS
	 * 
	 * @param i 
	 */
	public void onInit(int i) {
		if (i == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(this.locale);
			
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.w(TAG, "Language not supported: "+this.locale);
			}else{
				mTts.setPitch(this.pitch);
				Log.i(TAG, "set pitch to: "+this.pitch);
				
				mTts.setSpeechRate(this.speechRate);
				Log.i(TAG, "set speechRate to: "+this.speechRate);
			
			}
		} else {
			Log.w(TAG, "Could not init TextToSpeech :(");
		}
	}
	
	@Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
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
}
