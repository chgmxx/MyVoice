package org.c0ders.myvoice.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Calendar;
import java.util.Locale;

/**
 *
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
final public class TextToSpeechModel implements TextToSpeech.OnInitListener {
	
	private static String TAG = "myvoice TextToSpeechModel";
	
	private TextToSpeech mTts = null;
	private SharedPreferences prefs = null;
	
	private Locale locale;
	private float pitch;
	private float speechRate;
	private boolean save;

	public static String getTAG() {
		return TAG;
	}

	public static void setTAG(String TAG) {
		TextToSpeechModel.TAG = TAG;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public float getSpeechRate() {
		return speechRate;
	}

	public void setSpeechRate(float speechRate) {
		this.speechRate = speechRate;
	}
	
	public TextToSpeechModel(Context context) {
		this.prefs = PreferenceManager.getDefaultSharedPreferences(context);		
		
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
		
		this.mTts = new TextToSpeech(context, this);
	}
	
	/**
	 * 
	 * @param whatsHappening 
	 */
	public void speak(String whatsHappening){
		mTts.speak(whatsHappening, TextToSpeech.QUEUE_FLUSH, null);
		
		if(true==this.isSave()){
			this.save(whatsHappening);
		}
	}
	
	/**
	 * 
	 * @param whatsHappening 
	 */
	public void save(String whatsHappening){
		String storagePath = Environment.getExternalStorageDirectory().getPath();
		Calendar rightNow = Calendar.getInstance();

		long timestamp = rightNow.getTimeInMillis() / 1000;
		String saveToFile = storagePath + "/" + timestamp + ".wav";

		int i = mTts.synthesizeToFile(whatsHappening, null, saveToFile);
	}

	@Override
	public void onInit(int i) {
		if (i == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(this.getLocale());
			
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.w(TAG, "Language not supported: "+this.locale);
			}else{
				mTts.setPitch(this.getPitch());
				Log.i(TAG, "set pitch to: "+this.pitch);
				
				mTts.setSpeechRate(this.getSpeechRate());
				Log.i(TAG, "set speechRate to: "+this.speechRate);
			}
		} else {
			Log.w(TAG, "Could not init TextToSpeech :(");
		}
	}
	
	public void destroy(){
		if(null!=this.mTts){
			this.mTts.stop();
			mTts.shutdown();	
		}
	}
}