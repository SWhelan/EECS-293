package eecs293.uxb.devices.peripherals.printers;

import eecs293.uxb.Connector;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.StringMessage;

public class SisterPrinter extends AbstractPrinter {
	
	public static class Builder extends AbstractPrinter.Builder {

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
			.append(NEW_LINE)
			.append("Printer serial number: ")
			.append(this.getSerialNumber());
		infoLog(builder.toString());
	}

	@Override
	public void recv(BinaryMessage message, Connector connector) {
		validateCanBeReceived(message, connector);
		// If there is not a product code don't add to the sum
		int productCode = this.getProductCode().isPresent() ? this.getProductCode().get() : 0;
		int sum = message.getValue().intValue() + productCode;
		StringBuilder builder = new StringBuilder();
		builder.append("Sister printer has printed the binary message: ")
			.append(sum);
		infoLog(builder.toString());
	}

}
