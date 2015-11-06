package com.example.sg;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

public class SendDataActivity extends Activity {
	EditText textToSend;
	ImageButton sendButton;
	String newText;
	int[] Pixels = new int[128*32];
	int maxLines = 4;
	int charsProLine = 21;

	public void sendData(View v){
		newText = textToSend.getText().toString();
	}
	
	public void manageNewText(){
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_send_data);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		textToSend = (EditText) findViewById(R.id.editTextToSend);
		sendButton = (ImageButton) findViewById(R.id.sendButton);
	    
		//screenPaint.setStyle(Paint.Style.STROKE);
		//screenCanvas.drawColor(Color.CYAN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_send_data, menu);
		return true;
	}
	
	public void sendText(View v){
		try {
			SG.manager.semBTout.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SG.connectedThread.write(textToSend.getText().toString().getBytes());
		SG.manager.semBTout.release();
	}

}
