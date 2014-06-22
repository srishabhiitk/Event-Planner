package com.rishabh.eventplanner;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.api.services.calendar.model.Event;
import com.leocardz.aelv.R;

public class Update extends Activity {

	public static List<Event> recieved = null;
	public static boolean sucess;

	@SuppressLint({ "SimpleDateFormat", "CommitPrefEdits", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updating);
		if (sucess && !recieved.isEmpty()) {
			DataBase db = new DataBase(this);
			db.open();
			String str = "";
			long num = 0;

			for (int i = 0; i < recieved.size(); i++) {
				str = "";
				try {
					num = recieved.get(i).getStart().getDateTime().getValue();
				} catch (Exception e) {
					e.printStackTrace();
					Log.v("time import", "was unccessful");
				}
				str = str + num;
				db.createEvent(recieved.get(i).getSummary(), recieved.get(i)
						.getLocation(), recieved.get(i).getDescription(), str);
			}
			db.close();
			String lastUpdate = "" + System.currentTimeMillis();
			SharedPreferences getPrefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			Editor edit = getPrefs.edit();
			edit.putString("timestamp", lastUpdate);
			edit.apply();
			Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
		} else if (sucess && recieved.isEmpty())
			Toast.makeText(this, "List Already Up-To-Date", Toast.LENGTH_SHORT)
					.show();
		else
			Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
		finish();
		/*
		 * Intent i=new Intent (); i.setClass(getBaseContext(),
		 * MainActivity.class); MainActivity.toBeKilled.finish();
		 * startActivity(i);
		 */

	}

}
