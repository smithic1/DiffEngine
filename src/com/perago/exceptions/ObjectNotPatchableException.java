/*
''-----------------------------------------------------
' Created by:  Carene Smith
' Created on:  13 July 2015
' Description: Custom Exception  
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

public class ObjectNotPatchableException  extends Exception {
	
	/**
	 * Constructor for ObjectNotPatchableException
	 * @param message
	 */
	public ObjectNotPatchableException(String message){
		super(message);
	}
}	