package com.rishabh.eventplanner;

import com.leocardz.aelv.R;

public class ListItem {

	private String logo;
	private String title;
	private String summary;
	private String detail;
	private boolean isOpen;
	private ListViewHolder holder;
	private int drawable;
	private String timeStamp;
	private String time;
	private String location;
	public int remindTime;
	public int alarmID;
	public long timeMilliSec;

	public ListItem(String logo, String title, String summary, String detail, String timeStamp, String time, String date,int remindTime,int alarmID,long timeMilliSec) {
		super();
		this.logo = logo;
		this.title=title;
		this.detail=detail;
		this.summary=summary;
		this.isOpen = false;
		this.drawable = R.drawable.down;
		this.timeStamp=timeStamp;
		this.time=time;
		this.location=date;
		this.remindTime=remindTime;
		this.alarmID=alarmID;
		this.timeMilliSec=timeMilliSec;
	}
	
	public int getRemindTime(){
		return remindTime;
	}
	
	public long getTimeMilliSec(){
		return timeMilliSec;
	}
	
	
	public int getAlarmID(){
		return alarmID;
	}
	
	
	public String getTimeStamp(){
		return timeStamp;
	}

	public String getTime(){
		return time;
	}
	
	public String getDate(){
		return location;
	}


	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String text) {
		this.logo = text;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String text) {
		this.title = text;
	}
	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String text) {
		this.summary = text;
	}
	
	public String getDetail() {
		return detail;
	}

	public void setDetail(String text) {
		this.detail = text;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public ListViewHolder getHolder() {
		return holder;
	}

	public void setHolder(ListViewHolder holder) {
		this.holder = holder;
	}

	public int getDrawable() {
		return drawable;
	}

	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}

}
