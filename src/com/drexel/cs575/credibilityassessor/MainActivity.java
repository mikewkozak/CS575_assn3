/**
 * Mike Kozak (Student ID 10223568)
 * CS575 Assignment 3
 * 
 */
package com.drexel.cs575.credibilityassessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.drexel.cs575.credibilityassessor.adapters.AndroidAdapter;
import com.drexel.cs575.credibilityassessor.adapters.AndroidMock;
import com.drexel.cs575.credibilityassessor.datamodel.Contact;
import com.drexel.cs575.credibilityassessor.datamodel.TextMessage;
import com.drexel.cs575.credibilityassessor.filters.ReportGenerator;
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

/**
 * This is the main activity that controls the UI. It acts as the "controller" for the UI and interacts with the model when
 * callbacks arrive from the view.
 * @author Computer
 *
 */
public class MainActivity extends Activity {

	//IF true, will load contacts and texts from a JSON file. If false, will request permissions from the OS and pull actual data from the device
	final boolean USE_MOCK_DB = true;
	
	//Constants
	final int REQUEST_CODE_SMS_ASK_PERMISSIONS = 123;
	final int REQUEST_CODE_CONTACTS_ASK_PERMISSIONS = 124;
	
	//The Adapters used for interacting with the underlying OS
	private AndroidMock mockDB;
	private AndroidAdapter androidDB;
	
	//This class handles processing data through util functions to produce reports
	private ReportGenerator reportGenerator = new ReportGenerator();

	//UI elements for adding listeners and updating labels dynamically
	private Button   rawTextButton;
	private EditText rawTextField;
	private Spinner contactsSpinner;
	private Button   contactsButton;
	private TextView resultsField;

	/**
	 * The initialize function. Grabs the UI elements, adds listeners, and populates the caches
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Grabs the UI elements
		rawTextButton = (Button)findViewById(R.id.textButton);
		rawTextField   = (EditText)findViewById(R.id.rawText);
		contactsSpinner   = (Spinner)findViewById(R.id.spinner1);
		contactsButton = (Button)findViewById(R.id.contactButton);
		resultsField = (TextView)findViewById(R.id.tokenizeResults);

		//When the user enters in raw text and hits the Tokenize button, parse the text and generate a TTR
		rawTextButton.setOnClickListener(
				new View.OnClickListener()
				{
					public void onClick(View view)
					{
						TextMessage msg = new TextMessage(-1,"",rawTextField.getText().toString());
						String report = reportGenerator.generateTTRReport(msg);
						resultsField.setText(report);
					}
				});

		//When the user selects a contact from the list and hits the Tokenize button, pull the text history for the contact generate a TTR
		contactsButton.setOnClickListener(
				new View.OnClickListener()
				{
					public void onClick(View view)
					{
						Log.v("contactsButton", "Getting texts for = " + contactsSpinner.getSelectedItem());
						TextMessage msg = mockDB.getMessageByName((Contact)contactsSpinner.getSelectedItem());
						
						//the DB always returns a TextMessage. If nothing is found a "None" Text will be returned
						String report = reportGenerator.generateTTRReport(msg);
						resultsField.setText(report);
					}
				});

		//initialize the adapters. Pass in the context necessary to process requests
		mockDB = new AndroidMock(getAssets());
		androidDB = new AndroidAdapter(getContentResolver());

		//Now that the UI elements are initialized, let's populate them
		loadContacts();
		loadSMSMessages();


	}

	/**
	 * This populates the contacts spinner with names
	 */
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

		//add the contacts to the spinner
		ArrayAdapter<Contact> dataAdapter = new ArrayAdapter<Contact>(this,
				android.R.layout.simple_spinner_item, contacts);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		contactsSpinner.setAdapter(dataAdapter);
	}


	/**
	 * This loads all the text messages and generates TTR for each as logs for testing. Eventually this should connect to the Contacts list
	 * above, but for testing they have to be seperate since "unsent" texts in VM don't have a populated "name" field and the VM can't receive
	 * texts
	 */
	protected void loadSMSMessages() {
		Log.v("MainActivity","Reading in all text messages");
		if(USE_MOCK_DB) {
			
			//TEST USING MOCK DB
			Collection<TextMessage> messages = mockDB.getAllMessage();
			for(TextMessage msg : messages) {
				String report = reportGenerator.generateTTRReport(msg);
				Log.v("MainActivity",report);
			}
			
		} else {
			
			Log.v("MainActivity", "Requesting SMS permissions");
			if(checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
				
				Log.v("MainActivity", "READ_SMS PERMISSIONS GRANTED");
				Collection<TextMessage> messages = androidDB.getAllMessage();
				for(TextMessage msg : messages) {
					String report = reportGenerator.generateTTRReport(msg);
					Log.v("MainActivity",report);
				}
				
			} else {
				
				requestPermissions(new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_SMS_ASK_PERMISSIONS);
				
			}
			
		}
	}

	/**
	 * This is automatically called by the OS in response to a request for permissions to access account data. This is required
	 * for Android OS 23 or later as a new security feature and WILL NOT be compatible with older android versions
	 */
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
