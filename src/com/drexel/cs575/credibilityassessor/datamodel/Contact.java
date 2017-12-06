package com.drexel.cs575.credibilityassessor.datamodel;

public class Contact {
	private String name;
	
	public Contact(String myName) {
		name = myName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		} else if(obj == this) {
			return true;
		} else if (obj instanceof Contact) {
			return ((Contact) obj).getName().equals(getName());
		} else {
			return false;
		}
	}
}
