package eecs293.uxb.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.junit.Test;

import eecs293.uxb.connectors.ConnectionException;
import eecs293.uxb.devices.Device;
import eecs293.uxb.devices.DeviceClass;
import eecs293.uxb.devices.Hub;
import eecs293.uxb.devices.peripherals.printers.CannonPrinter;
import eecs293.uxb.devices.peripherals.printers.SisterPrinter;
import eecs293.uxb.devices.peripherals.video.GoAmateur;
import eecs293.uxb.messages.Message;
import eecs293.uxb.tests.LoggerTester.LogTester;

public class BroadcastAndCycleTester extends Tester {
	
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
		assertTrue(allReachable.size() == 4);
	}
	
	@Test
	public void testBroadcast() {
		Hub hub = goodBuilder.build();
		SisterPrinter sisterPrinter = new SisterPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		CannonPrinter cannonPrinter = new CannonPrinter.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		GoAmateur goAmateur = new GoAmateur.Builder(TEST_VERSION_NUMBER).connectors(ONLY_PERIPHERALS).build();
		broadcast(Arrays.asList(hub, sisterPrinter, cannonPrinter, goAmateur), MESSAGES, LoggerTester.initializeLogTester());
	}
	
	private void broadcast(List<Device> devices, List<Message> messages, LogTester logTester) {
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
	
}
