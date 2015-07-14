/*
''-----------------------------------------------------
' Created by:  Carene Smith
' Created on:  13 July 2015
' Description:  
'				
' 
'
' Notes: 
' .
'
' Amendments:
'
' ChangeNo.		Name			Date				 Case ID/Description
' 001           	
''----------------------------------------------------------------------------------------------------------------------
*/
package com.perago.utils;

import java.io.Serializable;


public class AuditInfo implements Serializable {
	/**
	 * Constant serialVersionUID
	 */
	private static final long serialVersionUID = 2L;
	
	private String fieldName 		= null;
	private Object originalValue 	= null;
	private Object targetValue 		= null;
	
	
	/**
	 * Constructor to instantiate new differences.
	 */
	public AuditInfo(String fieldName, Object originalValue, Object targetValue){
		this.fieldName 		= fieldName;
		this.originalValue  = originalValue;
		this.targetValue 	= targetValue;
	}
	
	@Override
	public String toString() {
		
		String retVal = "";
		
		retVal += String.format(" %s was [%s] now [%s]", fieldName.split("\\.")[fieldName.split("\\.").length-1], originalValue, targetValue);
				
		return retVal;
	}



	/**
	 * @return fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	
	/**
	 * @param fieldName
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return originalValue
	 */
	public Object getOriginalValue() {
		return originalValue;
	}

	/**
	 * @param originalObject
	 */
	public void setOriginalValue(Object originalValue) {
		this.originalValue = originalValue;
	}

	/**
	 * @return targetObject
	 */
	public Object getTargetValue() {
		return targetValue;
	}

	/**
	 * @param targetObject
	 */
	public void setTargetValue(Object targetValue) {
		this.targetValue = targetValue;
	}
	
}
