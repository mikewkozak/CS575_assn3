package com.drexel.cs575.credibilityassessor.datamodel;

import java.util.Collection;
import java.util.HashMap;

public interface ITextMessages {
	public TextMessage getMessageByName(String name);
    
    public Collection<TextMessage> getAllMessage();
}
