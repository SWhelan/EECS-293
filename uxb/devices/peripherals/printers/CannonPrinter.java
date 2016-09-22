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
	public void recv(StringMessage message, Connector connector) {
		checkIfValid(message, connector);
		StringBuilder builder = new StringBuilder();
		builder.append("Cannon printer has printed the string: ")
		.append(message.getString())
		.append(NEW_LINE)
		.append("Printer UXB version number: ")
		.append(this.getVersion());
		infoLog(builder.toString());
	}

	@Override
	public void recv(BinaryMessage message, Connector connector) {
		checkIfValid(message, connector);
		BigInteger serialNumber = this.getSerialNumber().isPresent() ? this.getSerialNumber().get() : BigInteger.ONE;
		BigInteger product = message.getValue().multiply(serialNumber);
		StringBuilder builder = new StringBuilder();
		builder.append("Cannon printer has printed the binary message: ")
			.append(product);
		infoLog(builder.toString());
	}

}
