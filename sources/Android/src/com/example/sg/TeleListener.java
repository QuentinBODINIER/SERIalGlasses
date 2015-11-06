package com.example.sg;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class TeleListener extends PhoneStateListener{
	public void onCallStateChanged(int state, String incomingNumber) {
	
		   super.onCallStateChanged(state, incomingNumber);
		   switch (state) {
		   case TelephonyManager.CALL_STATE_IDLE:
		    // CALL_STATE_IDLE;
		   
		    break;
		   case TelephonyManager.CALL_STATE_OFFHOOK:
		    // CALL_STATE_OFFHOOK;
		 
		    break;
		   case TelephonyManager.CALL_STATE_RINGING:
			  
			try {
				SG.manager.semBTout.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				SG.connectedThread.write(CMD.NOTIF);
				SG.connectedThread.write(("tel "+ SG.manager.ContactFromNumber(incomingNumber)).getBytes());
				SG.manager.semBTout.release();
		  
		    break;
		   default:
		    break;
		   }
		  }

}
