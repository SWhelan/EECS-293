package eecs293.uxb.devices;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

import eecs293.uxb.connectors.Connector;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.StringMessage;

public interface Device {

	
	/**
	 * @return the product code of this device. If the product code is unknown, return an empty optional.
	 */
	public Optional<Integer> getProductCode();
	
	/**
	 * @return the serial number of this device. If the serial number is unknown, return an empty optional.
	 */
	public Optional<BigInteger> getSerialNumber();
	
	/**
	 * @return the UXB version that this device supports.
	 */
	public Integer getVersion();
	
	/**
	 * @return the class of this UXB device
	 */
	public DeviceClass getDeviceClass();
	
	/**
	 * @return the number of connectors that this device has.
	 */
	public Integer getConnectorCount();
	
	/**
	 * @return the type of each connector in this device.
	 */
	public List<Connector> getConnectors();
	
	/**
	 * @param index  
	 * @return the connector of this device at the given index.
	 */
	public Connector getConnector(int index);
	
	/**
	 * Signifies the arrival of a StringMessage at the given connector in the device.
	 * 
	 * @param message
	 * @param connector
	 */
	public void recv(StringMessage message, Connector connector);
	
	/**
	 * Signifies the arrival of a BinaryMessage at the given connector in the device.
	 * 
	 * @param message
	 * @param connector
	 */
	public void recv(BinaryMessage message, Connector connector);
	
	/**
	 * @return the devices to which this device is connected directly through one of its connectors.
	 */
	public Set<Device> peerDevices();
	
	/**
	 * @return all devices that are reachable either directly (the peerDevices) or indirectly from this device.
	 */
	public Set<Device> reachableDevices();
	
	/**
	 * @param device
	 * @return true if the argument is connected directly or indirectly to this device, false otherwise.
	 */
	public boolean isReachable(Device device);
	
	/**
	 * @param target optional target for early termination of search
	 * @return the set of devices explored until target is found or all of the devices reachable from this device
	 */
	public default Set<Device> depthFirstSearch(Optional<Device> target) {
		Set<Device> explored = new HashSet<>();
		Stack<Device> unexplored = new Stack<>();
		unexplored.push(this);
		while (!unexplored.isEmpty()) {
			Device current = unexplored.pop();
			boolean shouldExpandCurrent = explored.add(current);
			if (targetFound(current, target)) {
				return explored;
			}
			addNeighbors(shouldExpandCurrent, current.peerDevices(), unexplored);
		}
		return explored;
	}
	
	public default boolean targetFound(Device current, Optional<Device> target) {
		return target.isPresent() && current.equals(target.get());
	}

	public default void addNeighbors(boolean shouldAdd, Set<Device> neighbors, Stack<Device> unexplored) {
		if (shouldAdd) {
			unexplored.addAll(neighbors);
		}
	}
	
}
