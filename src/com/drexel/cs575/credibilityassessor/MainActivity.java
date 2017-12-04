package com.drexel.cs575.credibilityassessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import com.drexel.cs575.credibilityassessor.datamodel.AndroidMock;
import com.drexel.cs575.credibilityassessor.datamodel.TextMessage;
import com.drexel.cs575.credibilityassessor.util.TextTokenizer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	AndroidMock mockDB;
	
	//UI elements
	Button   mButton;
	EditText mEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//findViewById(R.layout.activity_main);
		
		 mButton = (Button)findViewById(R.id.button1);
		 mEdit   = (EditText)findViewById(R.id.editText1);

		 mButton.setOnClickListener(
				 new View.OnClickListener()
				 {
					 public void onClick(View view)
					 {
						 double ratio = TextTokenizer.deriveTypeTokenRatio(mEdit.getText().toString());
						 Log.v("MainActivity", "TTE for text = " + String.valueOf(ratio));
					 }
				 });
		
		Log.v("MainActivity","Converting Asset File to fake database");

		mockDB = new AndroidMock(getAssets());

		Collection<TextMessage> messages = mockDB.getAllMessage();
		for(TextMessage msg : messages) {
			double ratio = TextTokenizer.deriveTypeTokenRatio(msg.getText());
			Log.v("MainActivity", "TTR for message ID:" + msg.getId() + "From user "+ msg.getContact() + " is " + ratio);
		}
	}
}
