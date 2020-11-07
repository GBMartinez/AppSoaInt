package com.soaint.demo;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Gustavo Martinez
 *
 */
public class App 
{
	
	public static final App INSTANCE = new App(true, false, false, true, false, false, new HashMap<String, String>());
	
	private static boolean logToFile;
    private static boolean logToConsole;
    private static boolean logMessage;
    private static boolean logWarning;
    private static boolean logError;
    private static boolean logToDatabase;
    private static Map<String, String> dbParams;
    private static Logger logger;
    
    private App(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
            boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map<String, String> dbParamsMap) { 
	    logger = Logger.getLogger("MyLog");
	    logError = logErrorParam;
	    logMessage = logMessageParam;
	    logWarning = logWarningParam;
	    logToDatabase = logToDatabaseParam;
	    logToFile = logToFileParam;
	    logToConsole = logToConsoleParam;
	    dbParams = dbParamsMap;
	}

	public static void setLogToFile(boolean logToFile) {
		App.logToFile = logToFile;
	}

	public static void setLogToConsole(boolean logToConsole) {
		App.logToConsole = logToConsole;
	}

	public static void setLogMessage(boolean logMessage) {
		App.logMessage = logMessage;
	}

	public static void setLogWarning(boolean logWarning) {
		App.logWarning = logWarning;
	}

	public static void setLogError(boolean logError) {
		App.logError = logError;
	}

	public static void setLogToDatabase(boolean logToDatabase) {
		App.logToDatabase = logToDatabase;
	}

	public static void setDbParams(Map<String, String> dbParams) {
		App.dbParams = dbParams;
	}

	public static void setLogger(Logger logger) {
		App.logger = logger;
	}

	public static void LogMessage(String messageText, boolean message, boolean warning, boolean error) throws Exception, SQLException, IOException, SecurityException {
        
        if (messageText == null || messageText.length() == 0) {
            return;
        }
        
        messageText = messageText.trim();
        
        
        if (!logToConsole && !logToFile && !logToDatabase) {
            throw new Exception("Invalid configuration");
        }
        if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
            throw new Exception("Error or Warning or Message must be specified");
        }

        Connection connection = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", dbParams.get("userName"));
        connectionProps.put("password", dbParams.get("password"));

        connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
                + ":" + dbParams.get("portNumber") + "/" + dbParams.get("databaseName"), connectionProps);


        int t = 0;
        if (message && logMessage) {
            t = 1;
        }

        if (error && logError) {
            t = 2;
        }

        if (warning && logWarning) {
            t = 3;
        }

        Statement stmt = connection.createStatement();

        String l = null;
        File logFile = new File(dbParams.get("logFileFolder") + "/logFile.log");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
        ConsoleHandler ch = new ConsoleHandler();

        if (error && logError) {
            l = l + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if (warning && logWarning) {
            l = l + "warning " +DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if (message && logMessage) {
            l = l + "message " +DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if(logToFile) {
            logger.addHandler(fh);
            logger.log(Level.INFO, messageText);
        }

        if(logToConsole) {
            logger.addHandler(ch);
            logger.log(Level.INFO, messageText);
        }

        if(logToDatabase) {
        	stmt.executeUpdate("insert into Log_Values('" + message + "') values ('" + String.valueOf(t) + "')");
        }
    }
	  
    public static void main( String[] args ) throws SQLException, IOException, Exception
    {
    	    	
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
    	instance.setLogger(Logger.getLogger("SOAINT Logger"));
    	
    	instance.LogMessage("hello", true, false, false);
    	
    }
}
