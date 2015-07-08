package com.amaze.QuizActivity;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amaze.constants.Constants;
import com.amaze.database.MyDatabase;
import com.amaze.QuizActivity.R;

public class FlashCard extends Activity implements OnClickListener,TextToSpeech.OnInitListener {

	MyDatabase mDb;
	Typeface typeface;
	Cursor cursor,cursor1,mergeCursor;
	int index;
	LinearLayout layoutMeaning,layoutSentence,wordLayout;
	TextView wordText,meaning,Sentence,wordTextRelative;
	Button next,showRelative,speakButton;
	public int MIN = 0;
	public int MAX = 320;
	Dialog STdialog;
	RelativeLayout buttonLayout;
	private TextToSpeech mTts;
	String question;
	private AudioManager audio;
	private int phoneVolume;
	private boolean hasTTS = true;
	int previous;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if(hasTTS)
		{
			audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			phoneVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
			audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);
		}
		typeface = Typeface.createFromAsset(getAssets(),"fonts/bankgthd.ttf");
		setContentView(R.layout.flashcard);
		TextView text = (TextView)findViewById(R.id.flashcard_text);
		text.setTypeface(typeface);
		TextView textUsage = (TextView)findViewById(R.id.usage_text);textUsage.setTextColor(Color.MAGENTA);
		TextView textMeaning = (TextView)findViewById(R.id.meaning_text);textMeaning.setTextColor(Color.MAGENTA);
		
		////////////
		Intent intent = new Intent();
		intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(intent, 0);		
		//////////////
		mDb = MyDatabase.getInstance(getApplicationContext());
		index = 0;
		cursor = mDb.getAll(Constants.TABLE_ANTONYM);
		cursor1 = mDb.getAll(Constants.TABLE_SYNONYM);
		Cursor [] m = new Cursor[2];
		m[0] = cursor;
		m[1] = cursor1;
		mergeCursor = new MergeCursor(m);
		mergeCursor.moveToFirst();
		mergeCursor.moveToPosition(generateRandom());

		layoutMeaning = (LinearLayout)findViewById(R.id.layout_meaning);
		layoutSentence = (LinearLayout)findViewById(R.id.layout_sentence);
		buttonLayout = (RelativeLayout)findViewById(R.id.button_layout);
		wordLayout = (LinearLayout)findViewById(R.id.realtivelayout);
		
		wordTextRelative = (TextView)findViewById(R.id.word_text_relative);
		wordText = (TextView)findViewById(R.id.word_text);
		meaning = (TextView)findViewById(R.id.flash_meaning);
		Sentence = (TextView)findViewById(R.id.flash_sentence);
		next = (Button)findViewById(R.id.Next_Button_flash);
		showRelative = (Button)findViewById(R.id.button_show_relative);
		speakButton = (Button)findViewById(R.id.speak_text);
		mTts = new TextToSpeech(this,this);
		showRelative.setTextColor(Color.BLUE);
		next.setTypeface(typeface);		
		next.setOnClickListener(this);
		showRelative.setOnClickListener(this);
		speakButton.setOnClickListener(this);
		int height = getDeviceHeight();
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0,(int)(height*0.28),0,0);
		lp.gravity = Gravity.CENTER;
		wordLayout.setLayoutParams(lp);
		wordTextRelative.setText(mergeCursor.getString(mergeCursor.getColumnIndex(Constants.QUESTION_COLUMN)));
		wordText.setText(mergeCursor.getString(mergeCursor.getColumnIndex(Constants.QUESTION_COLUMN)));
		question = mergeCursor.getString(mergeCursor.getColumnIndex(Constants.QUESTION_COLUMN));

	}

	public void updateViewOnStart()
	{
		
		wordLayout.setVisibility(View.VISIBLE);
		wordText.setVisibility(View.GONE);
		layoutMeaning.setVisibility(View.GONE);
		layoutSentence.setVisibility(View.GONE);
		buttonLayout.setVisibility(View.GONE);
		next.setVisibility(View.GONE);
		speakButton.setVisibility(View.GONE);
	}

	public void updateViewOnShowClick()
	{
		wordLayout.setVisibility(View.GONE);
		wordText.setVisibility(View.VISIBLE);
		layoutMeaning.setVisibility(View.VISIBLE);
		layoutSentence.setVisibility(View.VISIBLE);
		buttonLayout.setVisibility(View.VISIBLE);
		next.setVisibility(View.VISIBLE);	
		speakButton.setVisibility(View.VISIBLE);
		if(hasTTS)
		{
			speakButton.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		
		case R.id.Next_Button_flash:
			mergeCursor.moveToPosition(generateRandom());
			while(wordText.getText().equals(mergeCursor.getString(mergeCursor.getColumnIndex(Constants.QUESTION_COLUMN))))
			{
				mergeCursor.moveToPosition(generateRandom());
			}
			mTts.stop();
			wordTextRelative.setText(mergeCursor.getString(mergeCursor.getColumnIndex(Constants.QUESTION_COLUMN)));
			wordText.setText(mergeCursor.getString(mergeCursor.getColumnIndex(Constants.QUESTION_COLUMN)));
			question = mergeCursor.getString(mergeCursor.getColumnIndex(Constants.QUESTION_COLUMN));
			updateViewOnStart();
			break;
		case R.id.button_show_relative:
			updateViewOnShowClick();
			meaning.setText(mergeCursor.getString(mergeCursor.getColumnIndex(Constants.ANSWER_DESCRIPTION)).trim());
			Sentence.setText(mergeCursor.getString(mergeCursor.getColumnIndex(Constants.ANSWER_SENTENCE)).trim());
			break;
		case R.id.button_yes:
			Intent intent = new Intent(FlashCard.this,StartPage.class);
			startActivity(intent);
			FlashCard.this.finish();
			break;
		case R.id.speak_text:
			say(question);
			break;
		case R.id.button_no:
			STdialog.dismiss();
			break;
		default:
			break;
		}

	}
	public int getDeviceHeight()
	{
		Display display = getWindowManager().getDefaultDisplay(); 
		int height = display.getHeight(); 
		return height;
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		Dialog tmpdialog = null;
		switch (id) {
		case 1:
			STdialog = new Dialog(FlashCard.this,android.R.style.Theme_Translucent);
			STdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			STdialog.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			STdialog.setContentView(R.layout.popupexit);
			TextView text = (TextView)STdialog.findViewById(R.id.Dialog_heading);
			text.setText("Quit Flash Card?");
			STdialog.setOnDismissListener(new OnDismissListener()
			{
				public void onDismiss(DialogInterface dialog)
				{
					FlashCard.this.removeDialog(1);
					STdialog.dismiss();
				}
			});
			Button button_yes = (Button)STdialog.findViewById(R.id.button_yes);
			button_yes.setOnClickListener(this);
			Button button_no = (Button)STdialog.findViewById(R.id.button_no);
			button_no.setOnClickListener(this);
			tmpdialog = STdialog;
			break;

		default:
			break;
		}
		return tmpdialog;
	}

	@SuppressLint("NewApi")
	public int generateRandom()
	{
		int random = (int)(Math.random()*(MAX - MIN));
		Log.d("", "Ashish: the previous is "+previous+" the random is "+random);
		/*while(Integer.compare(random, previous) == 0)
		{
			random = (int)(Math.random()*(MAX - MIN));
		}*/
		previous = random;
		return random;
	}
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		//super.onBackPressed();
		showDialog(1);
	}
	 @Override
	    public void onDestroy() {
		 if(hasTTS)
		 {
			 audio.setStreamVolume(AudioManager.STREAM_MUSIC, phoneVolume,AudioManager.FLAG_SHOW_UI);
		 }
	        // Don't forget to shutdown!
	        if (mTts != null) {
	            mTts.stop();
	            mTts.shutdown();
	        }

	        super.onDestroy();
	    }
	// Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
            } else {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.

                // The TTS engine has been successfully initialized.
                // Allow the user to press the button for the app to speak again.
                speakButton.setEnabled(true);
                // Greet the user.
            }
        } else {
            // Initialization failed.
            Log.e("", "Could not initialize TextToSpeech.");
        }
    }
    private void say(String question) {
        mTts.speak(question,
            TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
            null);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == 0){
    		if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
    			//Toast.makeText(getApplicationContext(),"Already Installed", Toast.LENGTH_LONG).show();
    			hasTTS = false;
    		} else {
    			/*Intent installIntent = new Intent();
    			installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
    			startActivity(installIntent);*/
    			hasTTS = true;
    			//Toast.makeText(getApplicationContext(),"Installed Now", Toast.LENGTH_LONG).show();
    		}
    	}
    }

}
