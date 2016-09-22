package eecs293.uxb.connectors;

import java.util.Optional;

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
	
	public void recv(Message message) {
		message.reach(device, this);
	}
}
