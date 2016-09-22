package eecs293.uxb.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eecs293.uxb.Connector;
import eecs293.uxb.Connector.Type;
import eecs293.uxb.devices.AbstractDevice;
import eecs293.uxb.devices.Device;
import eecs293.uxb.devices.DeviceClass;
import eecs293.uxb.devices.Hub;
import eecs293.uxb.devices.Hub.Builder;
import eecs293.uxb.devices.peripherals.printers.CannonPrinter;
import eecs293.uxb.devices.peripherals.printers.SisterPrinter;
import eecs293.uxb.devices.peripherals.video.GoAmateur;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.Message;
import eecs293.uxb.messages.StringMessage;

public class Tester {
	
	private static final int TEST_VERSION_NUMBER = 3;
	private static final int TEST_PRODUCT_CODE = 7;
	private static final BigInteger TEST_SERIAL_NUMBER = BigInteger.valueOf(9);

	// Tests depend on the order of this list.
	public static final List<Type> CONNECTORS_TYPES = Arrays.asList(Connector.Type.COMPUTER, Connector.Type.PERIPHERAL);
	public static final List<Type> ONLY_PERIPHERALS = Arrays.asList(Connector.Type.PERIPHERAL, Connector.Type.PERIPHERAL);
	public static final List<Message> MESSAGES = Arrays.asList(
			new StringMessage("The first message is a string."), 
			new BinaryMessage(BigInteger.valueOf(2)),
			new StringMessage("The third message is a string."),
			new StringMessage("The fourth message is a string."),
			new BinaryMessage(BigInteger.valueOf(5)));
	
	private Hub.Builder badBuilder;
	private Hub.Builder goodBuilder;
	
	@Before
	public void setUp() {
		badBuilder = new Builder(TEST_VERSION_NUMBER);
		goodBuilder = new Builder(TEST_VERSION_NUMBER).connectors(CONNECTORS_TYPES);
	}
	
	@After
	public void tearDown() {
		this.badBuilder = null;
		this.goodBuilder = null;
	}
	
	@Test
	public void testNullVersion() {
	    try {
	    	badBuilder = null;
	    	new Builder(null).build();
	        fail("Expected an exception to be thrown.");
	    } catch (NullPointerException e) {
	    	assertTrue(e.getMessage().equals(AbstractDevice.Builder.NULL_VERSION_NUMBER_MESSAGE));
	    }
	}
	
	@Test
	public void testNoConnectors() {
	    try {
	    	badBuilder.build();
	        fail("Expected an exception to be thrown.");
	    } catch (IllegalStateException e) {
	    	assertTrue(e.getMessage().equals(Hub.Builder.MISSING_COMPUTER_CONNECTOR_MESSAGE));
	    }
	}
	
	@Test
	public void testNoPerhipherals() {
	    try {
	    	List<Type> connectors = Arrays.asList(Connector.Type.COMPUTER);
	    	badBuilder.connectors(connectors).build();
	        fail("Expected an exception to be thrown.");
	    } catch (IllegalStateException e) {
	        assertTrue(e.getMessage().equals(Hub.Builder.MISSING_PERIPHERAL_CONNECTOR_MESSAGE));
	    }
	}
	
	@Test
	public void testSuccessfulHubCreation() {
    	goodBuilder.build();
	}
	
	@Test
	public void testDeviceClassOverride() {
    	assertEquals(goodBuilder.build().getDeviceClass(), (DeviceClass.HUB));
	}
	
	@Test
	public void testGetConnector() {
    	Hub hub = goodBuilder.build();
    	assertTrue(hub.getConnectorCount() == CONNECTORS_TYPES.size());
    	assertEquals(hub.getConnector(0).getType(), Connector.Type.COMPUTER);
    	assertEquals(hub.getConnector(1).getType(), Connector.Type.PERIPHERAL);
	}
	
	@Test
	public void testGetConnectorOutOfBounds() {
    	Hub hub = goodBuilder.build();   
    	assertEquals(hub.getConnector(-1), null);
    	assertEquals(hub.getConnector(2), null);
	}
	
	@Test
	public void testUnsetProductCode() {
    	assertEquals(goodBuilder.build().getProductCode(), Optional.empty());
	}
	
	@Test
	public void testSetProductCode() {
		Hub hub = goodBuilder.productCode(TEST_PRODUCT_CODE).build();
    	assertTrue(hub.getProductCode().get() == TEST_PRODUCT_CODE);
	}
	
	@Test
	public void testUnsetSerialNumber() {
    	assertEquals(goodBuilder.build().getSerialNumber(), Optional.empty());
	}
	
	@Test
	public void testSetSerialNumber() {
		Hub hub = goodBuilder.serialNumber(TEST_SERIAL_NUMBER).build();    	
    	assertEquals(hub.getSerialNumber().get(), TEST_SERIAL_NUMBER);
	}
	
	@Test
	public void testTalking() {
		LogTester logTester = initializeHandler();
		Hub hub = new Builder(3).connectors(CONNECTORS_TYPES).build();
		SisterPrinter sisterPrinter = ((SisterPrinter.Builder) (new SisterPrinter.Builder(1).connectors(ONLY_PERIPHERALS))).build();
		CannonPrinter cannonPrinter = ((CannonPrinter.Builder) (new CannonPrinter.Builder(1).connectors(ONLY_PERIPHERALS))).build();
		GoAmateur goAmateur = ((GoAmateur.Builder) (new GoAmateur.Builder(1).connectors(ONLY_PERIPHERALS))).build();
		broadcast(Arrays.asList(hub, sisterPrinter, cannonPrinter, goAmateur), MESSAGES, logTester);
	}

	private LogTester initializeHandler() {
		Logger logger = Logger.getGlobal();
		logger.setUseParentHandlers(false);		
		LogTester logTester = new LogTester();
		logger.addHandler(logTester);
		return logTester;
	}
	
	private void broadcast(List<Device> devices, List<Message> messages, LogTester handler) {
		for (Message message : messages) {
			for (Device device : devices) {
				message.reach(device, device.getConnector(0));
				assertTrue(handler.checkLastLevel(Level.INFO));
				if (device.getDeviceClass() == DeviceClass.HUB) {
					assertTrue(handler.checkLastMessageContains(Hub.NOT_YET_SUPPORTED_MESSAGE));
				} else if (device.getDeviceClass() == DeviceClass.PRINTER) {
					assertTrue(handler.checkLastMessageContains("printer has printed"));
				} else {
					assertTrue(handler.checkLastMessageContains("not"));
				}
			}
		}
	}
	
	// Adding appender to global logger for testing purposes
	// http://stackoverflow.com/a/1834789
	private class LogTester extends Handler {
		private String message = "";
	    private Level level = Level.ALL;
	    
	    public boolean checkLastMessageContains(String expected) {
	        return message.contains(expected);
	    }
	    
	    public boolean checkLastLevel(Level expected) {
	        return level.equals(expected);
	    }

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
