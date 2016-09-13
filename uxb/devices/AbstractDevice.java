package eecs293.uxb.devices;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import eecs293.uxb.Connector;
import eecs293.uxb.Connector.Type;

public class AbstractDevice<T extends AbstractDevice.Builder<T>> implements Device {
	
	private static final Optional<Integer> DEFAULT_VERSION = Optional.of(1);
	private final Optional<Integer> productCode;
	private final Optional<BigInteger> serialNumber;
	private final Optional<Integer> version; 
	private final List<Connector.Type> connectors;
	
	public static abstract class Builder<T> {
		private Optional<Integer> version;
		private Optional<Integer> productCode;
		private Optional<BigInteger> serialNumber;
		private List<Type> connectorTypes;
		
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
			this.connectorTypes = connectors == null ? Collections.emptyList() : connectors;
			return getThis();
		}
		
		protected abstract T getThis();
		
		protected List<Connector.Type> getConnectors() {
			return connectorTypes;
		}
		
		protected void validate() throws NullPointerException {
			version.orElseThrow(() -> new NullPointerException("The version was null."));
		}
		
	}
	
	public AbstractDevice(Builder<T> builder) {
		Optional<Integer> tempVersion = null;
		try {
			builder.validate();
			tempVersion = builder.version;
		} catch (NullPointerException e) {
			tempVersion = DEFAULT_VERSION;
		} finally {
			this.version = tempVersion;
			this.productCode = builder.productCode;
			this.serialNumber = builder.serialNumber;
			this.connectors = builder.getConnectors();
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
		return version.get();
	}

	@Override
	public Integer getConnectorCount() {
		return connectors.size();
	}

	@Override
	public List<Type> getConnectors() {
		return connectors.stream().map(e -> e.getType()).collect(Collectors.toList());
	}

	@Override
	public Connector getConnector(int index) {
		if (0 > index || index > connectors.size() - 1) {
			return null;
		}
		return connectors.get(index);
	}

	@Override
	public DeviceClass getDeviceClass() {
		return null;
	}

}
