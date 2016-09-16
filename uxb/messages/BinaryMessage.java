package eecs293.uxb.messages;

import java.math.BigInteger;

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
	public boolean equals(Object obj) {
		return	obj != null && 
				obj.getClass() == this.getClass() &&
				this.value.equals(((BinaryMessage)obj).getValue());
	}
	
}
