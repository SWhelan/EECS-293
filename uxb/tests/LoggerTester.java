package eecs293.uxb.tests;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import eecs293.uxb.devices.AbstractDevice;

public class LoggerTester {
	
	private static LoggerTester loggerTester;
	
	protected LogTester logTester = null;
	
	public static LoggerTester getInstance() {
		if (loggerTester == null) {
			loggerTester = new LoggerTester();
		}
		return loggerTester;
	}
	
	public static LogTester initializeLogTester() {
		return getInstance().getLogTester();
	}
	
	private LogTester getLogTester() {
		if (logTester == null) {
			Logger logger = Logger.getLogger(AbstractDevice.class.getName());
			logger.setUseParentHandlers(false);		
			logTester = new LogTester();
			logger.addHandler(logTester);
		}
		
		return logTester;
	}
	

	// Adding appender to global logger for testing purposes
	// http://stackoverflow.com/a/1834789
	public class LogTester extends Handler {
		private String message = "";
	    private Level level = Level.ALL;
	    
	    public boolean checkLastMessageContains(String expected) {
	        return message.contains(expected);
	    }
	    
	    public boolean checkLastLevel(Level expected) {
	        return level.equals(expected);
	    }

	    @Override
	    public void publish(LogRecord record) {
	    	message = record.getMessage();
	        level = record.getLevel();
	    }

		@Override
		public void flush() {
			// Do Nothing
		}

		@Override
		public void close() throws SecurityException {
			// Do Nothing			
		}
	}
	
}
