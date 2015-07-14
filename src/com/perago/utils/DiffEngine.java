/*
''-----------------------------------------------------
' Created by:  Carene Smith
' Created on:  13 July 2015
' Description: 
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.perago.entity.Person;
import com.perago.exceptions.ObjectsNotCompareableException;
import com.perago.exceptions.ObjectNotPatchableException;;

public class DiffEngine {
	
	/* constants */
	private static final Logger logger = Logger.getLogger(DiffEngine.class.getName());
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return Diff object (List of changes (AuditInfo objects) on the 2 objects that you compared)
	 * @throws ObjectsNotCompareableException
	 * 
	 * This will calculate the difference between sourceObject & targetObject
	 * 
	 */
	public Diff calculate(Object source, Object target) throws ObjectsNotCompareableException{
		
		logger.info("DiffEngine -- start");
		long time = System.currentTimeMillis();
		
		Diff differences = new Diff();
		try{
			//validation:
			if(source == null && target == null){
				throw new ObjectsNotCompareableException("DiffEngine::calculate error: Both source and target objects are null. \n "
						+ "This cannot be compared.");
			}
			
			if(target == null){
				logger.info("This can be a possible delete, in future implement this...");
			}
			
			// use reflection to determine changes on fields
			// target is the base of the comparison
			// compare results against source to generate an output in a format that 
			// target object is recreateable
			String parent = target.getClass().getSimpleName();
			List<Object> alreadyCompared = new ArrayList<Object>();
			
			differences.setSourceObjectClass(target.getClass());
			
			calculate(parent, source, target, differences, alreadyCompared);
			
		}catch(Exception e){
			logger.log(Level.SEVERE,  e.getMessage(), e);
		}finally{
			time = System.currentTimeMillis() - time;
			logger.info("DiffEngine -- done in " + time + "ms.");
		}
		return differences;
	}
	
	/**
	 * @param parent
	 * @param source
	 * @param target
	 * @param diffs
	 * @param alreadyCompared
	 */
	private void calculate(String parent, Object source, Object target, Diff differences, List<Object> alreadyCompared) {
		logger.info("calculate -- start: parent = " + parent + ", source = " + source + ", target = " + target);
		
		if (target != null && alreadyCompared.contains(target)){    
			return;
	    }else{
	        alreadyCompared.add(target);
	    }
		 
		if(target != null){
			logger.info("target class name = " + target.getClass().getName());  //Something like "com.perago.entity.Person"
			
			// first compare java object (lowest level) e.g something like "java.lang.String"
			if(target.getClass().getName().startsWith("java")){
				if(source == null){
					//log a create
					differences.getDifferences().add(new AuditInfo(parent, null, target));
				}else if(!target.equals(source)){
					//log an update
					differences.getDifferences().add(new AuditInfo(parent, source, target));
				}
			}else{
				
				for(Method method : target.getClass().getMethods() ){
					try{
						// Only use public getters & setters to determine field validity
						// Fields not readibly/modifyable is not patchable
						int mLength = method.getName().length();
						String propertyName = method.getName().substring(3, mLength);
						
						if (mLength > 3 &&
								method.getName().startsWith("get") &&
								!method.getName().startsWith("getClass") &&
								method.getReturnType() != null){
							
							//find the setter
							Method setterMethod = target.getClass().getMethod("set" + propertyName ,method.getReturnType());
							
							if(setterMethod != null){
								logger.info("Found valid property [" + propertyName + "] on parent [" + parent + "]");
								
								//call getter and iterate its's subset
								String tParent = parent + (parent.length() > 0 ? "." : "") + propertyName;
								Object srcObj = null;
								Object trgObj = null;
								
								try{
									if(source != null){
										srcObj = method.invoke(source,null);
									}
								}catch(Exception e){
									logger.log(Level.WARNING, e.getMessage(), e);
								}
								
								try{
									trgObj = method.invoke(target, null);
								}catch (Exception e){
									logger.log(Level.WARNING, e.getMessage(), e);
								}
								
								//calculate the differences
								calculate(tParent, srcObj, trgObj,differences, alreadyCompared);
								
							}
						}
						
						
					}catch(Exception e){
						logger.log(Level.WARNING, e.getMessage(), e);
					}
				}
			}
		}
		
	}

	/**
	 * 
	 * @param objectToApplyDiffOn
	 * @param differenceToApply
	 * @return
	 * @throws ObjectNotPatchableException
	 */
	public Person apply(Object  objectToApplyDiffOn, Diff differenceToApply) throws ObjectNotPatchableException{
		
		if(differenceToApply == null){
			logger.log(Level.WARNING, "Difference object is null or empty. Nothing to patch.");
			return null;
		}
		
		if(objectToApplyDiffOn == null){
			throw new ObjectNotPatchableException("DiffEngine::apply:objectToApplyDiffOn is null. Apply aborted.");
		}
		
		for(AuditInfo aInfo: differenceToApply.getDifferences() ){
			
			String fieldName = aInfo.getFieldName();
			
			//get setters and apply the diffs
			applyDiff( objectToApplyDiffOn,  aInfo.getFieldName(),aInfo.getTargetValue());
			
		}//end for aInfo
		
		
		return (Person) objectToApplyDiffOn;
	}
	
	/**
	 * 
	 * @param objectToApplyDiffOn
	 * @param fieldName
	 * @return
	 */
	private void applyDiff(Object objectToApplyDiffOn, String fieldName, Object newValue){
		logger.info("applyDiff for fieldName = " + fieldName);
		
		String[] fields = fieldName.split("\\.");
		logger.info("fields length = " + fields.length);
		
		if(fields.length <2)
			return;
			
		try {
			//Check if getter method exist, and then if its a nested class, e.g do getFriend(), if null, create instance and add fields
			Method getterMethod = objectToApplyDiffOn.getClass().getMethod("get" + fields[1]);
			
			if(getterMethod != null  && getterMethod.getParameterTypes().length == 0 && getterMethod.getReturnType() != null){
				if(!getterMethod.getReturnType().getName().startsWith("java")){
					
					logger.info("this is not a java type class, see if there is and instance of the class. getterMethod = " + getterMethod.getName());;
					
					//get the value. if there is none, you will have to create the new instance
					try {
						Object inner = getterMethod.invoke(objectToApplyDiffOn, null);
						if(inner == null){
							logger.info("inner is null, create new instance");
							inner = getterMethod.getReturnType().newInstance();
							Method setterMethod = objectToApplyDiffOn.getClass().getMethod("set" + fields[1],getterMethod.getReturnType() );
							setterMethod.invoke(objectToApplyDiffOn, inner);	
						}
						
						applyDiff(inner, fieldName.substring(fieldName.indexOf(".")+1,fieldName.length()), newValue);
					
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}else{
					//now call the setter on the java returnType e.g Person.setName()
					String setter = "set" + fields[1];  
					Method setterMethod = objectToApplyDiffOn.getClass().getMethod(setter,getterMethod.getReturnType() );
					
					if(setterMethod != null){
						
						try {
							setterMethod.invoke(objectToApplyDiffOn, newValue);
							logger.info("Calling setter set" + fields[1] + " with newvalue = "+ newValue);
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
				}//end java 
				
			}//end if getterMethod
			
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
