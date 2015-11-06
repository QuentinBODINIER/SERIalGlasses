package com.example.sg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ChoicesActivity extends Activity {
	public int REQUEST_SD_ACTIVITY = 1;
	

	public void launchSendData(View v){
		startActivityForResult(   new Intent(getApplicationContext(), com.example.sg.SendDataActivity.class),
                REQUEST_SD_ACTIVITY);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_choices);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_choices, menu);
		return true;
	}
	
	public void startRoute(View v){
		 Intent intent = new Intent(this, DirectionActivity.class);
		 startActivity(intent);
	}
	public void startDebug(View v){
		 Intent intent = new Intent(this, SendDataActivity.class);
		 startActivity(intent);
	}

}
