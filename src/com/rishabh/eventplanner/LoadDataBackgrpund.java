package com.rishabh.eventplanner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import android.annotation.SuppressLint;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

public class LoadDataBackgrpund extends
		AsyncTask<String, Integer, java.util.List<Event>> {
	@Override
	protected java.util.List<Event> doInBackground(String... params) {
		com.google.api.services.calendar.Calendar todo;
		try {
			GetCalendarService serve = new GetCalendarService();
			todo = serve.getDriveService();
			String pageToken = null;
			// com.google.api.services.calendar.Calendar.Events.List
			// list=all.list("goforrishabh@gmail.com");
			// return list.getCalendarId();
			Log.v("execute", "succesful");
			Events events;
			long update = Long.parseLong(MainActivity.timeStamp);
			long limit = 864000000;
			DateTime updateMin = new DateTime(update);
			java.util.List<Event> items = null;
			do {
				if ((System.currentTimeMillis() - update) > limit)
					events = todo.events().list("goforrishabh@gmail.com")
							.setPageToken(pageToken).execute();
				else
					events = todo.events().list("goforrishabh@gmail.com")
							.setPageToken(pageToken).setUpdatedMin(updateMin)
							.execute();
				items = events.getItems();
				for (Event event : items) {
					Log.v("event", event.getSummary());
				}
				pageToken = events.getNextPageToken();
			} while (pageToken != null);
			java.util.List<Event> returnlist = events.getItems();
			return returnlist;

		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPostExecute(java.util.List<Event> recieved) {
		// TODO Auto-generated method stub
		super.onPostExecute(recieved);
		DataBase db = new DataBase(Reciever.c);
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
		Editor edit = Reciever.getPrefs.edit();
		edit.putString("timestamp", lastUpdate);
		edit.apply();

	}

}
