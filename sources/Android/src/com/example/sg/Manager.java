package com.example.sg;

import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

public class Manager {
	public Semaphore semBTout = new Semaphore(1);
	public Activity activity;
	
	public Manager(Activity activity) {
		this.activity=activity;
	}

	
	public String ContactFromNumber(Context context, String number){
		String[] projection = new String[] {
		ContactsContract.PhoneLookup.DISPLAY_NAME,
		ContactsContract.PhoneLookup._ID};
		Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,Uri.encode(number));

		Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);
		if (cursor.moveToFirst()) {
			// Get values from contacts database:
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
			return cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		}
		return number;
	}
	public String ContactFromNumber(String number){
		String[] projection = new String[] {
		ContactsContract.PhoneLookup.DISPLAY_NAME,
		ContactsContract.PhoneLookup._ID};
		Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,Uri.encode(number));
		Context context = activity.getBaseContext();
		Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);
		if (cursor.moveToFirst()) {
			// Get values from contacts database:
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
			return cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		}
		return number;
	}
	
}
