package com.rishabh.eventplanner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SetAlarm extends Activity {

	@Override
	protected void onCreate(Bundle savedInstance) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstance);
		try {
			Bundle savedInstanceState = getIntent().getExtras();
			Log.v("was recieved", savedInstanceState.getLong("timeMilliSec")
					+ "");
			long time = savedInstanceState.getLong("timeMilliSec");
			int remindTime = savedInstanceState.getInt("remindTime");
			if ((time - remindTime * 60 * 1000) > System.currentTimeMillis()) {
				int alarmID = savedInstanceState.getInt("alarmID");
				int uniqueAlarmID = alarmID * 100 + remindTime;
				Intent i = new Intent(this, Remind.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle = new Bundle();
				bundle.putInt("alarmID", alarmID);
				bundle.putCharSequence("title",
						savedInstanceState.getCharSequence("title"));
				bundle.putCharSequence("summary",
						savedInstanceState.getCharSequence("summary"));
				i.putExtras(bundle);
				PendingIntent pendingIntent = PendingIntent.getActivity(this,
						uniqueAlarmID, i, PendingIntent.FLAG_ONE_SHOT);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP, time - remindTime
					* 60 * 1000, pendingIntent);
				DataBase db1 = new DataBase(this);
				db1.open();
				db1.update(alarmID, remindTime);
				db1.close();

				Toast.makeText(getBaseContext(), "Alarm was set!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						getBaseContext(),
						"Event has already passed.. Why set an alarm for that!!",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "There was an error!!", Toast.LENGTH_SHORT)
					.show();
		}
		/*
		 * Intent startAlarm= new Intent(this, Reciever.class); PendingIntent
		 * pendingIntent= PendingIntent.getBroadcast(SetAlarm.this, 0,
		 * startAlarm,PendingIntent.FLAG_UPDATE_CURRENT); AlarmManager
		 * alarmManager= (AlarmManager)getSystemService(ALARM_SERVICE);
		 * alarmManager
		 * .set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000
		 * ,pendingIntent);
		 */

		finish();

	}

}
