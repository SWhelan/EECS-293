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

import eecs293.uxb.Connector;
import eecs293.uxb.Connector.Type;
import eecs293.uxb.devices.AbstractDevice;
import eecs293.uxb.devices.DeviceClass;
import eecs293.uxb.devices.Hub;
import eecs293.uxb.devices.Hub.Builder;

public class Tester {
	
	private static final int TEST_VERSION_NUMBER = 3;
	private static final int TEST_PRODUCT_CODE = 7;
	private static final BigInteger TEST_SERIAL_NUMBER = BigInteger.valueOf(9);

	// Tests depend on the order of this list.
	private static final List<Type> CONNECTORS = Arrays.asList(Connector.Type.COMPUTER, Connector.Type.PERIPHERAL);
	
	private Hub.Builder badBuilder;
	private Hub.Builder goodBuilder;
	
	@Before
	public void setUp() {
		badBuilder = new Builder(TEST_VERSION_NUMBER);
		goodBuilder = new Builder(TEST_VERSION_NUMBER).connectors(CONNECTORS);
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
    	assertTrue(hub.getConnectorCount() == CONNECTORS.size());
    	assertEquals(hub.getConnector(0).getType(), Connector.Type.COMPUTER);
    	assertEquals(hub.getConnector(1).getType(), Connector.Type.PERIPHERAL);
    	assertEquals(hub.getConnectors(), CONNECTORS);
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
	
}
