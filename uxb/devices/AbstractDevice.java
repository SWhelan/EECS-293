package eecs293.uxb.devices;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import eecs293.uxb.connectors.Connector;
import eecs293.uxb.connectors.Connector.Type;
import eecs293.uxb.messages.Message;

/**
 * 
 * An abstract class that devices should extend. Concrete device classes
 * should also extend the inner Builder class as a way to create devices.
 * 
 * @author Sarah Whelan
 *
 */
public abstract class AbstractDevice<T extends AbstractDevice.Builder<T>> implements Device {
	
	public static final Logger LOGGER = Logger.getLogger(AbstractDevice.class.getName());
	
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
	
	@Override
	public Set<Device> peerDevices() {		
		return getConnectors()
				.stream()
				.map(Connector::getPeer)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(Connector::getDevice)
				.collect(Collectors.toSet());
	}
	
	@Override
	public Set<Device> reachableDevices() {
		Set<Device> allDevices = this.depthFirstSearch(Optional.empty());
		allDevices.remove(this);
		return allDevices;
	}
	
	@Override
	public boolean isReachable(Device device) {
		return this.depthFirstSearch(Optional.of(device)).contains(device);
	}
	
	/**
	 * Validates/determines if the message can be received on
	 * the specified connector. 
	 * 
	 * @param message the message to receive
	 * @param connector the connector to receive the message
	 * @throws NullPointerException thrown if the message or connector is null
	 * @throws IllegalStateException thrown if the connector specified is not connected to this device
	 */
	protected void validateCanBeReceived(Message message, Connector connector) 
			throws NullPointerException, IllegalStateException {
		Objects.requireNonNull(message, "The message was null and could not be sent.");
		Objects.requireNonNull(connector, "The connector was null and the message could not be sent.");
		
		if (!getConnectors().contains(connector)) {
			throw new IllegalStateException("The connector is not connected to this device. The message could not be sent.");
		}
	}
	
	/**
	 * @param target optional target for early termination of search
	 * @return the set of devices explored until target is found or all of the devices reachable from this device
	 */
	public Set<Device> depthFirstSearch(Optional<Device> target) {
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
	
	private boolean targetFound(Device current, Optional<Device> target) {
		return target.isPresent() && current.equals(target.get());
	}

	private void addNeighbors(boolean shouldAdd, Set<Device> neighbors, Stack<Device> unexplored) {
		if (shouldAdd) {
			unexplored.addAll(neighbors);
		}
	}
	
	protected void validateAndForward(Message message, Connector connector) {
		validateCanBeReceived(message, connector);
		forwardMessage(message, connector);
	}
	
	protected void forwardMessage(Message message, Connector connectorToIgnore) {
		this.getConnectors()
			.stream()
			.filter(connector -> !connector.equals(connectorToIgnore))
			.map(Connector::getPeer)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(peer -> peer.recv(message));
	}

}
