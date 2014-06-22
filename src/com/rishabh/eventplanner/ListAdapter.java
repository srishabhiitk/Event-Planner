package com.rishabh.eventplanner;

import java.util.ArrayList;

import com.leocardz.aelv.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter extends ArrayAdapter<ListItem> {
	private ArrayList<ListItem> listItems;
	private Context context;

	public ListAdapter(Context context, int textViewResourceId,
			ArrayList<ListItem> listItems) {
		super(context, textViewResourceId, listItems);
		this.listItems = listItems;
		this.context = context;
	}

	@Override
	@SuppressWarnings("deprecation")
	public View getView(final int position, View convertView, ViewGroup parent) {
		ListViewHolder holder = null;
		final ListItem listItem = listItems.get(position);

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.list_item, null);

			LinearLayout textViewWrap = (LinearLayout) convertView
					.findViewById(R.id.text_wrap);
			TextView logo = (TextView) convertView.findViewById(R.id.tvLogo);
			TextView title = (TextView) convertView.findViewById(R.id.tvTitle);
			TextView summary = (TextView) convertView
					.findViewById(R.id.tvSummary);
			TextView detail = (TextView) convertView
					.findViewById(R.id.tvDetail);
			TextView time = (TextView) convertView.findViewById(R.id.tvTime);
			TextView location = (TextView) convertView
					.findViewById(R.id.tvDate);
			Button remind = (Button) convertView.findViewById(R.id.bRemind);
			Button remind15 = (Button) convertView.findViewById(R.id.bRemind15);
			Button remind30 = (Button) convertView.findViewById(R.id.bRemind30);
			Button remind45 = (Button) convertView.findViewById(R.id.bRemind45);
			Button remind60 = (Button) convertView.findViewById(R.id.bRemind60);
			TextView tvRemindChk = (TextView) convertView
					.findViewById(R.id.tvRemindChk);

			holder = new ListViewHolder(textViewWrap, logo, title, summary,
					detail, remind, remind15, remind30, remind45, remind60,
					time, location, tvRemindChk);
		} else
			holder = (ListViewHolder) convertView.getTag();

		holder.getLogo().setText(listItem.getLogo());
		holder.getTitle().setText(listItem.getTitle());
		holder.getSummary().setText(listItem.getSummary());
		holder.getDetail().setText(listItem.getDetail());
		holder.getTime().setText(listItem.getTime());
		holder.getDate().setText(listItem.getDate());
		holder.getButton().setTag(listItem);
		holder.getButton15().setTag(listItem);
		holder.getButton30().setTag(listItem);
		holder.getButton45().setTag(listItem);
		holder.getButton60().setTag(listItem);
		holder.getRemindChk().setText(
				"You will be reminded " + listItem.getRemindTime()
						+ " min early.");
		if (listItem.getRemindTime() != 0) {
			holder.getButton().setText("Cancel Alarm");
			holder.getRemindChk().setVisibility(View.VISIBLE);
		} else {
			holder.getButton().setText("Remind Me");
			holder.getRemindChk().setVisibility(View.GONE);
		}
		holder.getButton().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ListItem getInfo = (ListItem) v.getTag();
				ListViewHolder getInfoHolder = getInfo.getHolder();
				if (listItem.getRemindTime() != 0) {
					Context c = getContext();
					Intent forAlarm = new Intent(c, CancelAlarm.class);
					Bundle bundle = new Bundle();
					bundle.putInt("remindTime", 15);
					bundle.putInt("alarmID", getInfo.getAlarmID());
					forAlarm.putExtras(bundle);
					c.startActivity(forAlarm);
				} else if (listItem.isOpen()) {
					getInfoHolder.getButton15().setVisibility(View.VISIBLE);
					getInfoHolder.getButton30().setVisibility(View.VISIBLE);
					getInfoHolder.getButton45().setVisibility(View.VISIBLE);
					getInfoHolder.getButton60().setVisibility(View.VISIBLE);
					getInfoHolder.getButton().setVisibility(View.GONE);
					LayoutParams layoutParams = new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					getInfoHolder.getTextViewWrap().setLayoutParams(
							layoutParams);
				}

			}

		});

		holder.getButton15().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context c = getContext();
				ListItem getInfo = (ListItem) v.getTag();
				Intent forAlarm = new Intent(c, SetAlarm.class);
				Bundle bundle = new Bundle();
				bundle.putInt("remindTime", 15);
				bundle.putInt("alarmID", getInfo.getAlarmID());
				bundle.putLong("timeMilliSec", getInfo.getTimeMilliSec());
				bundle.putCharSequence("title", getInfo.getTitle());
				bundle.putCharSequence("summary", getInfo.getSummary());
				bundle.putCharSequence("location", getInfo.getDate());
				forAlarm.putExtras(bundle);
				c.startActivity(forAlarm);
			}

		});
		holder.getButton30().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context c = getContext();
				ListItem getInfo = (ListItem) v.getTag();
				Intent forAlarm = new Intent(c, SetAlarm.class);
				Bundle bundle = new Bundle();
				bundle.putInt("remindTime", 30);
				bundle.putInt("alarmID", getInfo.getAlarmID());
				bundle.putLong("timeMilliSec", getInfo.getTimeMilliSec());
				bundle.putCharSequence("title", getInfo.getTitle());
				bundle.putCharSequence("summary", getInfo.getSummary());
				bundle.putCharSequence("location", getInfo.getDate());
				forAlarm.putExtras(bundle);
				c.startActivity(forAlarm);
			}

		});
		holder.getButton45().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context c = getContext();
				ListItem getInfo = (ListItem) v.getTag();
				Log.v("listItem", getInfo.getTimeMilliSec() + "");
				Intent forAlarm = new Intent(c, SetAlarm.class);
				Bundle bundle = new Bundle();
				bundle.putInt("remindTime", 45);
				bundle.putInt("alarmID", getInfo.getAlarmID());
				bundle.putLong("timeMilliSec", getInfo.getTimeMilliSec());
				bundle.putCharSequence("title", getInfo.getTitle());
				bundle.putCharSequence("summary", getInfo.getSummary());
				bundle.putCharSequence("location", getInfo.getDate());
				forAlarm.putExtras(bundle);
				c.startActivity(forAlarm);
			}

		});
		holder.getButton60().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context c = getContext();
				ListItem getInfo = (ListItem) v.getTag();
				Intent forAlarm = new Intent(c, SetAlarm.class);
				Bundle bundle = new Bundle();
				bundle.putInt("remindTime", 60);
				bundle.putInt("alarmID", getInfo.getAlarmID());
				bundle.putLong("timeMilliSec", getInfo.getTimeMilliSec());
				bundle.putCharSequence("title", getInfo.getTitle());
				bundle.putCharSequence("summary", getInfo.getSummary());
				bundle.putCharSequence("location", getInfo.getDate());
				forAlarm.putExtras(bundle);
				c.startActivity(forAlarm);
			}

		});

		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		holder.getTextViewWrap().setLayoutParams(layoutParams);

		if (listItem.isOpen()) {
			if (listItem.getTimeMilliSec() > System.currentTimeMillis())
				holder.getButton().setVisibility(View.VISIBLE);
			holder.getDetail().setVisibility(View.VISIBLE);
			holder.getTime().setVisibility(View.VISIBLE);
			holder.getDate().setVisibility(View.VISIBLE);
			holder.getSummary().setTextSize(25);
			holder.getSummary().setTextColor(Color.RED);
			holder.getButton15().setVisibility(View.GONE);
			holder.getButton30().setVisibility(View.GONE);
			holder.getButton45().setVisibility(View.GONE);
			holder.getButton60().setVisibility(View.GONE);
		} else {
			holder.getButton().setVisibility(View.GONE);
			holder.getDetail().setVisibility(View.GONE);
			holder.getTime().setVisibility(View.GONE);
			holder.getDate().setVisibility(View.GONE);
			holder.getSummary().setTextSize(18);
			holder.getSummary().setTextColor(Color.BLACK);
			holder.getButton15().setVisibility(View.GONE);
			holder.getButton30().setVisibility(View.GONE);
			holder.getButton45().setVisibility(View.GONE);
			holder.getButton60().setVisibility(View.GONE);
		}

		holder.getLogo().setCompoundDrawablesWithIntrinsicBounds(
				listItem.getDrawable(), 0, 0, 0);

		convertView.setTag(holder);

		listItem.setHolder(holder);

		return convertView;
	}

	public void setReminder(View v) {
		ListViewHolder getInfo = (ListViewHolder) v.getTag();
		getInfo.getTitle().setText("Reminder was set");
		Toast.makeText(getContext(), getInfo.getTitle().toString(),
				Toast.LENGTH_SHORT).show();
	}

}
