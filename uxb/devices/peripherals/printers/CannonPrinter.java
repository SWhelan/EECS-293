package eecs293.uxb.devices.peripherals.printers;

import java.math.BigInteger;

import eecs293.uxb.connectors.Connector;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.StringMessage;

public class CannonPrinter extends AbstractPrinter<CannonPrinter.Builder> {
	public static class Builder extends AbstractPrinter.Builder<CannonPrinter.Builder> {

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
		validateCanBeReceived(message, connector);
		StringBuilder builder = new StringBuilder();
		builder.append("Cannon printer has printed the string: ")
		.append(message.getString())
		.append("\nPrinter UXB version number: ")
		.append(this.getVersion());
		infoLog(builder.toString());
	}

	@Override
	public void recv(BinaryMessage message, Connector connector) {
		validateCanBeReceived(message, connector);
		// Because we are multiplying if there isn't a serial number multiply by 1 to print just the message value
		BigInteger serialNumber = this.getSerialNumber().orElse(BigInteger.ONE);
		BigInteger product = message.getValue().multiply(serialNumber);
		StringBuilder builder = new StringBuilder();
		builder.append("Cannon printer has printed the binary message: ")
			.append(product);
		infoLog(builder.toString());
	}

}
