package com.mymobkit.ui.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.mymobkit.R;
import com.mymobkit.app.MyMobKitApp;
import com.mymobkit.config.AppConfig;
import com.mymobkit.preference.SeekBarDialogPreference;
import com.mymobkit.ui.base.PreferenceListFragment;
import com.mymobkit.ui.base.PreferenceListFragment.OnPreferenceAttachedListener;

public class CameraSettingsFragment extends PreferenceListFragment implements OnSharedPreferenceChangeListener, OnPreferenceAttachedListener {

	private static final String TAG = AppConfig.LOG_TAG_APP + ":CameraSettingsFragment";

	public static final String KEY_VIDEO_STREAMING_PORT = "preferences_video_streaming_port";
	public static final String KEY_MOTION_DETECTION = "preferences_motion_dection";
	public static final String KEY_VIDEO_STREAMING_IMAGE_QUALITY = "preferences_video_streaming_image_quality";
	public static final String KEY_MOTION_DETECTION_THRESHOLD = "preferences_motion_detection_threshold";
	public static final String KEY_FACE_DETECTION = "preferences_face_detection";
	public static final String KEY_FACE_DETECTION_SIZE = "preferences_face_detection_size";
	public static final String KEY_MOTION_DETECTION_ALGORITHM = "preferences_motion_detection_algorithm";
	public static final String KEY_MOTION_DETECTION_CONTOUR_THICKNESS = "preferences_motion_detection_contour_thickness";
	public static final String KEY_STREAM_DETECTED_OBJECT = "preferences_stream_detected_object";

	public static final String SHARED_PREFS_NAME = "camera_settings";

	private EditTextPreference streamingPort;
	private SeekBarDialogPreference streamingImageQuality;
	private SeekBarDialogPreference motionDetectionThreshold;
	private CheckBoxPreference motionDetect;
	private CheckBoxPreference faceDetect;
	private ListPreference faceDetectionSize;
	private ListPreference motionDetectionAlgorithm;
	private SeekBarDialogPreference motionDetectionContourThickness;
	private CheckBoxPreference streamDetectedObject;

	public static CameraSettingsFragment newInstance() {
		return new CameraSettingsFragment();
	}

	public CameraSettingsFragment() {
		super(R.xml.pref_camera_settings);
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		PreferenceManager preferenceManager = getPreferenceManager();
		preferenceManager.setSharedPreferencesName(SHARED_PREFS_NAME);

		PreferenceScreen preferences = getPreferenceScreen();
		streamingPort = (EditTextPreference) preferences.findPreference(KEY_VIDEO_STREAMING_PORT);
		streamingImageQuality = (SeekBarDialogPreference) preferences.findPreference(KEY_VIDEO_STREAMING_IMAGE_QUALITY);
		motionDetect = (CheckBoxPreference) preferences.findPreference(KEY_MOTION_DETECTION);
		motionDetectionThreshold = (SeekBarDialogPreference) preferences.findPreference(KEY_MOTION_DETECTION_THRESHOLD);
		faceDetect = (CheckBoxPreference) preferences.findPreference(KEY_FACE_DETECTION);
		faceDetectionSize = (ListPreference) preferences.findPreference(KEY_FACE_DETECTION_SIZE);
		motionDetectionAlgorithm = (ListPreference) preferences.findPreference(KEY_MOTION_DETECTION_ALGORITHM);
		motionDetectionContourThickness = (SeekBarDialogPreference) preferences.findPreference(KEY_MOTION_DETECTION_CONTOUR_THICKNESS);
		streamDetectedObject = (CheckBoxPreference) preferences.findPreference(KEY_STREAM_DETECTED_OBJECT);

		streamingPort.setText(preferenceManager.getSharedPreferences().getString(KEY_VIDEO_STREAMING_PORT, getString(R.string.default_video_streaming_port)));
		streamingImageQuality.setProgress(preferenceManager.getSharedPreferences().getInt(KEY_VIDEO_STREAMING_IMAGE_QUALITY, Integer.valueOf(getString(R.string.default_video_streaming_image_quality))));
		motionDetect.setChecked(preferenceManager.getSharedPreferences().getBoolean(KEY_MOTION_DETECTION, Boolean.valueOf(this.getString(R.string.default_motion_detection))));
		motionDetectionThreshold.setProgress(preferenceManager.getSharedPreferences().getInt(KEY_MOTION_DETECTION_THRESHOLD, Integer.valueOf(getString(R.string.default_motion_detection_threshold))));
		faceDetect.setChecked(preferenceManager.getSharedPreferences().getBoolean(KEY_FACE_DETECTION, Boolean.valueOf(this.getString(R.string.default_face_detection))));
		faceDetectionSize.setValue(preferenceManager.getSharedPreferences().getString(KEY_FACE_DETECTION_SIZE, getString(R.string.default_face_detection_size)));
		motionDetectionAlgorithm.setValue(preferenceManager.getSharedPreferences().getString(KEY_MOTION_DETECTION_ALGORITHM, getString(R.string.default_motion_detection_algorithm)));
		motionDetectionContourThickness.setProgress(preferenceManager.getSharedPreferences().getInt(KEY_MOTION_DETECTION_CONTOUR_THICKNESS, Integer.valueOf(getString(R.string.default_motion_detection_contour_thickness))));
		streamDetectedObject.setChecked(preferenceManager.getSharedPreferences().getBoolean(KEY_STREAM_DETECTED_OBJECT, Boolean.valueOf(this.getString(R.string.default_stream_detected_object))));

		PreferenceManager.setDefaultValues(MyMobKitApp.getContext(), R.xml.pref_camera_settings, false);
		for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
			initSummary(getPreferenceScreen().getPreference(i));
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.i(TAG, "[onSharedPreferenceChanged] Key - " + key);
		Preference pref = findPreference(key);
		updatePrefSummary(pref);
	}

	/*
	 * private void validateMinMax(final EditTextPreference pref, final int min,
	 * final int max) { int value = (TextUtils.isEmpty(pref.getText()) ? min :
	 * Integer.parseInt(pref.getText())); if (value < min) {
	 * pref.setText(String.valueOf(min)); } if (value > max) {
	 * pref.setText(String.valueOf(max)); } }
	 */
	@Override
	public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		if (root == null)
			return;
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister listener
		getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Register listener
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

	}

	private void initSummary(Preference p) {
		if (p instanceof PreferenceCategory) {
			PreferenceCategory pCat = (PreferenceCategory) p;
			for (int i = 0; i < pCat.getPreferenceCount(); i++) {
				initSummary(pCat.getPreference(i));
			}
		} else {
			updatePrefSummary(p);
		}
	}

	private void updatePrefSummary(Preference p) {
		if (p instanceof ListPreference) {
			ListPreference listPref = (ListPreference) p;
			p.setSummary(listPref.getEntry() == null ? "" : String.valueOf(listPref.getEntry()));
		} else if (p instanceof EditTextPreference) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			p.setSummary(editTextPref.getText());
		} else if (p instanceof SeekBarDialogPreference) {
			SeekBarDialogPreference seekBar = (SeekBarDialogPreference) p;
			seekBar.setSummary(String.valueOf(seekBar.getProgress()));
		}
	}

}