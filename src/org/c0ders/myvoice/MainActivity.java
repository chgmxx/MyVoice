package org.c0ders.myvoice;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;	
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
	
	private static String TAG = "myvoice";
	
	private Button clearButton;
	private Button speakButton;
	private EditText text2speechInput;
	private Spinner locales;
	
	private TextToSpeech mTts;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		this.clearButton = (Button)  this.findViewById(R.id.clearButton);
		this.speakButton = (Button)  this.findViewById(R.id.speakButton);
		this.text2speechInput = (EditText) this.findViewById(R.id.text2speechInput);
		
		/*this.locales = (Spinner) this.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.locales_array, android.R.layout.simple_spinner_item
		);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.locales.setAdapter(adapter);*/
		
		
		mTts = new TextToSpeech(this,this);
		
		/**
		 * OnClickListener for 'clearButton'
		 */
		this.clearButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				EditText text2speechInput = (EditText) findViewById(R.id.text2speechInput);
				text2speechInput.setText("");
				Log.i(TAG, "clear text2speechEditBox");
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

	private void speak(String whatsHappening) {
		mTts.speak(whatsHappening, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public void onInit(int i) {
		if (i == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.getDefault());
			
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.w(TAG, "Language not supported: "+Locale.getDefault());
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
}
