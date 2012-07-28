package com.simpleblocker.ui.activities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.simpleblocker.R;

public class PrefsActivity extends PreferenceActivity implements OnClickListener {

	protected Method mLoadHeaders = null;
	protected Method mHasHeaders = null;

	/**
	 * Checks to see if using new v11+ way of handling PrefsFragments.
	 * 
	 * @return Returns false pre-v11, else checks to see if using headers.
	 */
	public boolean isNewV11Prefs() {
		if (mHasHeaders != null && mLoadHeaders != null) {
			try {
				return (Boolean) mHasHeaders.invoke(this);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
		return false;
	}

	@Override
	public void onCreate(Bundle aSavedState) {
		// onBuildHeaders() will be called during super.onCreate()
		try {
			mLoadHeaders = getClass().getMethod("loadHeadersFromResource", int.class, List.class);
			mHasHeaders = getClass().getMethod("hasHeaders");
		} catch (NoSuchMethodException e) {
		}
		super.onCreate(aSavedState);
		if (!isNewV11Prefs()) {
			//requestWindowFeature(Window.FEATURE_NO_TITLE);
			addPreferencesFromResource(R.xml.pref_service_settings);
			setContentView(R.layout.pref_header);
		}
	}

	@Override
	public void onBuildHeaders(List<Header> aTarget) {
		try {
			mLoadHeaders.invoke(this, new Object[] { R.xml.pref_service_settings_headers, aTarget });
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}

	static public class PrefsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle aSavedState) {
			super.onCreate(aSavedState);
			Context anAct = getActivity().getApplicationContext();
			int thePrefRes = anAct.getResources().getIdentifier(getArguments().getString("pref-resource"), "xml", anAct.getPackageName());
			addPreferencesFromResource(thePrefRes);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
