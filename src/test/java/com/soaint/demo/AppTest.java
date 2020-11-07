package com.soaint.demo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    

	
    public void testApp()
    {
    	
    	try {
    		
        	App instance = App.INSTANCE;
        	
        	Map<String, String> params = new HashMap<String, String>();
        	params.put("userName", "user");
        	params.put("password", "123");
        	params.put("dbms", "mysql");
        	params.put("serverName", "localhost");
        	params.put("portNumber", "8080");
        	params.put("databaseName", "soaintdb");
        	params.put("logFileFolder", "C://files");
        	params.put("logFileFolder", "logFiles");
        	
        	instance.setDbParams(params);
        	instance.setLogToFile(true);
        	instance.setLogToConsole(false);
        	instance.setLogMessage(false);
        	instance.setLogWarning(true);
        	instance.setLogError(false);
        	instance.setLogToDatabase(false);
        	instance.setDbParams(params);
        	instance.setLogger(Logger.getLogger("MyLog"));
        	
        	instance.LogMessage("hello", true, false, false);
        	
        	assertTrue("Conexión a base de datos",false);
        	assertTrue("Creación de archivo",false);
        	assertTrue("Inserción de datos",false);
        	
    	}catch(SecurityException se) {
    		
    	} catch (SQLException sqe) {
    		
    	} catch (IOException ioe) {
    		
    	} catch (Exception e) {
    		
    	}
    	        
    }
}
