package eecs293.uxb;

import java.util.Optional;

import eecs293.uxb.devices.Device;

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
}
