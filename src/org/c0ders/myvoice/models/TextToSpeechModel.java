package org.c0ders.myvoice.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
		
		Log.i(TAG, "try to load TextToSpeech");
		Log.i(TAG, "Loclale: "+this.getLocale().toString());
		Log.i(TAG, "Pitch: "+this.getPitch());
		Log.i(TAG, "SpeechRate: "+this.getSpeechRate());
		Log.i(TAG, "save: "+this.isSave());
		
		this.mTts = new TextToSpeech(context, this);
	}
	
	/**
	 * 
	 * @param whatsHappening 
	 */
	public void speak(String whatsHappening){
		mTts.speak(whatsHappening, TextToSpeech.QUEUE_FLUSH, null);
		
		if(true==this.isSave()){
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

	@Override
	public void onInit(int i) {
		if (i == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(this.getLocale());
			
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.w(TAG, "Language not supported: "+this.locale);
			}else{
				mTts.setPitch(this.getPitch());
				mTts.setSpeechRate(this.getSpeechRate());
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