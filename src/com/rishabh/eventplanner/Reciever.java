package com.rishabh.eventplanner;

import com.leocardz.aelv.R;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

public class Reciever extends BroadcastReceiver {
	public static Context c;
	public static SharedPreferences getPrefs;

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.v("Reciever", "Connectivity change was received");
		c = arg0;
		MainActivity.is = arg0.getResources().openRawResource(R.raw.keyfile);
		getPrefs = PreferenceManager.getDefaultSharedPreferences(arg0);
		MainActivity.timeStamp = getPrefs.getString("timestamp",
				"1400666400000");

		// TODO Auto-generated method stub
		// Intent service= new Intent(arg0,OnAlarmStart.class);
		// arg0.startService(service);
		if (isOnline(arg0)) {
			Log.v("Reciever", "Reported status:Online");
			try {
				new LoadDataBackgrpund().execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
}
