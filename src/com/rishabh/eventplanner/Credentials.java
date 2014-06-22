package com.rishabh.eventplanner;

import com.leocardz.aelv.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Credentials extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credentials);
		TextView str=(TextView)findViewById(R.id.tvcredentials);
		str.setText("This application is developed by Rishabh Sahu.\nIf you find any problems with the app please contact me stating the problem.\nEmail: srishabh@iitk.ac.in");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
