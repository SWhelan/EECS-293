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
	public void recvHelper(StringMessage message, Connector connector) {
		System.out.println("Sister printer has printed the string: " + message.getString());
		System.out.println("Printer serial number: " + this.getSerialNumber());
	}

	@Override
	public void recvHelper(BinaryMessage message, Connector connector) {
		int productCode = this.getProductCode().isPresent() ? this.getProductCode().get() : 0;
		int sum = message.getValue().intValue() + productCode;
		System.out.println("Sister printer has printed the binary message: " + sum);
	}

}
