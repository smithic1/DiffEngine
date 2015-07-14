/*
''-----------------------------------------------------
' Created by:  Carene Smith
' Created on:  13 July 2015
' Description: . 
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

package com.perago.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Diff implements Serializable{

	/**
	 * Constant serialVersionUID. 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The list of all differences
	 */
	List<AuditInfo> differences = new LinkedList<AuditInfo>();
	
	/**
	 * Source object class
	 */
	private Class sourceObjectClass = null;
	
	/**
	 * 
	 */			
	public String toString(){
		String retVal = "";
		
		if(differences == null || differences.size() < 1)
			return "No differences logged.";
		
		int count = 1;
		int depth = differences.get(0).getFieldName().split("\\.").length;
		
		//retVal = String.format("Found %d differences: \n", differences.size());
		retVal += String.format("%d Updated %s\n", count,sourceObjectClass.getSimpleName());
		
		for(AuditInfo aInfo: differences ){
			String[] fields = aInfo.getFieldName().split("\\.");
			
			// e.g "Person.Friend.Name"	vs "Person.Name
			// This will only increment the counter if a new object was created. Using depth and length of fieldName to check this.
			if(fields.length > depth){
				//created
				count  +=1;
				retVal +=  String.format("%d CREATED \\  %s\n", count, fields[1]);	
			}
			
			retVal +=  String.format("%d UPDATED | %s\n", count,aInfo.toString());
			
			//recalc depth
			depth = fields.length;
						
		}
		
		return retVal;
	}
	
	

	/**
	 * @return differences
	 */
	public List<AuditInfo> getDifferences(){
		return differences;
	}
	
	/**
	 * @param differences
	 */
	public void setDifferences(List<AuditInfo> differences){
		this.differences = differences;
	}
	
	/**
	 * @return sourceObjectClass
	 */
	public Class getSourceObjectClass() {
		return sourceObjectClass;
	}

	/**
	 * @param sourceObjectClass
	 */
	public void setSourceObjectClass(Class sourceObjectClass) {
		this.sourceObjectClass = sourceObjectClass;
	}
	
}
