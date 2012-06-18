package com.smsspeaker;

import java.util.List;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;

/**
 * Extend Application for global state information for an application. Access
 * the application via Activity.getApplication().
 * 
 * There are several ways to store global state information, this is one of
 * them. Another is to create a class with static members and just access it
 * from Activities.
 * 
 * Either approach works, and there is debate about which is better. Either way,
 * make sure to clean up in life-cycle pause or destroy methods if you use
 * resources that need cleaning up (static maps, etc).
 * 
 * @author MEKOH
 * 
 */
public final class SmsSpeakerApplication extends Application {

	//private static final String CLASS_TAG = SmsSpeakerApplication.class.getSimpleName();

	private List<Locale> availableLocales = null;
	private TextToSpeech tts = null;

	private static Context context = null;

	/**
	 * Constructor
	 */
	public SmsSpeakerApplication() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (context == null)
			context = this.getApplicationContext();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public List<Locale> getAvailableLocales() {
		return availableLocales;
	}

	public void setAvailableLocales(final List<Locale> locales) {
		this.availableLocales = locales;
	}

	public TextToSpeech getTTS() {
		return tts;
	}

	public void setTTS(final TextToSpeech tts) {
		this.tts = tts;
	}

	public static Context getContext() {
		return context;
	}

}
