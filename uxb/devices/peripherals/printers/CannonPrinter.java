package eecs293.uxb.devices.peripherals.printers;

import java.math.BigInteger;

import eecs293.uxb.Connector;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.StringMessage;

public class CannonPrinter extends AbstractPrinter {
	public static class Builder extends AbstractPrinter.Builder {

		public Builder(Integer version) {
			super(version);
		}
		
		public CannonPrinter build() throws IllegalStateException, NullPointerException {
			this.validate();
			return new CannonPrinter(this);
		}
		
		@Override
		protected Builder getThis() {
			return this;
		}

	}

	protected CannonPrinter(Builder builder) {
		super(builder);
	}

	@Override
	public void recvHelper(StringMessage message, Connector connector) {
		System.out.println("Cannon printer has printed the string: " + message.getString());
		System.out.println("Printer UXB version number: " + this.getVersion());
	}

	@Override
	public void recvHelper(BinaryMessage message, Connector connector) {
		BigInteger serialNumber = this.getSerialNumber().isPresent() ? this.getSerialNumber().get() : BigInteger.ONE;
		BigInteger product = message.getValue().multiply(serialNumber);
		System.out.println("Cannon printer has printed the binary message: " + product);
	}
}
