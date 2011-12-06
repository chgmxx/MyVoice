package org.c0ders.myvoice.models;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.io.File;
import java.util.Locale;

/**
 *
 * @author Manuel Wildauer <m.wildauer@gmail.com>
 */
final public class TextToSpeechModel implements TextToSpeech.OnInitListener {
	
	private static String TAG = "myvoice TextToSpeechModel";
	
	private Context context;
	
	private TextToSpeech mTts = null;
	private SharedPreferences prefs = null;
	
	private Locale locale;
	private float pitch;
	private float speechRate;
	private boolean save;
	private boolean share;
	
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

	public boolean isShare() {
		return share;
	}

	public void setShare(boolean share) {
		this.share = share;
	}

	public float getSpeechRate() {
		return speechRate;
	}

	public void setSpeechRate(float speechRate) {
		this.speechRate = speechRate;
	}
	
	public TextToSpeechModel(Context context) {
		
		this.context = context;
		
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
		this.setShare(prefs.getBoolean("sharePref", false));
		
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
		
		String strDate = Utils.dateFormat("yyyyMMdd-HHmmssSSS");
		
		final String saveToFile = Utils.getStoragePath() + strDate + ".wav";
		
		int i = mTts.synthesizeToFile(whatsHappening, null, saveToFile);
		
		if(TextToSpeech.SUCCESS == i){
			if(true==this.isSave() && this.isShare()){
				this.shareDialog(saveToFile);
			}
		}
	}
	
	/**
	 * 
	 * @param String saveToFile 
	 */
	private void shareDialog(final String saveToFile){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						shareFile(saveToFile);
					break;

					case DialogInterface.BUTTON_NEGATIVE:

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		builder.setMessage("Share").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

	}
	
	/**
	 * Share wav-File
	 * 
	 * @param String filename 
	 */
	private void shareFile(String filename){
		
		File file = new File(filename);
		
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("audio/wav");
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		
		this.context.startActivity(Intent.createChooser(shareIntent, "Share Voice File"));		
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