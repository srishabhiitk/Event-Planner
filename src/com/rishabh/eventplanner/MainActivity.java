package com.rishabh.eventplanner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import com.leocardz.aelv.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements
		OnSharedPreferenceChangeListener {

	private ListView listView;
	private ArrayList<ListItem> listItems;
	private ListAdapter adapter;
	private boolean accordion = true;
	public static Activity toBeKilled;
	public static InputStream is;
	public SharedPreferences getPrefs;
	public static String timeStamp;
	public static String filterChoice;
	public static boolean filterReminders;
	public static Uri audioUri = null;
	TextView tvMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvMain=(TextView)findViewById(R.id.tv_main);

		toBeKilled = this;

		getPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		getPrefs.registerOnSharedPreferenceChangeListener(this);

		filterChoice = getPrefs.getString("filter", "1");
		filterReminders = getPrefs.getBoolean("filterreminders", false);
		DataBase db1 = new DataBase(this);
		db1.open();
		listItems = db1.getData();
		db1.close();
		
		if(listItems.size()!=0){
			tvMain.setTextSize(35);
			if(filterChoice.contentEquals("1"))
				tvMain.setText("Today's Events");
			else if(filterChoice.contentEquals("2")){
				tvMain.setText("Events Today and Tommorow");
				tvMain.setTextSize(20);
			}
			else if(filterChoice.contentEquals("3"))
				tvMain.setText("Events This week");
			else if(filterChoice.contentEquals("4"))
				tvMain.setText("All Events");
		}else{
			tvMain.setText("List is empty. Try updating or changing filter from preferences.");
			tvMain.setTextSize(15);
		}

		listView = (ListView) findViewById(R.id.list);

		adapter = new ListAdapter(this, R.layout.list_item, listItems);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toggle(view, position);
			}
		});

		Context context = this;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR, 5);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent("com.leocardz.aelv.SHOWNOTIFICATION");
		PendingIntent pi = PendingIntent.getActivity(this, 0, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(),
				1000 * 60 * 60 * 24, pi);
	}

	private void toggle(View view, final int position) {
		ListItem listItem = listItems.get(position);
		listItem.getHolder().setTextViewWrap((LinearLayout) view);

		int fromHeight = 0;
		int toHeight = 0;

		if (listItem.isOpen()) {

			fromHeight = listItem.getHolder().getTextViewWrap()
					.getLayoutParams().height;
			toHeight = 0;
		} else {
			fromHeight = listItem.getHolder().getTextViewWrap()
					.getLayoutParams().height;
			toHeight = 500;

			// This closes all item before the selected one opens
			if (accordion) {
				closeAll();
			}
		}

		toggleAnimation(listItem, position, fromHeight, toHeight, true);
	}

	private void closeAll() {
		int i = 0;
		for (ListItem listItem : listItems) {
			if (listItem.isOpen()) {
				toggleAnimation(listItem, i, listItem.getHolder()
						.getTextViewWrap().getLayoutParams().height, 0, false);
			}
			i++;
		}
	}

	private void toggleAnimation(final ListItem listItem, final int position,
			final int fromHeight, final int toHeight, final boolean goToItem) {

		ResizeAnimation resizeAnimation = new ResizeAnimation(adapter,
				listItem, 0, fromHeight, 0, toHeight);
		resizeAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				listItem.setOpen(!listItem.isOpen());
				boolean foo = listItem.getTimeMilliSec() > System
						.currentTimeMillis();
				listItem.setDrawable(listItem.isOpen() ? R.drawable.up
						: R.drawable.down);
				listItem.getHolder()
						.getButton()
						.setVisibility(
								(listItem.isOpen() && foo) ? View.VISIBLE
										: View.GONE);
				adapter.notifyDataSetChanged();

				if (goToItem)
					goToItem(position);
			}
		});

		listItem.getHolder().getTextViewWrap().startAnimation(resizeAnimation);
	}

	public void setReminder(View v) {
		ListViewHolder getInfo = (ListViewHolder) v.getTag();
		Toast.makeText(getBaseContext(), getInfo.getTitle().toString(),
				Toast.LENGTH_SHORT).show();
		getInfo.getTitle().setText("Reminder was set");
	}

	private void goToItem(final int position) {
		listView.post(new Runnable() {
			@Override
			public void run() {
				try {
					listView.smoothScrollToPosition(position);
				} catch (Exception e) {
					listView.setSelection(position);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blow = getMenuInflater();
		blow.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		DataBase db1 = new DataBase(this);
		db1.open();
		listItems = db1.getData();
		db1.close();
		adapter = new ListAdapter(this, R.layout.list_item, listItems);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		if(listItems.size()!=0){
			tvMain.setTextSize(35);
			if(filterChoice.contentEquals("1"))
				tvMain.setText("Today's Events");
			else if(filterChoice.contentEquals("2")){
				tvMain.setText("Events Today and Tommorow");
				tvMain.setTextSize(20);
			}
			else if(filterChoice.contentEquals("3"))
				tvMain.setText("Events This week");
			else if(filterChoice.contentEquals("4"))
				tvMain.setText("All Events");
		}else{
			tvMain.setText("List is empty. Try updating or changing filter from preferences.");
			tvMain.setTextSize(15);
		}
	}

	String m_chosenDir = "";

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_update:
			try {
				getPrefs = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
			} catch (Exception e) {
				e.printStackTrace();
			}
			is = getResources().openRawResource(R.raw.keyfile);
			Calendar now = Calendar.getInstance();
			Log.v("Current Date",
					"Date is" + now.get(Calendar.DATE) + "Month: "
							+ now.get(Calendar.MONTH) + "Year:"
							+ now.get(Calendar.YEAR));
			Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();
			timeStamp = getPrefs.getString("timestamp", "1400666400000");
			filterChoice = getPrefs.getString("filter", "1");
			filterReminders = getPrefs.getBoolean("filterreminders", false);
			Log.v("TimeStamp", timeStamp);
			new LoadData().execute();
			break;

		case R.id.menu_preferences:
			Intent i = new Intent("com.leocardz.aelv.PREFS");
			startActivity(i);
			break;

		case R.id.menu_credentials:
			Intent i1 = new Intent("com.leocardz.aelv.Credentials");
			startActivity(i1);
			break;

		case R.id.menu_alarmtone:
			Intent tmpIntent = new Intent(
					RingtoneManager.ACTION_RINGTONE_PICKER);
			startActivityForResult(tmpIntent, 0);
			break;

		case R.id.menu_exit:
			finish();
			break;

		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Log.v("Ringtone", "result was ok");
			audioUri = data
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			Editor edit = getPrefs.edit();
			edit.putString("alarmtone", audioUri.toString());
			edit.apply();
			Log.v("Ringtone", audioUri.getEncodedPath());
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		Log.v("Shared Prefernce", "function was atleast called");
		if (key.contentEquals("filter") || key.contentEquals("filterreminders")) {
			Log.v("Shared Prefernce", "change was listened");
			filterChoice = getPrefs.getString("filter", "1");
			filterReminders = getPrefs.getBoolean("filterreminders", false);
			DataBase db1 = new DataBase(this);
			db1.open();
			listItems = db1.getData();
			db1.close();
			adapter = new ListAdapter(this, R.layout.list_item, listItems);
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			if(listItems.size()!=0){
				tvMain.setTextSize(35);
				if(filterChoice.contentEquals("1"))
					tvMain.setText("Today's Events");
				else if(filterChoice.contentEquals("2")){
					tvMain.setText("Events Today and Tommorow");
					tvMain.setTextSize(20);
				}
				else if(filterChoice.contentEquals("3"))
					tvMain.setText("Events This week");
				else if(filterChoice.contentEquals("4"))
					tvMain.setText("All Events");
			}else{
				tvMain.setText("List is empty. Try updating or changing filter from preferences.");
				tvMain.setTextSize(15);
			}
		}

	}

}
