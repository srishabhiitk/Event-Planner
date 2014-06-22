package com.rishabh.eventplanner;

import com.leocardz.aelv.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

@SuppressWarnings("deprecation")
public class OnAlarmStart extends Service {
	NotificationManager mManager;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.v("Notification", "was tried");
		
		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(
						this.getApplicationContext().NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(this.getApplicationContext(),
				MainActivity.class);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Tap to see Today's Events.", System.currentTimeMillis());
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this.getApplicationContext(),
				"Event Planner", "Tap to see Today's Events.",
				pendingNotificationIntent);
		mManager.notify(0, notification);
	
	}
}
