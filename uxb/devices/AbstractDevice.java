package eecs293.uxb.devices;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import eecs293.uxb.Connector;
import eecs293.uxb.Connector.Type;

/**
 * 
 * An abstract class that devices should extend. Concrete device classes
 * should also extend the inner Builder class as a way to create devices.
 * 
 * @author Sarah Whelan
 *
 */
public abstract class AbstractDevice<T extends AbstractDevice.Builder<T>> implements Device {
	
	private final Integer version;
	private final Optional<Integer> productCode;
	private final Optional<BigInteger> serialNumber;
	private final List<Connector> connectors;
	
	/**
	 * Builds an device. Devices that extend AbstractDevice also should extend this
	 * inner Builder class as well.
	 * 
	 * This class is used to set values before the Device is created as the values
	 * after the device is created are final and cannot be changed.
	 */
	public static abstract class Builder<T> {
		public static final String NULL_VERSION_NUMBER_MESSAGE = "The version is null.";
		
		private Optional<Integer> version;
		private Optional<Integer> productCode;
		private Optional<BigInteger> serialNumber;
		private List<Type> connectorTypes;
		
		/**
		 * Creates a builder with specified version, empty optionals for 
		 * productCode and serialNumber, and an empty list of Connector.Type.
		 * 
		 * @param version can be null but the builder is invalid if version is null
		 */
		public Builder(Integer version) {
			this.version = Optional.ofNullable(version);
			this.productCode = Optional.empty();
			this.serialNumber = Optional.empty();
			this.connectorTypes = Collections.emptyList();
		}
		
		public T productCode(Integer productCode) {
			this.productCode = Optional.ofNullable(productCode);
			return getThis();
		}
		
		public T serialNumber(BigInteger serialNumber) {
			this.serialNumber = Optional.ofNullable(serialNumber);
			return getThis();
		}
		
		public T connectors(List<Connector.Type> connectors) {
			this.connectorTypes = connectors == null ? new ArrayList<Connector.Type>() : new ArrayList<>(connectors);
			return getThis();
		}
		
		protected abstract T getThis();
		
		protected List<Connector.Type> getConnectors() {
			return new ArrayList<>(connectorTypes);
		}
		
		protected void validate() throws NullPointerException {
			version.orElseThrow(() -> new NullPointerException(NULL_VERSION_NUMBER_MESSAGE));
		}
		
	}
	
	/**
	 * Makes a device with the same version, productCode, and serialNumber as the builder.
	 * If the builder's version is null {@code DEFAULT_VERSION} is used as version instead.
	 * 
	 * The List<{@link Connector.Type}> on the builder is translated into a
	 * List<{@link Connector}> by creating a new connector for each type in the list.
	 * Each new connector is assigned an index equal to the index of its place in the list being created. 
	 * 
	 * @param builder contains the information used to create the AbstractDevice
	 */
	public AbstractDevice(Builder<T> builder) {
		this.version = builder.version.get();
		this.productCode = builder.productCode;
		this.serialNumber = builder.serialNumber;
		this.connectors = new ArrayList<Connector>();
		int index = 0;
		for (Connector.Type type : builder.getConnectors()) {
			this.connectors.add(new Connector(this, index, type));
			index++;
		}
	}

	@Override
	public Optional<Integer> getProductCode() {
		return productCode;
	}

	@Override
	public Optional<BigInteger> getSerialNumber() {
		return serialNumber;
	}

	@Override
	public Integer getVersion() {
		return version;
	}

	@Override
	public Integer getConnectorCount() {
		return connectors.size();
	}

	@Override
	public List<Connector> getConnectors() {
		return new ArrayList<>(connectors);
	}

	/**
	 * @param index the index of the desired connector
	 * @return 
	 * If the index is within the bounds of the list, the connector at the index specified.
	 * If the index is out of bounds, null is returned.
	 */
	@Override
	public Connector getConnector(int index) {
		if (0 <= index && index < connectors.size()) {
			return connectors.get(index);
		}
		return null;
	}

}
