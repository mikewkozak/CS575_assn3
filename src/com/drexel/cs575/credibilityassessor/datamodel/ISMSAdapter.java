package com.drexel.cs575.credibilityassessor.datamodel;

import java.util.Collection;

public interface ISMSAdapter {
	public TextMessage getMessageByName(Contact name);
    
    public Collection<TextMessage> getAllMessage();
    
    public Collection<Contact> getAllContacts();
}
