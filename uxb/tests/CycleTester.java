package eecs293.uxb.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eecs293.uxb.connectors.ConnectionException;
import eecs293.uxb.devices.Hub;
import eecs293.uxb.devices.peripherals.printers.SisterPrinter;

public class CycleTester extends Tester {
	
	@Test
	public void testIsReachable() {
		Hub hub = goodBuilder.build();
		SisterPrinter sisterPrinter = ((SisterPrinter.Builder)(new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS))).build();
		try {
			sisterPrinter.getConnector(0).setPeer(hub.getConnector(0));
		} catch (ConnectionException e) {
			assertTrue("This is not supposed to throw an exception.", false);
		}
		
	}
	
}
