package com.rishabh.eventplanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CancelAlarm extends Activity{

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstance) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstance);
		try {
			Bundle savedInstanceState=getIntent().getExtras();
			int remindTime = savedInstanceState.getInt("remindTime");
			int alarmID = savedInstanceState.getInt("alarmID");
			int uniqueAlarmID = alarmID * 100 + remindTime;
			Intent i = new Intent(this, Remind.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bundle = new Bundle();
			bundle.putAll(savedInstanceState);
			PendingIntent pendingIntent = PendingIntent.getActivity(this,
					uniqueAlarmID, i, PendingIntent.FLAG_ONE_SHOT, bundle);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);
			DataBase db1 = new DataBase(this);
			db1.open();
			db1.update(alarmID,0);
			db1.close();
			
			
			Toast.makeText(getBaseContext(), "Alarm was cancelled!",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "There was an error!!", Toast.LENGTH_SHORT)
					.show();
		}
		
		finish();
	}

}
