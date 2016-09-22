package eecs293.uxb.devices.peripherals.video;

import eecs293.uxb.connectors.Connector;
import eecs293.uxb.messages.BinaryMessage;
import eecs293.uxb.messages.StringMessage;

public class GoAmateur extends AbstractVideo<GoAmateur.Builder> {
	
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
		infoLog(builder.toString());
	}

	@Override
	public void recv(BinaryMessage message, Connector connector) {
		validateCanBeReceived(message, connector);
		StringBuilder builder = new StringBuilder();
		builder.append("GoAmateur is not yet active: ")
			.append(message.getValue());
		infoLog(builder.toString());
	}
}
