package com.example.sg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class IncomingSMSReceiver extends BroadcastReceiver{
	final SmsManager sms = SmsManager.getDefault();
	@Override
	public void onReceive(Context context, Intent intent) {
		final Bundle bundle = intent.getExtras();
		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();

				
					String message = currentMessage.getDisplayMessageBody();

					// Show alert
					int duration = Toast.LENGTH_LONG;
					SG.manager.semBTout.acquire();
					SG.connectedThread.write(CMD.NOTIF);
					SG.connectedThread.write(("sms "+ SG.manager.ContactFromNumber(context, phoneNumber)).getBytes());
					SG.manager.semBTout.release();

				} // end for loop
			} // bundle is null

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" +e);
		}
	}


}
