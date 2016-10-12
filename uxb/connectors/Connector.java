package eecs293.uxb.connectors;

import java.util.Objects;
import java.util.Optional;

import eecs293.uxb.connectors.ConnectionException.ErrorCode;
import eecs293.uxb.devices.Device;
import eecs293.uxb.messages.Message;

public final class Connector {
	
	public enum Type {
		COMPUTER,
		PERIPHERAL;
	}
	
	private Device device;
	private final int index;
	private final Type type;
	private Optional<Connector> peer;
	
	public Connector(Device device, int index, Type type) {
		this.device = device;
		this.index = index;
		this.type = type;
		this.peer = Optional.empty();
	}
	
	public Device getDevice() {
		return device;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Type getType() {
		return type;
	}
	
	public Optional<Connector> getPeer() {
		return peer;
	}
	
	public void setPeer(Connector peer) throws ConnectionException {
		Objects.requireNonNull(peer, "Cannot add a null peer to a connector.");
		
		if (this.getPeer().isPresent()) { // TODO Check other peer for busyness
			throw new ConnectionException(this, ErrorCode.CONNECTOR_BUSY);
		}
		
		if (this.getType() == peer.getType()) {
			throw new ConnectionException(this, ErrorCode.CONNECTOR_MISMATCH);
		}
		
		if (connectionCycleExists(peer)) {
			throw new ConnectionException(this, ErrorCode.CONNECTION_CYCLE);
		}
		
		this.peer = Optional.of(peer);
		peer.peer = Optional.of(this);
	}
	
	private boolean connectionCycleExists(Connector peer) {
		return this.isReachable(peer.getDevice()) || peer.isReachable(this.getDevice());
	}

	public void recv(Message message) {
		message.reach(device, this);
	}
	
	public boolean isReachable(Device device) {
		return this.getDevice().isReachable(device);
	}
}
