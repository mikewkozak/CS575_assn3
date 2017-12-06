/**
 * Mike Kozak (Student ID 10223568)
 * CS575 Assignment 3
 * 
 */
package com.drexel.cs575.credibilityassessor.datamodel;

/**
 * Simple container class. This SHOULD use Contact instead of String for the contact field, but GSON doesn't play well with nested classes
 * and I wanted to demonstrate functionality without messing with custom parsers.
 * @author Computer
 *
 */
public class TextMessage {
	private int id;
	private String contact;
	private String text;
	
	public TextMessage() {}
	
	public TextMessage(int id, String contact, String text) {
		this.id = id;
		this.contact = contact;
		this.text = text;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	
}
