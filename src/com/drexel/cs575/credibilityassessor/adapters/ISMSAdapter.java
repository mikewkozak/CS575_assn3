/**
 * Mike Kozak (Student ID 10223568)
 * CS575 Assignment 3
 * 
 */
package com.drexel.cs575.credibilityassessor.adapters;

import java.util.Collection;

import com.drexel.cs575.credibilityassessor.datamodel.Contact;
import com.drexel.cs575.credibilityassessor.datamodel.TextMessage;

/**
 * This interface represents the adapter that sits between the main class and the model and allows for using the same calls
 * @author Computer
 *
 */
public interface ISMSAdapter {
	
	/**
	 * This returns a text message representing all the text associated with the given contact
	 * @param name
	 * @return
	 */
	public TextMessage getMessageByName(Contact name);
    
	/**
	 * This gets all text messages in the DB across all contacts
	 * @return
	 */
    public Collection<TextMessage> getAllMessage();
    
    /**
     * This gets all contacts, even ones that may not have sent any text messages
     * @return
     */
    public Collection<Contact> getAllContacts();
}
