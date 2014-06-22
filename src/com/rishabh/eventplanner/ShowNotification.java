package com.rishabh.eventplanner;

import java.util.ArrayList;

import com.leocardz.aelv.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class ShowNotification extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Event Planner")
				.setContentText("Tap to see Today's Events.");
		Intent resultIntent = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, resultIntent,
				PendingIntent.FLAG_ONE_SHOT);
		mBuilder.setContentIntent(pi);
		MainActivity.filterChoice="1";
		DataBase db1 = new DataBase(this);
		db1.open();
		ArrayList<ListItem> listItems = db1.getData();
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		inboxStyle.addLine(listItems.size()+" Events Today.");
		for(int i=0;i<listItems.size();i++){
			inboxStyle.addLine(listItems.get(i).getTitle()+"-"+listItems.get(i).getSummary());
		}
		db1.close();
		mBuilder.setStyle(inboxStyle);
		
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
		finish();

	}

}
