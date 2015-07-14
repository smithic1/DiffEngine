/*
''-----------------------------------------------------
' Created by:  Carene Smith
' Created on:  13 July 2015
' Description: Exception class used when DiffEngine fails to compare 2 objects .  
'				
' 
'
' Notes: 
' 
'
' Amendments:
'
' ChangeNo.		Name			Date				 Case ID/Description
' 001           	
''----------------------------------------------------------------------------------------------------------------------
*/
package com.perago.exceptions;

public class ObjectsNotCompareableException extends Exception {
	
	/**
	 * Constructor for ObjectsNotCompareableException
	 * @param message
	 */
	public ObjectsNotCompareableException(String message){
		super(message);
	}
}
