/**
 * Mike Kozak (Student ID 10223568)
 * CS575 Assignment 3
 * 
 */
package com.drexel.cs575.credibilityassessor.adapters;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.drexel.cs575.credibilityassessor.datamodel.Contact;
import com.drexel.cs575.credibilityassessor.datamodel.EmptyMessage;
import com.drexel.cs575.credibilityassessor.datamodel.TextMessage;
import com.google.gson.Gson;

import android.content.res.AssetManager;
import android.util.Log;

/**
 * This class pulls contacts and messages from a JSON file to fake OS interactions
 * @author Computer
 *
 */
public class AndroidMock implements ISMSAdapter {

	//setting this to null so on the first request we pull from the data, delaying memory assignment
    private HashMap<Contact, TextMessage> messageCache = null;

    //setting this to null out of habit, but this should always come in through the constructor
    private AssetManager assetMgr = null; 
    
    public AndroidMock (AssetManager textMessages) {
    	assetMgr = textMessages;
    	
    	queryMessages();
    }

    public TextMessage getMessageByName(Contact name) {
        HashMap<Contact, TextMessage> msgDB = queryMessages();
        TextMessage msg;
        if(msgDB.containsKey(name)) {
        	msg = msgDB.get(name);
        } else {
        	msg = new EmptyMessage();
        }
        
        return msg;
    }
    

    public Collection<TextMessage> getAllMessage() {
        HashMap<Contact, TextMessage> msgDB = queryMessages();
        return msgDB.values();
    }

    //courtesy of lecture GitHub code
    private HashMap<Contact, TextMessage> queryMessages() {
        if (messageCache != null) return messageCache;
        
    	Log.v("AndroidMock", "Reading JSON file to build message set");
        HashMap<Contact, TextMessage> msgDB = new HashMap<Contact, TextMessage>();
        
        try {
            InputStream is = assetMgr.open("texts.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            TextMessage[] messages = new Gson().fromJson(json, TextMessage[].class);
            Log.v("AndroidMock", "Num texts loaded: " + messages.length);
            for (TextMessage p : messages) {
            	Contact c = new Contact(p.getContact());
            	Log.v("AndroidMock", "Adding " + c + " to DB");
                msgDB.put(c, p);
            }

            messageCache = msgDB;
            
        } catch (IOException ex) {
        	Log.v("Failure", ex.toString());
        }
        
        return msgDB;
    }

	@Override
	public Collection<Contact> getAllContacts() {
		 if (messageCache != null) return messageCache.keySet();
		 
		return new ArrayList<Contact>();
	}

}
