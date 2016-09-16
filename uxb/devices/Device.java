package eecs293.uxb.devices;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import eecs293.uxb.Connector;

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
	public List<Connector.Type> getConnectors();
	
	/**
	 * @param index  
	 * @return the connector of this device at the given index.
	 */
	public Connector getConnector(int index);
}
