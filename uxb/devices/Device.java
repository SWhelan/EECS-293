package eecs293.uxb.devices;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import eecs293.uxb.Connector;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.Message;
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
	public default void recv(StringMessage message, Connector connector) {
		checkIfValid(message, connector);
		recvHelper(message, connector);
	}
	
	public void recvHelper(StringMessage message, Connector connector);
	
	/**
	 * Signifies the arrival of a BinaryMessage at the given connector in the device.
	 * 
	 * @param message
	 * @param connector
	 */
	public default void recv(BinaryMessage message, Connector connector) {
		checkIfValid(message, connector);
		recvHelper(message, connector);
	}
	
	public void recvHelper(BinaryMessage message, Connector connector);
	
	public default void checkIfValid(Message message, Connector connector) {
		if (message == null || connector == null) {
			throw new NullPointerException();
		}
		
		if (!getConnectors().contains(connector)) {
			throw new IllegalStateException();
		}
	}
	
}
