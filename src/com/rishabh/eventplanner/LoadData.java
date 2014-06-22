package com.rishabh.eventplanner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

public class LoadData extends AsyncTask<String, Integer, java.util.List<Event>> {
	private boolean wasSuccessful = true;

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
			long limit=864000000;
			DateTime updateMin = new DateTime(update);
			java.util.List<Event> items = null;
			do {
				if ((System.currentTimeMillis()-update)>limit)
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
			wasSuccessful = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			wasSuccessful = false;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			wasSuccessful = false;
		}
		wasSuccessful = false;
		return null;
	}

	@Override
	protected void onPostExecute(java.util.List<Event> items) {
		// TODO Auto-generated method stub
		super.onPostExecute(items);
		Activity activity = MainActivity.toBeKilled;
		activity.startActivity(new Intent(activity, Update.class));
		Update.recieved = items;
		Update.sucess = wasSuccessful;

	}

}
