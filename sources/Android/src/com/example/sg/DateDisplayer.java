package com.example.sg;

import java.text.SimpleDateFormat;

import android.util.Log;

public class DateDisplayer extends Thread{
	public void run(){
		int i=0;
		long time = 0;
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat formatHeure = new SimpleDateFormat("HH : mm");
		while(true){
			time = System.currentTimeMillis();
			try {
				SG.manager.semBTout.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SG.connectedThread.write(CMD.TIME);
			if(i==0) SG.connectedThread.write(formatDate.format(time).getBytes());
			else  SG.connectedThread.write(formatHeure.format(time).getBytes());
			SG.manager.semBTout.release();
			i = (i+1)%2;
			Log.d("SG Date",""+time);
			try {
				sleep(5000, 0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
