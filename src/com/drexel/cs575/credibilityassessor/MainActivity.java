package com.drexel.cs575.credibilityassessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.drexel.cs575.credibilityassessor.datamodel.AndroidAdapter;
import com.drexel.cs575.credibilityassessor.datamodel.AndroidMock;
import com.drexel.cs575.credibilityassessor.datamodel.Contact;
import com.drexel.cs575.credibilityassessor.datamodel.TextMessage;
import com.drexel.cs575.credibilityassessor.util.TextTokenizer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	final boolean USE_MOCK_DB = true;
	AndroidMock mockDB;
	AndroidAdapter androidDB;

	//UI elements
	Button   rawTextButton;
	EditText mEdit;
	Spinner contactsSpinner;
	Button   contactsButton;
	TextView resultsField;


	final int REQUEST_CODE_SMS_ASK_PERMISSIONS = 123;
	final int REQUEST_CODE_CONTACTS_ASK_PERMISSIONS = 124;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//findViewById(R.layout.activity_main);

		rawTextButton = (Button)findViewById(R.id.textButton);
		mEdit   = (EditText)findViewById(R.id.editText1);
		contactsSpinner   = (Spinner)findViewById(R.id.spinner1);
		contactsButton = (Button)findViewById(R.id.contactButton);
		resultsField = (TextView)findViewById(R.id.tokenizeResults);

		rawTextButton.setOnClickListener(
				new View.OnClickListener()
				{
					public void onClick(View view)
					{
						double ratio = TextTokenizer.deriveTypeTokenRatio(mEdit.getText().toString());
						resultsField.setText("TTE for text = " + String.valueOf(ratio));
					}
				});

		contactsButton.setOnClickListener(
				new View.OnClickListener()
				{
					public void onClick(View view)
					{
						Log.v("contactsButton", "Getting texts for = " + contactsSpinner.getSelectedItem());
						TextMessage msg = mockDB.getMessageByName((Contact)contactsSpinner.getSelectedItem());
						double ratio = TextTokenizer.deriveTypeTokenRatio(msg.getText());
						resultsField.setText("TTE for text from " + msg.getContact() + " = " + String.valueOf(ratio));
					}
				});

		Log.v("MainActivity","Converting Asset File to fake database");

		mockDB = new AndroidMock(getAssets());
		androidDB = new AndroidAdapter(getContentResolver());

		loadContacts();

		loadSMSMessages();


	}

	protected void loadContacts() {
		Log.v("MainActivity","Loading Contacts");

		List<Contact> contacts = new ArrayList<Contact>();
		if(USE_MOCK_DB) {
			//TEST USING MOCK DB
			contacts = new ArrayList<Contact>(mockDB.getAllContacts());
		} else {
			//TEST USING VM CONTACTS
			Log.v("MainActivity", "Requesting Contacts permissions");
			if(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
				Log.v("MainActivity", "READ_CONTACTS PERMISSIONS GRANTED");
				contacts = new ArrayList<Contact>(androidDB.getAllContacts());
			} else {
				requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_CONTACTS_ASK_PERMISSIONS);
			}
		}

		ArrayAdapter<Contact> dataAdapter = new ArrayAdapter<Contact>(this,
				android.R.layout.simple_spinner_item, contacts);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		contactsSpinner.setAdapter(dataAdapter);
	}


	//courtesy of stack overflow
	protected void loadSMSMessages() {
		Log.v("MainActivity","Reading in all text messages");
		if(USE_MOCK_DB) {
			//TEST USING MOCK DB
			Collection<TextMessage> messages = mockDB.getAllMessage();
			for(TextMessage msg : messages) {
				double ratio = TextTokenizer.deriveTypeTokenRatio(msg.getText());
				Log.v("MainActivity", "TTR for message ID:" + msg.getId() + "From user "+ msg.getContact() + " is " + ratio);
			}
		} else {
			Log.v("MainActivity", "Requesting SMS permissions");
			if(checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
				Log.v("MainActivity", "READ_SMS PERMISSIONS GRANTED");
				Collection<TextMessage> messages = androidDB.getAllMessage();
				for(TextMessage msg : messages) {
					double ratio = TextTokenizer.deriveTypeTokenRatio(msg.getText());
					Log.v("MainActivity", "TTR for message ID:" + msg.getId() + "From user "+ msg.getContact() + " is " + ratio);
				}
			} else {
				requestPermissions(new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_SMS_ASK_PERMISSIONS);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
		case REQUEST_CODE_SMS_ASK_PERMISSIONS:
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				loadSMSMessages();
			} else {
				// Permission Denied
				Log.v("MainActivity", "READ_SMS PERMISSIONS DENIED");
			}
			break;
		case REQUEST_CODE_CONTACTS_ASK_PERMISSIONS:
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				loadContacts();
			} else {
				// Permission Denied
				Log.v("MainActivity", "READ_CONTACTS PERMISSIONS DENIED");
			}
			break;
		default:
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
