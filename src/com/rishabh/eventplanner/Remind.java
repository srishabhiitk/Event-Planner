package com.rishabh.eventplanner;

import java.io.IOException;

import com.leocardz.aelv.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Remind extends Activity{
	MediaPlayer mediaPlayer;
	public static PowerManager.WakeLock wakeLock;


	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.ECLAIR) @SuppressLint({ "InlinedApi", "Wakelock" }) @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		wakeLock = ((PowerManager)this.getSystemService("power")).newWakeLock(268435482, "TAG");
	    wakeLock.acquire();
	    KeyguardManager km=(KeyguardManager) this.getSystemService("keyguard");
	    km.newKeyguardLock("TAG").disableKeyguard();
		wakeLock.release();
		Bundle bundle=getIntent().getExtras();
		int alarmID = bundle.getInt("alarmID");
		DataBase db1 = new DataBase(this);
		db1.open();
		db1.update(alarmID,0);
		db1.close();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
				+ WindowManager.LayoutParams.FLAG_FULLSCREEN+ WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
				+WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		setContentView(R.layout.remind_screen);
		TextView title=(TextView)findViewById(R.id.tvRemindTitle);
		TextView summary=(TextView)findViewById(R.id.tvRemindSummary);
		Button cancelAlarm=(Button)findViewById(R.id.bCancelAlarm);
		title.setText(bundle.getCharSequence("title"));
		summary.setText(bundle.getCharSequence("summary"));
		SharedPreferences getPrefs=PreferenceManager.getDefaultSharedPreferences(this);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		Uri defaultRingtone= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		Uri audioUri = Uri.parse(getPrefs.getString("alarmtone", defaultRingtone.toString()));
		try {
			mediaPlayer.setDataSource(getApplicationContext(), audioUri);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "Please select a valid track!", Toast.LENGTH_SHORT).show();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "Please select an alarm tone from options menu.", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
			Toast.makeText(this, "Please select an alarm tone from options menu.", Toast.LENGTH_SHORT).show();
		}
		cancelAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mediaPlayer.release();
				finish();
				
			}
		});
		
		
		
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mediaPlayer.release();
	}
	

}
