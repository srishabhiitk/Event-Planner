package com.rishabh.eventplanner;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewHolder {
	private LinearLayout textViewWrap;
	private TextView logo;
	private TextView title;
	private TextView summary;
	private TextView detail;
	private TextView reminder;
	private TextView time;
	private TextView date;
	private Button remind;
	private Button remind15;
	private Button remind30;
	private Button remind45;
	private Button remind60;
	private TextView remindChk;

	public ListViewHolder(LinearLayout textViewWrap, TextView logo , TextView title , TextView summary , TextView detail , Button remind, Button remind15, Button remind30, Button remind45, Button remind60, TextView time, TextView date,TextView remindChk ) {
		super();
		this.textViewWrap = textViewWrap;
		this.logo = logo;
		this.detail=detail;
		this.summary=summary;
		this.title=title;
		this.remind=remind;
		this.remind15=remind15;
		this.remind30=remind30;
		this.remind45=remind45;
		this.remind60=remind60;
		this.time=time;
		this.date=date;
		this.remindChk=remindChk;
	}

	
	public void setTitle(TextView title) {
		this.title=title;
	}
	
	public void setSummary(TextView summary) {
		this.summary=summary;
	}
	
	public void setDetail(TextView detail) {
		this.detail=detail;
	}
	
	public TextView getTime(){
		return time;
	}
	
	public TextView getDate(){
		return date;
	}
	
	public TextView getRemindChk(){
		return remindChk;
	}
	
	public TextView getTitle(){
		return title;
	}
	
	public TextView getReminder(){
		return reminder;
	}
	
	public TextView getSummary(){
		return summary;
	}
	
	public TextView getDetail(){
		return detail;
	}
	
	public Button getButton15(){
		return remind15;
	}
	
	public Button getButton30(){
		return remind30;
	}
	
	public Button getButton45(){
		return remind45;
	}
	
	public Button getButton60(){
		return remind60;
	}
	
	public Button getButton(){
		return remind;
	}
	
	public TextView getLogo(){
		return logo;
	}
	

	public void setLogo(TextView textView) {
		this.logo = textView;
	}

	public LinearLayout getTextViewWrap() {
		return textViewWrap;
	}

	public void setTextViewWrap(LinearLayout textViewWrap) {
		this.textViewWrap = textViewWrap;
	}
}
