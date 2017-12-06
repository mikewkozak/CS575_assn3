package com.drexel.cs575.credibilityassessor.datamodel;

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
