package com.rishabh.eventplanner;

import com.leocardz.aelv.R;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

public class Prefs extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		EditTextPreference timeStamp = (EditTextPreference) findPreference("timestamp");
		EditTextPreference alarmTone = (EditTextPreference) findPreference("alarmtone");
		PreferenceCategory mCategory = (PreferenceCategory) findPreference("timestampparent");
		mCategory.removePreference(timeStamp);
		mCategory.removePreference(alarmTone);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	

}
