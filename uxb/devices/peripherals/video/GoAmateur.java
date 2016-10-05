package eecs293.uxb.devices.peripherals.video;

import java.math.BigInteger;
import java.util.logging.Level;

import eecs293.uxb.connectors.Connector;
import eecs293.uxb.devices.AbstractDevice;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.StringMessage;

public class GoAmateur extends AbstractVideo<GoAmateur.Builder> {
	
	private static final BigInteger BROADCAST_MESSAGE = BigInteger.valueOf(293);

	public static class Builder extends AbstractVideo.Builder<GoAmateur.Builder>  {

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
	public void recv(StringMessage message, Connector connector) {
		validateCanBeReceived(message, connector);
		StringBuilder builder = new StringBuilder();
		builder.append("GoAmateur does not understand string messages: ")
			.append(message.getString())
			.append("\nConnector Index: ")
			.append(connector.getIndex());
		AbstractDevice.LOGGER.log(Level.INFO, builder.toString());
	}

	@Override
	public void recv(BinaryMessage message, Connector connector) {
		validateAndForward(new BinaryMessage(BROADCAST_MESSAGE), connector);
	}
}
