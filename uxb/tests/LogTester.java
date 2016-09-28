package eecs293.uxb.tests;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import eecs293.uxb.devices.AbstractDevice;

public class LogTester {
	
	private static LogTester logTester;
	
	protected LogHandler logHandler = null;
	
	public static LogTester getInstance() {
		if (logTester == null) {
			logTester = new LogTester();
		}
		return logTester;
	}
	
	public static LogHandler initializeLogTester() {
		return getInstance().getLogHandler();
	}
	
	private LogHandler getLogHandler() {
		if (logHandler == null) {
			Logger logger = Logger.getLogger(AbstractDevice.class.getName());
			logger.setUseParentHandlers(false);		
			logHandler = new LogHandler();
			logger.addHandler(logHandler);
		}
		
		return logHandler;
	}

	// Adding appender to global logger for testing purposes
	// http://stackoverflow.com/a/1834789
	public class LogHandler extends Handler {
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
