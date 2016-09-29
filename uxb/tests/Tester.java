package eecs293.uxb.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eecs293.uxb.connectors.ConnectionException;
import eecs293.uxb.connectors.Connector;
import eecs293.uxb.connectors.Connector.Type;
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
import eecs293.uxb.tests.LogTester.LogHandler;

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
	public void testBroadcast() {
		Hub hub = goodBuilder.build();
		SisterPrinter sisterPrinter = new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		CannonPrinter cannonPrinter = new CannonPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		GoAmateur goAmateur = new GoAmateur.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		broadcast(Arrays.asList(hub, sisterPrinter, cannonPrinter, goAmateur), MESSAGES, LogTester.initializeLogTester());
	}
	
	private void broadcast(List<Device> devices, List<Message> messages, LogHandler logTester) {
		for (Message message : messages) {
			for (Device device : devices) {
				message.reach(device, device.getConnector(0));
				assertTrue(logTester.checkLastLevel(Level.INFO));
				if (device.getDeviceClass() == DeviceClass.HUB) {
					assertTrue(logTester.checkLastMessageContains(Hub.NOT_YET_SUPPORTED_MESSAGE));
				} else if (device.getDeviceClass() == DeviceClass.PRINTER) {
					assertTrue(logTester.checkLastMessageContains("printer has printed"));
				} else {
					assertTrue(logTester.checkLastMessageContains("not"));
				}
			}
		}
	}
	
	@Test
	public void testIsReachable() {
		Hub hub = goodBuilder.build();
		SisterPrinter sisterPrinter = new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		try {
			sisterPrinter.getConnector(0).setPeer(hub.getConnector(0));
		} catch (ConnectionException e) {
			fail("This is not supposed to throw an exception.");
		}
		
	}
	
	@Test
	public void testSetPeer() {
		Hub hub = goodBuilder.build();
		SisterPrinter sisterPrinter = new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		try {
			sisterPrinter.getConnector(0).setPeer(hub.getConnector(0));
		} catch (ConnectionException e) {
			fail("This is not supposed to throw an exception.");
		}
		
		assertTrue(sisterPrinter.isReachable(hub));
		assertFalse(hub.isReachable(sisterPrinter));
		
		Set<Device> temp = sisterPrinter.peerDevices();
		assertTrue(temp.contains(hub));
		assertTrue(temp.size() == 1);
		
		try {
			sisterPrinter.getConnector(0).setPeer(hub.getConnector(0));
			fail("This is supposed to throw an exception.");
		} catch (ConnectionException e) {
			assertEquals(e.getClass(), ConnectionException.class);
		}
	}
	
	@Test
	public void testPeerDevices() {
		Hub hub1 = goodBuilder.build();
		Hub hub2 = goodBuilder.build();
		Hub hub3 = goodBuilder.build();
		Hub hub4 = goodBuilder.build();
		SisterPrinter sisterPrinter = new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		try {
			sisterPrinter.getConnector(0).setPeer(hub1.getConnector(0));
			hub1.getConnector(1).setPeer(hub2.getConnector(0));
			hub2.getConnector(1).setPeer(hub3.getConnector(0));
			hub3.getConnector(1).setPeer(hub4.getConnector(0));
		} catch (ConnectionException e) {
			fail("This is not supposed to throw an exception.");
		}
		
		Set<Device> directPeers = sisterPrinter.peerDevices();
		assertTrue(directPeers.contains(hub1));
		assertTrue(directPeers.size() == 1);
		
		Set<Device> allReachable = sisterPrinter.reachableDevices();
		assertTrue(allReachable.contains(hub1));
		assertTrue(allReachable.contains(hub2));
		assertTrue(allReachable.contains(hub3));
		assertTrue(allReachable.contains(hub4));
		assertTrue(allReachable.size() == 5); // The all reachable set contains the device itself
	}
	
}
