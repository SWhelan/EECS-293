package eecs293.uxb.messages;

import java.math.BigInteger;

import eecs293.uxb.Connector;
import eecs293.uxb.devices.Device;

public final class BinaryMessage implements Message {

	private final static BigInteger DEFAULT_VALUE = BigInteger.ZERO;
	
	private final BigInteger value;
	
	public BinaryMessage(BigInteger value) {
		this.value = value == null ? DEFAULT_VALUE : value;
	}
	
	public BigInteger getValue() {
		return value;
	}
	
	@Override
	public void reach(Device device, Connector connector) {
		device.recv(this, connector);
	}	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BinaryMessage other = (BinaryMessage) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
