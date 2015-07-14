/*
''-----------------------------------------------------
' Created by:  Carene Smith
' Created on:  13 July 2015
' Description: Class to test Difference Engine.  
'				
' 
'
' Notes: For now this will compare POJO's. 
'        Phase 2 should include checks for primitive wrappers and collections
' 
'
' Amendments:
'
' ChangeNo.		Name			Date				 Case ID/Description
' 001           	
''----------------------------------------------------------------------------------------------------------------------
*/
package test.com.perago;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.perago.entity.Person;
import com.perago.utils.*;
import com.perago.exceptions.*;;


public class DiffEngineTest {
	
	/* Initialize the logger */
	private static final Logger logger = Logger.getLogger(DiffEngineTest.class.getName());
	
	/**
	 * Method to print changes
	 * @param changes
	 */
	static void printDiff(Diff differences){
		System.out.println(differences.toString());
	}
	
	/**
	 * main method for testing our diff engine
	 * @param args
	 */
	public static void main(String[] args) {
		
		logger.info("DiffEngineTest -- start");
		
		long time = System.currentTimeMillis();
		
		try{
			Person firstActor = new Person();
			firstActor.setName("Arnold");
			firstActor.setSurname("Schwarzenegger");
		
			Person secondActor = new Person();
			secondActor.setName("Tom");
			secondActor.setSurname("Cruise");
		
			Person thirdActor = new Person();
			thirdActor.setName("Sylvester");
			thirdActor.setSurname("Stallone");
			
			secondActor.setFriend(thirdActor);
			
			DiffEngine de = new DiffEngine();
			Diff differences = de.calculate(firstActor, secondActor);
		
			System.out.println("simpleTest Diff");
			
			printDiff(differences);
		
			try {
				firstActor = de.apply(firstActor, differences);
				System.out.println("firstActor == secondActor : " + firstActor.equals(secondActor));
			} catch (ObjectNotPatchableException ex) {
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			}    
		}catch (ObjectsNotCompareableException once){
			logger.info(once.getMessage());
		}finally{
			time = System.currentTimeMillis() - time;
	        logger.info("DiffEngineTest -- Completed in " + time + "ms.");
		}
        
        
	}
}

