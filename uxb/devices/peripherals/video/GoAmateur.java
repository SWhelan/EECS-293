package eecs293.uxb.devices.peripherals.video;

import eecs293.uxb.Connector;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.StringMessage;

public class GoAmateur extends AbstractVideo {
	
	public static class Builder extends AbstractVideo.Builder {

		public Builder(Integer version) {
			super(version);
		}
		
		public GoAmateur build() throws IllegalStateException, NullPointerException {
			this.validate();
			return new GoAmateur(this);
		}
		
		@Override
		protected Builder getThis() {
			return this;
		}

	}

	protected GoAmateur(Builder builder) {
		super(builder);
	}

	@Override
	public void recvHelper(StringMessage message, Connector connector) {
		System.out.println("GoAmateur does not understand string messages: " + message.getString());
		System.out.println("Connector Index: " + connector.getIndex());
	}

	@Override
	public void recvHelper(BinaryMessage message, Connector connector) {
		System.out.println("GoAmateur is not yet active: " + message.getValue());
	}
}
