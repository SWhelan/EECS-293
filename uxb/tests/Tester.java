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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eecs293.uxb.connectors.ConnectionException;
import eecs293.uxb.connectors.ConnectionException.ErrorCode;
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

	// Tests depend on the order of these lists.
	public static final List<Type> CONNECTOR_TYPES = Arrays.asList(Connector.Type.COMPUTER, Connector.Type.PERIPHERAL);
	public static final List<Type> ONLY_PERIPHERALS = Arrays.asList(Connector.Type.PERIPHERAL, Connector.Type.PERIPHERAL);
	public static final List<Type> ONLY_COMPUTERS = Arrays.asList(Connector.Type.COMPUTER, Connector.Type.COMPUTER);
	public static final List<Message> MESSAGES = Arrays.asList(
			new StringMessage("The first message is a string."), 
			new BinaryMessage(BigInteger.valueOf(2)),
			new StringMessage("The third message is a string."),
			new StringMessage("The fourth message is a string."),
			new BinaryMessage(BigInteger.valueOf(5)));
	
	public Hub.Builder badBuilder;
	public Hub.Builder goodBuilder;
	public Hub hub;
	public SisterPrinter sisterPrinter;
	public CannonPrinter cannonPrinter;
	public GoAmateur goAmateur;
	
	// Runs once per test before the test is run.
	@Before
	public void setUp() {
		badBuilder = new Builder(TEST_VERSION_NUMBER);
		goodBuilder = new Builder(TEST_VERSION_NUMBER).connectors(CONNECTOR_TYPES);
		hub = goodBuilder.build();
		sisterPrinter = new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		cannonPrinter = new CannonPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		goAmateur = new GoAmateur.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
	}
	
	// Runs once per test after the test completes.
	@After
	public void tearDown() {
		badBuilder = null;
		goodBuilder = null;
		hub = null;
		sisterPrinter = null;
		cannonPrinter = null;
		goAmateur = null;
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
		broadcast(Arrays.asList(hub, sisterPrinter, cannonPrinter, goAmateur), MESSAGES, LogTester.initializeLogTester());
	}
	
	private void broadcast(List<Device> devices, List<Message> messages, LogHandler logTester) {
		for (Message message : messages) {
			for (Device device : devices) {
				message.reach(device, device.getConnector(0));
			}
		}
	}
	
	@Test
	public void testIsReachable() {
		try {
			sisterPrinter.getConnector(0).setPeer(hub.getConnector(0));
		} catch (ConnectionException e) {
			fail("This is not supposed to throw an exception.");
		}
		
	}
	
	@Test
	public void testSetPeer() {
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
	}
	
	@Test
	public void testSetPeerBusy() {
		try {
			sisterPrinter.getConnector(0).setPeer(hub.getConnector(0));
			sisterPrinter.getConnector(0).setPeer(hub.getConnector(0));
			fail("This is supposed to throw an exception.");
		} catch (ConnectionException e) {
			assertEquals(e.getClass(), ConnectionException.class);
			assertEquals(e.getErrorCode(), ErrorCode.CONNECTOR_BUSY);
		}
	}
	
	@Test
	public void testSetPeerMismatch() {
		try {
			sisterPrinter.getConnector(1).setPeer(hub.getConnector(1));
			fail("This is supposed to throw an exception.");
		} catch (ConnectionException e) {
			assertEquals(e.getClass(), ConnectionException.class);
			assertEquals(e.getErrorCode(), ErrorCode.CONNECTOR_MISMATCH);
		}
	}
	
	@Test
	public void testSetPeerCycle() {
		Hub hub = goodBuilder.connectors(Arrays.asList(Connector.Type.COMPUTER, Connector.Type.PERIPHERAL, Connector.Type.COMPUTER)).build();
		try {
			hub.getConnector(0).setPeer(sisterPrinter.getConnector(0));
			sisterPrinter.getConnector(1).setPeer(this.hub.getConnector(0));
			this.hub.getConnector(1).setPeer(hub.getConnector(2));
			fail("This is supposed to throw an exception.");
		} catch (ConnectionException e) {
			assertEquals(e.getClass(), ConnectionException.class);
			assertEquals(e.getErrorCode(), ErrorCode.CONNECTION_CYCLE);
		}
	}
	
	@Test
	public void testPeerDevices() {
		Hub hub1 = goodBuilder.build();
		Hub hub2 = goodBuilder.build();
		Hub hub3 = goodBuilder.build();
		Hub hub4 = goodBuilder.build();
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
		assertTrue(allReachable.size() == 4);
	}
	
	@Test
	public void testGoAmateur() {
		Hub hub1 = goodBuilder.build();
		Hub hub2 = goodBuilder.build();
		try {
			hub1.getConnector(0).setPeer(goAmateur.getConnector(0));
			goAmateur.getConnector(1).setPeer(hub2.getConnector(0));
		} catch (ConnectionException e) {
			fail("This is not supposed to throw an exception.");
		}
		MESSAGES.get(1).reach(goAmateur, goAmateur.getConnector(0));
	}
	
	@Test
	public void testHub() {
		try {
			hub.getConnector(0).setPeer(goAmateur.getConnector(0));
		} catch (ConnectionException e) {
			fail("This is not supposed to throw an exception.");
		}
		MESSAGES.get(0).reach(hub, hub.getConnector(0));
	}	
	
	@Test
	public void testUXBSystem() {
		Hub hub1 = goodBuilder.connectors(Arrays.asList(Connector.Type.COMPUTER, Connector.Type.COMPUTER, Connector.Type.PERIPHERAL, Connector.Type.COMPUTER, Connector.Type.COMPUTER)).build();
		Hub hub2 = goodBuilder.connectors(Arrays.asList(Connector.Type.COMPUTER, Connector.Type.COMPUTER, Connector.Type.COMPUTER, Connector.Type.COMPUTER, Connector.Type.PERIPHERAL)).build();
		SisterPrinter sisterPrinter1 = new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		SisterPrinter sisterPrinter2 = new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		GoAmateur goAmateur2 = new GoAmateur.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		try {
			goAmateur.getConnector(0).setPeer(hub1.getConnector(0));
			hub1.getConnector(1).setPeer(sisterPrinter1.getConnector(0));
			hub1.getConnector(2).setPeer(hub2.getConnector(0));
			hub1.getConnector(3).setPeer(cannonPrinter.getConnector(0));
			hub1.getConnector(4).setPeer(goAmateur2.getConnector(0));
			hub2.getConnector(1).setPeer(sisterPrinter1.getConnector(1));
			sisterPrinter2.getConnector(0).setPeer(hub2.getConnector(2));
			cannonPrinter.getConnector(1).setPeer(hub2.getConnector(3));
			
			// A string message is broadcast from a hub
			new StringMessage("A String message broadcast from a hub.").reach(hub1, hub1.getConnector(0));
			// A binary message is sent from a hub along a connector that links the hub to a Webcam
			new BinaryMessage(BigInteger.ONE).reach(hub1, hub1.getConnector(0));
			// A binary message broadcast from a hub
			new BinaryMessage(BigInteger.TEN).reach(hub2, hub2.getConnector(2));
		} catch (ConnectionException e) {
			fail("This is not supposed to throw an exception.");
		}
	}
	
}
