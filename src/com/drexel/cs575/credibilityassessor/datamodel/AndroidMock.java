package com.drexel.cs575.credibilityassessor.datamodel;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import android.content.res.AssetManager;
import android.util.Log;

public class AndroidMock {

    private HashMap<Integer, TextMessage> messageCache = null;
    
    private AssetManager assetMgr = null; 
    
    public AndroidMock (AssetManager textMessages) {
    	assetMgr = textMessages;
    }

    public HashMap<Integer, TextMessage> getFullPubList() {
        return queryMessages();
    }

    public TextMessage getMessageById(int id) {
        HashMap<Integer, TextMessage> pubDB = queryMessages();
        Integer index = new Integer(id);
        return pubDB.get(index);
    }

    public Collection<TextMessage> getAllMessage() {
        HashMap<Integer, TextMessage> pubDB = queryMessages();
        return pubDB.values();
    }

    private HashMap<Integer, TextMessage> queryMessages() {
    	Log.v("AndroidMock", "Reading JSON file to build message set");
        if (messageCache != null) return messageCache;

        ObjectMapper mapper = new ObjectMapper();
        HashMap<Integer, TextMessage> msgDB = new HashMap<Integer, TextMessage>();

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
                Integer idx = new Integer(p.getId());
                msgDB.put(idx, p);
            }
            
        } catch (IOException ex) {
        	Log.v("Failure", ex.toString());
        }
        
        return msgDB;
    }

}
