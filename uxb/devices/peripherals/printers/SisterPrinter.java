package eecs293.uxb.devices.peripherals.printers;

import java.math.BigInteger;
import java.util.logging.Level;

import eecs293.uxb.connectors.Connector;
import eecs293.uxb.devices.AbstractDevice;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.StringMessage;

public class SisterPrinter extends AbstractPrinter<SisterPrinter.Builder> {
	
	public static class Builder extends AbstractPrinter.Builder<SisterPrinter.Builder> {

		public Builder(Integer version) {
			super(version);
		}
		
		public SisterPrinter build() throws IllegalStateException, NullPointerException {
			this.validate();
			return new SisterPrinter(this);
		}
		
		@Override
		protected Builder getThis() {
			return this;
		}

	}

	protected SisterPrinter(Builder builder) {
		super(builder);
	}

	@Override
	public void recv(StringMessage message, Connector connector) {
		validateCanBeReceived(message, connector);
		StringBuilder builder = new StringBuilder();
		builder.append("Sister printer has printed the string: ")
			.append(message.getString())
			.append("\nPrinter serial number: ")
			.append(this.getSerialNumber());
		AbstractDevice.LOGGER.log(Level.INFO, builder.toString());
	}

	@Override
	public void recv(BinaryMessage message, Connector connector) {
		validateCanBeReceived(message, connector);
		StringBuilder builder = new StringBuilder();
		builder.append("Sister printer has printed the binary message: ")
			.append(message.getValue().add(BigInteger.valueOf(this.getProductCode().orElse(0))).toString());
		AbstractDevice.LOGGER.log(Level.INFO, builder.toString());
	}

}
