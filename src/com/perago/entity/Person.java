/*
''-----------------------------------------------------
' Created by:  Carene Smith
' Created on:  13 July 2015
' Description: Persons Class. 
'				
' 
'
' Notes: 
' Making this Serializable so that you can persist the changes.
'
' Amendments:
'
' ChangeNo.		Name			Date				 Case ID/Description
' 001           	
''----------------------------------------------------------------------------------------------------------------------
*/
package com.perago.entity;


import java.io.Serializable;

public class Person implements Serializable {

	private static final long serialVersionUID = -8026663665922336524L;
	
	/** instance variables
	 * 
	 */
	private String name;
	private String surname;
	private Person friend;
	private Person brother;

	
	public Person getBrother() {
		return brother;
	}

	public void setBrother(Person brother) {
		this.brother = brother;
	}

	/**
	 * Gets the name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the new name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the surname
	 * @return surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Set the new surname
	 * @param surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * Gets the friend
	 * @return friend
	 */
	public Person getFriend() {
		return friend;
	}

	/**
	 * Set the new friend
	 * @param friend
	 */
	public void setFriend(Person friend) {
		this.friend = friend;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	  
	@Override
	public int hashCode() {
		
		//TODO change this. If you add a new property on the class, you need to change this too.
		final int prime = 31;
		int result = 1;
		result = prime * result + ((friend == null) ? 0 : friend.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		//TODO change this. If you add a new property on the class, you need to change this too.
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (friend == null) {
			if (other.friend != null)
				return false;
		} else if (!friend.equals(other.friend))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return String.format("[%s: %s, %s, Friend: %s]", getClass().getSimpleName(),
                name, surname, (friend != null ? friend.toString() : "None"));
	}
}
