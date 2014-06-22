package com.rishabh.eventplanner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

public class DataBase {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "event_title";
	public static final String KEY_SUMMARY = "event_summary";
	public static final String KEY_DETAIL = "event_detail";
	public static final String KEY_TIMESTAMP = "event_time";
	public static final String KEY_REMINDTIME = "event_remind";

	private static final String DATABASE_NAME = "Event_db";
	private static final String DATABASE_TABLE = "Event_table";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private Context ourContext;
	private SQLiteDatabase ourDatabase;

	SharedPreferences getPrefs;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TITLE
					+ " TEXT NOT NULL, " + KEY_SUMMARY + " TEXT NOT NULL, "
					+ KEY_TIMESTAMP + " TEXT NOT NULL, " + KEY_REMINDTIME
					+ " INTEGER, " + KEY_DETAIL + " TEXT NOT NULL);");
			Log.v("Database", "table was created successfully");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);

		}

	}

	public DataBase(Context c) {
		ourContext = c;
	}

	public DataBase open() {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public void createEvent(String title, String summary, String detail,
			String timeStamp) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_TITLE, title);
		cv.put(KEY_SUMMARY, summary);
		cv.put(KEY_DETAIL, detail);
		cv.put(KEY_TIMESTAMP, timeStamp);
		cv.put(KEY_REMINDTIME, 0);
		ourDatabase.insert(DATABASE_TABLE, null, cv);

	}

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings({ "deprecation" })
	public ArrayList<ListItem> getData() {
		getPrefs = PreferenceManager.getDefaultSharedPreferences(ourContext);
		String months[] = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };
		String title, summary, detail, logo, timeStamp, timeString, date, location;
		String[] columns = new String[] { KEY_ROWID, KEY_TITLE, KEY_SUMMARY,
				KEY_DETAIL, KEY_TIMESTAMP, KEY_REMINDTIME };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, KEY_TIMESTAMP);
		ArrayList<ListItem> listItems = new ArrayList<ListItem>();
		java.util.Date smthng = new java.util.Date(System.currentTimeMillis());
		java.util.Calendar now = Calendar.getInstance();
		Log.v("Compare", "Date:" + now.get(Calendar.DATE) + smthng.getDate()
				+ "Month:" + (now.get(Calendar.MONTH)) + smthng.getMonth()
				+ "Year:" + (now.get(Calendar.YEAR) - 1900) + smthng.getYear());
		Long time;
		if (!getPrefs.getString("filter", "1").contentEquals("4")) {
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				boolean b = true;
				if ((c.getInt(c.getColumnIndex(KEY_REMINDTIME)) == 0)
						&& MainActivity.filterReminders)
					b = false;

				if (b
						&& shouldBeImported(c.getString(c
								.getColumnIndex(KEY_TIMESTAMP)))) {
					timeStamp = c.getString(c.getColumnIndex(KEY_TIMESTAMP));
					time = Long.parseLong(timeStamp);
					if (time != 0) {
						smthng = new java.util.Date(time);
						date = smthng.getDate() + " "
								+ months[smthng.getMonth()];
						timeString = new SimpleDateFormat("hh:mm aa")
								.format(smthng);
						now = Calendar.getInstance();
						if (now.get(Calendar.DATE) == smthng.getDate()
								&& now.get(Calendar.MONTH) == smthng.getMonth()
								&& (now.get(Calendar.YEAR) - 1900) == smthng
										.getYear())
							timeString = "Today at " + timeString;
						else {
							now.add(Calendar.DATE, 1);
							if (now.get(Calendar.DATE) == smthng.getDate()
									&& now.get(Calendar.MONTH) == smthng
											.getMonth()
									&& (now.get(Calendar.YEAR) - 1900) == smthng
											.getYear())
								timeString = "Tommorow at " + timeString;
							else {
								if (time > System.currentTimeMillis())
									timeString = "Is on " + date + " at "
											+ timeString;
								else
									timeString = "Was on " + date + " at "
											+ timeString;
							}
						}
						title = c.getString(c.getColumnIndex(KEY_TITLE));
						int i = gethyphen(title);
						if (i == -1)
							summary = "Not Mentioned";
						else {
							summary = title.substring(i + 1);
							title = title.substring(0, i);
						}
						location = c.getString(c.getColumnIndex(KEY_SUMMARY));
						location = "Reach " + location;
						detail = c.getString(c.getColumnIndex(KEY_DETAIL));
						logo = title.substring(0, 1);
						listItems.add(new ListItem(logo, title, summary,
								detail, timeStamp, timeString, location,
								c.getInt(c.getColumnIndex(KEY_REMINDTIME)), c
										.getInt(c.getColumnIndex(KEY_ROWID)),
								time));
					} else {
						title = c.getString(c.getColumnIndex(KEY_TITLE));
						summary = c.getString(c.getColumnIndex(KEY_SUMMARY));
						detail = c.getString(c.getColumnIndex(KEY_DETAIL));
						logo = title.substring(0, 1);
						listItems.add(new ListItem(logo, title, summary,
								detail, timeStamp, "could not import",
								"could not import", c.getInt(c
										.getColumnIndex(KEY_REMINDTIME)), c
										.getInt(c.getColumnIndex(KEY_ROWID)),
								time));
					}
				}

			}
		} else {
			for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()) {
				boolean b = true;
				if ((c.getInt(c.getColumnIndex(KEY_REMINDTIME)) == 0)
						&& MainActivity.filterReminders)
					b = false;

				if (b
						&& shouldBeImported(c.getString(c
								.getColumnIndex(KEY_TIMESTAMP)))) {
					timeStamp = c.getString(c.getColumnIndex(KEY_TIMESTAMP));
					time = Long.parseLong(timeStamp);
					if (time != 0) {
						smthng = new java.util.Date(time);
						date = smthng.getDate() + " "
								+ months[smthng.getMonth()];
						timeString = new SimpleDateFormat("hh:mm aa")
								.format(smthng);
						now = Calendar.getInstance();
						if (now.get(Calendar.DATE) == smthng.getDate()
								&& now.get(Calendar.MONTH) == smthng.getMonth()
								&& (now.get(Calendar.YEAR) - 1900) == smthng
										.getYear())
							timeString = "Today at " + timeString;
						else {
							now.add(Calendar.DATE, 1);
							if (now.get(Calendar.DATE) == smthng.getDate()
									&& now.get(Calendar.MONTH) == smthng
											.getMonth()
									&& (now.get(Calendar.YEAR) - 1900) == smthng
											.getYear())
								timeString = "Tommorow at " + timeString;
							else {
								if (time > System.currentTimeMillis())
									timeString = "Is on " + date + " at "
											+ timeString;
								else
									timeString = "Was on " + date + " at "
											+ timeString;
							}
						}
						title = c.getString(c.getColumnIndex(KEY_TITLE));
						int i = gethyphen(title);
						if (i == -1)
							summary = "Not Mentioned";
						else {
							summary = title.substring(i + 1);
							title = title.substring(0, i);
						}
						location = c.getString(c.getColumnIndex(KEY_SUMMARY));
						location = "Reach " + location;
						detail = c.getString(c.getColumnIndex(KEY_DETAIL));
						logo = title.substring(0, 1);
						listItems.add(new ListItem(logo, title, summary,
								detail, timeStamp, timeString, location,
								c.getInt(c.getColumnIndex(KEY_REMINDTIME)), c
										.getInt(c.getColumnIndex(KEY_ROWID)),
								time));
					} else {
						title = c.getString(c.getColumnIndex(KEY_TITLE));
						summary = c.getString(c.getColumnIndex(KEY_SUMMARY));
						detail = c.getString(c.getColumnIndex(KEY_DETAIL));
						logo = title.substring(0, 1);
						listItems.add(new ListItem(logo, title, summary,
								detail, timeStamp, "could not import",
								"could not import", c.getInt(c
										.getColumnIndex(KEY_REMINDTIME)), c
										.getInt(c.getColumnIndex(KEY_ROWID)),
								time));
					}
				}
			}
		}
		return listItems;

	}

	@SuppressWarnings({ "deprecation" })
	private boolean shouldBeImported(String string) {
		// TODO Auto-generated method stub
		getPrefs = PreferenceManager.getDefaultSharedPreferences(ourContext);
		String filterChoice = getPrefs.getString("filter", "1");
		try {
			long eventTime = Long.parseLong(string);
			java.util.Date event = new java.util.Date(eventTime);
			java.util.Calendar now = Calendar.getInstance();
			if (filterChoice.contentEquals("1")) {
				if (now.get(Calendar.DATE) == event.getDate()
						&& now.get(Calendar.MONTH) == event.getMonth()
						&& (now.get(Calendar.YEAR) - 1900) == event.getYear())
					return true;
			} else if (filterChoice.contentEquals("2")) {
				if (now.get(Calendar.DATE) == event.getDate()
						&& now.get(Calendar.MONTH) == event.getMonth()
						&& (now.get(Calendar.YEAR) - 1900) == event.getYear())
					return true;
				now.add(Calendar.DATE, 1);
				if (now.get(Calendar.DATE) == event.getDate()
						&& now.get(Calendar.MONTH) == event.getMonth()
						&& (now.get(Calendar.YEAR) - 1900) == event.getYear())
					return true;
			} else if (filterChoice.contentEquals("3")) {
				for (int i = 0; i <= 7; i++) {
					if (now.get(Calendar.DATE) == event.getDate()
							&& now.get(Calendar.MONTH) == event.getMonth()
							&& (now.get(Calendar.YEAR) - 1900) == event
									.getYear())
						return true;
					now.add(Calendar.DATE, 1);
				}
			} else if (filterChoice.contentEquals("4")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private int gethyphen(String title) {
		// TODO Auto-generated method stub
		char str[] = title.toCharArray();
		for (int i = 0; i < str.length; i++) {
			if (str[i] == '-') {
				return i;
			}
		}
		return -1;
	}

	public void update(int alarmID, int remindTime) {
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_REMINDTIME, remindTime);
		ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID + "=" + alarmID,
				null);
	}

}
