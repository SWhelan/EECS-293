package eecs293.uxb.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eecs293.uxb.connectors.Connector;
import eecs293.uxb.connectors.Connector.Type;
import eecs293.uxb.devices.AbstractDevice;
import eecs293.uxb.devices.DeviceClass;
import eecs293.uxb.devices.Hub;
import eecs293.uxb.devices.Hub.Builder;
import eecs293.uxb.devices.peripherals.printers.CannonPrinter;
import eecs293.uxb.devices.peripherals.printers.SisterPrinter;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.Message;
import eecs293.uxb.messages.StringMessage;
import eecs293.uxb.tests.LoggerTester.LogTester;

public class Tester {
	
	public static final int TEST_VERSION_NUMBER = 3;
	public static final int TEST_PRODUCT_CODE = 7;
	public static final BigInteger TEST_SERIAL_NUMBER = BigInteger.valueOf(9);

	// Tests depend on the order of this list.
	public static final List<Type> CONNECTOR_TYPES = Arrays.asList(Connector.Type.COMPUTER, Connector.Type.PERIPHERAL);
	public static final List<Type> ONLY_PERIPHERALS = Arrays.asList(Connector.Type.PERIPHERAL, Connector.Type.PERIPHERAL);
	public static final List<Message> MESSAGES = Arrays.asList(
			new StringMessage("The first message is a string."), 
			new BinaryMessage(BigInteger.valueOf(2)),
			new StringMessage("The third message is a string."),
			new StringMessage("The fourth message is a string."),
			new BinaryMessage(BigInteger.valueOf(5)));
	
	public Hub.Builder badBuilder;
	public static Hub.Builder goodBuilder;
	
	// Runs once per test before the test is run.
	@Before
	public void setUp() {
		badBuilder = new Builder(TEST_VERSION_NUMBER);
		goodBuilder = new Builder(TEST_VERSION_NUMBER).connectors(CONNECTOR_TYPES);
	}
	
	// Runs once per test after the test completes.
	@After
	public void tearDown() {
		this.badBuilder = null;
		goodBuilder = null;
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
    	assertTrue(hub.getConnectorCount() == CONNECTOR_TYPES.size());
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
	public void testSisterPrinter() {
		LogTester logTester = LoggerTester.initializeLogTester();
		SisterPrinter sisterPrinter = new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		int testValue = 2;
		BinaryMessage message = new BinaryMessage(BigInteger.valueOf(testValue));
		message.reach(sisterPrinter, sisterPrinter.getConnector(0));
		assertTrue(logTester.checkLastMessageContains(Integer.toString(testValue)));
	}
	
	@Test
	public void testCannonPrinter() {
		LogTester logTester = LoggerTester.initializeLogTester();
		CannonPrinter cannonPrinter = new CannonPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		String testValue = "super helpful test";
		StringMessage message = new StringMessage(testValue);
		message.reach(cannonPrinter, cannonPrinter.getConnector(0));
		assertTrue(logTester.checkLastMessageContains(testValue));
	}
	
}
