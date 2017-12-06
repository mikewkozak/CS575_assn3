package com.drexel.cs575.credibilityassessor.datamodel;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class AndroidAdapter implements ISMSAdapter {

    private HashMap<Contact, TextMessage> messageCache = null;
    
    private ContentResolver contentMgr = null; 
    
    public AndroidAdapter (ContentResolver contentResolver) {
    	contentMgr = contentResolver;
    	
    	queryMessages();
    }

    public TextMessage getMessageByName(Contact name) {
        HashMap<Contact, TextMessage> pubDB = queryMessages();
        return pubDB.get(name);
    }
    

    public Collection<TextMessage> getAllMessage() {
        HashMap<Contact, TextMessage> pubDB = queryMessages();
        return pubDB.values();
    }
    
  //courtesy of stack overflow
  	public Collection<Contact> getAllContacts() {
  		Log.v("loadSMSMessages","Reading in all contacts");
  		List<Contact> contacts = new ArrayList<Contact>();
  		
  	    Cursor cur = contentMgr.query(ContactsContract.Contacts.CONTENT_URI,
  	            null, null, null, null);

  	    if ((cur != null ? cur.getCount() : 0) > 0) {
  	        while (cur != null && cur.moveToNext()) {
  	            String name = cur.getString(cur.getColumnIndex(
  	                    ContactsContract.Contacts.DISPLAY_NAME));
  	            Log.v("loadContacts", "Name: " + name);
  	            contacts.add(new Contact(name));
  	        }
  	    }
  	    if(cur!=null){
  	        cur.close();
  	    }
  	    
  	    return contacts;
  	}

    
    private HashMap<Contact, TextMessage> queryMessages() {
        if (messageCache != null) return messageCache;
        
        HashMap<Contact, TextMessage> msgDB = new HashMap<Contact, TextMessage>();

        Log.v("loadSMSMessages","Reading in all text messages");
		Cursor cursor = contentMgr.query(Uri.parse("content://sms"), null, null, null, null);
		//Normally we'd retrieve from inbox, but for testing purposes I can only send messages, so I pull from sent
		Log.v("loadSMSMessages","Num messages retrieved = " + cursor.getCount());
		
		List<String> list = new ArrayList<String>();
		if (cursor.moveToFirst()) { // must check the result to prevent exception
			do {
				String msgData = "";
				for(int idx=0;idx<cursor.getColumnCount();idx++)
				{
					//Log.v("loadSMSMessages","Column="+cursor.getColumnName(idx)+" Value="+cursor.getString(idx));
					msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
					if(cursor.getColumnName(idx).equals("body")) {
						msgData += cursor.getString(idx);
					}
				}
				// use msgData
				list.add(msgData);

			} while (cursor.moveToNext());

			
		} else {
			// empty box, no SMS
			list.add("Empty");
		}
		
		messageCache = msgDB;
		return msgDB;
    }

}
